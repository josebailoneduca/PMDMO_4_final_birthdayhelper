package com.imagenprogramada.birthdayhelper;


import android.os.Bundle;
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

public class ListaContactosFragment extends Fragment {

    private FragmentListaContactosBinding binding;
    private ContactoRepositorio repositorio;
    private ContactosViewModel viewModel;
    private ContactosRecyclerViewAdapter adapter;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentListaContactosBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=  new  ViewModelProvider(requireActivity()).get(ContactosViewModel.class);

        viewModel.getAllContactos().observe(getActivity(), new Observer<List<Contacto>>() {
            @Override
            public void onChanged(List<Contacto> contactos) {
                adapter.setContactos(contactos);
            }
        });

        binding.listaContactosRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
         adapter= new ContactosRecyclerViewAdapter(this,viewModel.getAllContactos().getValue());
         binding.listaContactosRecycleView.setAdapter(adapter);

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                //bundle.putSerializable("contacto",new Contacto("holita"));
//                NavHostFragment.findNavController(ListaContactosFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void editar(int id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("idcontacto",id);
        NavHostFragment.findNavController(ListaContactosFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
    }
}