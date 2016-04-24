package com.example.android.treadmilltestapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    String sexString = "";
    int height = 0;
    int weight = 0;
    double time = 0;
    double incline = 0;
    double speed = 0;
    Button buttonAdd;
    LinearLayout container;
    double totalCalorieBurn = 0;
    int totalSteps = 0;
    double totalDistance = 0;
    float numberOfSegments = 0;
    boolean workoutSubmitted = false;
    private GoogleApiClient mClient = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void setDate(View v) {
        PickerDialogs pickerDialogs = new PickerDialogs();
        pickerDialogs.show(getFragmentManager(), "date picker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        sexString = sharedPreferences.getString(getString(R.string.GENDER), "Not Defined");
        height = sharedPreferences.getInt(getString(R.string.HEIGHT), 60);
        weight = sharedPreferences.getInt(getString(R.string.WEIGHT), 125);
        buttonAdd = (Button) findViewById(R.id.add_segment_button);

        if (sexString.equals("Not Defined") || height == 0 || weight == 0) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Welcome to the Treadmill Calculator, please input your Gender, Weight, and Height", Toast.LENGTH_LONG).show();
        }

        container = (LinearLayout) findViewById(R.id.container);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimeInput(v);
                getSpeedInput(v);
                getInclineInput(v);
                addNewView(time, speed, incline);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void addNewView(final double treadmillTime, final Double treadmillSpeed, final Double treadmillIncline) {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View newView = layoutInflater.inflate(R.layout.row, null);

        final TextView timeView = (TextView) newView.findViewById(R.id.length_of_run);
        timeView.setText(String.valueOf(treadmillTime));

        final TextView speedView = (TextView) newView.findViewById(R.id.speed_of_run);
        speedView.setText(String.valueOf(treadmillSpeed));

        final TextView inclineView = (TextView) newView.findViewById(R.id.incline_of_run);
        inclineView.setText(String.valueOf(100 * treadmillIncline));

        TextView numberOfSegmentsView = (TextView) newView.findViewById(R.id.segment_id);
        numberOfSegmentsView.setText(String.valueOf(numberOfSegments));

        totalCalorieBurn += calculateCalorieBurn();
        totalSteps += calculateSteps();
        totalDistance += calculateDistance();


        Button buttonRemove = (Button) newView
                .findViewById(R.id.delete_button);

        buttonRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView temporaryTimeView = (TextView) newView.findViewById(R.id.length_of_run);
                TextView temporarySpeedView = (TextView) newView.findViewById(R.id.speed_of_run);
                TextView temporaryInclineView = (TextView) newView.findViewById(R.id.incline_of_run);

                time = Double.parseDouble(temporaryTimeView.getText().toString());
                speed = Double.parseDouble(temporarySpeedView.getText().toString());
                incline = Double.parseDouble(temporaryInclineView.getText().toString()) / 100;


                totalCalorieBurn -= calculateCalorieBurn();
                totalSteps -= calculateSteps();
                totalDistance -= calculateDistance();

                ((LinearLayout) newView.getParent())
                        .removeView(newView);

                displayResults();


            }
        });

        container.addView(newView);

        displayResults();
    }

    private void getTimeInput(View v) {
        EditText timeTextView = (EditText) findViewById(R.id.time_on_treadmill);

        try {
            time = Double.parseDouble(timeTextView.getText().toString());
        } catch (NumberFormatException ex) {
            Toast.makeText(getApplicationContext(), "Error: Run Duration Missing", Toast.LENGTH_SHORT).show();
            return;
        }

        time = Double.parseDouble(timeTextView.getText().toString());

    }

    private void getSpeedInput(View v) {

        EditText speedTextView = (EditText) findViewById(R.id.speed);

        try {
            speed = Double.parseDouble(speedTextView.getText().toString());
        } catch (NumberFormatException ex) {
            Toast.makeText(getApplicationContext(), "Error: Treadmill Speed Missing", Toast.LENGTH_SHORT).show();
            return;
        }

        speed = Double.parseDouble(speedTextView.getText().toString());

    }

    private void getInclineInput(View v) {

        EditText inclineTextView = (EditText) findViewById(R.id.incline_percentage);
        incline = Double.parseDouble(inclineTextView.getText().toString());
        incline = incline / 100;

    }

    public void calculateResults(View v) {

        long currentTime = SystemClock.currentThreadTimeMillis();
        long lengthOfWorkout = (new Double(time)).longValue();
        long startTime = currentTime - lengthOfWorkout;
        long endTime = startTime + lengthOfWorkout;

        //grab the time
        getTimeInput(v);

        //grab the speed
        getSpeedInput(v);

        //grab the incline
        getInclineInput(v);

        totalCalorieBurn += calculateCalorieBurn();
        totalSteps += calculateSteps();
        totalDistance += calculateDistance();

        displayResults();

        if (workoutSubmitted = true) {
            return;
        }

        Session session = new Session.Builder()
                .setName("test data")
                .setDescription("test description")
                .setIdentifier("UniqueIdentifierHere")
                .setActivity(FitnessActivities.RUNNING)
                .setStartTime(startTime, TimeUnit.MILLISECONDS)
                .setEndTime(endTime, TimeUnit.MILLISECONDS)
                .build();

        SessionInsertRequest insertRequest = new SessionInsertRequest.Builder()
                .setSession(session)
                .build();

        // Then, invoke the Sessions API to insert the session and await the result,
// which is possible here because of the AsyncTask. Always include a timeout when
// calling await() to avoid hanging that can occur from the service being shutdown
// because of low memory or other conditions.
        com.google.android.gms.common.api.Status insertStatus =
                Fitness.SessionsApi.insertSession(mClient, insertRequest)
                        .await(1, TimeUnit.MINUTES);

// Before querying the session, check to see if the insertion succeeded.
        if (!insertStatus.isSuccess()) {

            return;
        }

// At this point, the session has been inserted and can be read.
        //  Log.i(TAG, "Session insert was successful!");

        workoutSubmitted = true;

    }

    private double calculateDistance() {

        return time * (speed / 60);

    }

    private double calculateSteps() {

        return (int) ((speed / 60) * stepsPerMile() * time);

    }

    public double calculateCalorieBurn() {

        double caloriesPerMinute;
        double metersPerMinute;
        double weightInKilograms;
        double caloriesBurnedThisSegment;

        metersPerMinute = speed * 26.8;
        weightInKilograms = weight / 2.2;

        caloriesPerMinute = (oxygenUsed(metersPerMinute) * weightInKilograms) / 200;
        caloriesBurnedThisSegment = caloriesPerMinute * time;

        return caloriesBurnedThisSegment;

    }

    private void displayResults() {

        TextView resultsTextView = (TextView) findViewById(R.id.results);

        String workoutSummary = "";
        workoutSummary += "Congratulations!";
        workoutSummary += "\nYou burned " + (int) round(totalCalorieBurn, 0) + " Calories";
        workoutSummary += "\nYou took " + (int) round(totalSteps, 0) + " Steps";
        workoutSummary += "\nYou traveled " + round(totalDistance, 2) + " Miles";

        resultsTextView.setText(workoutSummary);
    }

    private double oxygenUsed(double metersPerMinute) {

        double oxygenUsedCalculation;

        if (speed > 3.7) {
            oxygenUsedCalculation = (metersPerMinute * 0.2);
            oxygenUsedCalculation += (metersPerMinute * incline * 0.9);
            oxygenUsedCalculation += 3.5;
        } else {
            oxygenUsedCalculation = (metersPerMinute * 0.1);
            oxygenUsedCalculation += (metersPerMinute * incline * 1.8);
            oxygenUsedCalculation += 3.5;
        }

        return oxygenUsedCalculation;
    }

    private int stepsPerMile() {

        int stepsPerMileCalculation;

        if (speed >= 5) {
            stepsPerMileCalculation = 1084;
            stepsPerMileCalculation += ((143.6 * (60 / speed)) - (13.5 * height));
            return stepsPerMileCalculation;
        }
        if (sexString.equals("Male")) {
            stepsPerMileCalculation = 1916;
        } else
            stepsPerMileCalculation = 1949;

        stepsPerMileCalculation += ((63.4 * (60 / speed)) - (14.1 * height));
        return stepsPerMileCalculation;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.treadmilltestapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.treadmilltestapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
