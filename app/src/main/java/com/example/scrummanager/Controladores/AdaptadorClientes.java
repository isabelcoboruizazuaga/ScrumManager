package com.example.scrummanager.Controladores;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorClientes extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Cliente> clientes;
    private Context contexto;
    private Cliente cliente;
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;
    StorageReference storageReference;

    public AdaptadorClientes(ArrayList<Cliente> clientes, Context contexto) {
        this.clientes = clientes;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == MOSTRAR_MENU) {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_cliente, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorClientes.MenuViewHolder(v);
        } else {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorClientes.AdaptadorClientesViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        cliente = clientes.get(position);

        String nombreCliente = cliente.getNombreCliente();
        String email= cliente.getEmailCliente();
        String tipo= cliente.getTipoCliente();
        String apellido= cliente.getApellidoCliente();

        //Si se está mostrando el Empleado
        if (holder instanceof AdaptadorClientesViewHolder) {

            //Se incluye el empleado en el layout
            ((AdaptadorClientesViewHolder) holder).tv_nombreCliente.setText(nombreCliente.toString());
            ((AdaptadorClientesViewHolder) holder).tv_apellidoCliente.setText(apellido.toString());
            ((AdaptadorClientesViewHolder) holder).tv_tipoCliente.setText(tipo.toString());
            ((AdaptadorClientesViewHolder) holder).tv_emailCliente.setText(email.toString());

            //Imagen de perfil
            storageReference= FirebaseStorage.getInstance().getReference();
            StorageReference perfilRef= storageReference.child("clients/"+cliente.getNifCliente()+"/profile.jpg");
            perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(((AdaptadorClientes.AdaptadorClientesViewHolder) holder).iv_imagenCliente);
                }
            });


            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorClientesViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mostrarMenu(position);
                    return true;
                }
            });

        }
        //Si se está mostrando el menú
        if (holder instanceof MenuViewHolder) {
            //Se asignan onClickListeners para las opciones del menú
            ((MenuViewHolder) holder).btn_atrasCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((MenuViewHolder) holder).btn_verCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verCliente();
                }
            });
            ((MenuViewHolder) holder).btn_editarCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarCliente();
                }
            });
            ((MenuViewHolder) holder).btn_borrarCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarCliente();
                }
            });

            //Si se mantiene pulsado se cierra el menú de opciones
            ((MenuViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    cerrarMenu();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        try{
            return clientes.size();
        }catch (NullPointerException e){
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (clientes.get(position).isShowMenu()) {
            return MOSTRAR_MENU;
        } else {
            return OCULTAR_MENU;
        }
    }

    public class AdaptadorClientesViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_nombreCliente, tv_apellidoCliente, tv_tipoCliente, tv_emailCliente;
        private ImageView iv_imagenCliente;

        public AdaptadorClientesViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreCliente = itemView.findViewById(R.id.tv_nombreCliente);
            tv_apellidoCliente = itemView.findViewById(R.id.tv_apellidoCliente);
            tv_tipoCliente = itemView.findViewById(R.id.tv_empresaCliente);
            tv_emailCliente = itemView.findViewById(R.id.tv_emailCliente);
            iv_imagenCliente= itemView.findViewById(R.id.fotoCliente);

        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private ImageButton btn_verCliente, btn_atrasCliente, btn_borrarCliente, btn_editarCliente;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            btn_verCliente = itemView.findViewById(R.id.btn_verCliente);
            btn_atrasCliente = itemView.findViewById(R.id.btn_atrasCliente);
            btn_borrarCliente = itemView.findViewById(R.id.btn_borrarCliente);
            btn_editarCliente = itemView.findViewById(R.id.btn_editarCliente);
        }
    }

    public void mostrarMenu(int position) {
        for (int i = 0; i < clientes.size(); i++) {
            clientes.get(i).setShowMenu(false);
        }
        clientes.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuMostrado() {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).isShowMenu()) {
                return true;
            }
        }
        return false;
    }

    public void cerrarMenu() {
        for (int i = 0; i < clientes.size(); i++) {
            clientes.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    public void editarCliente() {
        System.out.println("Editandooooo");
        Toast.makeText(contexto, "EDITANDO SEÑORES", Toast.LENGTH_LONG);
    }

    public void verCliente() {
        System.out.println("VIENDOOOOOOO");
        Toast.makeText(contexto, "VIENDO SEÑORES", Toast.LENGTH_LONG);
    }

    public void borrarCliente() {
        System.out.println("BORRANDOOOOOOO");
        Toast.makeText(contexto, "BORRANDO SEÑORES", Toast.LENGTH_LONG);
    }
}