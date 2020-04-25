package com.example.android.navigationdrawer2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;

    private String resultado;
    private Intent i;

    private int contador = 0;

    private String ocupador, ocupante;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference lugar, o;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        String c = getIntent().getStringExtra("Mensaje");
        Toast.makeText(getApplicationContext(), String.valueOf(c), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleResult(Result rawResult) {


        if (!rawResult.getText().substring(0, 5).equals("lugar"))
        {
            Toast.makeText(getApplicationContext(), "Código inválido", Toast.LENGTH_SHORT).show();
            QRFragment.resultTextView.setText("Tocar el boton para escanear");
        }

        else {
            lugar = ref.child("lugar").child(rawResult.getText());

            lugar.child("usuarioOcupado").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ocupador = dataSnapshot.getValue(String.class);

                    String a = ocupador;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            o = ref.child("Usuarios").child(cargarPreferencias());

            o.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ocupante = dataSnapshot.getKey();

                    String b = ocupante;

                    if (ocupador.equals("null")) {
                        QRFragment.resultTextView.setText("Se ha guardado su ubicación");
                        contador = 1;
                        lugar.child("estado").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String l = dataSnapshot.getValue(String.class);
                                if (l.equals("libre") && contador == 1) {
                                    lugar.child("estado").setValue("ocupado");
                                    lugar.child("usuarioOcupado").setValue(ocupante);
                                    contador = 0;
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });
                    }
                    else if (ocupador.equals(ocupante)) {
                        QRFragment.resultTextView.setText("Se ha guardado su ubicación");
                        contador = 1;
                        lugar.child("estado").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String l = dataSnapshot.getValue(String.class);
                                if (l.equals("ocupado") && contador == 1) {
                                    lugar.child("estado").setValue("libre");
                                    lugar.child("usuarioOcupado").setValue("null");
                                    contador = 0;
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });
                    }
                    else if (!ocupador.equals(ocupante)) {
                        Toast.makeText(getApplicationContext(), "Lugar ocupado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);



        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();


    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }

    private String cargarPreferencias() {

        SharedPreferences prefs = getSharedPreferences("NUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        return (prefs.getString("user", "NO").toString());

    }


}