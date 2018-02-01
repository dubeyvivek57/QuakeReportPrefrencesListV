package com.example.android.quakereport;

/**
 * Created by Lal on 28-12-2017.
 */

public class Earthquake {

  private double mag;
  private String location;
  private long date;
  private String earthquakeDetails;

  public Earthquake(double magnitude, String city, long date1, String url){

    mag = magnitude;
    location = city;
    date = date1;
    earthquakeDetails = url;
  }

  public double getMagnitude(){
    return mag;
  }

  public String getCityName(){
    return location;
  }

  public long getDate(){
    return date;
  }

  public String getEarthquakeDetails(){ return earthquakeDetails; }
}
