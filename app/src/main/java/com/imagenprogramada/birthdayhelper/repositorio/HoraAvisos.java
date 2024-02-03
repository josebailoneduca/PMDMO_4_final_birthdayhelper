package com.imagenprogramada.birthdayhelper.repositorio;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de hora de alarma para la base de datos
 */
@Entity(tableName="hora_tabla")
public class HoraAvisos implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private int hora;
    private int minutos;

    public HoraAvisos(int hora, int minutos){
        this.hora = hora;
        this.minutos = minutos;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }
}