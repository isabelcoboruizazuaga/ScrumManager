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

public class AdaptadorDepartamentos extends RecyclerView.Adapter<AdaptadorDepartamentos.AdaptadorDepartamentosViewHolder> {
    private ArrayList<Departamento> departamentos;
    private Context contexto;
    private Departamento departamento;

    public AdaptadorDepartamentos(ArrayList<Departamento> departamentos, Context contexto) {
        this.departamentos = departamentos;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public AdaptadorDepartamentosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se infla la View
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.departamento_item, parent, false);

        //Se crea el ViewHolder
        AdaptadorDepartamentos.AdaptadorDepartamentosViewHolder avh=new AdaptadorDepartamentos.AdaptadorDepartamentosViewHolder((itemView));

        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorDepartamentosViewHolder holder, int position) {
        departamento= departamentos.get(position);

        String nombreDepartamento= departamento.getNombreDepartamento();

        //Se incluye el departamento en el layout
        holder.tv_nombreDepartamento.setText(nombreDepartamento.toString());
    }

    @Override
    public int getItemCount() {
        return departamentos.size();
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
}
