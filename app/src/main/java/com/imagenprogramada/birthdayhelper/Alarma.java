package com.imagenprogramada.birthdayhelper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.imagenprogramada.birthdayhelper.repositorio.Contacto;
import com.imagenprogramada.birthdayhelper.repositorio.ContactoRepositorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BroadcastReceiver que se activa con las alarmas de comprobación de los cumpleaños
 */
public class Alarma extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("jjbo", "onReceive");
        //recoger contactos
        ContactoRepositorio repositorio = new ContactoRepositorio(context);
        List<Contacto> contactos = repositorio.getAllContactosSynchronous();
        ArrayList<Contacto> avisar = new ArrayList<>();
        //filtrar los que cumplen años hoy
        contactos.stream().forEach(contacto -> {
            if (cumpleHoy(contacto)) {
                Log.i("jjbo", "si avissar a" + contacto.getNombre());
                avisar.add(contacto);
            }
        });

        //mandar sms a los que lo tienen marcado
        avisar.forEach(contacto -> {
            if (contacto.getTipoNotif().equals(Contacto.SMS))
                enviarSms(contacto, context);
        });
        //mostrar la notificacion
        lanzarNotificacion(context, avisar);
    }


    /**
     * Comprueba si cumple años hoy
     * @param contacto
     * @return
     */
    private boolean cumpleHoy(Contacto contacto) {
        Date date = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Log.i("jjbo", "Comprobar" + contacto.getNombre() + " " + contacto.getFechaNacimiento() + "" + fDate + " " + (fDate.equals(contacto.getFechaNacimiento())));
        return fDate.equals(contacto.getFechaNacimiento());
    }

    /**
     * Lanza la notificacion de los que cumplen años hoy
     * @param context
     * @param contactos
     */
    private void lanzarNotificacion(Context context, List<Contacto> contactos) {
        Log.i("jjbo", "NUMERO CONTACTOS A AVISAR " + contactos.size());
        //salir si nadie cumple años
        if (contactos.size()==0) return;

        //mostrar la notificacion
       //builder
        int notifId1 = 1; //Identifier for this notification
        NotificationCompat.Builder constructorNotif =
                new NotificationCompat.Builder(context, "mi_canal");
        //configurar notificacion
        constructorNotif.setSmallIcon(android.R.drawable.ic_dialog_alert);
        constructorNotif.setContentTitle("Birthday Helper");
        String listacontactos = contactos.stream().map(contacto -> contacto.getNombre()).collect(Collectors.joining(", "));
        Log.i("jjbo", "Lanzar notificacion de " + listacontactos);
        constructorNotif.setContentText("Hoy es el cumpleaños de " + listacontactos + ".");
        Intent resultadoIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder pila = TaskStackBuilder.create(context);
        pila.addParentStack(MainActivity.class);
        //Add to the top of the stack the intent that launches the app
        pila.addNextIntent(resultadoIntent);
        PendingIntent resultadoPendingIntent =
                pila.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        constructorNotif.setContentIntent(resultadoPendingIntent);
        NotificationManager notificator =
                (NotificationManager) context.getSystemService(context.getApplicationContext().NOTIFICATION_SERVICE);


        //canal de notificacion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel("mi_canal",
                    "Canal birthday helper",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificator.createNotificationChannel(canal);
        }
        //lanzar la notificacion
        notificator.notify(notifId1, constructorNotif.build());
    }


    /**
     * Envia un SMS
     *
     * @param contacto Contacto completo
     */
    public void enviarSms(Contacto contacto, Context context) {

        String texto = contacto.getMensaje();
        String telefono = contacto.getTelefono();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefono, null, texto, null, null);
            Log.i("jjbo","Mensaje:"+contacto.getMensaje()+" . Enviado a "+contacto.getNombre());
            Toast.makeText(context, "Mensaje:"+contacto.getMensaje()+" . Enviado a "+contacto.getNombre(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {

            Toast.makeText(context, "No se pudo enviar el SMS", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
