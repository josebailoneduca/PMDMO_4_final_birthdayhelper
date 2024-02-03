package com.imagenprogramada.birthdayhelper;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.imagenprogramada.birthdayhelper.databinding.FragmentListaContactosItemBinding;
import com.imagenprogramada.birthdayhelper.repositorio.Contacto;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactosRecyclerViewAdapter extends RecyclerView.Adapter<ContactosRecyclerViewAdapter.ViewHolder> {


    private List<Contacto> lista =new ArrayList<>();
    /**
     * Referencia al fragmento para tener acceso a la funcion de mandar email
     */
    ListaContactosFragment fragment;
    Context contexto;

    public ContactosRecyclerViewAdapter(ListaContactosFragment fragment,List<Contacto> lista){
        this.fragment=fragment;
        this.lista=lista;
    }

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
        contexto=parent.getContext();
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
        holder.mTelefono.setText(holder.mItem.getTelefono());
        holder.mTipo.setText(holder.mItem.getTipoNotif());

        if(holder.mItem.getFoto()==null){
            Uri contactUri= ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,holder.mItem.getID());
            InputStream inputStream=ContactsContract.Contacts.
                    openContactPhotoInputStream(contexto.getContentResolver(),
                            contactUri,true);
            Bitmap foto=BitmapFactory.decodeStream(inputStream);
                    holder.mItem.setFoto(foto);
        }

        holder.mFoto.setImageBitmap(holder.mItem.getFoto());

        holder.mBtn.setOnClickListener(v -> fragment.editar(holder.mItem.getID()) );
    }



    @Override
    public int getItemCount() {
        if (lista!=null)
        return lista.size();
        else return 0;
    }


    public void setContactos(List<Contacto> contactos, String busqueda){
        if (busqueda.length()>0){
         this.lista=contactos.stream().filter(contacto -> {
             return contacto.getNombre().toLowerCase().contains(busqueda.toLowerCase());
         }).collect(Collectors.toList());
        }
            else {
            this.lista = contactos;
        }
        notifyDataSetChanged();
    }




    /**
     * Clase de vistas de items
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Contacto mItem;

        public final TextView mNombre;
        public final TextView mTelefono;
        public final TextView mTipo;

        public final ImageView mFoto;
        public final ImageButton mBtn;
        public ViewHolder(FragmentListaContactosItemBinding binding) {
            super(binding.getRoot());
            mNombre=binding.nombre;
            mTelefono=binding.telefono;
            mTipo=binding.tipo;
            mFoto=binding.foto;
            mBtn=binding.btnEditar;
        }
    }
}