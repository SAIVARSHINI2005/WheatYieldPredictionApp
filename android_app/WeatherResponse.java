package com.example.wheatyieldpredictionproject;

import java.util.List;

public class WeatherResponse {
    private String name; // Location name
    private Main main; // Main weather data
    private List<Weather> weather; // Weather conditions
    private List<Daily> daily; // Daily forecast

    public CharSequence getWeather() {
        return null;
    }

    // Getters and inner classes for Main, Weather, and Daily
    public static class Main {
        private float temp; // Current temperature
        // Add other fields as needed

        public float getTemp() {
            return temp;
        }
    }

    public static class Weather {
        private String description; // Weather description

        public String getDescription() {
            return description;
        }
    }

    public static class Daily {
        private Temp temp; // Daily temperature

        public Temp getTemp() {
            return temp;
        }

        public static class Temp {
            private float day; // Day temperature

            public float getDay() {
                return day;
            }
        }
    }

    public String getName() {
        return name;
    }

    public Main getMain() {
        return main;
    }

    public List<Daily> getDaily() {
        return daily;
    }
}