package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.scrummanager.Controladores.AdaptadorDepartamentos;
import com.example.scrummanager.Controladores.AdaptadorEmpleados;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Empleados extends Fragment {private FirebaseAuth mAuthAdmin, mAuthWorker;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;
    private RecyclerView recView;

    private ArrayList<Empleado> empleados =new ArrayList<Empleado>();
    private Empleado empleado;
    private String eid;

    public Empleados() {
        // Required empty public constructor
    }


    public static Empleados newInstance(String param1, String param2) {
        Empleados fragment = new Empleados();
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
        return inflater.inflate(R.layout.fragment_empleados, container, false);
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
        recView= view.findViewById(R.id.rv_empleados);
        //Creación del layout y asignación al recycler
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recView.setLayoutManager(layoutManager);

        rellenarRecyclerView();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Se ocultan las opciones del menú que no pertenecen a este fragment
        MenuItem item=menu.findItem(R.id.menuCosaInutil);
        if(item!=null)
            item.setVisible(false);
    }
    //Database listener
    public void rellenarRecyclerView (){
        dbReference.child(eid).child("Empleados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    empleados.clear();
                    Iterable<DataSnapshot> datos = dataSnapshot.getChildren();
                    for(DataSnapshot snap: datos){
                        empleados.add(snap.getValue(Empleado.class));
                    }
                    AdaptadorEmpleados adaptadorEmpleados= new AdaptadorEmpleados(empleados,getContext());
                    recView.setAdapter(adaptadorEmpleados);
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
            case R.id.menuAnEmp:
                startActivity(new Intent(getContext(), NuevoEmpleadoActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}