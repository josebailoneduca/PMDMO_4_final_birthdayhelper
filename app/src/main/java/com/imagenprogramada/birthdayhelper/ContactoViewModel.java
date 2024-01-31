package com.imagenprogramada.birthdayhelper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class ContactoViewModel extends AndroidViewModel {

    private ContactoRepositorio contactoRepositorio;
    private LiveData<List<Contacto>> allContactos;
    public ContactoViewModel(@NonNull Application application) {
        super(application);
        contactoRepositorio = new ContactoRepositorio(application);
        allContactos=contactoRepositorio.getAllContactos();
    }

    public void insert(Contacto contacto){
        contactoRepositorio.insert(contacto);
    }
    public void update(Contacto contacto){
        contactoRepositorio.update(contacto);
    }

    public void delete(Contacto contacto){
        contactoRepositorio.delete(contacto);
    }

    public void deleteAllContacto(){
        contactoRepositorio.deleteAllContactos();
    }
    public LiveData<List<Contacto>> getAllContactos(){
        return allContactos;
    }
}
