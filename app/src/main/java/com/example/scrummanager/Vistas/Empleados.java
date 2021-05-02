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
import android.widget.Toast;

import com.example.scrummanager.Controladores.AdaptadorDepartamentos;
import com.example.scrummanager.Controladores.AdaptadorEmpleados;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;

import java.util.ArrayList;

public class Empleados extends Fragment {
    private RecyclerView recView;
    private ArrayList<Empleado> empleados;
    private Empleado empleado;

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

        //Inicialización del recycler view
        recView= view.findViewById(R.id.rv_empleados);
        //Creación del layout y asignación al recycler
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recView.setLayoutManager(layoutManager);

        empleados =new ArrayList<Empleado>();
        empleado = new Empleado("1","Empleado1", "Apellido","1");
        empleados.add(empleado);
        empleado = new Empleado("1","Empleado2", "Apellido","1");
        empleados.add(empleado);
        empleado = new Empleado("1","Empleado3", "Apellido","1");
        empleados.add(empleado);
        empleado = new Empleado("1","Empleado4", "Apellido","1");
        empleados.add(empleado);
        empleado = new Empleado("1","Empleado5", "Apellido","1");
        empleados.add(empleado);
        empleado = new Empleado("1","Empleado6", "Apellido","1");
        empleados.add(empleado);
        empleado = new Empleado("1","Empleado7", "Apellido","1");
        empleados.add(empleado);

        AdaptadorEmpleados adaptadorEmpleados= new AdaptadorEmpleados(empleados,getContext());
        recView.setAdapter(adaptadorEmpleados);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Se ocultan las opciones del menú que no pertenecen a este fragment
        MenuItem item=menu.findItem(R.id.menuCosaInutil);
        if(item!=null)
            item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAnEmp:
                Toast.makeText(getContext(), "Añadir", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), NuevoEmpleadoActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}