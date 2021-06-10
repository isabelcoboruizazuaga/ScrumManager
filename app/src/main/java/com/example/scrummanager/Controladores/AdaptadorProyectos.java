package com.example.scrummanager.Controladores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.R;
import com.example.scrummanager.Vistas.EditarClienteActivity;
import com.example.scrummanager.Vistas.EditarEmpleadoActivity;
import com.example.scrummanager.Vistas.EditarProyectoActivity;
import com.example.scrummanager.Vistas.NuevoSprintActivity;
import com.example.scrummanager.Vistas.VerClienteActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Adaptador para rellenar Recycler Views con una lista de proyectos
 */
public class AdaptadorProyectos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Proyecto> proyectos;
    private Context contexto;
    private Cliente clie= inicializarCliente();
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;
    private Fragment fragment;

    /**
     * Constructor del adaptador
     * @param proyectos
     * @param contexto
     * @param fragment
     */
    public AdaptadorProyectos(ArrayList<Proyecto> proyectos, Context contexto, Fragment fragment) {
        this.proyectos = proyectos;
        this.contexto = contexto;
        this.fragment= fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == MOSTRAR_MENU) {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_proyecto, parent, false);
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
        Proyecto proyecto = proyectos.get(position);
        String eid=proyecto.getIdEmpresa();
        String did=proyecto.getDid();

        String nombreProyecto = proyecto.getNombreProyecto();
        int color= Color.parseColor(proyecto.getColor());
        String especificaciones= proyecto.getEspecificacionesProyecto();
        String presupuesto= proyecto.getPresupuesto();
        findCliente(proyecto.getCliente(), eid);
        ArrayList <Date>fechas= proyecto.getFechasProyecto();

        String fechaInicio;
        String fechaFin;
        //Conversión de la fecha al formato correcto
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
             fechaInicio = formatter.format(fechas.get(0));
             fechaFin = formatter.format(fechas.get(1));
        }catch (Exception e){
             fechaInicio = "dd/MM/yyyy";
             fechaFin = "dd/MM/yyyy";
        }
        String finalFechaFin = fechaFin;
        String finalFechaInicio = fechaInicio;


        //Si se está mostrando el Proyecto
        if (holder instanceof AdaptadorProyectosViewHolder) {
            //Se incluye el proyecti en el layout
            ((AdaptadorProyectosViewHolder) holder).tv_nombreProyecto.setText(nombreProyecto);
            ((AdaptadorProyectosViewHolder) holder).layoutProyecto.setBackgroundTintList(ColorStateList.valueOf(color));

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorProyectosViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mostrarMenu(position);
                    return true;
                }
            });
            ((AdaptadorProyectosViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv_nombreCliente= fragment.getView().findViewById(R.id.tv_nombreCliente);
                    TextView tv_apellidoCliente= fragment.getView().findViewById(R.id.tv_apellidoCliente);
                    TextView tv_emailCliente= fragment.getView().findViewById(R.id.tv_emailCliente);
                    TextView tv_especificaciones= fragment.getView().findViewById(R.id.tv_especificaciones);
                    TextView tv_fechaInicioProyecto= fragment.getView().findViewById(R.id.tv_fechaInicioProyecto);
                    TextView tv_fechaFinProyecto= fragment.getView().findViewById(R.id.tv_fechaFinProyecto);
                    TextView tv_presupuesto= fragment.getView().findViewById(R.id.tv_presupuesto);


                    //Se rellena la información del proyecto
                    tv_nombreCliente.setText(clie.getNombreCliente());
                    tv_apellidoCliente.setText(clie.getApellidoCliente());
                    tv_emailCliente.setText(clie.getEmailCliente());
                    tv_especificaciones.setText(especificaciones);
                    tv_fechaInicioProyecto.setText(finalFechaInicio);
                    tv_fechaFinProyecto.setText(finalFechaFin);
                    tv_presupuesto.setText(presupuesto);


                    rellenarRecyclerViewEquipo(eid, did);
                    rellenarRecyclerViewSprint(proyecto);
                }
            });

        }
        //Si se está mostrando el menú
        if (holder instanceof MenuViewHolder) {
            //Se asignan onClickListeners para las opciones del menú
            ((MenuViewHolder) holder).btn_atrasProyecto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((MenuViewHolder) holder).btn_nuevoSprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sprintProyecto(proyecto);
                }
            });
            ((MenuViewHolder) holder).btn_editarProyecto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarProyecto(proyecto);
                }
            });
            ((MenuViewHolder) holder).btn_borrarProyecto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarConfirmacion(proyecto);
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
            return proyectos.size();
        }catch (NullPointerException e){
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (proyectos.get(position).isShowMenu()) {
            return MOSTRAR_MENU;
        } else {
            return OCULTAR_MENU;
        }
    }

    /**
     * Método que inicializa el recycler view de la vista de un equipo dentro de un proyecto
     * @param eid id de la empresa a la que pertenece el proyecto (y por tanto el equipo)
     * @param did id del departamento o equipo encargado del proyecto
     */
    private void rellenarRecyclerViewEquipo(String eid, String did){
        ArrayList<Empleado> miembrosDpt= new ArrayList<>();

        //Creación del Recycler View para el equipo
        RecyclerView recView= fragment.getView().findViewById(R.id.rv_equipoProyecto);
        LinearLayoutManager layoutManager= new LinearLayoutManager(contexto,LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(layoutManager);

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(eid);
        dbReference.child("Departamentos").child(did).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                miembrosDpt.clear();
                //Se extraen los uid de los empleados
                Departamento dpt= snapshot.getValue(Departamento.class);

                //Se recorre la lista de uid de miembros y se añade a la de objetos
                try {
                    ArrayList<String> empleados= dpt.getMiembrosDepartamento();
                    for (int i = 0; i < empleados.size(); i++) {
                        String uid = empleados.get(i);
                        dbReference.child("Empleados").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Empleado empld = snapshot.getValue(Empleado.class);
                                miembrosDpt.add(empld);

                                //Asignación del equipo al recyclerView
                                AdaptadorEmpleadosDpt adaptadorEmpleadosDpt= new AdaptadorEmpleadosDpt(miembrosDpt,contexto);
                                recView.setAdapter(adaptadorEmpleadosDpt);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } catch (NullPointerException e){
                    //Si esta vacío se limpia
                    AdaptadorEmpleadosDpt adaptadorEmpleadosDpt= new AdaptadorEmpleadosDpt(miembrosDpt,contexto);
                    recView.setAdapter(adaptadorEmpleadosDpt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    /**
     * Método que inicializa el recycler view de la vista de los sprints dentro de un proyecto
     * @param proyecto proyecto seleccionado al que pertenencen los sprints
     */
    private void rellenarRecyclerViewSprint(Proyecto proyecto){
        ArrayList sprints =new ArrayList<Sprint>();
        sprints= proyecto.getListaSprints();

        //Creación del Recycler View para el equipo
        RecyclerView recView3= fragment.getView().findViewById(R.id.rv_sprints);
        LinearLayoutManager layoutManager3= new LinearLayoutManager(contexto,LinearLayoutManager.HORIZONTAL,false);
        recView3.setLayoutManager(layoutManager3);

        //Asignación del equipo al recyclerView
        AdaptadorSprints adaptadorSprints= new AdaptadorSprints(sprints,proyecto,contexto,fragment);
        recView3.setAdapter(adaptadorSprints);
    }

    /**
     * ViewHolder del adaptador de proyectos, recoge el layout del item
     * Obtiene los items del layout indicado en el método onCreateViewHolder
     */
    public class AdaptadorProyectosViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_nombreProyecto;
        private LinearLayout layoutProyecto;

        public AdaptadorProyectosViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreProyecto = itemView.findViewById(R.id.tv_nombreProyecto);
            layoutProyecto= itemView.findViewById(R.id.layoutProyecto);

        }
    }

    /**
     * MenuViewHolder del adaptador de proyectos, recoge el layout del menú del item
     * Obtiene los items del layout indicado en el método onCreateViewHolder
     */
    public class MenuViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private ImageButton btn_nuevoSprint, btn_atrasProyecto, btn_borrarProyecto, btn_editarProyecto;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            btn_nuevoSprint = itemView.findViewById(R.id.btn_nuevoSprintProyecto);
            btn_atrasProyecto = itemView.findViewById(R.id.btn_atrasProyecto);
            btn_borrarProyecto = itemView.findViewById(R.id.btn_borrarProyecto);
            btn_editarProyecto = itemView.findViewById(R.id.btn_editarProyecto);
        }
    }

    /**
     * Cambia a true el atributo de mostrar menú de un item seleccionado
     * @param position
     */
    public void mostrarMenu(int position) {
        for (int i = 0; i < proyectos.size(); i++) {
            proyectos.get(i).setShowMenu(false);
        }
        proyectos.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }

    /**
     * Cambia a false el atributo de mostrar menú de todos los item
     */
    public void cerrarMenu() {
        for (int i = 0; i < proyectos.size(); i++) {
            proyectos.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    /**
     * Ejecuta la actividad de  Editar proyecto pasándole el proyecto seleccionado
     * @see EditarProyectoActivity
     * @param proyecto
     */
    public void editarProyecto(Proyecto proyecto) {
        Intent intent= new Intent(contexto, EditarProyectoActivity.class);
        intent.putExtra("proyecto",proyecto);
        contexto.startActivity(intent);
    }

    /**
     * Ejecuta la actividad de Crear Sprint pasándole el proyecto seleccionado
     * @see NuevoSprintActivity
     * @param proyecto
     */
    public void sprintProyecto(Proyecto proyecto) {
        Intent intent= new Intent(contexto, NuevoSprintActivity.class);
        intent.putExtra("proyecto",proyecto);
        contexto.startActivity(intent);
    }

    /**
     * Muestra un alertDialog para confirmar que se desea borrar el proyecto pasado como parámetro
     * Si se acepta llama al método para borrar
     * @param proyecto
     */
    private void borrarConfirmacion(Proyecto proyecto) {
        //Inicialización
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(contexto);
        alertDialogBu.setTitle("Borrar");
        alertDialogBu.setMessage("¿Seguro que quiere eliminar " + proyecto.getNombreProyecto() +"? Esta acción no se puede deshacer");

        //Opción positiva
        alertDialogBu.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                borrarProyecto(proyecto);
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

    /**
     * Borra un proyecto pasado como parámetro de la base de datos
     * @param proyecto
     */
    public void borrarProyecto(Proyecto proyecto) {
        DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference().child(proyecto.getIdEmpresa()).child("Proyectos").child(proyecto.getIdProyecto());
        dbReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(contexto, "El proyecto " +proyecto.getNombreProyecto()+ " ha sido eliminado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Accede a la base de datos para sacar el cliente que posea el nif pasado como argumento
     * @param nifCliente    código identificativo del cliente
     * @param eid   código identificativo de la empresa a la que pertenece el cliente
     */
    private void findCliente(String nifCliente,String eid){
        if(nifCliente!=null && eid!=null) {
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(eid).child("Clientes").child(nifCliente);
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    clie= snapshot.getValue(Cliente.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    clie=inicializarCliente();
                }
            });
        }
        else{
            clie=inicializarCliente();
        }
    }

    /**
     * Devuelve un cliente con datos predeterminados
     * @return cliente
     */
    private Cliente inicializarCliente(){
        Cliente cliente= new Cliente();

        cliente=new Cliente();
        cliente.setEmailCliente("Email");
        cliente.setNombreCliente("Nombre");
        cliente.setApellidoCliente("Apellido");

        return cliente;
    }
}