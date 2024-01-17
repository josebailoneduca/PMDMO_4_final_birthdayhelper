package com.imagenprogramada.birthdayhelper;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Contacto  implements Serializable {

    String nombre;

    public Contacto(String nombre) {
        this.nombre = nombre;
    }


}
