package com.example.android.navigationdrawer2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditarPerfil extends AppCompatActivity {

    public Button submit;
    public EditText n, t;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usuario;// = ref.child("Usuarios").child(cargarPreferencias());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editar_perfil);

        usuario = ref.child("Usuarios").child(cargarPreferencias());

        submit = findViewById(R.id.button);
        n = findViewById(R.id.editText1);

      //  n.setText(cargarPreferencias());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar(v);
            }
        });
    }

    protected void onStart () {
        super.onStart();
        ref.getDatabase();
    }

    public void modificar (View v) {
        final String nusuario = n.getText().toString();

        if(!TextUtils.isEmpty(nusuario))
            {

                usuario.child("nombre").setValue(nusuario);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "Nombre de usuario modificado!", Toast.LENGTH_SHORT).show();
                startActivity(i);


            }else if (TextUtils.isEmpty(nusuario))
                        {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "No se modificaron datos", Toast.LENGTH_SHORT).show();
                        }
    }

    private String cargarPreferencias() {

        SharedPreferences prefs = getSharedPreferences("NUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //Toast.makeText(getContext(), prefs.getString("user", "NO").toString(), Toast.LENGTH_SHORT).show();
        return (prefs.getString("user", "NO").toString());

    }


}
