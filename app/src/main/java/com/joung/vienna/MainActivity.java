package com.joung.vienna;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String D_DAY = "01/01/2021 00:00:00";

    private TextView mTextDays, mTextHours, mTextMinutes, mTextSeconds;
    private LinearLayout mContainerSeconds;
    private ExplosionField mExplosionField;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExplosionField = ExplosionField.attach2Window(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mExplosionField.explode(findViewById(R.id.view));
            }
        }, 2000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Date dayDate = new Date();
        try {
            dayDate = dateFormat.parse(D_DAY);
        } catch (ParseException e) {
            Log.e(TAG, "e - " + e.toString());
        }

        mTextDays = findViewById(R.id.text_days);
        mTextHours = findViewById(R.id.text_hours);
        mTextMinutes = findViewById(R.id.text_minutes);
        mTextSeconds = findViewById(R.id.text_seconds);

        Date currentDate = Calendar.getInstance().getTime();

        long countDown = dayDate.getTime() - currentDate.getTime();

        int days = (int) Math.floor(countDown / (1000 * 60 * 60 * 24));
        int hours = (int) Math.floor((countDown % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        int minutes = (int) Math.floor((countDown % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) Math.floor((countDown % (1000 * 60)) / 1000);

        mContainerSeconds = findViewById(R.id.container_seconds);

        Log.v(TAG, "LinearLayout Widht - " + mContainerSeconds.getMeasuredWidth());
        ViewTreeObserver observer= mContainerSeconds.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Log.d("Log", "Height: " + mContainerSeconds.getWidth());
                    }
                });

        for (int i = 0; i < 60; i++) {
            View view = new View(this);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            if (i < seconds - 1) {
                view.setBackgroundColor(getResources().getColor(android.R.color.black));
            } else {
                view.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
            mContainerSeconds.addView(view);
        }
        Log.v(TAG, "LinearLayout Widht - " + mContainerSeconds.getMeasuredWidth());

        ViennaCount count = new ViennaCount(countDown, 1000);
        count.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "LinearLayout Widht - " + mContainerSeconds.getMeasuredWidth());
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

            // mExplosionField.explode(mContainterSeconds.getChildAt(seconds + 1));

            if (seconds == 59) {
                for (int i = 0; i < 60 - 1; i++) {
                    mContainerSeconds.getChildAt(i).setBackgroundColor(getResources().getColor(android.R.color.black));
                }
            } else {
                // mExplosionField.explode(mContainerSeconds.getChildAt(seconds));
                mContainerSeconds.getChildAt(seconds).setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }
    }
}
