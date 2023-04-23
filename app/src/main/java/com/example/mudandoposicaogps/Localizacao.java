package com.example.mudandoposicaogps;

import static com.example.mudandoposicaogps.ConexaoBanco.DATABASE_NAME;
import static com.example.mudandoposicaogps.ConexaoBanco.DATABASE_VERSION;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Localizacao implements LocationListener{
    public static double latitude;
    public static double longitude;

    public double getlatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Localizacao(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Localizacao() {

    }

    public static List<Localizacao> buscarLocalizacoes(Context context) {
        List<Localizacao> localizacoes = new ArrayList<>();

        ConexaoBanco conexao = new ConexaoBanco(context.getApplicationContext(), DATABASE_NAME, DATABASE_VERSION);
        SQLiteDatabase db = conexao.getReadableDatabase();
        Cursor cursor = db.query("localizacao", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            Localizacao loc = new Localizacao(latitude, longitude);
            localizacoes.add(loc);
        }

        cursor.close();
        db.close();
        return localizacoes;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
