package com.joung.vienna;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String D_DAY = "01/01/2021 00:00:00";
    private static final String[] images = {"image_1", "image_2"};

    private ImageSwitcher mImageSwitcher;
    private TextView mImageTextView;

    private TextView mTextDays, mTextHours, mTextMinutes, mTextSeconds;

    private static int mImageIndex = 0;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextDays = findViewById(R.id.text_days);
        mTextHours = findViewById(R.id.text_hours);
        mTextMinutes = findViewById(R.id.text_minutes);
        mTextSeconds = findViewById(R.id.text_seconds);

        mImageTextView = findViewById(R.id.text_image);
        mImageSwitcher = findViewById(R.id.switcher_image);

        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        mImageSwitcher.setInAnimation(in);
        mImageSwitcher.setOutAnimation(out);

        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                imageView.setOnClickListener(MainActivity.this);

                return imageView;
            }
        });

        showImage(mImageIndex);
        mImageIndex += 1;

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

    private void showImage(int index) {
        String[] textImages = getResources().getStringArray(R.array.text_images);
        int resourceID = getResources().getIdentifier(images[index], "drawable", getPackageName());

        mImageSwitcher.setImageResource(resourceID);
        mImageTextView.setText(textImages[index]);
    }

    @Override
    public void onClick(View v) {
        if (mImageIndex > images.length - 1) {
            mImageIndex = 0;
        }

        showImage(mImageIndex);
        mImageIndex += 1;
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
