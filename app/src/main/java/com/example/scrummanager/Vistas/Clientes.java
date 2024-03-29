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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Controladores.AdaptadorClientes;
import com.example.scrummanager.Controladores.AdaptadorEmpleados;
import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;

import java.util.ArrayList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Controladores.AdaptadorClientes;
import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Clientes extends Fragment {
     private RecyclerView recView;
     private ArrayList<Cliente> clientes=new ArrayList<Cliente>();
     private Cliente cliente;
     private String eid;

    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;

    public Clientes() {
        // Required empty public constructor
    }

    public static Clientes newInstance() {
        Clientes fragment = new Clientes();
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
        return inflater.inflate(R.layout.fragment_clientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicialización database
        database=FirebaseDatabase.getInstance();
        dbReference=database.getReference();

        //Obtención del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        eid= sp.getString("eid","-1");

        //Inicialización del recycler view
        recView= view.findViewById(R.id.rv_clientes);

        //Creación del layout y asignación al recycler
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recView.setLayoutManager(layoutManager);

        rellenarRecyclerView();


    }
    //Database listener
    public void rellenarRecyclerView (){
        dbReference.child(eid).child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    clientes.clear();
                    Iterable<DataSnapshot> datos = dataSnapshot.getChildren();
                    for(DataSnapshot snap: datos){
                        clientes.add(snap.getValue(Cliente.class));
                    }
                    AdaptadorClientes adaptadorClientes= new AdaptadorClientes(clientes,getContext());
                    recView.setAdapter(adaptadorClientes);
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
        switch (item.getItemId()){
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Se ocultan las opciones del menú que no pertenecen a este fragment
        MenuItem item=menu.findItem(R.id.menuAnTarea);
        if(item!=null)
            item.setVisible(false);
        item=menu.findItem(R.id.menuAnSprint);
        if(item!=null)
            item.setVisible(false);
    }
}