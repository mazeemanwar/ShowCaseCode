package com.tracker.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;
import com.tracker.data.DCJourneyPoint;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



/**
 * Utility functions to deal with location.
 *
 * @author Muhammad Azeem Anwar
 */

public class LocationUtilities {
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     **/
    public static boolean isBetterLocation(Location location,
                                           Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    public static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /**
     * Calculates distance between two points
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double distFrom(double lat1, double lng1, double lat2,
                                  double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c * 0.0006;

        return dist;
    }

    /**
     * Gets distance between two given points from the Google Maps.
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return String of distance in kilometres
     */
    public static String getDistFromGoogleMaps(double lat1, double lon1,
                                               double lat2, double lon2) {
        String distance = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2
                + "&sensor=false&units=metric";
        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);

            if (doc != null) {
                NodeList nl;
                ArrayList<String> args = new ArrayList<String>();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }
                distance = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Distance is in kilometres
        // String is returned in format of "(number) km"
        return distance;
    }

    /**
     * Gets an address from latitude and longitude.
     *
     * @param context
     * @param lat
     * @param lng
     * @return
     */
    public static String getAddressFromLatlng(Context context, double lat,
                                              double lng) {
        String addressStr = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses == null || addresses.size() == 0) {
                return "Error getting address.";
            } else {
                for (Address address : addresses) {
                    for (int i = 0; i < 3; i++) {
                        addressStr += address.getAddressLine(i) + "\n";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressStr.trim().replace(", ", "\n");
    }

    /**
     * Gets city name from latitude and longitude.
     *
     * @param context
     * @param lat
     * @param lng
     * @return
     */
    public static String getCityFromLatlng(Context context, double lat,
                                           double lng) {
        String cityStr = "";
        Address address = LocationUtilities.getAddress(context, lat, lng);
        if (address != null) {
            cityStr = address.getLocality();
            if (cityStr.contains(",")) {
                cityStr = cityStr.substring(0, cityStr.indexOf(","));

            }

        }
        if (cityStr == null) {
            cityStr = "";
        }

        return cityStr;
    }

    /**
     * Gets an address from a given JourneyPoint
     *
     * @param point
     * @return
     */
    public static Address getAddressFromPoint(Context context,
                                              DCJourneyPoint point) {
        return getAddress(context, point.getLat(), point.getLng());
    }

    /**
     * Gets an address from given latitude and longitude.
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public static Address getAddress(Context context, double latitude,
                                     double longitude) {
        // Check if there is data connection
        if (Utilities.checkDataConnection(context)) {
            List<Address> addresses = new ArrayList<Address>();

            try {
                // Get some addresses from a point
                // Some addresses will not contain needed information, that's
                // why we need to
                // get more addresses than just one
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                addresses = geocoder.getFromLocation(latitude, longitude, 10);

                // Loop through all addresses
                for (int i = 0; i < addresses.size(); i++) {
                    // Check if address has postal code
                    if (addresses.get(i).getPostalCode() != null) {
                        // Check if address doesn't have locality
                        if (addresses.get(i).getLocality() == null) {
                            // Get locality manually from address line
                            String locality = addresses.get(i)
                                    .getAddressLine(0);
                            addresses.get(i).setLocality(locality);
                        }

                        return addresses.get(i);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // It didn't get any address
        return null;
    }

    /**
     * Prepares URL from a starting point and a destination point.
     *
     * @param sourcelat
     * @param sourcelog
     * @param destlat
     * @param destlog
     * @return
     */
    public static String makeURL(double sourcelat, double sourcelog,
                                 double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");

        return urlString.toString();
    }

    /**
     * Decodes poly line.
     *
     * @param encoded
     * @return
     */
    public static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static Location getLastKnownLoaction(Context context) {

        LocationManager manager;

        manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setPowerRequirement(Criteria.POWER_LOW);

        // Gets the best matched provider, and only if it's on
        String provider = manager.getBestProvider(crit, true);
        Location myLocation = manager.getLastKnownLocation(provider);
        if (myLocation == null) {
            myLocation = manager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }
        return myLocation;
    }


}
