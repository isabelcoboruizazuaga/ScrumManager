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

/**
 * Una subclase de {@link Fragment} simple.
 * Usa el metodo {@link Empleados#newInstance} para
 * crear una instancia del fragmento.
 * Muestra los empleados de la empresa
 */
public class Empleados extends Fragment {private FirebaseAuth mAuthAdmin, mAuthWorker;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;
    private RecyclerView recView;

    private ArrayList<Empleado> empleados =new ArrayList<Empleado>();
    private Empleado empleado;
    private String eid;

    /**
     * Constructor vacío por defecto
     */
    public Empleados() {
    }

    /**
     * Usa este metodo para crear una nueva instancia de
     * este fragmento
     * @return una nueva instancia del fragmento Empleados
     */
    public static Empleados newInstance() {
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


    /**
     * Extrae los datos de los empleados de la base de datos y los coloca en el Recycler View
     */
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
                    AdaptadorEmpleados adaptadorEmpleados= new AdaptadorEmpleados(empleados,getContext(),getParentFragment());
                    recView.setAdapter(adaptadorEmpleados);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        });
    }

    /**
     * Gestiona las acciones del menú del NavigationDrawer
     * @param item : MenuItem elemento del menu seleccionado
     * @return true si se ha seleccionado un elemente
     */
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

    /**
     * Gestiona los elementos que serán visibles en el menú
     * @param menu: Menu
     */
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