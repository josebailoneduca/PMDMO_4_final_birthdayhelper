package com.imagenprogramada.birthdayhelper.repositorio;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contacto contacto);
    @Update
    void update(Contacto contacto);
    @Delete
    void delete(Contacto contacto);

    @Query("DELETE FROM contacto_tabla")
    void deleteAllContactos();

    @Query("SELECT * FROM contacto_tabla ORDER BY nombre DESC")
    List<Contacto> getAllContactos();
}
