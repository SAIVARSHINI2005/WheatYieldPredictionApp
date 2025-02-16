package com.example.wheatyieldpredictionproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    public TextView currentTemperatureTextView, currentConditionTextView, locationTextView;
    private RecyclerView forecastRecyclerView;
    private ForecastAdapter forecastAdapter;
    private ArrayList<Forecast> forecastList = new ArrayList<>();
    private static final String API_KEY = "4b20902f7f1c7155ac22062487336862";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        currentTemperatureTextView = findViewById(R.id.currentTemperatureTextView);
        currentConditionTextView = findViewById(R.id.currentConditionTextView);
        locationTextView = findViewById(R.id.locationTextView);
        forecastRecyclerView = findViewById(R.id.forecastRecyclerView);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        forecastAdapter = new ForecastAdapter(forecastList);
        forecastRecyclerView.setAdapter(forecastAdapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationAndFetchWeather();
    }

    private void getLocationAndFetchWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        fetchWeatherData(latitude, longitude);
                    } else {
                        Toast.makeText(WeatherActivity.this, "Unable to fetch location.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void fetchWeatherData(double latitude, double longitude) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";
        Log.d(TAG, "Request URL: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            JSONArray dailyForecast = response.getJSONArray("list");
                            forecastList.clear();

                            for (int i = 0; i < dailyForecast.length(); i += 8) { // Approx every 24 hours
                                JSONObject dayData = dailyForecast.getJSONObject(i);

                                long dt = dayData.getLong("dt");
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd", Locale.getDefault());
                                String date = sdf.format(new Date(dt * 1000));

                                JSONObject main = dayData.getJSONObject("main");
                                double dayTemp = main.getDouble("temp");
                                int humidity = main.getInt("humidity");

                                JSONObject windObject = dayData.getJSONObject("wind");
                                double windSpeed = windObject.getDouble("speed");

                                JSONArray weatherArray = dayData.optJSONArray("weather");
                                String condition = "Unknown";

                                if (weatherArray != null && weatherArray.length() > 0) {
                                    try {
                                        JSONObject weather = weatherArray.getJSONObject(0);
                                        condition = weather.has("main") ? weather.getString("main") : "Unknown";
                                    } catch (JSONException e) {
                                        Log.e(TAG, "Weather parsing error: " + e.getMessage());
                                    }
                                } else {
                                    Log.e(TAG, "Weather data missing for timestamp: " + dt);
                                }

                                if (i == 0) {
                                    currentTemperatureTextView.setText(String.format("%.1f°C", dayTemp));
                                    currentConditionTextView.setText(condition);
                                }

                                Forecast forecast = new Forecast(date, dayTemp, humidity, windSpeed);
                                forecastList.add(forecast);
                            }

                            forecastAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(WeatherActivity.this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching weather data: " + error.getMessage());
                        Toast.makeText(WeatherActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndFetchWeather();
            } else {
                Toast.makeText(this, "Permission denied. Cannot fetch location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
