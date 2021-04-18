package com.example.scrummanager.Controladores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.R;

import java.util.ArrayList;

public class AdaptadorProyectos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Proyecto> proyectos;
    private Context contexto;
    private Proyecto proyecto;
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;

    public AdaptadorProyectos(ArrayList<Proyecto> proyectos, Context contexto) {
        this.proyectos = proyectos;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == MOSTRAR_MENU) {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_empleado, parent, false);
            //Se crea el ViewHolder
            return new MenuViewHolder(v);
        } else {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorProyectosViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        proyecto = proyectos.get(position);

        String nombreProyecto = proyecto.getNombreProyecto();
        Cliente nombreClienteProyecto = proyecto.getCliente();


        //Si se está mostrando el Empleado
        if (holder instanceof AdaptadorProyectosViewHolder) {

            //Se incluye el empleado en el layout
            ((AdaptadorProyectosViewHolder) holder).tv_nombreProyecto.setText(nombreProyecto.toString());
            ((AdaptadorProyectosViewHolder) holder).tv_nombreClienteProyecto.setText("UN CLIENTE");

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorProyectosViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
            ((MenuViewHolder) holder).btn_atrasEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((MenuViewHolder) holder).btn_verEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verDepartamento();
                }
            });
            ((MenuViewHolder) holder).btn_editarEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarDepartamento();
                }
            });
            ((MenuViewHolder) holder).btn_borrarEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarDepartamento();
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
        return proyectos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (proyectos.get(position).isShowMenu()) {
            return MOSTRAR_MENU;
        } else {
            return OCULTAR_MENU;
        }
    }

    public class AdaptadorProyectosViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_nombreProyecto, tv_nombreClienteProyecto;

        public AdaptadorProyectosViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout

            tv_nombreProyecto = itemView.findViewById(R.id.tv_nombreProyecto);
            tv_nombreClienteProyecto = itemView.findViewById(R.id.tv_nombreClienteProyecto);
            //iv_fotoDepartamento= itemView.findViewById(R.id.iv_fotoDepartamento);

        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private ImageButton btn_verEmpleado, btn_atrasEmpleado, btn_borrarEmpleado, btn_editarEmpleado;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            btn_verEmpleado = itemView.findViewById(R.id.btn_verEmpleado);
            btn_atrasEmpleado = itemView.findViewById(R.id.btn_atrasEmpleado);
            btn_borrarEmpleado = itemView.findViewById(R.id.btn_borrarEmpleado);
            btn_editarEmpleado = itemView.findViewById(R.id.btn_editarEmpleado);
        }
    }

    public void mostrarMenu(int position) {
        for (int i = 0; i < proyectos.size(); i++) {
            proyectos.get(i).setShowMenu(false);
        }
        proyectos.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuMostrado() {
        for (int i = 0; i < proyectos.size(); i++) {
            if (proyectos.get(i).isShowMenu()) {
                return true;
            }
        }
        return false;
    }

    public void cerrarMenu() {
        for (int i = 0; i < proyectos.size(); i++) {
            proyectos.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    public void editarDepartamento() {
        System.out.println("Editandooooo");
        Toast.makeText(contexto, "EDITANDO SEÑORES", Toast.LENGTH_LONG);
    }

    public void verDepartamento() {
        System.out.println("VIENDOOOOOOO");
        Toast.makeText(contexto, "VIENDO SEÑORES", Toast.LENGTH_LONG);
    }

    public void borrarDepartamento() {
        System.out.println("BORRANDOOOOOOO");
        Toast.makeText(contexto, "BORRANDO SEÑORES", Toast.LENGTH_LONG);
    }
}