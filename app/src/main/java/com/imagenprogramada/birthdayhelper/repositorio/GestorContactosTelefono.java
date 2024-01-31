package com.imagenprogramada.birthdayhelper.repositorio;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class GestorContactosTelefono {
    Application aplicacion;
    public GestorContactosTelefono(Application application) {
        this.aplicacion=application;
    }

    @SuppressLint("Range")
    public List<Contacto> getAll() {
            //SELECT
            String proyeccion[]={ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
                    ContactsContract.Contacts.PHOTO_ID,
            };

            //Crear lista con el resultado
            ArrayList<Contacto> lista_contactos=new ArrayList<Contacto>();
            ContentResolver cr = aplicacion.getContentResolver();
            Cursor cursorContactos = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    proyeccion, null, null, null);
            //Iterar el cursor
            if (cursorContactos.getCount() > 0) {
                while (cursorContactos.moveToNext()) {
                    //Id del contacto
                    int idContacto = cursorContactos.getInt(
                            cursorContactos.getColumnIndex(ContactsContract.Contacts._ID));
                    //Nombre del contacto
                    String nombreContacto = cursorContactos.getString(
                            cursorContactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //Ver si tiene telefono para quedarnos solo con los que tengan telefono
                    boolean tieneTelefono=Integer.parseInt(cursorContactos.getString(cursorContactos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0;
                    if (tieneTelefono) {
                        String telefono="";
                        //Obtener telefono haciendo query
                        Cursor cursorTelefono = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{String.valueOf(idContacto)}, null);
                        //recoger ultimo telefono
                        while (cursorTelefono.moveToNext()) {
                            telefono = cursorTelefono.getString(cursorTelefono.getColumnIndex
                                    (ContactsContract.CommonDataKinds.Phone.DATA));
                        }
                        //agregar a la lista
                        lista_contactos.add(new Contacto(idContacto,Contacto.SOLO_NOTIFICACION,"",telefono,"",nombreContacto));
                    }
                }
            }
            cursorContactos.close(); //close the cursor
            return lista_contactos;
        }
}