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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by August on 2/28/15.
 */
public class AddActivity extends ActionBarActivity{
    Context context = this;
    DefaultHttpClient client = new DefaultHttpClient();
    HttpPost posting = new HttpPost("http://getgrapes.org/submit");
    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
    private Spinner grapes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Submit a post");

        final Button button = (Button) findViewById(R.id.button);
        final EditText post = (EditText) findViewById(R.id.editText);


        grapes = (Spinner) findViewById(R.id.spinner);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //no post written
                //
                if(post.getText().length() == 0){
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("No post found");
                    builder.setMessage("Please enter a post")
                            .setPositiveButton("update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setNegativeButton("not now", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    Intent intent = new Intent(AddActivity.this, MainActivity.class);

                                    startActivity(intent);
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.show();

                }


                int grapez = 0;
                switch (String.valueOf(grapes.getSelectedItem())){
                    case "Food":
                        grapez = 2;
                        break;
                    case "Shelter":
                        grapez = 3;
                        break;
                    case "Favors":
                        grapez = 4;
                        break;
                    default:
                        grapez = 1;
                        break;
                }

                //no phone number specified
                //

                //send to database
                //
                enableStrictMode();
                JSONObject result = new JSONObject();
                try {

                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("text", post.getText().toString()));
                    pairs.add(new BasicNameValuePair("grape", Integer.toString(grapez) ));
                    pairs.add(new BasicNameValuePair("loc", preferences.getString("Location", "null") ));
                    pairs.add(new BasicNameValuePair("token", preferences.getString("Key", "null")));
                    pairs.add(new BasicNameValuePair("user", preferences.getString("User", "null")));
                    posting.setEntity(new UrlEncodedFormEntity(pairs));
                    HttpResponse response = client.execute(posting);
                 


                 //   String t = EntityUtils.toString(response.getEntity());
               //     preferences.edit().putString("Key", t).commit();

                }catch(Exception e){
                    //throw new RuntimeException(e);
                    Toast.makeText(getApplicationContext(), "cannot communicate with server", Toast.LENGTH_SHORT).show();
                    Log.e("MYAPP", "exception", e);
                }
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);



            }
        });


    }
}
