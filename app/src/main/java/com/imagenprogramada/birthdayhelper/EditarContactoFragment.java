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

public class EditarContactoFragment extends Fragment {

    private FragmentEditarContactoBinding binding;
    private ContactosViewModel viewModel;
    private Contacto contacto;
    int idContacto=0;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEditarContactoBinding.inflate(inflater, container, false);
        idContacto=getArguments().getInt("idcontacto");

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=  new ViewModelProvider(requireActivity()).get(ContactosViewModel.class);
        contacto=viewModel.getContacto(idContacto);
        if(contacto!=null)
            rellenarFormulario();
        binding.btnGuardar.setOnClickListener(v -> guardar());
        binding.btnEditar.setOnClickListener(v -> editar());
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.fecha.setText(viewModel.getFechaCumple(idContacto));
    }

    private void editar() {
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contacto.getID()));
        editIntent.setDataAndType(uri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        getContext().startActivity(editIntent);
    }

    private void guardar() {
        contacto.setMensaje(binding.mensaje.getText().toString());
        contacto.setTipoNotif((binding.tipo.isChecked()?Contacto.SMS:Contacto.SOLO_NOTIFICACION));
        contacto.setFechaNacimiento(binding.fecha.getText().toString());
        viewModel.update(contacto);
        Toast.makeText(this.getContext(),"Guardado",Toast.LENGTH_SHORT).show();
    }

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