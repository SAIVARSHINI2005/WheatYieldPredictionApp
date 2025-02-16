package com.example.wheatyieldpredictionproject;


import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    private static final String API_KEY = "4b20902f7f1c7155ac22062487336862";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=YOUR_CITY&appid=" + API_KEY + "&units=metric";

    public interface WeatherCallback {
        void onSuccess(JSONObject weatherData);
        void onFailure(String errorMessage);
    }

    public static void getLiveWeather(Context context, WeatherCallback callback) {
        new FetchWeatherTask(callback).execute();
    }

    private static class FetchWeatherTask extends AsyncTask<Void, Void, JSONObject> {
        private final WeatherCallback callback;

        public FetchWeatherTask(WeatherCallback callback) {
            this.callback = callback;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return new JSONObject(result.toString());
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                callback.onSuccess(result);
            } else {
                callback.onFailure("Failed to retrieve weather data.");
            }
        }
    }
}
