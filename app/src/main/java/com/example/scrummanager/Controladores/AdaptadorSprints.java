package com.example.scrummanager.Controladores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.R;
import com.example.scrummanager.Vistas.EditarSprintActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class AdaptadorSprints extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Sprint> sprints;
    private Proyecto proyecto;
    private Context contexto;
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;

    public AdaptadorSprints(ArrayList<Sprint> sprints, Proyecto proyecto, Context contexto) {
        this.sprints = sprints;
        this.contexto = contexto;
        this.proyecto=proyecto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == MOSTRAR_MENU) {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_cliente, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorSprints.MenuViewHolder(v);
        } else {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sprint, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorSprints.AdaptadorSprintsViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Sprint sprint = sprints.get(position);

        String nombre = sprint.getNombre();
        String objetivoSprint = sprint.getObjetivoSprint();
        ArrayList<Date> fechas = sprint.getFechasSprint();
        int color= Color.parseColor(sprint.getColor());

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String inicioSprint = formatter.format(fechas.get(0));
        String finSprint = formatter.format(fechas.get(1));

        //Si se está mostrando el Sprint
        if (holder instanceof AdaptadorSprints.AdaptadorSprintsViewHolder) {

            //Se incluye el sprint en el layout
            ((AdaptadorSprintsViewHolder) holder).tv_sprintNombre.setText(nombre);
            ((AdaptadorSprintsViewHolder) holder).tv_objetivoSprint.setText(objetivoSprint);
            ((AdaptadorSprintsViewHolder) holder).tv_inicioSprint.setText(inicioSprint);
            ((AdaptadorSprintsViewHolder) holder).tv_finSprint.setText(finSprint);
            ((AdaptadorSprintsViewHolder) holder).cardViewSprint.setCardBackgroundColor(color);

            //Un click simple muestra el sprint
            ((AdaptadorSprints.AdaptadorSprintsViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verSprint(sprint);
                }
            });

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorSprints.AdaptadorSprintsViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mostrarMenu(position);
                    return true;
                }
            });

        }
        //Si se está mostrando el menú
        if (holder instanceof AdaptadorSprints.MenuViewHolder) {
            //Se asignan onClickListeners para las opciones del menú
            ((AdaptadorSprints.MenuViewHolder) holder).btn_atrasCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((AdaptadorSprints.MenuViewHolder) holder).btn_verCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verSprint(sprint);
                }
            });
            ((AdaptadorSprints.MenuViewHolder) holder).btn_editarCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarSprint(sprint);
                }
            });
            ((AdaptadorSprints.MenuViewHolder) holder).btn_borrarCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { borrarConfirmacion(sprint,proyecto.getIdEmpresa(),proyecto.getIdProyecto());}
            });

            //Si se mantiene pulsado se cierra el menú de opciones
            ((AdaptadorSprints.MenuViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
        try {
            return sprints.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (sprints.get(position).isShowMenu()) {
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
    public class AdaptadorSprintsViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_sprintNombre, tv_inicioSprint, tv_objetivoSprint, tv_finSprint;
        private CardView cardViewSprint;

        public AdaptadorSprintsViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            tv_sprintNombre = itemView.findViewById(R.id.tv_sprintId);
            tv_inicioSprint = itemView.findViewById(R.id.tv_inicioSprint);
            tv_objetivoSprint = itemView.findViewById(R.id.tv_objetivoSprint);
            tv_finSprint = itemView.findViewById(R.id.tv_finSprint);
            cardViewSprint = itemView.findViewById(R.id.cardViewSprint);

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
        for (int i = 0; i < sprints.size(); i++) {
            sprints.get(i).setShowMenu(false);
        }
        sprints.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuMostrado() {
        for (int i = 0; i < sprints.size(); i++) {
            if (sprints.get(i).isShowMenu()) {
                return true;
            }
        }
        return false;
    }

    public void cerrarMenu() {
        for (int i = 0; i < sprints.size(); i++) {
            sprints.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    public void editarSprint(Sprint sprint) {
        Intent intent= new Intent(contexto, EditarSprintActivity.class);
        intent.putExtra("sprint",sprint);
        intent.putExtra("proyecto",proyecto);
        contexto.startActivity(intent);
    }

    public void verSprint(Sprint sprint) {
        /*Intent intent= new Intent(contexto, VerClienteActivity.class);
        intent.putExtra("cliente",cliente);
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
