package com.imagenprogramada.birthdayhelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.imagenprogramada.birthdayhelper.databinding.FragmentEditarContactoBinding;
import com.imagenprogramada.birthdayhelper.repositorio.Contacto;

/**
 * Fragmento para editar un contacto
 */
public class EditarContactoFragment extends Fragment {

    private FragmentEditarContactoBinding binding;
    private ContactosViewModel viewModel;
    private Contacto contacto;
    int idContacto=0;

    /**
     * On create view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEditarContactoBinding.inflate(inflater, container, false);
        idContacto=getArguments().getInt("idcontacto");
        return binding.getRoot();

    }

    /**
     * On viewCreated. Recoge la instancia del viewModel y lanza el rellenado del formulario.
     * También establece los listener de los botones
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=  new ViewModelProvider(requireActivity()).get(ContactosViewModel.class);
        contacto=viewModel.getContacto(idContacto);
        if(contacto!=null)
            rellenarFormulario();
        binding.btnGuardar.setOnClickListener(v -> guardar());
        binding.btnEditar.setOnClickListener(v -> editar());
    }

    /**
     * OnResume actualiza la fecha de cumpleaños por si se ha modificado al editar el contacto
     */
    @Override
    public void onResume() {
        super.onResume();
        binding.fecha.setText(viewModel.getFechaCumple(idContacto));
    }

    /**
     * Abre el editor de contactos propio del telefono
     */
    private void editar() {
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contacto.getID()));
        editIntent.setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        getContext().startActivity(editIntent);
    }

    /**
     * Guarda los cambios del contacto en la base de datos de la aplicacion
     */
    private void guardar() {
        contacto.setMensaje(binding.mensaje.getText().toString());
        contacto.setTipoNotif((binding.tipo.isChecked()?Contacto.SMS:Contacto.SOLO_NOTIFICACION));
        contacto.setFechaNacimiento(binding.fecha.getText().toString());
        viewModel.update(contacto);
        Toast.makeText(this.getContext(),"Guardado",Toast.LENGTH_SHORT).show();
    }

    /**
     * Rellena los campos del formulario
     */
    private void rellenarFormulario() {
        binding.nombre.setText(contacto.getNombre());
        if (contacto.getFoto()!=null)
            binding.foto.setImageBitmap(contacto.getFoto());
        binding.telefono.setText(contacto.getTelefono());
        binding.fecha.setText(contacto.getFechaNacimiento());
        binding.mensaje.setText(contacto.getMensaje());
        binding.tipo.setChecked(contacto.getTipoNotif().equals(Contacto.SMS));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}