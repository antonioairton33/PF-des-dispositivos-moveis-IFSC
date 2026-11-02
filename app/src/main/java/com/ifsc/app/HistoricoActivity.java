package com.ifsc.app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoricoActivity extends AppCompatActivity {

    private MedicaoDbHelper banco;
    private List<Medicao> medicoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        banco = new MedicaoDbHelper(this);

        ListView lista = findViewById(R.id.lista_medicoes);

        medicoes = banco.listarTodas();

        ArrayList<String> itens = new ArrayList<>();

        SimpleDateFormat formato = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
        );

        for (Medicao medicao : medicoes) {

            String data = formato.format(new Date(medicao.dataHora));

            String situacao = medicao.nivelado
                    ? "Nivelado"
                    : "Fora de nível";

            itens.add(data + " - " + situacao);
        }

        if (itens.isEmpty()) {
            itens.add("Nenhuma medição salva.");
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itens
        );

        lista.setAdapter(adaptador);

        lista.setOnItemClickListener((parent, view, position, id) -> {

            if (medicoes.isEmpty()) {
                return;
            }

            Medicao medicao = medicoes.get(position);

            String situacao = medicao.nivelado
                    ? "Nivelado"
                    : "Fora de nível";

            String detalhes = "Ângulo X: " + medicao.anguloX + "°\n"
                    + "Ângulo Y: " + medicao.anguloY + "°\n"
                    + "Desnível: " + medicao.desnivelMmPorMetro + " mm/m\n"
                    + "Situação: " + situacao;

            if (medicao.latitude != null && medicao.longitude != null) {
                detalhes += "\nLatitude: " + medicao.latitude
                        + "\nLongitude: " + medicao.longitude;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Detalhes da medição")
                    .setMessage(detalhes)
                    .setPositiveButton("Fechar", null)
                    .show();
        });
    }
}