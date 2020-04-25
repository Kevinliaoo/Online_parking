package com.example.android.navigationdrawer2;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.android.navigationdrawer2.Obects.Usuario;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;
import java.util.List;




public class Log_in extends AppCompatActivity {

    public Button botonusuario, registrarcuenta;
    public EditText email, contraseña;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    public static final int MULTIPLE_PERMISSIONS = 10;

    String[] permissions= new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.LogIn);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        checkPermissions();
        email = findViewById(R.id.txtemail);
        contraseña = findViewById(R.id.txtcontraseña);
        botonusuario = findViewById(R.id.botonsesion);
        registrarcuenta = findViewById(R.id.botoncrearcuenta);


        registrarcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentCuenta = new Intent(getApplicationContext(), op_cuenta.class);
                startActivity(intentCuenta);
            }
        });

        botonusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                logInAuth(email.getText().toString(), contraseña.getText().toString());
            }
        });
    }

    private void logInAuth(final String email, final String pword)
    {

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"Se requiere un email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pword))
        {
            Toast.makeText(getApplicationContext(),"Se requiere una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Ingresando...");
        progressDialog.show();
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(email, pword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();

                                updateUI(user);
                                guardarPreferencias(mAuth.getUid());

                                Intent intentli = new Intent(getApplicationContext(), MainActivity.class);

                                startActivity(intentli);
                                finish();
                            } else {
                                updateUI(null);
                        }

                        if (!task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Email incorrecto", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied
                    updateViews();
                }
                return;
            }
        }
    }

    private void updateViews() {
    }




    private void guardarPreferencias(String nombreDeUsuario) {
        SharedPreferences preferencia = getSharedPreferences("NUsuario", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("user", nombreDeUsuario);
        editor.commit();
    }

    private void updateUI(FirebaseUser user)
    {
        progressDialog.dismiss();
    }
}


