/*
 * Copyright (c) 2015 T4Connex.
 */

package com.tracker.singleton;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.Chronometer;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.tracker.data.DCBehaviourPoint;
import com.tracker.data.DCJourney;
import com.tracker.data.DCJourneyPoint;
import com.tracker.database.JourneyDataSource;
import com.tracker.utilities.Utilities;

import java.util.ArrayList;
/**
 * Singleton for tracking a journey in the background.
 *
 * @author Muhammad Azeem Anwar
 */

public class TrackJourneySingleton {
    // Interval between capturing a DCJourneyPoint if available.
    private static final int DETECTION_INTERVAL_MILLISECONDS = 500;
    public static float distaneInMeter = 0;
    public static boolean isSwitchOn = false;
    public static boolean isRestAlertShowed = false;
    // DC Journey Object. Holds information about a journey/
    private static DCJourney journey = null;
    private static JourneyDataSource dataSource;
    // The number of location data points currently captured.
    private static int journeyPoints = 0;
    private static GoogleApiClient locationClient = null;
    private static LocationRequest locationRequest = null;
    private static Chronometer chronometer = null;
    private static long timeWhenStopped = 0;
    private static String distance = "0.0";
    private static float totalDistance = 0; // Total travelled distance
    private static float topSpeed = 0; // the highest speed
    // Save the previous location each time a new location is received
    // in order to calculate distance etc.
    private static Location previousLocation = null;
   // Data about behaviour is stored in a DCBehaviourPoint
    private static long startJourneyTime = 0;
    private static long endJourneyTime = 0;
    private static float currentAccuracy = (float) 0.0;
    // Activity Recognition gets the users activity from device sensors
    // e.g walking, running
    private static ActivityRecognition recognitionClient;
    private static PendingIntent recognitionPendingIntent;
    // Others sensors are used to capture behaviour data
    // such as acceleration.
    private static SensorManager sensorManager;
    private static SensorEventListener sensorEventListener;
    private static OrientationEventListener myOrientationEventListener;
    private static float[] accelerometer = new float[3];
    private static float[] gravity = new float[3];
    private static float speed = 0; // current speed
    private static boolean deviceFlat;
    private static boolean deviceLandscape;
    private static boolean devicePortrait;
    private static String activity;
    // Timer
    private static long timeElapsed = 0;
    private static Handler timerHandler = new Handler();
    private static ArrayList<LatLng> routePoints = null;
    private static boolean isTracking = false;
    /**
     * Location listener receives location information from the device and processes it into a DCJourneyPoint.
     */
    private static LocationListener locationListener = new LocationListener() {
        private static final int TWO_MINUTES = 1000 * 60 * 1;
        @Override
        public void onLocationChanged(Location location) {
            if (journey != null) {
                currentAccuracy = location.getAccuracy();
                // Check to see if the location is accurate enough to use. If it's below 10 metres accuracy then just discard it.
                if (location.getAccuracy() > 15) {
                    Log.v("Tracker", "Accuracy too low, not adding point: " + location.getAccuracy());
                    return;
                }
                // If there's no location, use this as the first previous location.
                if (previousLocation == null) {
                    // Don't bother comparing with the previous point since there isn't one, just add it to the route as the first location.
                    previousLocation = location;
                    // Draw the point on the map and add it to the journey points
                    addPointToRoute(location);
                    return;
                }
                // If a previous point exists, check to see if the new point is a better location, i.e more accurate, if it is then add it to the route.
                if (isBetterLocation(location, previousLocation)) {
                    // Update the total distance travelled using the current location.
                    // This also stores this location to use next time a location is received.
                    distance = calDistance(location);
                    // Update the top speed if the current top speed is greater than the previuous recorded one.
                    topSpeed = Utilities.roundTwoDecimalsDistanc(location
                            .getSpeed()) > topSpeed ? Utilities
                            .roundTwoDecimalsDistanc(location.getSpeed())
                            : topSpeed;
                    Log.d("Tracker", "Current top speed: " + topSpeed);
                    speed = location.getSpeed();
                    addPointToRoute(location);
                } else {
                    Log.d("Tracker", "New location is worse than old one");
                }
            } else {
                Log.d("Tracker", "Journey is null, cannot add point");
            }
        }

        private void addPointToRoute(Location location) {
              DCJourneyPoint point = new DCJourneyPoint();
            point.setLat(location.getLatitude());
            point.setLng(location.getLongitude());
            dataSource.open();
            dataSource.createJourneyPoint(journey.getId(), point);
            dataSource.close();
            journeyPoints++;
            // Add this point to the polyline to display on the map.
            routePoints.add(new LatLng(location.getLatitude(), location
                    .getLongitude()));
            Log.d("Tracker", "Current Route Points: " + routePoints.size());
        }

        /** Determines whether one Location reading is better than the current Location fix
         * @param location  The new Location that you want to evaluate
         * @param currentBestLocation  The current Location fix, to which you want to compare the new one
         */
        private boolean isBetterLocation(Location location, Location currentBestLocation) {
            if (currentBestLocation == null) {
                // A new location is always better than no location
                return true;
            }
            // Check whether the new location fix is newer or older
            long timeDelta = location.getTime() - currentBestLocation.getTime();
            boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
            boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
            boolean isNewer = timeDelta > 0;
            // If it's been more than two minutes since the current location, use the new location
            // because the user has likely moved
            if (isSignificantlyNewer) {
                return true;
                // If the new location is more than two minutes older, it must be worse
            } else if (isSignificantlyOlder) {
                return false;
            }
            // Check whether the new location fix is more or less accurate
            int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;
            // Determine location quality using a combination of timeliness and accuracy
            if (isMoreAccurate) {
                return true;
            } else if (isNewer && !isLessAccurate) {
                return true;
            } else if (isNewer && !isSignificantlyLessAccurate) {
                return true;
            }
            return false;
        }
    };
    private static ConnectionCallbacks locationConnectionCallbacks = new ConnectionCallbacks() {

        @Override
        public void onConnectionSuspended(int i) {
        }
        @Override
        public void onConnected(Bundle arg0) {
            locationRequest = LocationRequest.create();
            // by default device check which highest accuaracy available
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            // Use Google fused location api to determine the location
            LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, locationListener);
        }
    };

    private static OnConnectionFailedListener mConnectionFailedListener = new OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult arg0) {
            Log.e("Connection", "ConnectionFailed");
        }
    };
    /**
     * Sets timer. after every .45 second DCBehaviourPoint should be saved in the
     * database.
     */
    private static int pointSaved = 0;
    private static Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - timeElapsed;
            int seconds = (int) (millis / 20);
            if (seconds > DETECTION_INTERVAL_MILLISECONDS / 1000) {
                if (pointSaved < 5000) { // right now we storing and calculate 5000 location
//                    saveBehaviourPoint();
                }
                timeElapsed = System.currentTimeMillis();
            }
            timerHandler.postDelayed(this, 450);
        }
    };
    /**
     * Starts tracking journey
     */
    public static void startTracking(Context context) {
        if (!isTracking) { // Check if it's not already tracking
            routePoints = new ArrayList<LatLng>();
            // Initialise variables if they are not yet initialised
            if (chronometer == null)
                chronometer = new Chronometer(context);
            // Create a new journey object in the data source.
            if (journey == null) {
                if (dataSource == null)
                    dataSource = new JourneyDataSource(context);
                dataSource.open();
                journey = new DCJourney();
                journey.setId(dataSource.createJourney(journey, null));
                dataSource.close();
            }
            // Check if Google Play Services are available
            if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
                // On success check location client
                if (locationClient == null)
                    locationClient = new GoogleApiClient.Builder(context)

                            .addConnectionCallbacks(locationConnectionCallbacks)
                            .addOnConnectionFailedListener(mConnectionFailedListener)
                            .addApi(ActivityRecognition.API)
                            .addApi(LocationServices.API)
                            .build();
                // Check users behaviour and waking up device i.e walking
                if (recognitionClient == null) {// recognition is null create and register
                    // here was activity recognition service logic to detect user behaviour.
                }

                if (recognitionPendingIntent == null) {// if null create new pending intent
                //register intent.
                }
                locationClient.connect();
            }
                // Get the current time and set it as the journey start
            if (startJourneyTime == 0)
                startJourneyTime = System.currentTimeMillis();
            // Set chronometer
            chronometer
                    .setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();
             timeElapsed = System.currentTimeMillis();           // Restart timer
            timerHandler.postDelayed(timerRunnable, 0);
            timerRunnable.run();
            startSensor(context);
           isTracking = true;
        }
    }


    /**
     * Stops tracking journey
     */
    public static void stopTracking() {
        if (isTracking) {
            endJourneyTime = System.currentTimeMillis();    // Record the current end journey time.
            timeWhenStopped = chronometer.getBase()
                    - SystemClock.elapsedRealtime();
            chronometer.stop();
            isTracking = false;
            sensorManager.unregisterListener(sensorEventListener);        // Stop all listeners
            if (locationClient.isConnected()) { // make sure it was connected. Need some more testing for it
                LocationServices.FusedLocationApi.removeLocationUpdates(locationClient, locationListener);
                locationClient.disconnect();
            }
            locationClient.disconnect();
            if (timerHandler != null)
                timerHandler.removeCallbacks(timerRunnable);


        }
    }

    /**
     * Resets tracker.
     */

    public static void reset() {
        stopTracking();
        if (chronometer != null)
            chronometer.stop();
        journey = null;
        timeWhenStopped = 0;
        isTracking = false;
        distance = "0.0";
        topSpeed = 0;
        totalDistance = 0;
        startJourneyTime = 0;
        endJourneyTime = 0;
        journeyPoints = 0;
        previousLocation = null;
        if (routePoints != null)        // Remove all saved location points
            routePoints.clear();
        for (int i = 0; i < accelerometer.length; i++)// Reset all behaviour variables
            accelerometer[i] = 0;
        speed = 0;
        deviceFlat = false;
        deviceLandscape = false;
        devicePortrait = false;
        activity = null;
        pointSaved = 0;
    }

    /**
     * Starts sensor to read behaviour data.we capture sensor data depend on need.
     */

    private static void startSensor(final Context context) {
        myOrientationEventListener = new OrientationEventListener(context,
                SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {

                if ((orientation > 45 && orientation < 135)
                        || (orientation > 225 && orientation < 315)) {
                    deviceLandscape = true;
                    devicePortrait = false;
                } else {
                    devicePortrait = true;
                    deviceLandscape = false;
                }

                if (orientation == -1) {
                    deviceFlat = true;
                    devicePortrait = false;
                    deviceLandscape = false;
                } else
                    deviceFlat = false;
            }
        };

        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                int type = event.sensor.getType();

                if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                } else if (type == Sensor.TYPE_LINEAR_ACCELERATION) {
                    final float alpha = 0.8f;
                    accelerometer = event.values.clone();
                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        myOrientationEventListener.enable();
        sensorManager
                .registerListener(sensorEventListener, sensorManager
                                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                        SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Saves DCBehaviourPoint into database
     */
    private static void saveBehaviourPoint() {
        if (journey != null) {
            DCBehaviourPoint point = new DCBehaviourPoint();
// time difference from start of the journey to current point recording time .
            long timeDifference = startJourneyTime - System.currentTimeMillis();
            for (int i = 0; i < accelerometer.length; i++)
                accelerometer[i] /= 9.81f;
            point.setAccelerationX(accelerometer[0]);
            point.setAccelerationY(accelerometer[1]);
            point.setAccelerationZ(accelerometer[2]);
            point.setGravityX(gravity[0]);
            point.setGravityY(gravity[1]);
            point.setGravityZ(gravity[2]);
            point.setSpeed(speed);
            point.setDeviceFlat(deviceFlat);
            point.setDeviceLandscape(deviceLandscape);
            point.setDevicePortrait(devicePortrait);
            point.setActivity(activity);
            point.setPointTimeDifferenceFromStart(timeDifference);
            dataSource.open();
            dataSource.createBehaviourPoint(journey.getId(), point);
            dataSource.close();
        }
    }

    /***
     * Gets the duration the current journey was tracked for
     * @return long
     */

    public static long getTrackingDuration() {
        if (isTracking)
            return chronometer.getBase();

        return SystemClock.elapsedRealtime() + timeWhenStopped;
    }

    /**
     * Calculates total travelled distance
     * @param curLoc
     * @return string
     */
    private static String calDistance(Location curLoc) {
        // Check if there is previous location
        if (previousLocation != null) {
            // Convert meters into miles
            distaneInMeter += previousLocation.distanceTo(curLoc);
            totalDistance += previousLocation.distanceTo(curLoc) * 0.000621371192;
        }
        previousLocation = curLoc;
        return String.valueOf(Utilities.roundTwoDecimalsDistanc(totalDistance));
    }
    // Getters and Setters
    public static boolean isTracking() {
        return isTracking;
    }
    public static Chronometer getChronometer() {
        return chronometer;
    }
    public static String getDistance() {
        return distance;
    }
    public static int getJourneyPoints() {
        return journeyPoints;
    }
    public static long getStartTime() {
        return startJourneyTime;
    }
    public static long getEndTime() {
        return endJourneyTime;
    }
    public static float getTopSpeed() {
        return topSpeed;
    }
    public static ArrayList<LatLng> getRoutePoints() {
        return routePoints;
    }
    public static String getActivity() {
        return activity;
    }
    public static void setActivity(String activity) {
        TrackJourneySingleton.activity = activity;
    }
    public static DCJourney getJourney() {
        return journey;
    }
    public static float getCurrentAccuracy() {
        return currentAccuracy;
    }

}