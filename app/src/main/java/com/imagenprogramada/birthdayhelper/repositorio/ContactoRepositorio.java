package com.imagenprogramada.birthdayhelper.repositorio;

import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imagenprogramada.birthdayhelper.ContactoDatabase;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactoRepositorio {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private  ContactoDao contactoDao;

    private LiveData<List<Contacto>> _allContactos=new MutableLiveData<List<Contacto>>();

    private GestorContactosTelefono gct;


    public ContactoRepositorio (Application application){
        ContactoDatabase database = ContactoDatabase.getInstance(application);
        contactoDao = database.contactoDao();
        gct=new GestorContactosTelefono(application);
        _allContactos=contactoDao.getAllContactos();

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

    public void actualzarContactosDesdeTelefono() {
        List<Contacto> contactosTel = gct.getAll();
        contactosTel.stream().forEach(contacto -> insert(contacto));
    }





}
