package com.example.android.quakereport;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lal on 29-12-2017.
 */

public class EarthquakeCallbackTask extends AsyncTask<String, Void, List<Earthquake>> {

  public final Callback<List<Earthquake>> callback;

  public EarthquakeCallbackTask(Callback<List<Earthquake>> callback) {
    this.callback = callback;
  }

  @Override protected List<Earthquake> doInBackground(String... urls) {
    // Don't perform the request if there are no URLs, or the first URL is null.
    if (urls.length < 1 || urls[0] == null) {
      return null;
    }

    List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
    return result;
  }

  @Override protected void onPostExecute(List<Earthquake> data) {
    // Clear the adapter of previous earthquake data
    callback.onComplete(data);
  }
}
