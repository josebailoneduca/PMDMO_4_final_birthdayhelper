package com.imagenprogramada.birthdayhelper;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.imagenprogramada.birthdayhelper.databinding.ActivityMainBinding;
import com.imagenprogramada.birthdayhelper.repositorio.Contacto;
import com.imagenprogramada.birthdayhelper.repositorio.HoraAvisos;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewOutlineProvider;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Actividad principal.  Controla el establecimiento de la alarma y el menu superior.
 * Tambien inicializa el viewmodel ycomprueba los permisos necesarios para acceder a los
 * contactos y mandar sms.
 */
public class MainActivity extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private ContactosViewModel contactoViewModel;

    private int hora=0;
    private int minutos=0;

    /**
     * OnCreate hace las configuraciones iniciales de navegacion y de control del menu de acciones.
     * Recoge del viewmodel la hora establecida actualmente para la alarma
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //action bar
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        contactoViewModel= new  ViewModelProvider(this).get(ContactosViewModel.class);
        contactoViewModel.getAllAvisos().observe(this,new Observer<List<HoraAvisos>>() {
            @Override
            public void onChanged(List<HoraAvisos> horaAvisos) {
                if (horaAvisos!=null){
                    horaAvisos.stream().forEach(horaAvisos1 -> {
                        hora=horaAvisos1.getHora();
                        minutos=horaAvisos1.getMinutos();
                    });
                }
            }
        });
        obtenerContactos();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Escucha de seleccion del menud eeditar alarma lanzando el timepickerdialog si se pulsa.
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Calendar c = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,this,hora,minutos,true);
            timePickerDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Confirma permisos y si se tiene se recogen los contactos y se pasan al adaptador. Si no se
     * tienen los permisos se piden
     */
    private void obtenerContactos() {

        //comprobar permiso

        if (
                ContextCompat.checkSelfPermission(this,"android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED
                        ||
                        ContextCompat.checkSelfPermission(this,"android.permission.SEND_SMS") != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CONTACTS","android.permission.SEND_SMS"}, 1);
        } else {
            contactoViewModel.actualizarContactosDesdeTelefono();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 1){
            //si el permiso ha sido concedido
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                contactoViewModel.actualizarContactosDesdeTelefono();
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED||grantResults[1] == PackageManager.PERMISSION_DENIED){
                //si no ha sido concedido entonces:
                // Si aún no ha marcado el no ser preguntado más se le da al usuario
                // una explicacion de la necesidad de dar el permiso
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_CONTACTS") ||ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.SEND_SMS") ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.msg_explicacion).setTitle(R.string.aviso);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    //Si ha marcado que no quiere volver a ser preguntado se le explica que es imprescindible
                    //y que si quiere usar la aplicacion debe aceptarlo manualmente en la configuracion del telefono
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.msg_explicacion_manual).setTitle(R.string.aviso);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }


    /**
     * Escucha del cambio de hora de alarma desde el timepicker. Guarda en la base de datos la hora
     * elegida y lanza la alarma de comprobacion de cumpleaños al AlarmManager
     * @param view the view associated with this listener
     * @param hourOfDay the hour that was set
     * @param minute the minute that was set
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        contactoViewModel.setHora(hourOfDay,minute);
        //generar alarma
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        /* Set up the alarm */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(getApplicationContext(), Alarma.class);


        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmIntent = PendingIntent.getActivity(this,
                    1, intent,PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        }else {
            alarmIntent = PendingIntent.getActivity(this,
                    1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }



        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(
                getApplicationContext().ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
        Log.i("jjbo","Define alarma a las "+hourOfDay+":"+minute);
    }




}