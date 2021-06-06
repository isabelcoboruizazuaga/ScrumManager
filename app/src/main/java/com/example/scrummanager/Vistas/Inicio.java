package com.example.scrummanager.Vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Controladores.AdaptadorEmpleados;
import com.example.scrummanager.Controladores.AdaptadorTareas;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.Modelos.Tarea;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {
    private RecyclerView recPorHacer,recEnProceso,recHechas;
    private ArrayList<Tarea> tareas;
    String pid="377529fa-6c50-478f-bee8-da2fcc89b070", sid="b22bab63-b431-454a-8abe-895655f051c7",eid="ad96328d-ce92-4da7-88cf-b7ff81d0e6d3";
    ArrayList<Tarea> tareasPorHacer= new ArrayList<>();
    ArrayList<Tarea> tareasEnProceso= new ArrayList<>();
    ArrayList<Tarea> tareasHechas= new ArrayList<>();

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
        if(this.getArguments().getSerializable("sprint")!=null){
            Sprint sprint = (Sprint) getArguments().getSerializable("sprint");
            System.out.println(sprint.getNombre().toString());
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


        ArrayList<Tarea> tareas= new ArrayList<>();
        Tarea tarea= new Tarea("1","Depuración", "Dcp",-1,3,"uid",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Falta layout", "Dcp",0,0,"uid",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Nombre3", "Dcp",1,2,"Fernando García Pérez",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Nombre4", "Dcp",0,3,"uid",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Nombre5", "Dcp",1,1,"uid",new Date());
        tareas.add(tarea);


        rellenarRecyclers(view);

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
        AdaptadorTareas adaptadorTareas= new AdaptadorTareas(tareasPorHacer,new Sprint(),new Proyecto(),getContext());
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
        AdaptadorTareas adaptadorTareas= new AdaptadorTareas(tareasEnProceso,new Sprint(),new Proyecto(),getContext());
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
        AdaptadorTareas adaptadorTareas= new AdaptadorTareas(tareasHechas,new Sprint(),new Proyecto(),getContext());
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
                        //Se evalua el estado de cada tarea para añadirlo a lista correspondiente
                        ArrayList<Tarea> tareas= sprint.getListaTareasSprint();
                        for(int i=0;i<tareas.size();i++){
                            Tarea tarea= tareas.get(i);
                            int estado= tarea.getEstadoTarea();
                            switch (estado){
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
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        });
    }
}