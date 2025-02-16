package com.example.wheatyieldpredictionproject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("onecall?exclude=hourly,minutely&appid=4b20902f7f1c7155ac22062487336862&units=metric")
    Call<WeatherResponse> getWeather(@Query("lat") double lat, @Query("lon") double lon);
}