package com.imagenprogramada.birthdayhelper;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
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

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewOutlineProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private ContactosViewModel contactoViewModel;


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
        obtenerContactos();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

}