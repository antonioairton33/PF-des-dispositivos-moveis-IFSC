package com.ifsc.app;

/**
 * Uma medição de nivelamento.
 * Campos públicos para ficar simples; o colega do banco pode anotar
 * com @Entity (Room) ou converter para ContentValues (SQLite).
 *
 * latitude, longitude e precisaoGps são null quando o GPS ainda não tinha sinal.
 */
public class Medicao {

    public long id;
    public float anguloX;            // graus, já calibrado
    public float anguloY;            // graus, já calibrado
    public float desnivelMmPorMetro;
    public boolean nivelado;
    public long dataHora;            // System.currentTimeMillis()

    public Double latitude;
    public Double longitude;
    public Float precisaoGps;        // metros

    public String observacao;        // opcional (ex.: "Sala 2", "Laje térreo")
}