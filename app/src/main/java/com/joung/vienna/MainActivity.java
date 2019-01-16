package com.joung.vienna;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.joung.vienna.adapter.ImageAdapter;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String D_DAY = "01/01/2021 00:00:00";

    @BindView(R.id.text_days)
    TextView mTextDays;
    @BindView(R.id.text_hours)
    TextView mTextHours;
    @BindView(R.id.text_minutes)
    TextView mTextMinutes;
    @BindView(R.id.text_seconds)
    TextView mTextSeconds;

    @BindView(R.id.recycler_image)
    RecyclerView mRecyclerView;

    @BindView(R.id.pager_indicator)
    IndefinitePagerIndicator indefinite;

    @BindView(R.id.count_down_view)
    CountdownView mCountDownView;

    @BindString(R.string.format_date_time)
    String dateTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new ImageAdapter(this));

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);

        indefinite.attachToRecyclerView(mRecyclerView);

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat, Locale.KOREA);
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

        mCountDownView.resume();
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
