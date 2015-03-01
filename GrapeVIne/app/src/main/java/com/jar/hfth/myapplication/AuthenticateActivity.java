package com.jar.hfth.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by August on 2/28/15.
 */
public class AuthenticateActivity extends ActionBarActivity {
    DefaultHttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost("http://getgrapes.org/register");
    Context context = this;

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        setTitle("Authenticate");
        final EditText number = (EditText) findViewById(R.id.editText1);
        final EditText password = (EditText) findViewById(R.id.editText2);
        final Button button = (Button) findViewById(R.id.button2);
        final EditText name = (EditText) findViewById(R.id.username);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(number.getText().length() != 10){
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Invalid Phone Number");
                    builder.setMessage("Please enter a 10 digit phone number with no spaces or dashes")
                            .setPositiveButton("update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // Create the AlertDialog object and return it
                    builder.show();

                }
                //no phone number specified
                //
                else if(password.getText().length() == 0){
                    AlertDialog.Builder setter = new AlertDialog.Builder(context);
                    setter.setTitle("Password error");
                    setter.setMessage("Please enter a valid password")
                            .setPositiveButton("edit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // Create the AlertDialog object and return it
                    setter.show();

                }
                //no user name specified
                else if(name.getText().length() == 0){
                    AlertDialog.Builder setter = new AlertDialog.Builder(context);
                    setter.setTitle("Username error");
                    setter.setMessage("Please enter a valid user name")
                            .setPositiveButton("edit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // Create the AlertDialog object and return it
                    setter.show();

                }
                else {
                    //send to database
                    enableStrictMode();
                    JSONObject result = new JSONObject();
                    try {

                        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("phoneNumber", number.getText().toString()));
                        pairs.add(new BasicNameValuePair("password", password.getText().toString()));
                        post.setEntity(new UrlEncodedFormEntity(pairs));
                        HttpResponse response = client.execute(post);
                        String t = EntityUtils.toString(response.getEntity());
                        preferences.edit().putString("Key", t).commit();
                        preferences.edit().putString("User", name.getText().toString()).commit();
                        Intent intent = new Intent(AuthenticateActivity.this, MainActivity.class);
                        startActivity(intent);

                    } catch (Exception e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(getApplicationContext(), "cannot communicate with server", Toast.LENGTH_SHORT).show();
                        Log.e("MYAPP", "exception", e);
                    }
                }
            }
        });


    }
}
