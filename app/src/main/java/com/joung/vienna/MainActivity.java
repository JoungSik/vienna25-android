package com.joung.vienna;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String D_DAY = "01/01/2021 00:00:00";

    private TextView mTextDays, mTextHours, mTextMinutes, mTextSeconds;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextDays = findViewById(R.id.text_days);
        mTextHours = findViewById(R.id.text_hours);
        mTextMinutes = findViewById(R.id.text_minutes);
        mTextSeconds = findViewById(R.id.text_seconds);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date dayDate = new Date();
        try {
            dayDate = dateFormat.parse(D_DAY);
        } catch (ParseException e) {
            Log.e(TAG, "e - " + e.toString());
        }

        Date currentDate = Calendar.getInstance().getTime();

        long countDown = dayDate.getTime() - currentDate.getTime();
        ViennaCount count = new ViennaCount(countDown, 1000);
        count.start();
    }

    public class ViennaCount extends CountDownTimer {

        ViennaCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Toast.makeText(MainActivity.this, "Complete!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTick(long millis) {
            int days = (int) Math.floor(millis / (1000 * 60 * 60 * 24));
            int hours = (int) Math.floor((millis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int minutes = (int) Math.floor((millis % (1000 * 60 * 60)) / (1000 * 60));
            int seconds = (int) Math.floor((millis % (1000 * 60)) / 1000);

            mTextDays.setText(String.valueOf(days));
            mTextHours.setText(String.valueOf(hours));
            mTextMinutes.setText(String.valueOf(minutes));
            mTextSeconds.setText(String.valueOf(seconds));
        }
    }
}
