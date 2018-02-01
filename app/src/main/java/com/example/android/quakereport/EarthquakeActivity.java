/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;

public class EarthquakeActivity extends AppCompatActivity implements Callback<List<Earthquake>>,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    /** TextView that is displayed when the list is empty */
    TextView emptStateTextView;

    View loadingIndicator;

    /** Adapter for the list of earthquakes */
    public EarthquakeAdapter adapter;
    SharedPreferences sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.earthquake_activity);

        // Hide loading indicator because the data has been loaded
        loadingIndicator = findViewById(R.id.loading_spinner);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        emptStateTextView = (TextView) findViewById(R.id.empty_text);
        earthquakeListView.setEmptyView(emptStateTextView);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        // Create a new adapter that takes an empty list of earthquakes as input
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        /*EarthquakeCallbackTask callbackTask = new EarthquakeCallbackTask(this);*/
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current earthquake that was clicked on



                Earthquake currentEarthquake = adapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getEarthquakeDetails());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //Get the refrence of the Connectivity Manager to check state of the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
            Context.CONNECTIVITY_SERVICE);
        //Get the details of the currentlly active default network.
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            loadQuakes(false);
        }else {
            // Set empty state text to display "No earthquakes found."
            emptStateTextView.setText(R.string.no_internet_connection);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    private void loadQuakes(boolean forceRefresh){
        String minMagnitude = sharedPrefs.getString(
            getString(R.string.settings_min_magnitude_key),
            getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(SAMPLE_JSON_RESPONSE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");
        TaskLoader.getInstance().loadEarthquakes(uriBuilder.toString(), forceRefresh, this);
    }


    @Override public void onComplete(List<Earthquake> data) {

        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        adapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
           adapter.addAll(data);
        }
        // Set empty state text to display "No earthquakes found."
        emptStateTextView.setText(R.string.no_earthwake_data);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadQuakes(true);
    }
}
