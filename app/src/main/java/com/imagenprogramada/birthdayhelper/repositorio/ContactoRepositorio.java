package com.imagenprogramada.birthdayhelper.repositorio;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repositorio para acceder a los contactos
 */

public class ContactoRepositorio {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private  ContactoDao contactoDao;
    private  HoraAvisosDao horaAvisosDao;
    private LiveData<List<Contacto>> _allContactos=new MutableLiveData<List<Contacto>>();
    private LiveData<List<HoraAvisos>> _allHoras=new MutableLiveData<List<HoraAvisos>>();
    private GestorContactosTelefono gct;


    /**
     * Constructor carga los datos de la base de datos y agrega los nuevos contactos
     * @param application
     */
    public ContactoRepositorio (Context application){
        ContactoDatabase database = ContactoDatabase.getInstance(application);
        contactoDao = database.contactoDao();
        horaAvisosDao = database.horaAvisosDao();
        gct=new GestorContactosTelefono(application);
        _allContactos=contactoDao.getAllContactos();
        _allHoras= horaAvisosDao.getAllHora();
    }


    /**
     * Guarda la hora de la alarma
     * @param hora
     * @param minutos
     */
    public void guardarHora(int hora,int minutos){
        executor.execute(() -> {
            horaAvisosDao.insert(new HoraAvisos(hora,minutos));
        });
    }

    /**
     * Insertar contacto
     * @param contacto
     */
    public void insert(Contacto contacto){
        executor.execute(() -> contactoDao.insert(contacto));
    }

    /**
     * Update contacto
     * @param contacto
     */
    public void update(Contacto contacto){
        executor.execute(() -> contactoDao.update(contacto));
    }

    /**
     * Borrar contacto
     * @param contacto
     */
    public void delete(Contacto contacto){
        executor.execute(() -> contactoDao.delete(contacto));
    }

    /**
     * Borrar todos los contactos
     */
    public void deleteAllContactos(){
        executor.execute(()->contactoDao.deleteAllContactos());
    }

    /**
     * Devuelve todos los contactos como LiveData
     * @return
     */
    public LiveData<List<Contacto>> getAllContactos() {
        return _allContactos;
    }

    /**
     * Devuelve los contactos de modo síncrono
     * @return
     */
    public List<Contacto> getAllContactosSynchronous(){
        return contactoDao.getAllContactosSynchronous();
    }

    /**
     * Devuelve todas las horas de la tabla de horas
     * @return
     */
    public LiveData<List<HoraAvisos>> getAllAvisos() {
        return _allHoras;
    }

    /**
     * Actualiza contactos desde el telefono
     */
    public void actualzarContactosDesdeTelefono() {
        List<Contacto> contactosTel = gct.getAll();
        contactosTel.stream().forEach(contacto -> insert(contacto));
    }


    /**
     * Devuelve la fecha de cumpleaños de un cotacto cogida desde los contactos del telefono
     * @param idContacto
     * @return
     */
    public String getFechaCumple(int idContacto) {
        return gct.getCumpleaños(idContacto);
    }
}
