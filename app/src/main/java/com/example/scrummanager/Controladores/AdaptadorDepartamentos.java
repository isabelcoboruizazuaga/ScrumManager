package com.example.scrummanager.Controladores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.R;

import java.util.ArrayList;

public class AdaptadorDepartamentos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Departamento> departamentos;
    private Context contexto;
    private Departamento departamento;
    private final int MOSTRAR_MENU=1, OCULTAR_MENU=2;

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
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.departamento_item, parent, false);
            //Se crea el ViewHolder
            return new MenuViewHolder(v);
        }else{
            //Se infla la View
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.departamento_item, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorDepartamentosViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        departamento= departamentos.get(position);

        if(holder instanceof AdaptadorDepartamentosViewHolder){
            String nombreDepartamento= departamento.getNombreDepartamento();

            //Se incluye el departamento en el layout
            ((AdaptadorDepartamentosViewHolder) holder).tv_nombreDepartamento.setText(nombreDepartamento.toString());

            ((AdaptadorDepartamentosViewHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mostrarMenu(position);
                    return true;
                }
            });

        }
        if(holder instanceof MenuViewHolder){
            String nombreDepartamento= departamento.getNombreDepartamento();

            //Se incluye el departamento en el layout
            ((MenuViewHolder) holder).tv_nombreDepartamento.setText("AAAAAAAAA");

        }
    }

    @Override
    public int getItemCount() {
        return departamentos.size();
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
        private TextView tv_nombreDepartamento, iv_fotoDepartamento;
        public AdaptadorDepartamentosViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto= itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreDepartamento= itemView.findViewById(R.id.tv_nombreDepartamento);
            //iv_fotoDepartamento= itemView.findViewById(R.id.iv_fotoDepartamento);

        }
    }
    public class MenuViewHolder extends RecyclerView.ViewHolder{
        //items del layout
        private TextView tv_nombreDepartamento, iv_fotoDepartamento;
        public MenuViewHolder(@NonNull View itemView){
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto= itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreDepartamento= itemView.findViewById(R.id.tv_nombreDepartamento);
            //iv_fotoDepartamento= itemView.findViewById(R.id.iv_fotoDepartamento);

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
}
