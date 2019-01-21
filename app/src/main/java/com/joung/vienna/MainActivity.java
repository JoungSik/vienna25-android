package com.joung.vienna;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.joung.vienna.adapter.ImageAdapter;
import com.joung.vienna.view.CountdownView;
import com.marcoscg.materialtoast.MaterialToast;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String D_DAY = "01/01/2021 00:00:00";

    @BindView(R.id.recycler_image)
    RecyclerView mRecyclerView;

    @BindView(R.id.pager_indicator)
    IndefinitePagerIndicator indefinite;

    @BindView(R.id.count_down_view)
    CountdownView mCountDownView;

    @BindString(R.string.format_date_time)
    String dateTimeFormat;

    private int index = 1;
    private boolean isRunnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        ImageAdapter adapter = new ImageAdapter(this);
        mRecyclerView.setAdapter(adapter);

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

        mCountDownView.resume(dayDate.getTime());

        final Handler handler = new Handler();
        Thread thread = new Thread(() -> {
            isRunnable = true;
            while (isRunnable) {
                SystemClock.sleep(1000);
                handler.post(() -> {
                    SystemClock.sleep(2000);
                    mRecyclerView.smoothScrollToPosition(index);
                    index = index >= 3 ? 0 : index + 1;
                });
            }
        });
        thread.start();
    }

    @OnClick(R.id.button_share)
    public void onClick() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", getString(R.string.text_share_app_download));
        clipboard.setPrimaryClip(clip);

        new MaterialToast(this)
                .setMessage(getString(R.string.text_share_complete))
                .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunnable = false;
    }

}
