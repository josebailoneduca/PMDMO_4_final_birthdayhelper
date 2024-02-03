package com.imagenprogramada.birthdayhelper;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.imagenprogramada.birthdayhelper.databinding.FragmentListaContactosBinding;
import com.imagenprogramada.birthdayhelper.repositorio.Contacto;
import com.imagenprogramada.birthdayhelper.repositorio.ContactoRepositorio;

import java.util.List;


/**
 * Fragmento de lista de contactos
 */
public class ListaContactosFragment extends Fragment {

    private FragmentListaContactosBinding binding;
    private ContactoRepositorio repositorio;
    private ContactosViewModel viewModel;
    private ContactosRecyclerViewAdapter adapter;

    /**
     * OnCreateView
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

        binding = FragmentListaContactosBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    /**
     * OnViewCreated recoge la instancia del viewmodel y activa el observador de livedata ademas
     * de inicializar el recyclerview de la lista y establecer los listener de lla caja de texto
     * de busqueda
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=  new  ViewModelProvider(requireActivity()).get(ContactosViewModel.class);

        viewModel.getAllContactos().observe(getActivity(), new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                if (binding!=null&&binding.inputBuscar!=null) {
                    adapter.setContactos(contactos, binding.inputBuscar.getText().toString());
                }
            }
        });

        binding.listaContactosRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
         adapter= new ContactosRecyclerViewAdapter(this,viewModel.getAllContactos().getValue());
         binding.listaContactosRecycleView.setAdapter(adapter);
         binding.inputBuscar.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 List<Contacto> lista = viewModel.getAllContactos().getValue();
                 if (lista!=null) {
                     adapter.setContactos(lista, binding.inputBuscar.getText().toString());
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /**
     * Lanza la navegacion hacia la edicion de un contacto
     * @param id
     */
    public void editar(int id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("idcontacto",id);
        NavHostFragment.findNavController(ListaContactosFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
    }
}