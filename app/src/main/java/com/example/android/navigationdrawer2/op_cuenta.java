package com.example.android.navigationdrawer2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.android.navigationdrawer2.Objects.Usuario;


public class op_cuenta extends AppCompatActivity {


    public EditText email, contraseña, contraseña2, usuario;
    public Button botonok, btnyatengocuenta;


    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(R.style.LogIn);
        setContentView(R.layout.activity_op_cuenta);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        usuario = findViewById(R.id.username);
        email = findViewById(R.id.email);
        contraseña = findViewById(R.id.contraseña);
        contraseña2 = findViewById(R.id.contraseña2);

        botonok = findViewById(R.id.confirmar);
        btnyatengocuenta = findViewById(R.id.yaTengoCuenta);


        btnyatengocuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentYaTengoCuenta = new Intent(getApplicationContext(), Log_in.class);
                startActivity(intentYaTengoCuenta);
            }
        });

        botonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroDatabase(usuario.getText().toString(), email.getText().toString(), contraseña.getText().toString(), contraseña2.getText().toString());
            }
        });
    }

    private void registroDatabase(final String nombre, final String email, final String password, final String repPass) {

        if (TextUtils.isEmpty(nombre))
        {
            Toast.makeText(getApplicationContext(),"Seleccione un nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"Seleccione un email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"Seleccione una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(),"La contraseña debe tener 6 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(repPass))
        {
            Toast.makeText(getApplicationContext(),"Repita su contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repPass))
        {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                progressDialog.setMessage("Registrando usuario...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                final DatabaseReference users = FirebaseDatabase.getInstance().getReference("Usuarios");
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("email").exists()) {
                            if (!email.isEmpty()) {
                                Usuario usuarioExistente = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(Usuario.class);
                                if (usuarioExistente.getEmail().equals(email)) {
                                    Toast.makeText(getApplicationContext(), "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }else {
                            Usuario nuevoUsuario = new Usuario(nombre, email, password);
                            Toast.makeText(getApplicationContext(), "Usuario registrado!", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            users.child(mAuth.getCurrentUser().getUid()).setValue(nuevoUsuario);

                            Intent intent2 = new Intent(getApplicationContext(), Log_in.class);
                            startActivity(intent2);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentatras = new Intent(getApplicationContext(), Log_in.class);
        startActivity(intentatras);
    }

    private void updateUI (FirebaseUser user)
    {        }
}




