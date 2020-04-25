package com.example.android.navigationdrawer2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.android.navigationdrawer2.Objects.Usuario;


public class PerfilFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ImageView fotoDePerfil;
    public TextView nombreDeUsuario;
    public TextView patenteDelUsuario;
    public Button editarPerfil;

    public DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference nombredelususario;// = ref.child("Usuarios").child(cargarPreferencias());

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PerfilFragment() {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        View v;
        v = inflater.inflate(R.layout.fragment_perfil, container, false);

        nombreDeUsuario = (TextView) v.findViewById(R.id.NombreDeLaPersona);
        patenteDelUsuario = (TextView) v.findViewById(R.id.carplateDeLaPersona);
        editarPerfil = (Button) v.findViewById(R.id.editarPerfil);

        nombredelususario = ref.child("Usuarios").child(cargarPreferencias());

        editarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditarPerfil.class));
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Usuario u = dataSnapshot.getValue(Usuario.class);

                    nombreDeUsuario.setText(u.getNombre());
                    patenteDelUsuario.setText(u.getEmail());
                    //etiketa.setText(u.getNombre());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    // ...
                }
            };
        nombredelususario.addValueEventListener(postListener);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        View onCreateView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState);

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private String cargarPreferencias() {

           SharedPreferences prefs = getContext().getSharedPreferences("NUsuario", Context.MODE_PRIVATE);
           SharedPreferences.Editor editor = prefs.edit();
           //Toast.makeText(getContext(), prefs.getString("user", "NO").toString(), Toast.LENGTH_SHORT).show();
           return (prefs.getString("user", "NO").toString());

    }
}
