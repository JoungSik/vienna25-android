package com.joung.vienna.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.joung.vienna.R;
import com.xenione.digit.TabDigit;

import java.util.Calendar;
import java.util.Date;

import androidx.core.view.ViewCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eugeni on 04/12/2016.
 */
public class CountdownView extends LinearLayout implements Runnable {

    private static final char[] HOUR_DECIMAL = new char[]{'2', '1', '0'};
    private static final char[] TIME_DECIMAL = new char[]{'5', '4', '3', '2', '1', '0'};
    private static final char[] DECIMAL = new char[]{'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};

    @BindView(R.id.charVeryHighDate)
    TabDigit mCharVeryHighDate;
    @BindView(R.id.charHighDate)
    TabDigit mCharHighDate;
    @BindView(R.id.charLowDate)
    TabDigit mCharLowDate;

    @BindView(R.id.charHighHour)
    TabDigit mCharHighHour;
    @BindView(R.id.charLowHour)
    TabDigit mCharLowHour;

    @BindView(R.id.charHighMinute)
    TabDigit mCharHighMinute;
    @BindView(R.id.charLowMinute)
    TabDigit mCharLowMinute;

    @BindView(R.id.charHighSecond)
    TabDigit mCharHighSecond;
    @BindView(R.id.charLowSecond)
    TabDigit mCharLowSecond;

    private View mClock = this;

    private int elapsedTime = 0;

    private boolean mPause = true;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        inflate(getContext(), R.layout.clock, this);

        ButterKnife.bind(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mCharVeryHighDate.setTextSize(80);
        mCharVeryHighDate.setChars(DECIMAL);
        mCharHighDate.setTextSize(80);
        mCharHighDate.setChars(DECIMAL);
        mCharLowDate.setTextSize(80);
        mCharLowDate.setChars(DECIMAL);

        mCharHighHour.setTextSize(80);
        mCharHighHour.setChars(HOUR_DECIMAL);
        mCharLowHour.setTextSize(80);
        mCharLowHour.setChars(DECIMAL);

        mCharHighMinute.setTextSize(80);
        mCharHighMinute.setChars(TIME_DECIMAL);
        mCharLowMinute.setTextSize(80);
        mCharLowMinute.setChars(DECIMAL);

        mCharHighSecond.setTextSize(80);
        mCharHighSecond.setChars(TIME_DECIMAL);
        mCharLowSecond.setTextSize(80);
        mCharLowSecond.setChars(DECIMAL);

    }

    public void resume(long dayDate) {
        mPause = false;

        Date currentDate = Calendar.getInstance().getTime();
        long dateTime = (dayDate - currentDate.getTime()) / 1000;

        int dateVeryHeight = (int) (dateTime / 8640000);
        mCharVeryHighDate.setChar(9 - dateVeryHeight);

        dateTime -= dateVeryHeight * 8640000;

        int dateHeight = (int) (dateTime / 864000);
        mCharHighDate.setChar(9 - dateHeight);

        dateTime -= dateHeight * 864000;

        int dateLow = (int) (dateTime / 86400);
        mCharLowDate.setChar(9 - dateLow);

        dateTime -= dateLow * 86400;

        int hourHeight = (int) (dateTime / 36000);
        mCharHighHour.setChar(2 - hourHeight);

        dateTime -= hourHeight * 36000;

        int hourLow = (int) (dateTime / 3600);
        mCharLowHour.setChar(9 - hourLow);

        dateTime -= hourLow * 3600;

        int minuteHeight = (int) (dateTime / 600);
        mCharHighMinute.setChar(5 - minuteHeight);

        dateTime -= minuteHeight * 600;

        int minuteLow = (int) (dateTime / 60);
        mCharLowMinute.setChar(9 - minuteLow);

        dateTime -= minuteLow * 60;

        int secHeight = (int) (dateTime / 10);
        mCharHighSecond.setChar(5 - secHeight);

        dateTime -= secHeight * 10;

        int secLow = (int) dateTime;
        mCharLowSecond.setChar(9 - secLow);

        elapsedTime = 10 - Character.getNumericValue(DECIMAL[9 - secLow]);
        ViewCompat.postOnAnimationDelayed(mClock, this, 500);
    }

    @Override
    public void run() {
        if (mPause) {
            return;
        }

        mCharLowSecond.start();

        if (elapsedTime % 10 == 0) {
            mCharHighSecond.start();
        }

        if (elapsedTime % 60 == 0) {
            mCharLowMinute.start();
        }

        if (elapsedTime % 600 == 0) {
            mCharHighMinute.start();
        }

        if (elapsedTime % 3600 == 0) {
            mCharLowHour.start();
        }

        if (elapsedTime % 36000 == 0) {
            mCharHighHour.start();
        }

        if (elapsedTime % 86400 == 0) {
            mCharLowDate.start();
        }

        if (elapsedTime % 864000 == 0) {
            mCharHighDate.start();
        }

        if (elapsedTime % 8640000 == 0) {
            mCharVeryHighDate.start();
        }

        elapsedTime += 1;
        ViewCompat.postOnAnimationDelayed(mClock, this, 1000);
    }
}
