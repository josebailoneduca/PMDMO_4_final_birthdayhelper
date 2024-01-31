package com.imagenprogramada.birthdayhelper.repositorio;

import android.app.Application;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.imagenprogramada.birthdayhelper.ContactoDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactoRepositorio {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private  ContactoDao contactoDao;

    private MutableLiveData<List<Contacto>> _allContactos;

    private GestorContactosTelefono gct;


    public ContactoRepositorio (Application application){
        ContactoDatabase database = ContactoDatabase.getInstance(application);
        contactoDao = database.contactoDao();
        gct=new GestorContactosTelefono(application);
        _allContactos.setValue(contactoDao.getAllContactos());
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
        _allContactos.setValue(contactoDao.getAllContactos());
    }

//
//
//    private void obtenerContactosDelTelefono() {
//
//        //comprobar permiso
//
//        if (
//                ContextCompat.checkSelfPermission(this,"android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED
//                        ||
//                        ContextCompat.checkSelfPermission(this,"android.permission.SEND_SMS") != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CONTACTS","android.permission.SEND_SMS"}, 1);
//        } else {
//            ArrayList<Contacto> contactosRecogidos = buscar(binding.textoBusqueda.getText().toString());
//            contactos.clear();
//            contactos.addAll(contactosRecogidos);
//            adapter.notifyDataSetChanged();
//
//        }
//    }

}
