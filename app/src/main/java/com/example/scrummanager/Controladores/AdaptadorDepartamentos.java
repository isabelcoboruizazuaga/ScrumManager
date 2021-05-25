package com.example.scrummanager.Controladores;

import android.content.Context;
import android.content.Intent;
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

import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.R;
import com.example.scrummanager.Vistas.EditarDepartamentoActivity;
import com.example.scrummanager.Vistas.EditarEmpleadoActivity;
import com.example.scrummanager.Vistas.VerDepartamentoActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorDepartamentos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Departamento> departamentos;
    private Context contexto;
    private final int MOSTRAR_MENU=1, OCULTAR_MENU=2;
    StorageReference storageReference;

    public AdaptadorDepartamentos(ArrayList<Departamento> departamentos, Context contexto) {
        this.departamentos = departamentos;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==MOSTRAR_MENU ){
            //Se infla la View
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_departamento, parent, false);
            //Se crea el ViewHolder
            return new MenuViewHolder(v);
        }else{
            //Se infla la View
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_departamento, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorDepartamentosViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Departamento departamento= departamentos.get(position);

        String nombreDepartamento= departamento.getNombreDepartamento();
        String did= departamento.getIdDepartamento();

        //Si se está mostrando el Departamento
        if(holder instanceof AdaptadorDepartamentosViewHolder){

            //Se incluye el departamento en el layout
            ((AdaptadorDepartamentosViewHolder) holder).tv_nombreDepartamento.setText(nombreDepartamento.toString());

            //Imagen de departamento
            storageReference= FirebaseStorage.getInstance().getReference();
            if((storageReference.child("departments/"+did+"/cover.jpg")).getDownloadUrl()!=null) {
                StorageReference perfilRef = storageReference.child("departments/" + did + "/cover.jpg");
                perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(((AdaptadorDepartamentos.AdaptadorDepartamentosViewHolder) holder).iv_fotoDepartamento);
                    }
                });
            }

            //Un click simple muestra el departamento
            ((AdaptadorDepartamentosViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verDepartamento(departamento);
                }
            });

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorDepartamentosViewHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageView image = ((AdaptadorDepartamentos.AdaptadorDepartamentosViewHolder) holder).iv_fotoDepartamento;
                    Picasso.get().cancelRequest(image);
                    mostrarMenu(position);
                    return true;
                }
            });

        }
        //Si se está mostrando el menú
        if(holder instanceof MenuViewHolder){
            //Se incluye el nombre del departamento en el layout
            ((MenuViewHolder) holder).tv_nombreDepartamento.setText(nombreDepartamento.toString());

            //Se asignan onClickListeners para las opciones del menú
            ((MenuViewHolder) holder).btn_atrasDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((MenuViewHolder) holder).btn_verDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verDepartamento(departamento);
                }
            });
            ((MenuViewHolder) holder).btn_editarDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarDepartamento(departamento);
                }
            });
            ((MenuViewHolder) holder).btn_borrarDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarDepartamento();
                }
            });

            //Si se mantiene pulsado se cierra el menú de opciones
            ((MenuViewHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
            return departamentos.size();
        }catch (NullPointerException e){
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(departamentos.get(position).isShowMenu()){
            return MOSTRAR_MENU;
        }else {
            return OCULTAR_MENU;
        }
    }

    public class AdaptadorDepartamentosViewHolder extends RecyclerView.ViewHolder{
        //items del layout
        private TextView tv_nombreDepartamento;
        private ImageView iv_fotoDepartamento;
        public AdaptadorDepartamentosViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto= itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreDepartamento= itemView.findViewById(R.id.tv_nombreDepartamento);
            iv_fotoDepartamento= itemView.findViewById(R.id.iv_fotoDepartamento);

        }
    }
    public class MenuViewHolder extends RecyclerView.ViewHolder{
        //items del layout
        private ImageButton btn_verDepartamento, btn_editarDepartamento,btn_atrasDepartamento, btn_borrarDepartamento;
        private TextView tv_nombreDepartamento;

        public MenuViewHolder(@NonNull View itemView){
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto= itemView.getContext();

            //inicializacón de los elementos del layout
            btn_verDepartamento= itemView.findViewById(R.id.btn_verDepartamento);
            btn_editarDepartamento= itemView.findViewById(R.id.btn_editarDepartamento);
            btn_atrasDepartamento= itemView.findViewById(R.id.btn_atrasDepartamento);
            btn_borrarDepartamento= itemView.findViewById(R.id.btn_borrarDepartamento);
            tv_nombreDepartamento= itemView.findViewById(R.id.tv_nombreDepartamento);
        }
    }

    public void mostrarMenu(int position) {
        for(int i=0; i<departamentos.size(); i++){
            departamentos.get(i).setShowMenu(false);
        }
        departamentos.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuMostrado() {
        for(int i=0; i<departamentos.size(); i++){
            if(departamentos.get(i).isShowMenu()){
                return true;
            }
        }
        return false;
    }

    public void cerrarMenu() {
        for(int i=0; i<departamentos.size(); i++){
            departamentos.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    public void editarDepartamento(Departamento departamento){
        Intent intent= new Intent(contexto, EditarDepartamentoActivity.class);
        intent.putExtra("departamento",departamento);
        contexto.startActivity(intent);
    }

    public void verDepartamento(Departamento departamento){
        Intent intent= new Intent(contexto, VerDepartamentoActivity.class);
        intent.putExtra("departamento",departamento);
        contexto.startActivity(intent);
    }

    public void borrarDepartamento(){
        System.out.println("BORRANDOOOOOOO");
        Toast.makeText(contexto,"BORRANDO SEÑORES", Toast.LENGTH_LONG);
    }

}
