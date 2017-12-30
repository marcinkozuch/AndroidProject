package com.example.kmarc.rainalert;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getCanonicalName();
    FirebaseJobDispatcher firebaseJobDispatcher;
    @BindView(R.id.switchBackground) Switch switchBackground;
    SharedPreferences sharedPreferences;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        sharedPreferences = getSharedPreferences("com.example.kmarc.rainalert", MODE_PRIVATE);
        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(MainActivity.this));
        switchBackground.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                sharedPreferences.edit().putBoolean("background", switchBackground.isChecked()).apply();
            }
        });
    }

    @Override protected void onResume() {
        super.onResume();
        firebaseJobDispatcher.cancelAll();
        switchBackground.setChecked(sharedPreferences.getBoolean("background", false));
    }

    @Override protected void onPause() {
        if (switchBackground.isChecked()) {
            Job.Builder job = firebaseJobDispatcher.newJobBuilder().setTag("weather").setTrigger(Trigger.NOW).setService(WeatherService.class);
            firebaseJobDispatcher.schedule(job.build());
        }
        super.onPause();
    }
}
