package com.kojiishi.bridgetimer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends WearableActivity {

    private Handler mHandler = new Handler();
    private Runnable mUpdate;
    private long mStart;
    private long mRemindInterval = 120;
    private long mNextRemind;
    private TextView mClockText;
    private TextView mElapsedText;
    private TextView mLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClockText = (TextView) findViewById(R.id.clock);
        mElapsedText = (TextView) findViewById(R.id.elapsed);
        mLast = (TextView) findViewById(R.id.last);

        // Enables Always-on
        setAmbientEnabled();

        mStart = Calendar.getInstance().getTimeInMillis();
        mNextRemind = mRemindInterval;

        this.mUpdate = new Runnable() {
            public void run() {
                update();
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(this.mUpdate);
    }

    private void update() {
        Date current = Calendar.getInstance().getTime();

        mClockText.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(current));

        long elapsed = (current.getTime() - mStart) / 1000;
        mElapsedText.setText(String.format("%d:%02d:%02d",
                elapsed / 60 / 60,
                (elapsed / 60) % 60,
                elapsed % 60));

        if (elapsed >= mNextRemind) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            mNextRemind += mRemindInterval;
        }
    }

    public void resetElapsed(View view) {
        mLast.setText(mElapsedText.getText());

        mStart = Calendar.getInstance().getTimeInMillis();
        mNextRemind = mRemindInterval;
    }
}
