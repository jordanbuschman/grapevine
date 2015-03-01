package com.jar.hfth.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by August on 2/28/15.
 */
public class AccountActivity extends ActionBarActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Create an Account");

        final Button button = (Button) findViewById(R.id.button);
        final EditText post = (EditText) findViewById(R.id.editText);
      //  final EditText number = (EditText) findViewById(R.id.editText2);
        final Spinner grapes = (Spinner) findViewById(R.id.spinner);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post.getText().equals("write your post")){

                }

            }
        });


    }
}
