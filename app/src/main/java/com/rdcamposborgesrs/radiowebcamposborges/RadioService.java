package com.rdcamposborgesrs.radiowebcamposborges;
// Dentro do seu Service
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.IOException;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import androidx.core.app.ServiceCompat;
import android.app.Notification;

public class RadioService extends Service {
    private static final int NOTIFICATION_ID = 999999; 
    private MediaPlayer mediaPlayer;
    private String radioStreamUrl = "https://azuracast.rdcamposborgesrs.com.br/listen/radio_web_campos_borges_88.5_fm/movel.mp3"; // Substitua pela URL rea
        private Notification createNotification() {
        // Create a notification channel for Android O (API 26) and above
        // ... (code for creating channel in Application class or here)
        
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_ID)
            .setContentTitle("A Rádio Web Campos Borges está ao vivo agora!")
            .setContentText("")
            .build();
        return notification;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = createNotification();
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        );
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(radioStreamUrl);
                mediaPlayer.prepareAsync(); // Assíncrono para não bloquear a thread principal

                mediaPlayer.setOnPreparedListener(mp -> mp.start());
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    // Lidar com erro
                    return false;
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return START_STICKY; // O serviço será recriado se for destruído
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Não estamos usando binding aqui
    }
}
