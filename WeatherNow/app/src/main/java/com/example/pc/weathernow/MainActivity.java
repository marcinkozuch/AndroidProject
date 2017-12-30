package com.example.pc.weathernow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.weathernow.api.Coord;
import com.example.pc.weathernow.api.Main;
import com.example.pc.weathernow.api.Weather;
import com.example.pc.weathernow.api.WeatherResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = MainActivity.class.getCanonicalName();
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.mapsview)
    MapView mapsview;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    GoogleApiClient googleApiClient;
    GoogleMap googleMaps;
    LocationRequest locationRequest = com.google.android.gms.location.LocationRequest.create()
            .setFastestInterval(100)
            .setMaxWaitTime(1000)
            .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        swipeRefresh.setEnabled(false);
        mapsview.getMapAsync(this);
        mapsview.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        boolean isDeny = false;
        for (int result : grantResults) {
            if (result < 0) {
                isDeny = true;
            }
        }
        if (isDeny) {
            Toast.makeText(this, "Need this permission ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapsview.onResume();

        getWeatherLocation("Krakow,pl");
    }

    private void getWeatherLocation(String location) {
        if (swipeRefresh != null && !swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
        WeatherNowApp.weatherApi.getWeather(WeatherNowApp.API_KEY, location).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.d(TAG, "onResponse: ");
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().weather != null && response.body().weather.size() > 0) {
                            Weather weather = response.body().weather.get(0);
                            Main main = response.body().main;
                            Coord coord = response.body().coord;
                            if (textView != null) {
                                textView.setText("Current weather is " + weather.description);
                            }
                            if (textView2 != null) {
                                textView2.setText((main.tempMax - 273.15) + "\u2103");
                            }
                            if (textView3 != null) {
                                textView3.setText(Double.toString(main.pressure)+"hPa");
                            }
                            if (imageView != null) {
                                Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + weather.icon + ".png").into(imageView);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
                Toast.makeText(MainActivity.this, "Network problem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        mapsview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapsview.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, MainActivity.this);
        googleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        MainActivity.this.googleMaps = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (LocationListener) MainActivity.this);
        Log.d(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: ");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: ");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        //getWeatherLocation(location);

    }
}