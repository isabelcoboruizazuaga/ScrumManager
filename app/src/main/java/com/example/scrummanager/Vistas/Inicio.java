package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Controladores.AdaptadorTareas;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.Modelos.Tarea;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {
    private RecyclerView recPorHacer,recEnProceso,recHechas;

    String pid,sid,eid;
    ArrayList<Tarea> tareasPorHacer= new ArrayList<>();
    ArrayList<Tarea> tareasEnProceso= new ArrayList<>();
    ArrayList<Tarea> tareasHechas= new ArrayList<>();
    Sprint sprint;
    Proyecto proyecto;

    public Inicio() {
        // Required empty public constructor
    }
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(this.getArguments().getSerializable("sprint")!=null){
            sprint = (Sprint) getArguments().getSerializable("sprint");
            proyecto = (Proyecto) getArguments().getSerializable("proyecto");

            pid=proyecto.getIdProyecto();
            eid=proyecto.getIdEmpresa();
            sid=sprint.getIdSprint();
        }else{
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                Gson gson = new Gson();
                String json = prefs.getString("sprint", "");
                sprint = gson.fromJson(json, Sprint.class);
                String json1 = prefs.getString("proyecto", "");
                proyecto = gson.fromJson(json1, Proyecto.class);

                pid = proyecto.getIdProyecto();
                eid = proyecto.getIdEmpresa();
                sid = sprint.getIdSprint();
            }catch (Exception e){

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            rellenarRecyclers(view);
        }catch (Exception e){

        }

    }

    /**
     * Control del RecyclerView de tareas por hacer
     * @param view : View
     */
    private void administrarTareasPorHacer(View view){
        //Creación layout
        LinearLayoutManager layoutManager1= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recPorHacer = view.findViewById(R.id.rv_tareasPorHacer);
        recPorHacer.setLayoutManager(layoutManager1);

        //Asignación del adapter
        AdaptadorTareas adaptadorTareas= new AdaptadorTareas(tareasPorHacer,sprint,proyecto,getContext());
        recPorHacer.setAdapter(adaptadorTareas);
    }

    /**
     * Control del RecyclerView de tareas en proceso
     * @param view : View
     */
    private void administrarTareasEnProceso(View view){
        //Creación layout
        LinearLayoutManager layoutManager1= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recEnProceso = view.findViewById(R.id.rv_tareasEnProceso);
        recEnProceso.setLayoutManager(layoutManager1);

        //Asignación del adapter
        AdaptadorTareas adaptadorTareas= new AdaptadorTareas(tareasEnProceso,sprint,proyecto,getContext());
        recEnProceso.setAdapter(adaptadorTareas);
    }

    /**
     * Control del RecyclerView de tareas hechas
     * @param view : View
     */
    private void administrarTareasHechas(View view){
        //Creación layout
        LinearLayoutManager layoutManager1= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recHechas = view.findViewById(R.id.rv_tareasHechas);
        recHechas.setLayoutManager(layoutManager1);

        //Asignación del adapter
        AdaptadorTareas adaptadorTareas= new AdaptadorTareas(tareasHechas,sprint,proyecto,getContext());
        recHechas.setAdapter(adaptadorTareas);
    }

    /**
     * Extrae los datos de la base de datos y llama a los controles de los recyclers
     * @param view
     */
    private void rellenarRecyclers(View view){
        //Se accede a la base de datos para obtener los datos
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(eid).child("Proyectos").child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tareasPorHacer.clear();
                    tareasEnProceso.clear();
                    tareasHechas.clear();

                    Proyecto proyecto= dataSnapshot.getValue(Proyecto.class);
                    //Se extrae el sprint en el que nos encontramos
                    Sprint sprint= proyecto.extraerSprint(sid);
                    if(sprint!=null){
                        try {
                            //Se evalua el estado de cada tarea para añadirlo a lista correspondiente
                            ArrayList<Tarea> tareas = sprint.getListaTareasSprint();
                            for (int i = 0; i < tareas.size(); i++) {
                                Tarea tarea = tareas.get(i);
                                int estado = tarea.getEstadoTarea();
                                switch (estado) {
                                    case 0:
                                        tareasPorHacer.add(tarea);
                                        break;
                                    case 1:
                                        tareasEnProceso.add(tarea);
                                        break;
                                    case 2:
                                        tareasHechas.add(tarea);
                                        break;
                                }
                                administrarTareasPorHacer(view);
                                administrarTareasEnProceso(view);
                                administrarTareasHechas(view);
                            }
                        }catch (NullPointerException e){

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAnCli:
                startActivity(new Intent(getContext(), NuevoClienteActivity.class));
                break;
            case R.id.menuAnDept:
                startActivity(new Intent(getContext(), NuevoDepartamentoActivity.class));
                break;
            case R.id.menuAnEmp:
                startActivity(new Intent(getContext(), NuevoEmpleadoActivity.class));
                break;
            case R.id.menuAnProy:
                startActivity(new Intent(getContext(), NuevoProyectoActivity.class));
                break;
            case R.id.menuAnTarea:
                if(proyecto!=null) {
                    Intent intent = new Intent(getContext(), NuevaTareaActivity.class);
                    intent.putExtra("sprint", sprint);
                    intent.putExtra("proyecto", proyecto);
                    getContext().startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Debe haber un sprint seleccionado",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menuAnSprint:
                if(proyecto!=null) {
                    Intent intent1 = new Intent(getContext(), NuevoSprintActivity.class);
                    intent1.putExtra("proyecto", proyecto);
                    getContext().startActivity(intent1);
                    break;
                }else {
                    Toast.makeText(getContext(),"Debe haber un sprint seleccionado",Toast.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}