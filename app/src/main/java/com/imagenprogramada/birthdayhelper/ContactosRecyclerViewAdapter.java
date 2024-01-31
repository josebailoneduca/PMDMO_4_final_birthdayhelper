package com.imagenprogramada.birthdayhelper;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.imagenprogramada.birthdayhelper.databinding.FragmentListaContactosItemBinding;
import com.imagenprogramada.birthdayhelper.repositorio.Contacto;

import java.util.ArrayList;
import java.util.List;

public class ContactosRecyclerViewAdapter extends RecyclerView.Adapter<ContactosRecyclerViewAdapter.ViewHolder> {


    private List<Contacto> lista =new ArrayList<>();
    /**
     * Referencia al fragmento para tener acceso a la funcion de mandar email
     */
    ListaContactosFragment fragment;



    public void setData(List<Contacto> lista){
        this.lista =lista;
    }
    /**
     * Creacion de vistas
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentListaContactosItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    /**
     * Rellenar una vista con los datos que tocan
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = lista.get(position);
        holder.mNombre.setText(holder.mItem.getNombre());
       // holder.mBtnMail.setOnClickListener(v -> fragment.mandarEmail(holder.mItem) );
    }



    @Override
    public int getItemCount() {
        return lista.size();
    }


    public void setContactos(List<Contacto> contactos){
        this.lista =contactos;
        notifyDataSetChanged();
    }


    /**
     * Clase de vistas de items
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Contacto mItem;

        public final TextView mNombre;


        public ViewHolder(FragmentListaContactosItemBinding binding) {
            super(binding.getRoot());
            mNombre=binding.nombre;
        }
    }
}