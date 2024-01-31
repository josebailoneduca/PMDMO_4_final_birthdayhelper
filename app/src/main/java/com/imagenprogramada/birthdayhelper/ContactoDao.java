package com.imagenprogramada.birthdayhelper;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactoDao {
    @Insert
    void insert(Contacto contacto);
    @Update
    void update(Contacto contacto);
    @Delete
    void delete(Contacto contacto);

    @Query("DELETE FROM contacto_tabla")
    void deleteAllContactos();

    @Query("SELECT * FROM contacto_tabla ORDER BY nombre DESC")
    LiveData<List<Contacto>> getAllContactos();
}
