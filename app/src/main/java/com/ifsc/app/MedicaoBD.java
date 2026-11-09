
package com.ifsc.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class MedicaoBD extends SQLiteOpenHelper {

    public MedicaoBD(Context context) {
        super(context, "nivelamento.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE medicao (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "anguloX REAL, " +
                "anguloY REAL, " +
                "desnivel REAL, " +
                "nivelado INTEGER, " +
                "dataHora INTEGER, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "precisaoGps REAL, " +
                "observacao TEXT)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {

    }

    public boolean salvar(Medicao medicao) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put("anguloX", medicao.anguloX);
        valores.put("anguloY", medicao.anguloY);
        valores.put("desnivel", medicao.desnivelMmPorMetro);
        valores.put("nivelado", medicao.nivelado ? 1 : 0);
        valores.put("dataHora", medicao.dataHora);

        valores.put("latitude", medicao.latitude);
        valores.put("longitude", medicao.longitude);
        valores.put("precisaoGps", medicao.precisaoGps);
        valores.put("observacao", medicao.observacao);

        long resultado = db.insert("medicao", null, valores);

        return resultado != -1;
    }

    public List<Medicao> listarTodas() {

        List<Medicao> lista = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM medicao ORDER BY dataHora DESC",
                null
        );

        while (cursor.moveToNext()) {

            Medicao medicao = new Medicao();

            medicao.id = cursor.getLong(0);
            medicao.anguloX = cursor.getFloat(1);
            medicao.anguloY = cursor.getFloat(2);
            medicao.desnivelMmPorMetro = cursor.getFloat(3);
            medicao.nivelado = cursor.getInt(4) == 1;
            medicao.dataHora = cursor.getLong(5);

            if (!cursor.isNull(6)) {
                medicao.latitude = cursor.getDouble(6);
                medicao.longitude = cursor.getDouble(7);
                medicao.precisaoGps = cursor.getFloat(8);
            }

            medicao.observacao = cursor.getString(9);

            lista.add(medicao);
        }

        cursor.close();

        return lista;
    }
    public void limparHistorico() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("medicao", null, null);
    }
}