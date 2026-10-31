
package com.ifsc.app;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager gerenciador;
    private Sensor acelerometro;
    private NivelHorizontalView nivelHorizontal;
    private NivelVerticalView nivelVertical;
    private NivelCircularView nivelCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nivelHorizontal = findViewById(R.id.nivel_horizontal);
        nivelVertical = findViewById(R.id.nivel_vertical);
        nivelCircular = findViewById(R.id.nivel_circular);

        gerenciador = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometro = gerenciador.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (acelerometro != null) {
            gerenciador.registerListener(
                    this,
                    acelerometro,
                    SensorManager.SENSOR_DELAY_GAME
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gerenciador.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent evento) {

        float inclinacaoX = evento.values[0] / 5f;
        float inclinacaoY = -evento.values[1] / 5f;

        inclinacaoX = Math.max(-1f, Math.min(1f, inclinacaoX));
        inclinacaoY = Math.max(-1f, Math.min(1f, inclinacaoY));

        nivelHorizontal.setInclinacao(inclinacaoX);
        nivelVertical.setInclinacao(inclinacaoY);
        nivelCircular.setInclinacao(inclinacaoX, inclinacaoY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int precisao) {
    }
}