package com.soruco.bruno.sidea;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId=null;
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    Button btn_encendido;
    Button btn_apagado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*mButtonLights = (ToggleButton)findViewById(R.id.buttonLights);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            mCameraId = getCameraId();
            if (mCameraId==null) {
                mButtonLights.setEnabled(false);
            } else {
                mButtonLights.setEnabled(true);
            }
        } else {
            mButtonLights.setEnabled(false);
        }*/

        btn_encendido=(Button)findViewById(R.id.btn_todoe);
        btn_apagado=(Button)findViewById(R.id.btn_todoa);
    }

    private String getCameraId() {
        try {
            String[] ids = mCameraManager.getCameraIdList();
            for (String id : ids) {
                CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer facingDirection = c.get(CameraCharacteristics.LENS_FACING);
                if (flashAvailable != null && flashAvailable && facingDirection != null && facingDirection == CameraCharacteristics.LENS_FACING_BACK) {
                    return id;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
//
    /*public void clickLights(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, mButtonLights.isChecked());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void encender(View v) {
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.sonido_humocorto);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        btn_encendido.setEnabled(false);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 3, 0);
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        vi.vibrate(pattern, 0);
        esperarysonar(4000);
    }

    public void esperarysonar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10, 0);
            }
        }, milisegundos);
    }

    public void apagar(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vi.cancel();
        btn_encendido.setEnabled(true);
    }
}

