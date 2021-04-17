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
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;

import java.util.ArrayList;

public class AdaptadorEmpleados extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Empleado> empleados;
    private Context contexto;
    private Empleado empleado;
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;

    public AdaptadorEmpleados(ArrayList<Empleado> empleados, Context contexto) {
        this.empleados = empleados;
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
            return new AdaptadorEmpleados.MenuViewHolder(v);
        } else {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleado, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorEmpleados.AdaptadorEmpleadosViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        empleado = empleados.get(position);

        String nombreEmpleado = empleado.getNombreEmpleado();

        //Si se está mostrando el Empleado
        if (holder instanceof AdaptadorEmpleadosViewHolder) {

            //Se incluye el empleado en el layout
            ((AdaptadorEmpleadosViewHolder) holder).tv_nombreEmpleado.setText(nombreEmpleado.toString());

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorEmpleadosViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
        return empleados.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (empleados.get(position).isShowMenu()) {
            return MOSTRAR_MENU;
        } else {
            return OCULTAR_MENU;
        }
    }

    public class AdaptadorEmpleadosViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_nombreEmpleado, iv_fotoEmpleado;

        public AdaptadorEmpleadosViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreEmpleado = itemView.findViewById(R.id.tv_nombreEmpleado);
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
        for (int i = 0; i < empleados.size(); i++) {
            empleados.get(i).setShowMenu(false);
        }
        empleados.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuMostrado() {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).isShowMenu()) {
                return true;
            }
        }
        return false;
    }

    public void cerrarMenu() {
        for (int i = 0; i < empleados.size(); i++) {
            empleados.get(i).setShowMenu(false);
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