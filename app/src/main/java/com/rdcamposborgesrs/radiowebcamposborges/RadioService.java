package com.rdcamposborgesrs.radiowebcamposborges;
// Dentro do seu Service
public class RadioService extends Service {

    private MediaPlayer mediaPlayer;
    private String radioStreamUrl = "https://azuracast.rdcamposborgesrs.com.br/listen/radio_web_campos_borges_88.5_fm/movel.mp3"; // Substitua pela URL rea
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Iniciar reprodução
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
