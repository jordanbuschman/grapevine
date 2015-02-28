package com.jar.hfth.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by August on 2/28/15.
 */
public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
    }
}
