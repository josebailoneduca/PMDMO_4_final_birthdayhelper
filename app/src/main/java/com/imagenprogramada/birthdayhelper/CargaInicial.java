package com.imagenprogramada.birthdayhelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.imagenprogramada.birthdayhelper.databinding.FragmentCargaInicialBinding;
import com.imagenprogramada.birthdayhelper.databinding.FragmentListaContactosBinding;

public class CargaInicial extends Fragment {

    private FragmentCargaInicialBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCargaInicialBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.buttonFirst.setOnClickListener(v ->
//                        NavHostFragment.findNavController(this).navigate(R.id.action_cargaInicial_to_ListaContactos)
//                );
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("contacto",new Contacto("holita"));
//                NavHostFragment.findNavController(ListaContactos.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment,bundle);
        
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}