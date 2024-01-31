package com.imagenprogramada.birthdayhelper;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactoRepositorio {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private ContactoDao contactoDao;
    private LiveData<List<Contacto>> allContactos;

    public ContactoRepositorio (Application application){
        ContactoDatabase database = ContactoDatabase.getInstance(application);
        contactoDao = database.contactoDao();
        allContactos = contactoDao.getAllContactos();
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
        return allContactos;
    }



}
