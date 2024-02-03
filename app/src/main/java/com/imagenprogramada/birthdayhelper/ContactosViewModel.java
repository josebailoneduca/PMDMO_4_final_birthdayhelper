package com.imagenprogramada.birthdayhelper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.imagenprogramada.birthdayhelper.repositorio.Contacto;
import com.imagenprogramada.birthdayhelper.repositorio.ContactoRepositorio;
import com.imagenprogramada.birthdayhelper.repositorio.HoraAvisos;

import java.util.List;


public class ContactosViewModel extends AndroidViewModel {

    private ContactoRepositorio contactoRepositorio;

    private LiveData<List<Contacto>> allContactos;
    private LiveData<List<HoraAvisos>> allAvisos;


    public ContactosViewModel(@NonNull Application application) {
        super(application);
        contactoRepositorio = new ContactoRepositorio(application);
        allContactos=contactoRepositorio.getAllContactos();
        allAvisos=contactoRepositorio.getAllAvisos();
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
    public LiveData<List<HoraAvisos>> getAllAvisos(){
        return allAvisos;
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

    public String getFechaCumple(int idContacto) {
        return contactoRepositorio.getFechaCumple(idContacto);
    }

    public void setHora(int hourOfDay, int minute) {
        contactoRepositorio.guardarHora(hourOfDay,minute);
    }
}
