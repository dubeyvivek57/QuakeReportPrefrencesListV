package com.example.android.quakereport;

import java.util.List;

/**
 * Created by Lal on 29-12-2017.
 */

public class TaskLoader {
  public static TaskLoader INSTANCE;

  List<Earthquake> earthquakes = null;

  public static TaskLoader getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TaskLoader();
    }
    return INSTANCE;
  }

  public void loadEarthquakes(String url, boolean forceRefresh, final Callback<List<Earthquake>> callback) {
    if (earthquakes != null && !forceRefresh) {
      callback.onComplete(earthquakes);
      return;
    }
    EarthquakeCallbackTask task = new EarthquakeCallbackTask(new Callback<List<Earthquake>>() {
      @Override public void onComplete(List<Earthquake> data) {
        earthquakes = data;
        callback.onComplete(data);
      }
    });
    task.execute(url);
  }

  public void cleanup(){
    earthquakes = null;
  }
}
