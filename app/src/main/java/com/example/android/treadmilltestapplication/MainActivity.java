package com.example.android.treadmilltestapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int sex = 0; //if sex = 0 then male, if sex = 1 then female
    String sexString = "";
    int height = 0;
    int weight = 0;
    float time = 0;
    float incline = 0;
    float speed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a new Spinner object
        Spinner spinner = (Spinner) findViewById(R.id.sex_selector_spinner);
        // create an ArrayAdapter using the string array (sex_values in this case) and the default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex_values, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) != null) {

                    sex = position;
                    if (sex == 1) {
                        sexString = "Female";
                    } else
                        sexString = "Male";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        caloriesPerMinute = oxygenUsed(milesPerMinute) * weightInKilograms;
        totalCalorieBurn = caloriesPerMinute * time;
        steps = (int) (stepsPerMile() * time);
        distance = time * (speed / 60);

        displayResults(totalCalorieBurn, steps, distance);


    }

    private void displayResults(double totalCalorieBurn, int steps, double distance) {
        TextView resultsTextView = (TextView) findViewById(R.id.results);

        String workoutSummary = "";
        workoutSummary += "Congratulations!";
        workoutSummary += "\nYou burned " + totalCalorieBurn + " Calories";
        workoutSummary += "\nYou took " + steps + " Steps";
        workoutSummary += "\nYou traveled " + distance + " Miles";

        resultsTextView.setText(workoutSummary);
    }

    private double oxygenUsed(double milesPerMinute) {

        double oxygenUsedCalculation = 0;

        if (speed > 3.7) {
            oxygenUsedCalculation = (milesPerMinute * 0.2);
            oxygenUsedCalculation += (milesPerMinute * incline * 0.9);
            oxygenUsedCalculation += 3.5;
        } else {
            oxygenUsedCalculation = (milesPerMinute * 0.1);
            oxygenUsedCalculation += (milesPerMinute * incline * 1.8);
            oxygenUsedCalculation += 3.5;
            Log.v("milesPerMinute", String.valueOf(milesPerMinute));
            Log.v("oxygenUsed", String.valueOf(oxygenUsedCalculation));
        }

        return oxygenUsedCalculation;
    }

    private int stepsPerMile() {

        int stepsPerMileCalculation = 0;

        if (speed >= 5) {
            stepsPerMileCalculation = 1084;
            stepsPerMileCalculation += ((143.6 * (60 / speed)) - (13.5 * height));
            return stepsPerMileCalculation;
        }
        if (sexString == "Male") {
            stepsPerMileCalculation = 1916;
        } else
            stepsPerMileCalculation = 1949;

        stepsPerMileCalculation += ((63.4 * (60 / speed)) - (14.1 * height));
        return stepsPerMileCalculation;
    }
}
