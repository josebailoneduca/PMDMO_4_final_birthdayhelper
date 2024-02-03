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


/**
 * ViewModel de contactos
 */
public class ContactosViewModel extends AndroidViewModel {

    private ContactoRepositorio contactoRepositorio;

    private LiveData<List<Contacto>> allContactos;
    private LiveData<List<HoraAvisos>> allAvisos;


    /**
     * Constructor
     * @param application
     */
    public ContactosViewModel(@NonNull Application application) {
        super(application);
        contactoRepositorio = new ContactoRepositorio(application);
        allContactos=contactoRepositorio.getAllContactos();
        allAvisos=contactoRepositorio.getAllAvisos();
    }

    /**
     * Insertar un contacto
     * @param contacto
     */
    public void insert(Contacto contacto){
        contactoRepositorio.insert(contacto);
    }

    /**
     * Actualizar un contacto
     * @param contacto
     */
    public void update(Contacto contacto){
        contactoRepositorio.update(contacto);
    }

    /**
     * Borrar un contacto
     * @param contacto
     */
    public void delete(Contacto contacto){
        contactoRepositorio.delete(contacto);
    }

    /**
     * Borrar todos los contactos
     */
    public void deleteAllContacto(){
        contactoRepositorio.deleteAllContactos();
    }

    /**
     * Devuelve el livedata de los contactos
     * @return
     */
    public LiveData<List<Contacto>> getAllContactos(){
        return allContactos;
    }

    /**
     * Devuelve el livedata de la hora de aviso de la alarma
     * @return
     */
    public LiveData<List<HoraAvisos>> getAllAvisos(){
        return allAvisos;
    }

    /**
     * Lanzar la actualizacion de contactos recogidos del telefono
     */
    public void actualizarContactosDesdeTelefono() {
       contactoRepositorio.actualzarContactosDesdeTelefono();
    }

    /**
     * Devuelve un contacto en particular
     * @param idContacto
     * @return
     */
    public Contacto getContacto(int idContacto) {
        for (Contacto c: allContactos.getValue()) {
            if (c.getID()==idContacto)
                return c;
        }
        return null;
    }

    /**
     * Devuelve la fecha de cumpleaños de un contacto dada su id
     * @param idContacto
     * @return
     */
    public String getFechaCumple(int idContacto) {
        return contactoRepositorio.getFechaCumple(idContacto);
    }

    /**
     * Guarda la hora de alarma
     * @param hourOfDay
     * @param minute
     */
    public void setHora(int hourOfDay, int minute) {
        contactoRepositorio.guardarHora(hourOfDay,minute);
    }
}
