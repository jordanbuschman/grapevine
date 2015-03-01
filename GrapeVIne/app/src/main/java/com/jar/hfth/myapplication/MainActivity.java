package com.jar.hfth.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    Context context = this;
    DefaultHttpClient client = new DefaultHttpClient();
   // HttpPost post = new HttpPost("172.16.21.81:3000");
    HttpPost post = new HttpPost("http://getgrapes.org/location");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final int hubId = preferences.getInt("hubId", 1);
        final boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Authentication");
        builder.setMessage("This app authenticates users via phone number, would you like to authenticate now?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
                        Intent intent = new Intent(MainActivity.this, AuthenticateActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("firstrun", true).apply();
                        startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        if (firstrun) {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                preferences.edit().putInt("hubId", 1).commit();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                preferences.edit().putInt("hubId", 2).commit();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                preferences.edit().putInt("hubId", 3).commit();
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                preferences.edit().putInt("hubId", 4).commit();
                break;

        }

        final int getHub = preferences.getInt("hubId", 1);
       // Toast.makeText(getApplicationContext(), "value: " + getHub, Toast.LENGTH_SHORT).show();

        try {
            enableStrictMode();
            JSONObject result = new JSONObject();
            String area = preferences.getString("Location", "null");
            if (area.equals("null")) {

                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Location not found");
                builder.setMessage("Please update your location")
                        .setPositiveButton("update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               Intent update = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(update);
                            }
                        })
                        .setNegativeButton("not now", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                // Create the AlertDialog object and return it
                    builder.show();

            } else
            {
                try {
                    //send data
                    result.put("loc", area);
                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("loc", area));
                    pairs.add(new BasicNameValuePair("grove", Integer.toString(getHub)));
                    post.setEntity(new UrlEncodedFormEntity(pairs));
                    HttpResponse response = client.execute(post);

                    //receive data
                    String t = EntityUtils.toString(response.getEntity());
                    //Toast.makeText(getApplicationContext(), "" + t, Toast.LENGTH_LONG).show();

                    JSONArray info = new JSONArray(t);
                    final List<Posts> topic = new ArrayList<>();

                  //  Toast.makeText(getApplicationContext(), "" + info.length(), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < info.length(); i++)
                    {
                        //get info from JSON objects
                        JSONObject object = info.getJSONObject(i);
                        long date = object.getLong("_timestamp");
                        String text = object.getString("_text");
                        int views = object.getInt("_views");
                        String phone = object.getString("_phoneNumber");
                        String user = object.getString("_username");
                        String title = object.getString("_title");
                        int groove = object.getInt("_grove");
                        String id = object.getString("_id");

                        //put data into listview
                        //Posts(long date, String text, int views, String phone, String user)
                        topic.add(new Posts(date, text, views, phone ,user, title, groove, id));
                    }

                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(new PostAdapter(this, R.layout.activity_list_single, topic));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Posts post = topic.get(position);
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("date", post.getDate());
                            intent.putExtra("text", post.getText());
                            intent.putExtra("views", post.getViews());
                            intent.putExtra("phone", post.getPhone());
                            intent.putExtra("user", post.getUser());
                            intent.putExtra("title", post.getTitle());
                            intent.putExtra("grove", post.getGrove());
                            intent.putExtra("id", post.getId());
                            startActivity(intent);

                        }
                    });




                }catch(JSONException e){
                    //throw new RuntimeException(e);
                    Toast.makeText(getApplicationContext(), "cannot communicate with server", Toast.LENGTH_SHORT).show();
                    Log.e("MYAPP", "exception", e);
                }
            }
           }catch(IOException ex) {
            ex.printStackTrace();
        }

    }
    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_add){
            Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(addIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }

}
