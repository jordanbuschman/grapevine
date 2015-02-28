package com.jar.hfth.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by August on 2/28/15.
 */
public class SettingsActivity extends ActionBarActivity {

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

       //Settings Button on click listener
        Button button = (Button) findViewById(R.id.commit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,
                        "Settings Saved",
                        Toast.LENGTH_SHORT).show();

                Intent goBack = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(goBack);
                
            }
        });

        Button account = (Button) findViewById(R.id.create);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent account = new Intent(SettingsActivity.this, AccountActivity.class);
                startActivity(account);

            }
        });

        Button location = (Button) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(SettingsActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Geocoder geocoder = new Geocoder(SettingsActivity.this, Locale.getDefault());
                   try{
                       List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                       String cityName = addresses.get(0).getAddressLine(0);
                       String stateName = addresses.get(0).getAddressLine(1);
                       String countryName = addresses.get(0).getAddressLine(2);
                       TextView textView = (TextView) findViewById(R.id.textL);
                       textView.setText(cityName + ", " + stateName);
                   }catch(IOException ex) {
                       ex.printStackTrace();
                   }



                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
    }
}
