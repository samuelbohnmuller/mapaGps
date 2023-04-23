package com.example.mudandoposicaogps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mudandoposicaogps.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConexaoBanco conexaoBanco;
    Button button5;
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conexaoBanco = new ConexaoBanco(this, ConexaoBanco.DATABASE_NAME, ConexaoBanco.DATABASE_VERSION);

        button5 = findViewById(R.id.button5);
        textView3 = findViewById(R.id.textView3);

        Button cadastrarBtn = findViewById(R.id.button4);
        cadastrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double latitude = Localizacao.latitude;
                double longitude = Localizacao.longitude;

                SQLiteDatabase db = conexaoBanco.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put("latitude", latitude);
                valores.put("longitude", longitude);
                db.insert("localizacao", null, valores);

                db.close();

                Toast.makeText(MainActivity.this, "Localização cadastrada com sucesso", Toast.LENGTH_SHORT).show();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Localizacao> localizacoes = Localizacao.buscarLocalizacoes(MainActivity.this);
                StringBuilder builder = new StringBuilder();
                for (Localizacao loc : localizacoes) {
                    builder.append("Latitude: ").append(loc.getlatitude()).append(", Longitude: ").append(loc.getLongitude()).append("\n");
                }
                textView3.setText(builder.toString());
            }
        });

        Button documentationButton = findViewById(R.id.button10);
        documentationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/samuelbohnmuller/localizacaoGPS");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }
    public void buscarGPS(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
        LocationListener locationListener = new Localizacao();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String texto = "Latitude.: " + Localizacao.latitude + "\n" +
                    "Longitude: " + Localizacao.longitude + "\n";
            Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "GPS DESABILITADO.", Toast.LENGTH_LONG).show();
        }

        this.mostrarMapa(Localizacao.latitude, Localizacao.longitude);
    }

    public void mostrarMapa(double latitude, double longitude) {
        WebView wv = findViewById(R.id.webv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
    }

}