package com.example.android.navigationdrawer2;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

import com.example.android.navigationdrawer2.Objects.lugar;


public class MapaFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    private DatabaseReference mapa = FirebaseDatabase.getInstance().getReference();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    private Marker marker;
    private View v;
    private LocationManager locManager;
    private Location loc;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private Double latPrincipal, lonPrincipal;

    private ArrayList<Marker> tmpRealTime = new ArrayList<>();
    private ArrayList<Marker> RealTime = new ArrayList<>();

    private LatLng	norte, sur;
    private LatLngBounds CABA;



    public MapaFragment() {

    }

    public static MapaFragment newInstance(String param1, String param2) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_mapa, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.clear();

                map.setMaxZoomPreference(21.0f);
                map.setMinZoomPreference(11.0f);

                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(true);
                    

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(-34.609679, -58.429209))
                        .bearing(0)
                        .zoom(15)
                        //.tilt(40)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                map.setBuildingsEnabled(true);

                norte = new LatLng(-34.365485,-58.735125);
                sur = new LatLng(-34.664624, -58.322853);

                //CABA = new LatLngBounds(new LatLng(-34.365485,-58.735125), new LatLng(-34.664624, -58.322853));

                //map.setLatLngBoundsForCameraTarget(CABA);


                mapa.child("lugar").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (Marker marker:RealTime) {
                            marker.remove();
                        }
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            lugar l  = snapshot.getValue(lugar.class);
                            Double lat = l.getLatitud();
                            Double lon = l.getLongitud();
                            String calle = l.getUbicacion();
                            String e = l.getEstado();
                            LatLng ll = new LatLng(lat, lon);
                            //Marker marcador = mMap.addMarker(new MarkerOptions().position(ll).title(calle));
                           if (e.equals("libre")) {
                                int height = 40;
                                int width = 40;
                               //Drawable libre = getResources().getDrawable(R.drawable.aprobacion);
                               mMap.addMarker(new MarkerOptions().position(ll).title(calle).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                              /* BitmapDrawable bitmapdraw = mMap;
                               Bitmap b = bitmapdraw.getBitmap();
                               Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                               MarkerOptions mark = new MarkerOptions();
                               mark.position(ll);
                               mark.title(calle);
                               mark.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                              tmpRealTime.add(mMap.addMarker(mark));
                              */

                            }else if (e.equals("ocupado")) {
                                int height = 40;
                                int width = 40;
                              /* BitmapDrawable bitmapdraw =(BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.lugarocupado);
                               Bitmap b = bitmapdraw.getBitmap();

                               Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                               MarkerOptions mark = new MarkerOptions();
                               mark.position(ll);
                               mark.title(calle);
                               mark.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                               tmpRealTime.add(mMap.addMarker(mark)); */

                        mMap.addMarker(new MarkerOptions().position(ll).title(calle).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            }
                            RealTime.clear();
                            RealTime.addAll(tmpRealTime);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
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
                Toast.makeText(getContext(), "Tu database no funciona kpo", Toast.LENGTH_SHORT).show();
            }
        });
/*      fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Double h = -34.60761111;
        Double j = -58.44575;

        LatLng cid = new LatLng(h, j);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cid, 15), 4000, null);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
*/
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
