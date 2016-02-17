package com.tracker.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


import com.tracker.journeys.TrackJourneyActivity;
/**
 * This Activity is only used to display a button for go to tracker screen.
 *
 * @author Muhammad Azeem Anwar
 */

public class MainActivity extends Activity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonListener() {

        button = (Button) findViewById(R.id.track);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
// on button click start the tracker screen.
                Intent intent = new Intent(MainActivity.this,
                        TrackJourneyActivity.class);
                startActivity(intent);

            }

        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
