/*
 * Copyright (c) 2015 Muhammad Azeem Anwar.
 */
package com.tracker.journeys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tracker.app.R;
import com.tracker.data.DCJourney;
import com.tracker.data.DCJourneyPoint;
import com.tracker.database.JourneyDataSource;
import com.tracker.utilities.LocationUtilities;
import java.util.ArrayList;

/**
 * Activity for displaying details of the journey.
 *
 * @author Muhammad Azeem Anwar
 */

public class JourneyDetailsActivity extends AppCompatActivity {
    private int[] txtIds = {R.id.startTimeTxt, R.id.endTimeTxt,
            R.id.durationTxt, R.id.distanceTxt, R.id.expenseTxt,
            R.id.startAddrTxt, R.id.endAddrTxt, R.id.avgSpeedTxt,
            R.id.emissionTxt};

    private EditText descEdit;
    private TextView regText;
    private Button businessBtn;
    private Button personalBtn;
    private ArrayList<DCJourney> data = new ArrayList<>();
    private DCJourney journey;
    private boolean isModify = false;
    private boolean isBusiness = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);
        descEdit = (EditText) findViewById(R.id.descEdit);
        regText = (EditText) findViewById(R.id.regTxt);
        businessBtn = (Button) findViewById(R.id.businessBtn);
        personalBtn = (Button) findViewById(R.id.personalBtn);
        descEdit.setOnClickListener(onClickListener);
        regText.setOnClickListener(onClickListener);
        businessBtn.setOnClickListener(onClickListener);
        personalBtn.setOnClickListener(onClickListener);
        descEdit.setOnEditorActionListener(onEditorActionListener);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setJourneyEmission();
    }

    private void init() {
        ArrayList<DCJourneyPoint> points;
        if (getIntent().getExtras() != null) {
               journey = getIntent().getExtras().getParcelable("journey");
            isModify = getIntent().getExtras().getBoolean("modify", false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(isModify);
            getSupportActionBar().setHomeButtonEnabled(isModify);
            // Gets journey points
            JourneyDataSource dataSource = new JourneyDataSource(
                    JourneyDetailsActivity.this);
            dataSource.open();
            points = dataSource.getJourneyPoints(journey.getId());
            dataSource.close();
            // Check if user is modifying existing journey
            if (isModify) {
                if (journey.getDescription() != null)
                    descEdit.setText(journey.getDescription().toString());
                isBusiness = journey.isBusiness();

            }
            for (int i = 0; i < txtIds.length; i++) {
                TextView txt = (TextView) findViewById(txtIds[i]);

                if (txtIds[i] == R.id.endAddrTxt)
                    new GetAddressTask().execute(points);
                if (journey.getValuesByOrder(i) != null)
                    txt.setText(journey.getValuesByOrder(i).toString());
            }

            if (journey.getVehicle_reg() != null
                    && !journey.getVehicle_reg().equals("")) {
                regText.setText(journey.getVehicle_reg());

            } else {
                regText.setText("AJ54ZBR");
            }
                     // If emissions have not been yet calculated it will calculate them.
            if (journey.getEmissions() <= 0) {
                setJourneyEmission();
            } else {
                for (int i = 0; i < txtIds.length; i++) {
                    TextView txt = (TextView) findViewById(txtIds[i]);
                    if (txtIds[i] == R.id.emissionTxt)
                        txt.setText(""
                                + DCJourney.round(
                                journey.getRoundedEmissions(), 1)
                                + " kg");
                }
            }
        } else {
            getJourney();
            isModify = true;
            getSupportActionBar().setDisplayHomeAsUpEnabled(isModify);
            getSupportActionBar().setHomeButtonEnabled(isModify);
            // Gets journey points
            JourneyDataSource dataSource = new JourneyDataSource(
                    JourneyDetailsActivity.this);
            dataSource.open();
            points = dataSource.getJourneyPoints(journey.getId());
            dataSource.close();
            // Check if user is modifying existing journey
            if (isModify) {
                if (journey.getDescription() != null)
                    descEdit.setText(journey.getDescription().toString());
                isBusiness = journey.isBusiness();
            }

            for (int i = 0; i < txtIds.length; i++) {
                TextView txt = (TextView) findViewById(txtIds[i]);

                if (txtIds[i] == R.id.endAddrTxt)
                    new GetAddressTask().execute(points);
                if (journey.getValuesByOrder(i) != null)
                    txt.setText(journey.getValuesByOrder(i).toString());
            }
            // If emissions have not been yet calculated it will calculate them.
            if (journey.getEmissions() <= 0) {
                setJourneyEmission();
            } else {
                              for (int i = 0; i < txtIds.length; i++) {
                    TextView txt = (TextView) findViewById(txtIds[i]);
                    if (txtIds[i] == R.id.emissionTxt)
                        txt.setText(""
                                + DCJourney.round(
                                journey.getRoundedEmissions(), 1)
                                + " kg");
                }
            }

        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Check if user touched business button
            Boolean ss = journey.isClaimedFor();
            if (!journey.isClaimedFor()) {
                if (v == businessBtn || v == personalBtn) {
                    selectBusinessButton(v);
                } else if (v == descEdit)
                    startEditingText(v, true);
            }
        }
    };

    private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (!journey.isClaimedFor()) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    startEditingText(v, false);

                }
                return false;

            } else {
                return true;

            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Check if user came from ReviewActivity, if so then user should go
            // back there
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            journey.setDescription(descEdit.getText().toString());
            journey.setVehicle_reg(regText.getText().toString());
            journey.setBusiness(isBusiness);
            new SaveTask().execute();
            finish();
        } else if (item.getItemId() == R.id.action_delete) {
            new AlertDialog.Builder(JourneyDetailsActivity.this)
                    .setTitle("Delete Journey")
                    .setMessage(
                            getResources().getString(
                                    R.string.journey_delete_warning2))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {

                                    long journeyId = journey.getId();

                                    boolean isClaime = journey
                                            .isClaimedFor();
                                    System.out.println(isClaime);
                                    // Deletes journey
                                    if (!isClaime) {
                                        JourneyDataSource dataSource = new JourneyDataSource(
                                                JourneyDetailsActivity.this);
                                        dataSource.open();
                                        dataSource
                                                .deleteJourney(journey);
                                        dataSource.close();
                                        /* Journey is deleted, now delete in background all journey points and  behaviour points associated
                                         with deleted journey */
                                        new deleteJourneyTask(
                                                JourneyDetailsActivity.this)
                                                .execute(journeyId);
                                        // Finish this activity
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("finish",
                                                true);
                                        setResult(RESULT_OK,
                                                returnIntent);
                                        finish();

                                    }
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                }
                            }).show();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.journey_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Overrides back soft key. If user came from TrackingActivity, then back
     * button shouldn't take him to previous screen. If he came from
     * ReviewActivity then back button should take him back to that screen.
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (isModify) {
            finish();
        }
    }

    /**
     * Gets start and end address of the journey in the background
     */
    private class GetAddressTask extends
            AsyncTask<ArrayList<DCJourneyPoint>, Void, String[]> {
        @Override
        protected String[] doInBackground(ArrayList<DCJourneyPoint>... points) {
            String[] results = new String[2];
            results[0] = " ";
            results[1] = " ";

            if (points[0].size() == 0) {
                results[0] = "-";
                results[1] = "-";
            } else {// Get address from first point, which is where user start tracking
                results[0] = LocationUtilities.getAddressFromLatlng(
                        JourneyDetailsActivity.this, points[0].get(0).getLat(),
                        points[0].get(0).getLng());
                String startCity = LocationUtilities.getCityFromLatlng(
                        JourneyDetailsActivity.this, points[0].get(0).getLat(),
                        points[0].get(0).getLng());
                journey.setStartCity(startCity);// Get address from the last point, which is where user stopped tracking
                results[1] = LocationUtilities.getAddressFromLatlng(
                        JourneyDetailsActivity.this,
                        points[0].get(points[0].size() - 1).getLat(), points[0]
                                .get(points[0].size() - 1).getLng());
                String endCity = LocationUtilities.getCityFromLatlng(
                        JourneyDetailsActivity.this,
                        points[0].get(points[0].size() - 1).getLat(), points[0]
                                .get(points[0].size() - 1).getLng());
                journey.setEndCity(endCity);
            }

            return results;
        }

        @Override
        protected void onPostExecute(String[] results) {
            super.onPostExecute(results);
            TextView startTxt = (TextView) findViewById(R.id.startAddrTxt);
            TextView endTxt = (TextView) findViewById(R.id.endAddrTxt);
            journey.setStartAddr(results[0]);
            journey.setEndAddr(results[1]);
            startTxt.setText(results[0]);
            endTxt.setText(results[1]);
        }
    }

    /**
     * Selects business or personal button.
     *
     * @param v
     */
    private void selectBusinessButton(View v) {
        if (v == businessBtn) {
            if (!isBusiness) {
                // Deselect personal button
                personalBtn.setTextColor(getResources().getColor(
                        R.color.black));
                // Select business button
                businessBtn
                        .setTextColor(getResources().getColor(R.color.white));
                // Set business button is selected
                isBusiness = true;
            }
        }
        // Check if user touched personal button
        else if (v == personalBtn) {
            if (isBusiness) {
                // Deselect business option
                businessBtn.setTextColor(getResources().getColor(
                        R.color.black));
                // Select personal option
                personalBtn
                        .setTextColor(getResources().getColor(R.color.white));
                // Choose personal option
                isBusiness = false;
            }
        }
    }

    /**
     * Method for edit text.
     * @param v ,editing
     */

    private void startEditingText(View v, boolean editing) {
        if (!journey.isClaimedFor()) {

            if (v == descEdit) {
                // Starts editing
                if (editing)
                    descEdit.setCursorVisible(true);
                    // Stops editing
                else
                    descEdit.setCursorVisible(false);
            }
        }
    }

    /**
     * Deletes behaviour and journey points associated with given journey in the
     * background.
     */
    private class deleteJourneyTask extends AsyncTask<Long, Void, Long> {
        private Context context;

        public deleteJourneyTask(Context context) {
            this.context = context;
        }

        @Override
        protected Long doInBackground(Long... params) {
            JourneyDataSource dataSource = new JourneyDataSource(context);
            dataSource.open();
            dataSource.deleteBehaviourPoints(params[0]);
            dataSource.deleteJourneyPoints(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Long journeyId) {
            super.onPostExecute(journeyId);
        }
    }


    private void getJourney() {
        JourneyDataSource dataSource = new JourneyDataSource(
                JourneyDetailsActivity.this);
        dataSource.open();
        ArrayList<DCJourney> journeys = dataSource.getAllJourneys();
        dataSource.close();
        String d = "";
        for (int i = 0; i < journeys.size(); i++) {
            if (journeys.get(i).getCreateDate() != null) {
                if (!journeys.get(i).getCreateDate().equals(d)) {
                    data.add(journeys.get(i));
                    journey = journeys.get(0);
                    d = journeys.get(i).getCreateDate();
                }
                data.add(journeys.get(i));
            }
        }
    }

    private void setJourneyEmission() {
        // Get default vehicle of the user
        double emissionDistance = Double
                .valueOf(journey.getDistance() * 1.609344);
        int gkm = 54;
        /** Convert the km to miles float gm = gkm * 0.621f;
         Multiply this by the number of miles travelled */
        float netEmissions = (float) (gkm * emissionDistance);
        // Add 15% to get the final reading
        float grossEmissions = netEmissions + ((netEmissions / 100) * 15);
        grossEmissions = grossEmissions / 1000;
        journey.setEmissions(grossEmissions);        // Set journey's emissions
        for (int j = 0; j < txtIds.length; j++) {
            TextView txt = (TextView) findViewById(txtIds[j]);

            if (txtIds[j] == R.id.emissionTxt)
                txt.setText(""
                        + DCJourney.round(journey.getRoundedEmissions(), 1)
                        + " kg");
        }
    }
    private class SaveTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            JourneyDataSource dataSource = new JourneyDataSource(
                    JourneyDetailsActivity.this);

            dataSource.open();
            dataSource.updateJourney(journey);
            dataSource.close();
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}