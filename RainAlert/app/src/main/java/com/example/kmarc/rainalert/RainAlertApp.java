package com.example.kmarc.rainalert;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RainAlertApp extends Application {
    private static final String BASE_URL ="http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "d779c31ab55ced04f2ce006a7c147769";

    Retrofit retrofit;
    public static WeatherApi weatherApi;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        weatherApi = retrofit.create(WeatherApi.class);
    }
}
