package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Controladores.AdaptadorProyectos;
import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Proyectos extends Fragment {
    private RecyclerView recView;
    private ArrayList<Proyecto> proyectos;
    private Proyecto proyecto;
    private Fragment fragmento;
    private ValueEventListener eventListenerProyectos;

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
        proyectos =new ArrayList<Proyecto>();
        fragmento= this;
        //Recuperación del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String eid= sp.getString("eid","-1");

        //Inicializaciónd el RecyclerView
        recView= view.findViewById(R.id.rv_proyectos);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(layoutManager);

        //Obtención de los datos
        setEventListenerClientes();
        DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference().child(eid).child("Proyectos");
        dbReference.addValueEventListener(eventListenerProyectos);
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

    private void setEventListenerClientes(){
        eventListenerProyectos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proyectos.clear();
                for (DataSnapshot x : snapshot.getChildren() ){
                    //Se añaden los proyectos a la lista
                    proyecto = x.getValue(Proyecto.class);
                    proyectos.add(proyecto);

                    //Se rellena el recycler view
                    AdaptadorProyectos adaptadorProyectos= new AdaptadorProyectos(proyectos,getContext(),fragmento);
                    recView.setAdapter(adaptadorProyectos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
}