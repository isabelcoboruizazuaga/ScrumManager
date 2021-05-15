package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.UUID;

public class NuevoDepartamentoActivity extends AppCompatActivity {
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;

    private ArrayList<Empleado> empleados;
    private ArrayList<String> empleadosNombres;
    private String uidJefeDpt;
    private String eid;

    private Spinner spinnerEmpleado;
    private Button btn_crear;
    private EditText et_nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_departamento);

        //Inicialización variables del layout
        spinnerEmpleado= findViewById(R.id.spinner_empleados);
        btn_crear= findViewById(R.id.btn_addDepartamento);
        et_nombre=findViewById(R.id.et_nombreDepartamento);

        //Obtención del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        eid= sp.getString("eid","-1");

        //Inicialización de firebaseDatabase
        database= FirebaseDatabase.getInstance();
        dbReference = database.getReference().child(eid);

        //Extracción de los datos de empleados de la base de datos
        empleados= new ArrayList<>();
        empleadosNombres= new ArrayList<>();
        setEventListener();
        dbReference.child("Empleados").addValueEventListener(eventListener);

       Empleado empleado= new Empleado("-1","Sin","Jefe",eid);
       empleados.add(empleado);
       empleadosNombres.add(empleado.getNombreEmpleado() + empleado.getApellidoEmpleado());

        //Control del spinner de selección de jefe de departamento
        rellenarSpinnerEmpleados(empleado);
        spinnerEmpleado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //La posición del nombre de empleado equivale a la de la lista de objetos empleado
                int posNombre =  spinnerEmpleado.getSelectedItemPosition();
                //Se asigna la id del empleado de esa posición para poder introducirlo como jefe de departamento
                uidJefeDpt = empleados.get(posNombre).getUid();
                Log.e("uid: ",  uidJefeDpt);
            }
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });

        btn_crear.setOnClickListener(this::crearDepartamento);
    }

    private void crearDepartamento(View v){
        String nombre= et_nombre.getText().toString();
        String did= UUID.randomUUID().toString();
        Departamento departamento= new Departamento(did,nombre,eid);

        //Si el departamento se crea sin jefe se añade sin más a la base de datos
        if(uidJefeDpt.equals("-1")){
            dbReference.child("Departamentos").child(did).setValue(departamento);
            Toast.makeText(getApplicationContext(), "Departamento creado", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            //Se obtiene el empleado a añadir
            dbReference.child("Empleados").child(uidJefeDpt).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Empleado empleado = snapshot.getValue(Empleado.class);
                        //Si el empleado pertenece ya a un departamento no puede añadirse como jefe del nuevo
                        if (empleado.getNivelJerarquia() != 2) {
                            //Se actualiza el departamento del empleado en la base de datos
                            empleado.setIdDepartamento(did);
                            empleado.setNivelJerarquia(2);
                            dbReference.child("Empleados").child(uidJefeDpt).setValue(empleado);

                            //Se introduce el empleado al departamento y se añade a la bd
                            departamento.setUidJefeDepartamento(uidJefeDpt);
                            dbReference.child("Departamentos").child(did).setValue(departamento);
                            Toast.makeText(getApplicationContext(), "Departamento creado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Este empleado ya es jefe de otro departamento", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error al recuperar los datos del empleado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    //Método para rellenar las opciones de departamento
    private void rellenarSpinnerEmpleados(Empleado empleado){
        ArrayAdapter<String> empleadoAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empleadosNombres);
        empleadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmpleado.setAdapter(empleadoAdapter);
    }
 private Empleado empleado;
    private void setEventListener(){
        eventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot xempleado : snapshot.getChildren() ){
                    //Se añaden los empleados a la lista
                    empleado = xempleado.getValue(Empleado.class);
                    empleados.add(empleado);
                    empleadosNombres.add(empleado.getNombreEmpleado() +" " +empleado.getApellidoEmpleado());
                    Log.e("onDataChange", "onDataChange:" + xempleado.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

}