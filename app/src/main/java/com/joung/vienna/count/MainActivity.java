package com.joung.vienna.count;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.joung.vienna.R;
import com.joung.vienna.admin.AdminActivity;
import com.joung.vienna.count.view.CountdownView;
import com.joung.vienna.count.view.ImageAdapter;
import com.joung.vienna.note.NoteActivity;
import com.marcoscg.materialtoast.MaterialToast;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recycler_image)
    RecyclerView mRecyclerView;

    @BindView(R.id.pager_indicator)
    IndefinitePagerIndicator indefinite;

    @BindView(R.id.count_down_view)
    CountdownView mCountDownView;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new MainPresenter(this, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);

        ImageAdapter adapter = new ImageAdapter(this);
        mRecyclerView.setAdapter(adapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);

        indefinite.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCurrentDateTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCountDownView.stop();
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

    @OnClick(R.id.button_book)
    public void showNote() {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    /*@OnClick(R.id.button_setting)
    public void setting() {
        startActivity(new Intent(this, AdminActivity.class));
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateCountView(Long dateTime) {
        mCountDownView.resume(dateTime);
    }

    @Override
    public void updateCountViewError() {
        new MaterialToast(this)
                .setMessage(getString(R.string.error_text_date_time_parse))
                .setIcon(R.mipmap.ic_launcher)
                .setDuration(Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(() -> finish(), 2000);
    }
}
