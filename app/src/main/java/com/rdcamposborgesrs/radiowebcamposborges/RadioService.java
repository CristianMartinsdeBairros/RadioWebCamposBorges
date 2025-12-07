package com.rdcamposborgesrs.radiowebcamposborges;
// Dentro do seu Service
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.pm.ServiceInfo;
import java.io.IOException;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import androidx.core.app.ServiceCompat;
import android.app.Notification;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RadioService extends Service {
    private static final int NOTIFICATION_ID = 999999; 
    private MediaPlayer mediaPlayer;
    private String radioStreamUrl = "https://azuracast.rdcamposborgesrs.com.br/listen/radio_web_campos_borges_88.5_fm/computador.mp3"; // Substitua pela URL rea
    String channelId = "rdcamposborgesrs";
    String channelName = "Rádio Web Campos Borges";
    
    private Notification createNotification() {
        // Create a notification channel for Android O (API 26) and above
        // ... (code for creating channel in Application class or here)
        int importance = NotificationManager.IMPORTANCE_HIGH; // High priority notifications
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription("Notificações da Rádio");
        NotificationManager nm = getSystemService(NotificationManager.class);
        nm.createNotificationChannel(channel);
        Notification notification = new NotificationCompat.Builder(this, "rdcamposborgesrs")
            .setContentTitle("A Rádio Web Campos Borges está ao vivo agora!")
            .setContentText("Escute nossa programação pelo aplicativo!")
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
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
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
