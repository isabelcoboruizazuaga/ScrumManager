package com.example.scrummanager.Controladores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.Modelos.Tarea;
import com.example.scrummanager.R;
import com.example.scrummanager.Vistas.EditarSprintActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdaptadorTareas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Tarea> tareas;
    private Proyecto proyecto;
    private Context contexto;
    private Sprint sprint;
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;

    public AdaptadorTareas(ArrayList<Tarea> tareas, Sprint sprint, Proyecto proyecto, Context contexto) {
        this.tareas = tareas;
        this.contexto = contexto;
        this.sprint= sprint;
        this.proyecto=proyecto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == MOSTRAR_MENU) {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_tarea, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorTareas.MenuViewHolder(v);
        } else {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorTareas.AdaptadorTareasViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);

        String nombre = tarea.getNombreTarea();
        String descripcion = tarea.getDescripcionTarea();
        Date creacion= tarea.getFechaCreacion();
        int prioridad= tarea.getPrioridadTarea();
        int tipo= tarea.getTipoTarea();
        String encargado= tarea.getEncargadoTarea();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaCreacion = formatter.format(creacion);


        //Si se está mostrando la tarea
        if (holder instanceof AdaptadorTareas.AdaptadorTareasViewHolder) {
            //Se incluye la tarea en el layout
            ((AdaptadorTareasViewHolder) holder).tv_nombreTarea.setText(nombre);
            ((AdaptadorTareasViewHolder) holder).tv_fechaCreacion.setText(fechaCreacion);
            ((AdaptadorTareasViewHolder) holder).tv_encargadoTarea.setText(encargado);
            ((AdaptadorTareasViewHolder) holder).iv_tipoTarea.setImageResource(R.drawable.others_icon);

            setColor(holder,prioridad);
            setIvTipo(holder,tipo);

            //Un click simple muestra el sprint
            ((AdaptadorTareasViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verSprint(sprint);
                }
            });

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorTareasViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mostrarMenu(position);
                    return true;
                }
            });

        }
        //Si se está mostrando el menú
        if (holder instanceof AdaptadorTareas.MenuViewHolder) {
            //Se asignan onClickListeners para las opciones del menú
            ((MenuViewHolder) holder).btn_atrasTarea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((MenuViewHolder) holder).btn_verTarea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verSprint(sprint);
                }
            });
            ((MenuViewHolder) holder).btn_editarTarea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarSprint(sprint);
                }
            });
            ((MenuViewHolder) holder).btn_borrarTarea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { borrarConfirmacion(sprint,proyecto.getIdEmpresa(),proyecto.getIdProyecto());}
            });
            ((MenuViewHolder) holder).btn_bajarEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            ((MenuViewHolder) holder).btn_subirEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {subirEstado(tarea);}
            });

            //Si se mantiene pulsado se cierra el menú de opciones
            ((AdaptadorTareas.MenuViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    cerrarMenu();
                    return true;
                }
            });
        }
    }

    /**
     * Evalúa la prioridad de la tarea y establece el color de fonde según esta
     * @param holder
     * @param prioridad 0 baja, 1 media, 2 alta, 3 terminal
     */
    public void setColor(RecyclerView.ViewHolder holder, int prioridad){
        ImageView iv_prioridad = ((AdaptadorTareasViewHolder) holder).iv_fondoTarea;

        iv_prioridad.setBackground(ContextCompat.getDrawable(contexto, R.drawable.task_background_icon));
        Drawable buttonDrawable = iv_prioridad.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);

        int color=0;
        switch (prioridad){
            case 0:
                color=0xff00ccff;
                break;
            case 1:
                color=0xff1cc990;
                break;
            case 2:
                color=0xffec9879;
                break;
            case 3:
                color=0xffdf4620;
                break;
        }

        DrawableCompat.setTint(buttonDrawable, color);
        iv_prioridad.setBackground(buttonDrawable);
    }

    /**
     * Carga el icono de una tarea dependiendo de su tipo
     * @param holder
     * @param tipo -1 error, 0 tarea, 1 otro
     */
    public void setIvTipo(RecyclerView.ViewHolder holder, int tipo){
        ImageView iv_prioridad = ((AdaptadorTareasViewHolder) holder).iv_tipoTarea;
        switch (tipo){
            case -1:
                ((AdaptadorTareasViewHolder) holder).iv_tipoTarea.setImageResource(R.drawable.bug_icon);
                break;
            case 0:
                ((AdaptadorTareasViewHolder) holder).iv_tipoTarea.setImageResource(R.drawable.task_icon);
                break;
            case 1:
                ((AdaptadorTareasViewHolder) holder).iv_tipoTarea.setImageResource(R.drawable.others_icon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        try {
            return tareas.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (tareas.get(position).isShowMenu()) {
                return MOSTRAR_MENU;
            } else {
                return OCULTAR_MENU;
            }
        }catch (Exception e){

        }
        return OCULTAR_MENU;
    }

    /**
     * Metodo de prueba
     */
    public class AdaptadorTareasViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_nombreTarea, tv_encargadoTarea, tv_fechaCreacion;
        private ProporcionalImageView iv_fondoTarea;
        private ImageView iv_tipoTarea;


        public AdaptadorTareasViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreTarea = itemView.findViewById(R.id.tv_nombreTarea);
            tv_encargadoTarea = itemView.findViewById(R.id.tv_encargadoTarea);
            tv_fechaCreacion = itemView.findViewById(R.id.tv_fechaCreacion);
            iv_tipoTarea = itemView.findViewById(R.id.iv_tipoTarea);
            iv_fondoTarea = itemView.findViewById(R.id.iv_fondoTarea);

        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        private ImageButton btn_verTarea, btn_editarTarea, btn_atrasTarea, btn_borrarTarea,btn_bajarEstado,btn_subirEstado;
        private TextView tv_nombreTarea;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            btn_verTarea = itemView.findViewById(R.id.btn_verTarea);
            btn_editarTarea = itemView.findViewById(R.id.btn_editarTarea);
            btn_atrasTarea = itemView.findViewById(R.id.btn_atrasTarea);
            btn_borrarTarea = itemView.findViewById(R.id.btn_borrarTarea);
           // tv_nombreTarea = itemView.findViewById(R.id.tv_nombreTarea);
            btn_subirEstado = itemView.findViewById(R.id.btn_subirEstado);
            btn_bajarEstado = itemView.findViewById(R.id.btn_bajarEstado);
        }
    }

    public void mostrarMenu(int position) {
        for (int i = 0; i < tareas.size(); i++) {
            tareas.get(i).setShowMenu(false);
        }
        tareas.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuMostrado() {
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).isShowMenu()) {
                return true;
            }
        }
        return false;
    }

    public void cerrarMenu() {
        for (int i = 0; i < tareas.size(); i++) {
            tareas.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    public void editarSprint(Sprint sprint) {
        Intent intent= new Intent(contexto, EditarSprintActivity.class);
        intent.putExtra("sprint",sprint);
        intent.putExtra("proyecto",proyecto);
        contexto.startActivity(intent);
    }

    /**
     * Aumenta el estado de una tarea de "Por hacer" a "Hecho" y de "Hecho" a "En proceso"
     * @param tarea : Tarea , la tarea en la que se editará el estado
     */
    public void subirEstado(Tarea tarea){
        int estadoAnterior= tarea.getEstadoTarea();
        //Si no está finalizada puede cambiar hacia arriba
        if(estadoAnterior<2){
            Proyecto proyecto= this.proyecto;
            Sprint sprint=this.sprint;
            String eid= proyecto.getIdEmpresa();
            String pid=proyecto.getIdProyecto();

            //Se saca el sprint del proyecto y la tarea del sprint
            proyecto.eliminarSprint(sprint);
            sprint.eliminarTarea(tarea);
            //Se edita el estado y se devuelve al sprint y al proyecto
            tarea.setEstadoTarea(estadoAnterior+1);
            sprint.nuevaTarea(tarea);
            proyecto.nuevoSprint(sprint);
            proyecto.ordenarSprints();
            //Se devuelve el proyecto a la base de datos
            DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference().child(eid);
            dbReference.child("Proyectos").child(pid).setValue(proyecto);
        }
    }

    public void verSprint(Sprint sprint) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("sprint", sprint);
        //NavHostFragment.findNavController(fragment).navigate(R.id.action_proyectos_to_inicio, bundle);


        /*Intent intent= new Intent(contexto, NuevaTareaActivity.class);
        intent.putExtra("sprint",sprint);
        intent.putExtra("proyecto",proyecto);
        contexto.startActivity(intent);*/
    }

    private void borrarConfirmacion(Sprint sprint,String eid,String pid) {
        //Inicialización
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(contexto);
        alertDialogBu.setTitle("Borrar");
        alertDialogBu.setMessage("¿Seguro que quiere eliminar a " + sprint.getNombre() + "? Esta acción no se puede deshacer");

        //Opción positiva
        alertDialogBu.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                borrarCliente(sprint,eid,pid);
            }
        });
        //Opción negativa
        alertDialogBu.setNegativeButton("Cancelar·", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(contexto, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        //Creación del dialog
        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }

    public void borrarCliente(Sprint sprint,String eid,String pid) {
        proyecto.eliminarSprint(sprint);

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(eid).child("Proyectos").child(pid);
        dbReference.setValue(proyecto);

        Toast.makeText(contexto, sprint.getNombre() +" borrado", Toast.LENGTH_SHORT).show();
    }
}
