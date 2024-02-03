package com.imagenprogramada.birthdayhelper.repositorio;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Creacion de base de datos de Room. Creada con patrón singleton
 */
@Database(entities = {Contacto.class, HoraAvisos.class}, version = 1)
public abstract class ContactoDatabase extends RoomDatabase {
    private static ContactoDatabase instancia;

    public abstract ContactoDao contactoDao();
    public abstract HoraAvisosDao horaAvisosDao();

    public static synchronized ContactoDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(),
                    ContactoDatabase.class, "basedatos_cumple")
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build();
        }
        return instancia;
    }

}