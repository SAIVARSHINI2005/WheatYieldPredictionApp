package com.example.wheatyieldpredictionproject;

public class Forecast {
    private String date;
    private double temperature;
    private int humidity;
    private double windSpeed;

    public Forecast(String date, double temperature, int humidity, double windSpeed) {
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getDate() {
        return date;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
