package com.example.shakedetection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.squareup.seismic.ShakeDetector;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {

    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;
    private Vibrator vibrator;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);
        //shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_HARD);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Shake Detect")
                .setMessage("Shack it baby!")
                .setPositiveButton(android.R.string.ok, null)
                .create();
        alertDialog.setOnShowListener(dialog -> makeVibrate());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            shakeDetector.start(sensorManager);
        }
    }

    @Override
    protected void onPause() {
        shakeDetector.stop();
        super.onPause();
    }

    private void makeVibrate() {
        if (vibrator == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(250);
        }
    }

    @Override
    public void hearShake() {
        runOnUiThread(() -> {
            if (isFinishing() || alertDialog.isShowing()) {
                return;
            }
            alertDialog.show();
        });
    }
}
