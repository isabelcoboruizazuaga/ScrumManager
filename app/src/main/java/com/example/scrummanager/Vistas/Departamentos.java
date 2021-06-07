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
import android.widget.LinearLayout;

import com.example.scrummanager.Controladores.AdaptadorDepartamentos;
import com.example.scrummanager.Controladores.AdaptadorEmpleados;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Una subclase de {@link Fragment} simple.
 * Usa el metodo {@link Departamentos#newInstance} para
 * crear una instancia del fragmento.
 * Muestra los departamentos de la empresa
 */
public class Departamentos extends Fragment {
   private RecyclerView recView;
   private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;

    private ArrayList<Departamento> departamentos=new ArrayList<>();
    private Departamento departamento;
    private String eid;

    /**
     * Constructor vacío por defecto
     */
    public Departamentos() {
    }

    /**
     * Usa este metodo para crear una nueva instancia de
     * este fragmento
     * @return una nueva instancia del fragmento Departamentos
     */
    public static Departamentos newInstance() {
        Departamentos fragment = new Departamentos();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_departamentos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Inicialización database
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();

        //Obtención del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        eid= sp.getString("eid","-1");

        //Inicialización del recycler view
        recView= view.findViewById(R.id.rv_departamentos);

        //Creación del layout y asignación al recycler
        GridLayoutManager gridlayoutManager= new GridLayoutManager(getContext(),2);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(gridlayoutManager);

        rellenarRecyclerView();
    }

    /**
     * Carga los datos de los departamentos en su correspondiente RecyclerView
     */
    public void rellenarRecyclerView (){
        dbReference.child(eid).child("Departamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    departamentos.clear();
                    Iterable<DataSnapshot> datos = dataSnapshot.getChildren();
                    for(DataSnapshot snap: datos){
                        departamentos.add(snap.getValue(Departamento.class));
                    }
                    AdaptadorDepartamentos adaptadorDepartamentos= new AdaptadorDepartamentos(departamentos,getContext());
                    recView.setAdapter(adaptadorDepartamentos);
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