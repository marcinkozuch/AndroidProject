package com.example.kmarc.rainalert;

import com.example.kmarc.rainalert.api.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PC on 2017-12-11.
 */

public interface WeatherApi {
    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("APPID") String appid, @Query("q") String location);
    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("APPID") String appid, @Query("lat") Double lat, @Query("lon") Double lon);
}
