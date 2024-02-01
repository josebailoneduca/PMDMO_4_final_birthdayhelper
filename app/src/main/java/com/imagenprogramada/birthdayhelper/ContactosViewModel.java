package com.imagenprogramada.birthdayhelper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imagenprogramada.birthdayhelper.repositorio.Contacto;
import com.imagenprogramada.birthdayhelper.repositorio.ContactoRepositorio;

import java.util.ArrayList;
import java.util.List;


public class ContactosViewModel extends AndroidViewModel {

    private ContactoRepositorio contactoRepositorio;

    private LiveData<List<Contacto>> allContactos;


    public ContactosViewModel(@NonNull Application application) {
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

    public void actualizarContactosDesdeTelefono() {
       contactoRepositorio.actualzarContactosDesdeTelefono();
    }

    public Contacto getContacto(int idContacto) {
        for (Contacto c: allContactos.getValue()) {
            if (c.getID()==idContacto)
                return c;
        }
        return null;
    }
}
