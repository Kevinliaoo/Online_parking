package com.example.android.navigationdrawer2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.navigationdrawer2.Objects.lugar;

public class QRFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static TextView resultTextView;
    public ImageButton scan_btn;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String resultado;

    private String latitud, longitud;

    private String NLugar = "lugar1";



    private lugar l;


    public QRFragment() {
        // Required empty public constructor
    }


    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v ;
        v = inflater.inflate(R.layout.fragment_qr, container, false);
        resultTextView = (TextView) v.findViewById(R.id.textViewqr);
        scan_btn = (ImageButton) v.findViewById(R.id.qrButton);
        resultTextView.setText("Tocar el boton para escanear");

        scan_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {

                Intent i = new Intent (getActivity(), ScannCode.class);
                Bundle b = new Bundle();
                b.putString("Mensaje", "Scan QR code");
                i.putExtras(b);
                startActivity(i);





             //   if (!resultado.equals("Tocar el boton para escanear") || !resultado.equals("Se ha guardado su ubicación")){


                }
          //  }
        });

        return v;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        resultado = String.valueOf(resultTextView.getText());


        if (!resultado.equals("Tocar el boton para escanear") || !resultado.equals("Se ha guardado su ubicación")) {



        }


        //NLugar = resultado;

        //lugar = ref.child("lugar").child("lugar1");



        //nombredelususario.addValueEventListener(postListener);

        /*latitud = String.valueOf(lugar.child("latitud").getDatabase());
        longitud = String.valueOf(lugar.child("longitud").getDatabase());


        */



        if (!resultado.equals("Tocar el boton para escanear"))
            resultTextView.setText("Se ha guardado su ubicación");
    }





}
