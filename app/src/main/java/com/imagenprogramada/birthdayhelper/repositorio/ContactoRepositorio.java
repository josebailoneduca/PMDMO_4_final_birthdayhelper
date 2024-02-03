package com.imagenprogramada.birthdayhelper.repositorio;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.imagenprogramada.birthdayhelper.ContactoDatabase;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
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


    public ContactoRepositorio (Context application){
        ContactoDatabase database = ContactoDatabase.getInstance(application);
        contactoDao = database.contactoDao();
        horaAvisosDao = database.horaAvisosDao();
        gct=new GestorContactosTelefono(application);
        _allContactos=contactoDao.getAllContactos();
        _allHoras= horaAvisosDao.getAllHora();
    }


    public void guardarHora(int hora,int minutos){
        executor.execute(() -> {
            horaAvisosDao.insert(new HoraAvisos(hora,minutos));
        });
    }

    public void insert(Contacto contacto){
        executor.execute(() -> contactoDao.insert(contacto));
    }

    public void update(Contacto contacto){
        executor.execute(() -> contactoDao.update(contacto));
    }
    public void delete(Contacto contacto){
        executor.execute(() -> contactoDao.delete(contacto));
    }
    public void deleteAllContactos(){
        executor.execute(()->contactoDao.deleteAllContactos());
    }

    public LiveData<List<Contacto>> getAllContactos() {
        return _allContactos;
    }

    public List<Contacto> getAllContactosSynchronous(){
        return contactoDao.getAllContactosSynchronous();
    }
    public LiveData<List<HoraAvisos>> getAllAvisos() {
        return _allHoras;
    }

    public void actualzarContactosDesdeTelefono() {
        List<Contacto> contactosTel = gct.getAll();
        contactosTel.stream().forEach(contacto -> insert(contacto));
    }


    public String getFechaCumple(int idContacto) {
        return gct.getCumpleaños(idContacto);
    }
}
