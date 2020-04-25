package com.example.android.navigationdrawer2.Objects;

public class lugar
{
   private String estado;
   private double latitud;
   private double longitud;
   private String ubicacion;

    public lugar(String estado, double latitud, double longitud, String ubicacion)
    {
        this.estado = estado;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ubicacion = ubicacion;

    }

    public lugar () {

    }

    public String getEstado()
    {
        return estado;
    }

    public double getLatitud()
    {
        return latitud;
    }

    public double getLongitud()
    {
        return longitud;
    }

    public String getUbicacion()
    {
        return ubicacion;
    }



    public void setEstado(String estado)
    {
        this.estado = estado;
    }

    public void setLatitud(double latitud)
    {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud)
    {
        this.longitud = longitud;
    }

    public void setUbicacion(String ubicacion)
    {
        this.ubicacion = ubicacion;
    }


}
