
package com.ifsc.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener, LocationListener {

    // ---------- Ajustes do nível ----------
    private static final float ALPHA = 0.85f;
    private static final float ESCALA_GRAUS = 30f;
    private static final float TOLERANCIA_GRAUS = 0.5f;

    // ---------- Sensor ----------
    private SensorManager gerenciador;
    private Sensor acelerometro;
    private final float[] gravidade = new float[3];
    private boolean primeiraLeitura = true;

    private float anguloBrutoX, anguloBrutoY;
    private float offsetX, offsetY;
    private float anguloX, anguloY;
    private float desnivelMmPorMetro;
    private boolean nivelado;

    // ---------- GPS ----------
    private LocationManager locationManager;
    private Location ultimaLocalizacao;

    private final ActivityResultLauncher<String> pedirPermissaoGps =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), concedida -> {
                if (concedida) {
                    iniciarGps();
                }
            });

    // ---------- Views ----------
    private NivelHorizontalView nivelHorizontal;
    private NivelVerticalView nivelVertical;
    private NivelCircularView nivelCircular;
    private TextView tvStatus, tvAngulos, tvGps;

    // ---------- Banco de dados ----------
    private MedicaoDbHelper banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banco = new MedicaoDbHelper(this);

        nivelHorizontal = findViewById(R.id.nivel_horizontal);
        nivelVertical = findViewById(R.id.nivel_vertical);
        nivelCircular = findViewById(R.id.nivel_circular);
        tvStatus = findViewById(R.id.tv_status);
        tvAngulos = findViewById(R.id.tv_angulos);
        tvGps = findViewById(R.id.tv_gps);

        Button btnCalibrar = findViewById(R.id.btn_calibrar);
        Button btnSalvar = findViewById(R.id.btn_salvar);
        Button btnHistorico = findViewById(R.id.btn_historico);

        btnCalibrar.setOnClickListener(v -> calibrar());
        btnSalvar.setOnClickListener(v -> salvarMedicao());

        btnHistorico.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoricoActivity.class);
            startActivity(intent);
        });

        gerenciador = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometro = gerenciador.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (acelerometro == null) {
            tvStatus.setText("Este aparelho não tem acelerômetro");
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!temPermissaoGps()) {
            pedirPermissaoGps.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (acelerometro != null) {
            primeiraLeitura = true;
            gerenciador.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_GAME);
        }

        if (temPermissaoGps()) {
            iniciarGps();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gerenciador.unregisterListener(this);
        locationManager.removeUpdates(this);
    }

    // ---------- SENSOR ----------
    @Override
    public void onSensorChanged(SensorEvent evento) {

        if (primeiraLeitura) {
            System.arraycopy(evento.values, 0, gravidade, 0, 3);
            primeiraLeitura = false;
        } else {
            for (int i = 0; i < 3; i++) {
                gravidade[i] = ALPHA * gravidade[i] + (1 - ALPHA) * evento.values[i];
            }
        }

        float gx = gravidade[0];
        float gy = gravidade[1];
        float gz = gravidade[2];

        anguloBrutoX = (float) Math.toDegrees(
                Math.atan2(gx, Math.sqrt(gy * gy + gz * gz)));

        anguloBrutoY = (float) Math.toDegrees(
                Math.atan2(gy, Math.sqrt(gx * gx + gz * gz)));

        anguloX = anguloBrutoX - offsetX;
        anguloY = anguloBrutoY - offsetY;

        double tanX = Math.tan(Math.toRadians(anguloX));
        double tanY = Math.tan(Math.toRadians(anguloY));

        desnivelMmPorMetro = (float) (Math.hypot(tanX, tanY) * 1000);

        boolean niveladoX = Math.abs(anguloX) <= TOLERANCIA_GRAUS;
        boolean niveladoY = Math.abs(anguloY) <= TOLERANCIA_GRAUS;

        nivelado = niveladoX && niveladoY;

        float inclinacaoX = limitar(anguloX / ESCALA_GRAUS);
        float inclinacaoY = limitar(-anguloY / ESCALA_GRAUS);

        nivelHorizontal.setNivelado(niveladoX);
        nivelVertical.setNivelado(niveladoY);
        nivelCircular.setNivelado(nivelado);

        nivelHorizontal.setInclinacao(inclinacaoX);
        nivelVertical.setInclinacao(inclinacaoY);
        nivelCircular.setInclinacao(inclinacaoX, inclinacaoY);

        atualizarTextos();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int precisao) {
    }

    private void atualizarTextos() {
        tvAngulos.setText(String.format(Locale.getDefault(),
                "X: %.1f°   Y: %.1f°\nDesnível: %.1f mm/m",
                anguloX, anguloY, desnivelMmPorMetro));

        if (nivelado) {
            tvStatus.setText("NIVELADO");
            tvStatus.setTextColor(Color.rgb(0, 110, 40));
        } else {
            tvStatus.setText("FORA DE NÍVEL");
            tvStatus.setTextColor(Color.rgb(180, 30, 30));
        }
    }

    private void calibrar() {
        offsetX = anguloBrutoX;
        offsetY = anguloBrutoY;

        Toast.makeText(this, "Calibrado: posição atual definida como referência",
                Toast.LENGTH_SHORT).show();
    }

    private float limitar(float valor) {
        return Math.max(-1f, Math.min(1f, valor));
    }

    // ---------- GPS ----------
    private boolean temPermissaoGps() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void iniciarGps() {
        if (!temPermissaoGps()) return;

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            tvGps.setText("GPS desligado: ative a localização do aparelho");
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2000, 1f, this);

        Location ultima = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (ultima != null) {
            onLocationChanged(ultima);
        } else {
            tvGps.setText("GPS: buscando sinal...");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        ultimaLocalizacao = location;

        tvGps.setText(String.format(Locale.getDefault(),
                "GPS: %.6f, %.6f (±%.0f m)",
                location.getLatitude(), location.getLongitude(), location.getAccuracy()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    // ---------- SALVAR MEDIÇÃO ----------
    private void salvarMedicao() {

        Medicao medicao = new Medicao();

        medicao.anguloX = anguloX;
        medicao.anguloY = anguloY;
        medicao.desnivelMmPorMetro = desnivelMmPorMetro;
        medicao.nivelado = nivelado;
        medicao.dataHora = System.currentTimeMillis();

        if (ultimaLocalizacao != null) {
            medicao.latitude = ultimaLocalizacao.getLatitude();
            medicao.longitude = ultimaLocalizacao.getLongitude();
            medicao.precisaoGps = ultimaLocalizacao.getAccuracy();
        }

        boolean salvou = banco.salvar(medicao);

        if (salvou) {
            Toast.makeText(this, "Medição salva!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao salvar medição!", Toast.LENGTH_SHORT).show();
        }
    }
}
