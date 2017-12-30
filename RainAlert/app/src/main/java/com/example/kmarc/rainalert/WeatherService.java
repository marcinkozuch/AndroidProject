package com.example.kmarc.rainalert;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.kmarc.rainalert.api.Weather;
import com.example.kmarc.rainalert.api.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService extends com.firebase.jobdispatcher.JobService {
    public static final String TAG = WeatherService.class.getCanonicalName();

    public WeatherService() {
    }

    @Override public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        RainAlertApp.weatherApi.getWeather(RainAlertApp.API_KEY, "Krakow,pl").enqueue(new Callback<WeatherResponse>() {
            @Override public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.d(TAG, "onResponse: ");
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().weather != null) {
                        if (response.body().weather.size() > 0) {
                            showNotificaton(response.body().weather.get(0));
                            if (response.body().weather.get(0).description.contains("rain")) {
                            }
                        }
                    }
                }
            }

            @Override public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
        return false;
    }

    private void showNotificaton(Weather weather) {
        Intent intent = new Intent(WeatherService.this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(WeatherService.this, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(WeatherService.this).setContentTitle("Rain alarm!")
                .setContentText(weather.description)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(123, notificationBuilder.build());
    }

    @Override public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }
}
