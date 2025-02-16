package com.example.wheatyieldpredictionproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherParser {

    private WeatherActivity activity;

    public WeatherParser(WeatherActivity activity) {
        this.activity = activity;
    }

    public void parseWeatherData(String jsonResponse) {
        try {
            // Parse the JSON response
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray weatherArray = responseObject.getJSONArray("weather");

            // Check if the weather array is not empty
            if (weatherArray.length() > 0) {
                JSONObject weather = weatherArray.getJSONObject(0);
                String condition = weather.getString("description");
                activity.currentConditionTextView.setText(condition);
            } else {
                activity.currentConditionTextView.setText("No weather data available");
            }

            // Get the main object to fetch temperature
            JSONObject main = responseObject.getJSONObject("main");
            double temperature = main.getDouble("temp");
            activity.currentTemperatureTextView.setText(String.format("%.1f °C", temperature));

            // Get the location name
            String location = responseObject.getString("name");
            activity.locationTextView.setText(location);

        } catch (JSONException e) {
            // Handle JSON parsing errors
            e.printStackTrace();
            activity.currentConditionTextView.setText("Error parsing weather data");
        }
    }
}