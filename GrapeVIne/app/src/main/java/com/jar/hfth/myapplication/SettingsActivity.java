package com.jar.hfth.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by August on 2/28/15.
 */
public class SettingsActivity extends ActionBarActivity {

    GPSTracker gps;
    DefaultHttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost("http://getgrapes.org");

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

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
                       //get city/state from lat and long
                       List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                       String cityName = addresses.get(0).getAddressLine(0);
                       String stateName = addresses.get(0).getAddressLine(1);
                       String countryName = addresses.get(0).getAddressLine(2);

                      //set the text view to update
                       TextView textView = (TextView) findViewById(R.id.textL);
                       textView.setText(stateName);

                       //toast notification
                       Toast.makeText(getApplicationContext(), "Your Location is "+ stateName , Toast.LENGTH_SHORT).show();

                      //send the data to back end
                       /*
                       List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                       pairs.add(new BasicNameValuePair("location", stateName));
                       post.setEntity(new UrlEncodedFormEntity(pairs));
                       HttpResponse response = client.execute(post);   */

                       try {
                           enableStrictMode();
                           JSONObject result = new JSONObject();
                           result.put("location", stateName);
                           StringEntity se = new StringEntity(result.toString());
                           post.setEntity(se);
                           HttpResponse httpresponse = client.execute(post);
                       } catch (Exception e) {
                           //throw new RuntimeException(e);
                           Toast.makeText(getApplicationContext(), "cannot communicate with server" , Toast.LENGTH_SHORT).show();
                           Log.e("MYAPP", "exception", e);
                       }

                   }catch(IOException ex) {
                       ex.printStackTrace();
                   }


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
