package com.example.android.navigationdrawer2;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.android.navigationdrawer2.Objects.lugar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference mapa = FirebaseDatabase.getInstance().getReference();

    private GoogleMap mMap;
    private Marker marker;
    private View v;
    private LocationManager locManager;
    private Location loc;

    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private Double latPrincipal, lonPrincipal;

    private LatLngBounds CABA;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latPrincipal = location.getLatitude();
                            lonPrincipal = location.getLongitude();
                        }
                    }
                });

        Double h = -34.60761111;
        Double j = -58.44575;

        LatLng cid = new LatLng(h, j);

       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cid, 15), 4000, null);


        mapa.child("lugar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        lugar l = snapshot.getValue(lugar.class);
                        Double lat = l.getLatitud();
                        Double lon = l.getLongitud();
                        String calle = l.getUbicacion();
                        String estado = l.getEstado();

                        LatLng ll = new LatLng(lat, lon);

                        if (estado.equals("libre")) {
                            mMap.addMarker(new MarkerOptions().position(ll).title(calle).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        } else if (estado.equals("ocupado")) {
                            mMap.addMarker(new MarkerOptions().position(ll).title(calle).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

