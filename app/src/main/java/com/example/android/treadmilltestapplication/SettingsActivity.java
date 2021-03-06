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

//import butterknife.ButterKnife;

//public class SettingsActivity extends AppCompatActivity {
public class SettingsActivity extends Activity {

   // @BindView(R.id.male_button) Button maleButton1;
    //  int sex = 0; //if sex = 0 then male, if sex = 1 then female
    String sexString = "";
    int height = 0;
    int weight = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = SettingsActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        sexString = sharedPreferences.getString(getString(R.string.GENDER), "");
        height = sharedPreferences.getInt(getString(R.string.HEIGHT), 60);
        weight = sharedPreferences.getInt(getString(R.string.WEIGHT), 125);

      //  ButterKnife.bind(this);

        EditText weightTextView = (EditText) findViewById(R.id.weight_input);
        weightTextView.setText(String.valueOf(weight));

        EditText heightTextView = (EditText) findViewById(R.id.height_input);
        heightTextView.setText(String.valueOf(height));

        TextView tempMaleText = (TextView) findViewById(R.id.male_button_selected);
        TextView tempFemaleText = (TextView) findViewById(R.id.female_button_selected);

        Button maleButton = (Button) findViewById(R.id.male_button);
        Button femaleButton = (Button) findViewById(R.id.female_button);

        switch (sexString) {
            case "Female":
                femaleButton.setBackgroundResource(R.drawable.filled_in_background);
                maleButton.setBackgroundResource(R.drawable.rounded_background);
                sexString = "Female";
                break;
            case "Male":
                femaleButton.setBackgroundResource(R.drawable.rounded_background);
                maleButton.setBackgroundResource(R.drawable.filled_in_background);
                sexString = "Male";
                break;
            default:
                femaleButton.setBackgroundResource(R.drawable.rounded_background);
                maleButton.setBackgroundResource(R.drawable.rounded_background);
                break;
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void selectSexMale(View v) {
        Button maleButton = (Button) findViewById(R.id.male_button);
        Button femaleButton = (Button) findViewById(R.id.female_button);

        femaleButton.setBackgroundResource(R.drawable.rounded_background);
        maleButton.setBackgroundResource(R.drawable.filled_in_background);
        sexString = "Male";
    }

    public void selectSexFemale(View v) {
        Button maleButton = (Button) findViewById(R.id.male_button);
        Button femaleButton = (Button) findViewById(R.id.female_button);

        maleButton.setBackgroundResource(R.drawable.rounded_background);
        femaleButton.setBackgroundResource(R.drawable.filled_in_background);
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

        Button maleButton = (Button) findViewById(R.id.male_button);
        Button femaleButton = (Button) findViewById(R.id.female_button);

        femaleButton.setBackgroundResource(R.drawable.rounded_background);
        maleButton.setBackgroundResource(R.drawable.rounded_background);
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
