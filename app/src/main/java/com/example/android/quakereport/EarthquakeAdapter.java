package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lal on 28-12-2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

  private static final String LOCATION_SEPRETOR = " of ";

  public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakes) {
    super(context, 0, earthquakes);
  }
  @NonNull @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    // Check if an existing view is being reused, otherwise inflate the view
    View listItemView = convertView;
    if (listItemView == null){
      listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
    }

    // Get the {@link Earthwauke} object located at this position in the list
    Earthquake earthquake = getItem(position);

    TextView magnitudeView = (TextView) listItemView.findViewById(R.id.mag_text);
    String formattedMagnitude = formatMagnitude(earthquake.getMagnitude());
    magnitudeView.setText(formattedMagnitude);

    // Set the proper background color on the magnitude circle.
    // Fetch the background from the TextView, which is a GradientDrawable.
    GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

    // Get the appropriate background color based on the current earthquake magnitude
    int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());

    // Set the color on the magnitude circle
    magnitudeCircle.setColor(magnitudeColor);

    String originalLocation = earthquake.getCityName();

    String primaryLocation, offsetLocation;
    if(originalLocation.contains(LOCATION_SEPRETOR)){
      String[] parts = originalLocation.split(LOCATION_SEPRETOR);

      // Location offset should be "5km N " + " of " --> "5km N of"
      offsetLocation = parts[0] + LOCATION_SEPRETOR;
      // Primary location should be "Cairo, Egypt"
      primaryLocation = parts[1];
    }else {
      offsetLocation = getContext().getString(R.string.near_the);
      primaryLocation = originalLocation;
    }

    TextView offsetLocationView = (TextView) listItemView.findViewById(R.id.distance_name_text);
    offsetLocationView.setText(offsetLocation);
    TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.city_name_text);
    primaryLocationView.setText(primaryLocation);

    // Create a new Date object from the time in milliseconds of the earthquake
    Date dateObject = new Date(earthquake.getDate());

    TextView dateView = (TextView) listItemView.findViewById(R.id.date_text);
    String formattedDate = formatDate(dateObject);
    dateView.setText(formattedDate);

    TextView timeView = (TextView) listItemView.findViewById(R.id.time);
    String formattedTime = formatTime(dateObject);
    timeView.setText(formattedTime);

    return listItemView;
  }
  private String formatDate(Date dateObject){
    SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
    return dateFormat.format(dateObject);
  }
  private String formatTime(Date dateObject){
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    return timeFormat.format(dateObject);
  }
  /**
   * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
   * from a decimal magnitude value.
   */
  private String formatMagnitude(double magnitude) {
    DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
    return magnitudeFormat.format(magnitude);
  }

  private int getMagnitudeColor(double magniColor) {
    int magnitudeColorResourceId;
    int mangnitudeFloor = (int) Math.floor(magniColor);
    switch (mangnitudeFloor) {
      case 0:

      case 1:
        magnitudeColorResourceId = R.color.magnitude1;
        break;
      case 2:
        magnitudeColorResourceId = R.color.magnitude2;
        break;
      case 3:
        magnitudeColorResourceId = R.color.magnitude3;
        break;
      case 4:
        magnitudeColorResourceId = R.color.magnitude4;
        break;
      case 5:
        magnitudeColorResourceId = R.color.magnitude5;
        break;
      case 6:
        magnitudeColorResourceId = R.color.magnitude6;
        break;
      case 7:
        magnitudeColorResourceId = R.color.magnitude7;
        break;
      case 8:
        magnitudeColorResourceId = R.color.magnitude8;
        break;
      case 9:
        magnitudeColorResourceId = R.color.magnitude9;
        break;
      default:
        magnitudeColorResourceId = R.color.magnitude10plus;
        break;
    }
    return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
  }
}
