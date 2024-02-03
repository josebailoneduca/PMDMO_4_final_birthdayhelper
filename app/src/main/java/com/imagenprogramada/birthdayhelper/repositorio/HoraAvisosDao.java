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
public interface HoraAvisosDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HoraAvisos horaAvisos);
    @Update
    void update(HoraAvisos horaAvisos);
    @Delete
    void delete(Contacto contacto);

    @Query("SELECT * FROM hora_tabla")
    LiveData<List<HoraAvisos>> getAllHora();
}
