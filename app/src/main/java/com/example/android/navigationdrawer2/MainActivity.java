package com.example.android.navigationdrawer2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.android.navigationdrawer2.Objects.Usuario;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QRFragment.OnFragmentInteractionListener, MapaFragment.OnFragmentInteractionListener, PerfilFragment.OnFragmentInteractionListener{

    private Fragment miFragment = null;
    private boolean fragmentSeleccionado = false;
    public TextView etiketa;
    public View v;
    public String nombre;
    public DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference nombredelusuario;// = ref.child("Usuarios").child(cargarPreferencias());
    public String c = "";
    public static final String s = "Personal data changed successufuly";

    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(R.style.MainTheme);
        setContentView(R.layout.activity_main);

        nombredelusuario = ref.child("Usuarios").child(cargarPreferencias());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        etiketa = (TextView) headerView.findViewById(R.id.etiketa);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        miFragment = new MapaFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miFragment).commit();

        c = getIntent().getStringExtra("Mensaje");

        if (s.equals(c)) {
            Toast.makeText(getApplicationContext(), String.valueOf(c), Toast.LENGTH_LONG).show();
            c ="";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return v;
    }

    @Override
    public void onStart() {

    super.onStart();
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Usuario x = dataSnapshot.getValue(Usuario.class);
            etiketa.setText(x.getNombre());
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
        }
    };
    nombredelusuario.addValueEventListener(postListener);
}
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(!drawer.isDrawerOpen(GravityCompat.START))
            {
                AlertDialog.Builder salirApp = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
                salirApp.setMessage("¿Desea salir de Online Parking?")
                        .setCancelable(true)
                        .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                                FirebaseAuth.getInstance().signOut();
                                finish();

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog title = salirApp.create();
                title.setTitle("Salir de la aplicación");
                title.show();
            }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragmentSeleccionado = false;

        if (id == R.id.nav_camera) {
            miFragment = new QRFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_gallery) {
            miFragment = new MapaFragment();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_manage) {
            miFragment = new PerfilFragment();
            fragmentSeleccionado = true;

        }

        if (fragmentSeleccionado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miFragment).commit();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private String cargarPreferencias() {

        SharedPreferences prefs = getSharedPreferences("NUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //Toast.makeText(getContext(), prefs.getString("user", "NO").toString(), Toast.LENGTH_SHORT).show();
        return (prefs.getString("user", "NO").toString());

    }
}
