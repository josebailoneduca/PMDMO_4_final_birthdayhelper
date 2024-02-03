package com.imagenprogramada.birthdayhelper.repositorio;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de contacto para la base de datos
 */
@Entity(tableName="contacto_tabla")
public class Contacto  implements Serializable {
    @Ignore
    public static final String SOLO_NOTIFICACION = "Solo notificacion";
    @Ignore
    public static final String SMS = "Mandar SMS";
    @PrimaryKey(autoGenerate = false)
    private int ID;
    private String tipoNotif;
    private String mensaje;
    private String telefono;
    private String fechaNacimiento;
    private String nombre;

    private int idFoto;

    @Ignore
    private Bitmap foto;
    @Ignore
    private List<String> telefonos;

    public Contacto(int ID, String tipoNotif, String mensaje, String telefono, String fechaNacimiento, String nombre, int idFoto) {
        this.ID = ID;
        this.tipoNotif = tipoNotif;
        this.mensaje = mensaje;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.idFoto=idFoto;
        telefonos=new ArrayList<>();

    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }


    public int getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(int idFoto) {
        this.idFoto = idFoto;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTipoNotif() {
        return tipoNotif;
    }

    public void setTipoNotif(String tipoNotif) {
        this.tipoNotif = tipoNotif;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}