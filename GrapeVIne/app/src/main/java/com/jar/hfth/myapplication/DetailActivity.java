package com.jar.hfth.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by August on 3/1/15.
 */
public class DetailActivity extends ActionBarActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        setTitle("Post Detail");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            /*
            intent.putExtra("date", post.getDate());
                            intent.putExtra("text", post.getText());
                            intent.putExtra("views", post.getViews());
                            intent.putExtra("phone", post.getPhone());
                            intent.putExtra("user", post.getUser());
                            intent.putExtra("title", post.getTitle());
             */
            String text = intent.getStringExtra("text");
            int views = intent.getIntExtra("views", 0);
           final String phone = intent.getStringExtra("phone");
            String user = intent.getStringExtra("user");
            String title = intent.getStringExtra("title");


            TextView Text = (TextView) findViewById(R.id.post);
            TextView Title = (TextView) findViewById(R.id.title);
            TextView User = (TextView) findViewById(R.id.user);
            TextView Views = (TextView) findViewById(R.id.views);
            Button button = (Button) findViewById(R.id.sendText);

            Title.setText("Title: " +title);
            Text.setText("Post\n" +text);
            User.setText("User: " +user);
            Views.setText("Views: " + views);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", phone);
                    smsIntent.putExtra("sms_body","Hey, I saw your post on Grape Vine!");
                    startActivity(smsIntent); */
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phone));

                    sendIntent.putExtra("sms_body", "Hey, I saw your post on Grape Vine!");

                    startActivity(sendIntent);
                }
            });





        }
    }
}
