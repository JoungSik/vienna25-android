package com.joung.vienna.count;

import android.content.Context;
import android.util.Log;

import com.joung.vienna.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private static final String D_DAY = "01/01/2021 00:00:00";

    private final MainContract.View mView;
    private final Context mContext;

    MainPresenter(Context context, MainContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getCurrentDateTime() {
        String dateTimeFormat = mContext.getString(R.string.format_date_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormat, Locale.KOREA);

        try {
            Date dayDate = dateFormat.parse(D_DAY);
            mView.updateCountView(dayDate.getTime());
        } catch (ParseException e) {
            Log.e(TAG, "e - " + e.toString());
            mView.updateCountViewError();
        }
    }
}
