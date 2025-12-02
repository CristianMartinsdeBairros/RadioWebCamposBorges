package com.rdcamposborgesrs.radiowebcamposborges;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Build;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements OnClickListener {

    private Button buttonPlay;

    private Button buttonStopPlay;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        initializeUIElements();

    }

    private void initializeUIElements() {

        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (Button) findViewById(R.id.buttonStopPlay);
        buttonStopPlay.setOnClickListener(this);

    }

    public void onClick(View v) {
    if (v == buttonPlay) {
    Intent prepareIntent = VpnService.prepare(this);
    if (prepareIntent != null) {
    startService(new Intent(MainActivity.this, MyVpnService.class));
    } else {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    finishAffinity();
    } else {
    finish();
    }
    }
        Context context = getApplicationContext();
        Intent intent = new Intent(MainActivity.this, RadioService.class);
        context.startForegroundService(intent);
        } else if (v == buttonStopPlay) {
        stopService(new Intent(MainActivity.this, RadioService.class));
        }
    }
}
