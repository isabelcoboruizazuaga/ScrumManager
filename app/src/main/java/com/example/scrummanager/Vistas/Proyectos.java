package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Controladores.AdaptadorDepartamentos;
import com.example.scrummanager.Controladores.AdaptadorProyectos;
import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.R;

import java.util.ArrayList;

public class Proyectos extends Fragment {
    private RecyclerView recView;
    private ArrayList<Proyecto> proyectos;
    private Proyecto proyecto;

    public Proyectos() {
        // Required empty public constructor
    }

    public static Proyectos newInstance() {
        Proyectos fragment = new Proyectos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proyectos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recView= view.findViewById(R.id.rv_proyectos);

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(layoutManager);

        proyectos =new ArrayList<Proyecto>();
        proyecto = new Proyecto("id01","Proyecto especial de Isa", "a","a");
        proyectos.add(proyecto);
        proyecto = new Proyecto("id01","Proyecto 3", "a","a");
        proyectos.add(proyecto);
        proyecto = new Proyecto("id01","Proyecto 4", "a","a");

        proyectos.add(proyecto);
        proyecto = new Proyecto("id01","Proyectosd ds ad asd", "a","a");

        proyectos.add(proyecto);
        proyecto = new Proyecto("id01","Proyecto 6", "a","a");

        proyectos.add(proyecto);
        proyecto = new Proyecto("id01","Proyecto 8", "a","a");

        proyectos.add(proyecto);
        proyecto = new Proyecto("id01","Proyecto especi9", "a","a");

        proyectos.add(proyecto);

        AdaptadorProyectos adaptadorProyectos= new AdaptadorProyectos(proyectos,getContext(), this);
        recView.setAdapter(adaptadorProyectos);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Se ocultan las opciones del menú que no pertenecen a este fragment
        MenuItem item=menu.findItem(R.id.menuAnCli);
        if(item!=null)
            item.setVisible(false);
        item=menu.findItem(R.id.menuAnDept);
        if(item!=null)
            item.setVisible(false);
        item=menu.findItem(R.id.menuAnEmp);
        if(item!=null)
            item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAnProy:
                startActivity(new Intent(getContext(), NuevoProyectoActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}