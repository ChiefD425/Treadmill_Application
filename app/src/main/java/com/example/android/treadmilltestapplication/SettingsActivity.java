package com.example.android.treadmilltestapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

//public class SettingsActivity extends AppCompatActivity {
public class SettingsActivity extends Activity {
    //  int sex = 0; //if sex = 0 then male, if sex = 1 then female
    String sexString = "";
    int height = 0;
    int weight = 0;
    float time = 0;
    float incline = 0;
    float speed = 0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        sexString = sharedPreferences.getString(getString(R.string.GENDER), "");
        height = sharedPreferences.getInt(getString(R.string.HEIGHT), 60);
        weight = sharedPreferences.getInt(getString(R.string.WEIGHT), 125);

        EditText weightTextView = (EditText) findViewById(R.id.weight_input);
        weightTextView.setText(String.valueOf(weight));

        EditText heightTextView = (EditText) findViewById(R.id.height_input);
        heightTextView.setText(String.valueOf(height));

        TextView tempMaleText = (TextView) findViewById(R.id.male_button_selected);
        TextView tempFemaleText = (TextView) findViewById(R.id.female_button_selected);

        Button maleButton = (Button) findViewById(R.id.male_button);
        Button femaleButton = (Button) findViewById(R.id.female_button);

        if (sexString.equals("Female")) {
            tempMaleText.setText("");
            tempFemaleText.setText("selected");
        } else if (sexString.equals("Male")) {
            tempFemaleText.setText("");
            tempMaleText.setText("selected");
        } else {
            tempFemaleText.setText("");
            tempMaleText.setText("");
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void calculateResults(View v) {

        double milesPerMinute = 0;
        double weightInKilograms = 0;
        double caloriesPerMinute = 0;
        double totalCalorieBurn = 0;
        int steps = 0;
        double distance = 0;

        //grab the height
        EditText heightTextView = (EditText) findViewById(R.id.height_input);
//todo fix error with blank input
        height = Integer.parseInt(heightTextView.getText().toString());

        //grab the weight
        EditText weightTextView = (EditText) findViewById(R.id.weight_input);
        weight = Integer.parseInt(weightTextView.getText().toString());

        //grab the time
        EditText timeTextView = (EditText) findViewById(R.id.time_on_treadmill);
        time = Float.parseFloat(timeTextView.getText().toString());

        //grab the speed
        EditText speedTextView = (EditText) findViewById(R.id.speed);
        speed = Float.parseFloat(speedTextView.getText().toString());

        //grab the incline
        EditText inclineTextView = (EditText) findViewById(R.id.incline_percentage);
        incline = Float.parseFloat(inclineTextView.getText().toString());
        incline = incline / 100;

        milesPerMinute = speed * 26.8;
        weightInKilograms = weight / 2.2;
        caloriesPerMinute = (oxygenUsed(milesPerMinute) * weightInKilograms) / 200;
        totalCalorieBurn = caloriesPerMinute * time;
        steps = (int) ((speed / 60) * stepsPerMile() * time);
        distance = time * (speed / 60);

        displayResults(totalCalorieBurn, steps, distance);


    }

    private void displayResults(double totalCalorieBurn, int steps, double distance) {
        TextView resultsTextView = (TextView) findViewById(R.id.results);

        String workoutSummary = "";
        workoutSummary += "Congratulations!";
        workoutSummary += "\nYou burned " + (int) round(totalCalorieBurn, 0) + " Calories";
        workoutSummary += "\nYou took " + (int) round(steps, 0) + " Steps";
        workoutSummary += "\nYou traveled " + round(distance, 2) + " Miles";

        resultsTextView.setText(workoutSummary);
    }

    private double oxygenUsed(double milesPerMinute) {

        double oxygenUsedCalculation;

        if (speed > 3.7) {
            oxygenUsedCalculation = (milesPerMinute * 0.2);
            oxygenUsedCalculation += (milesPerMinute * incline * 0.9);
            oxygenUsedCalculation += 3.5;
        } else {
            oxygenUsedCalculation = (milesPerMinute * 0.1);
            oxygenUsedCalculation += (milesPerMinute * incline * 1.8);
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

    public void selectSexMale(View v) {
        TextView tempMaleText = (TextView) findViewById(R.id.male_button_selected);
        TextView tempFemaleText = (TextView) findViewById(R.id.female_button_selected);

        tempFemaleText.setText("");
        tempMaleText.setText("selected");
        sexString = "Male";
    }

    public void selectSexFemale(View v) {
        TextView tempMaleText = (TextView) findViewById(R.id.male_button_selected);
        TextView tempFemaleText = (TextView) findViewById(R.id.female_button_selected);

        tempMaleText.setText("");
        tempFemaleText.setText("selected");
        sexString = "Female";
    }

    public void saveSettings(View v) {

        EditText weightTextView = (EditText) findViewById(R.id.weight_input);
        weight = Integer.parseInt(weightTextView.getText().toString());

        EditText heightTextView = (EditText) findViewById(R.id.height_input);
        height = Integer.parseInt(heightTextView.getText().toString());

        if (height == 0 || weight == 0) {
            Toast.makeText(getApplicationContext(), "Please input your Height and Weight so calculations can be made", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.GENDER), sexString);
        editor.putInt(getString(R.string.HEIGHT), height);
        editor.putInt(getString(R.string.WEIGHT), weight);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public void clearSettings(View v) {

        EditText weightTextView = (EditText) findViewById(R.id.weight_input);
        weight = 0;
        weightTextView.setText(String.valueOf(weight));

        EditText heightTextView = (EditText) findViewById(R.id.height_input);
        height = 0;
        heightTextView.setText(String.valueOf(height));

        TextView tempMaleText = (TextView) findViewById(R.id.male_button_selected);
        TextView tempFemaleText = (TextView) findViewById(R.id.female_button_selected);

        tempFemaleText.setText("");
        tempMaleText.setText("");
        sexString = "";

        SharedPreferences sharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.GENDER), sexString);
        editor.putInt(getString(R.string.HEIGHT), height);
        editor.putInt(getString(R.string.WEIGHT), weight);
        editor.apply();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
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
                "Settings Page", // TODO: Define a title for the content shown.
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
