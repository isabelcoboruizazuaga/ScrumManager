package com.example.scrummanager.Vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.scrummanager.Controladores.AdaptadorDepartamentos;
import com.example.scrummanager.Controladores.AdaptadorTareas;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.Modelos.Tarea;
import com.example.scrummanager.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {
    private RecyclerView recView1,recView2,recView3;
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
        Tarea tarea= new Tarea("1","Depuración", "Dcp",0,1,"uid",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Falta layout", "Dcp",0,1,"uid",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Nombre3", "Dcp",0,1,"Fernando García Pérez",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Nombre4", "Dcp",0,1,"uid",new Date());
        tareas.add(tarea);
        tarea= new Tarea("1","Nombre5", "Dcp",0,1,"uid",new Date());
        tareas.add(tarea);


        LinearLayoutManager layoutManager1= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager layoutManager2= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager layoutManager3= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        AdaptadorTareas adaptadorDepartamentos= new AdaptadorTareas(tareas,new Sprint(),new Proyecto(),getContext());

        //Inicialización del recycler view
        recView1= view.findViewById(R.id.rv_tareasPorHacer);
        recView1.setLayoutManager(layoutManager1);
        //recView1.setAdapter(adaptadorDepartamentos);


        recView2= view.findViewById(R.id.rv_tareasEnProceso);
        recView2.setLayoutManager(layoutManager2);
        recView2.setAdapter(adaptadorDepartamentos);


        recView3= view.findViewById(R.id.rv_tareasHechas);
        recView3.setLayoutManager(layoutManager3);
        recView3.setAdapter(adaptadorDepartamentos);
    }
}