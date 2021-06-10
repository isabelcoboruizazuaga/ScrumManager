package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scrummanager.Controladores.AdaptadorEmpleadosDpt;
import com.example.scrummanager.Modelos.Departamento;
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
import java.util.UUID;

/**
 * Crea una tarea nueva perteneciente al sprint desde el que se ha lanzado la actividad
 * Deben pasarse tanto el sprint como el proyecto al que pertenece el sprint como intents
 */
public class NuevaTareaActivity extends AppCompatActivity {
    private DatabaseReference dbReference;
    private ValueEventListener eventListenerEmpleados;

    private ArrayList<Empleado> empleados =new ArrayList<>();
    private ArrayList<String> tipos=new ArrayList<>(),prioridades=new ArrayList<>(), empleadosNombres =new ArrayList<>();
    private  String eid,pid,sid,uidEncargado;
    private int tipo; //-1 bug, 0 tarea, 1 otro
    private int prioridad; //0 baja, 1 normal, 2 alta, 3 terminal
    private Proyecto proyecto;
    private Sprint sprint;

    private Spinner spinner_empleadosTarea,spinner_tipoTarea,spinner_prioridadTarea ;
    private Button btn_aniadir;
    private EditText et_nombreTarea, et_descripcionTarea;
    private ImageView iv_imagenTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Crear tarea");

        //Se recogen los datos del proyecto y del sprint
        recogerProyecto();
        recogerSprint();

        //Inicialización del layout
        et_nombreTarea= findViewById(R.id.et_nombreTarea);
        et_descripcionTarea= findViewById(R.id.et_descripcionTarea);
        spinner_empleadosTarea= findViewById(R.id.spinner_empleadosTarea);
        spinner_tipoTarea=findViewById(R.id.spinner_tipoTarea);
        spinner_prioridadTarea=findViewById(R.id.spinner_prioridadTarea);
        btn_aniadir= findViewById(R.id.btn_addTarea);
        iv_imagenTarea= findViewById(R.id.iv_imagenTarea);

        //Inicialización de Firebase
        dbReference= FirebaseDatabase.getInstance().getReference().child(eid);

        //Extracción de los datos del spinner empleados
        setEventListenerEmpleados();


        dbReference.child("Departamentos").child(proyecto.getDid()).addValueEventListener(eventListenerEmpleados);

        //Control spinner empleados
        rellenarSpinnerEmpleados();
        spinner_empleadosTarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //La posición del nombre de empleado equivale a la de la lista de objetos empleado
                int posNombre =  spinner_empleadosTarea.getSelectedItemPosition();
                //Se asigna la id del empleado de esa posición para poder introducirlo como encargado
                uidEncargado= empleados.get(posNombre).getUid();
            }
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });
        //Control spinner prioridad
        rellenarSpinnerPrioridad();
        spinner_prioridadTarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                prioridad=  spinner_prioridadTarea.getSelectedItemPosition();
            }
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });
        //Control spinner tipos
        rellenarSpinnerTipo();
        spinner_tipoTarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int pos =  spinner_tipoTarea.getSelectedItemPosition();
                tipo=pos-1;
                setImagenTarea(tipo);
            }
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });

        btn_aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearTarea();
            }
        });
    }

    /**
     * Pone la imagen basándose en el tipo de tarea que sea la tarea a crear
     * @param tipo 0 bug, 1 tarea, 3 otro
     */
    private void setImagenTarea(int tipo){
        switch (tipo){
            case -1:
                iv_imagenTarea.setImageResource(R.drawable.bug_icon);
                break;
            case 0:
                iv_imagenTarea.setImageResource(R.drawable.task_icon);
                break;
            case 1:
                iv_imagenTarea.setImageResource(R.drawable.others_icon);
                break;
        }
    }

    /**
     * Obtiene el proyecto enviado por el intent que llamaba a la Activity
     */
    private void recogerProyecto(){
        Intent intent = getIntent();
        proyecto = (Proyecto) intent.getSerializableExtra("proyecto");
        pid= proyecto.getIdProyecto();
        eid=proyecto.getIdEmpresa();
    }

    /**
     * Obtiene el sprint enviado por el intent que llamaba a la Activity
     */
    private void recogerSprint(){
        Intent intent = getIntent();
        sprint = (Sprint) intent.getSerializableExtra("sprint");
        sid= proyecto.getIdProyecto();
    }

    /**
     * Este método rellena el spinner de los empleados con un array de nombres presente en la clase
     */
    private void rellenarSpinnerEmpleados(){
        Empleado empl= new Empleado( "-1", "Encargado", "",eid);
        empleadosNombres.add(empl.getNombreEmpleado()+""+empl.getApellidoEmpleado());
        empleados.add(0,empl);

        ArrayAdapter<String> empleadoAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empleadosNombres);
        empleadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_empleadosTarea.setAdapter(empleadoAdapter);
    }

    /**
     * Este método rellena el spinner de los tipos con el arrayList de opciones
     */
    private void rellenarSpinnerTipo(){
        tipos.add(0,"Error");
        tipos.add(1,"Tarea");
        tipos.add(2,"Otro");

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipoTarea.setAdapter(adapter);
    }

    /**
     * Rellena el spinner de la prioridad con el arrayList de opciones
     */
    private void rellenarSpinnerPrioridad(){
        prioridades.add(0,"Prioridad baja");
        prioridades.add(1,"Prioridad normal");
        prioridades.add(2,"Prioridad alta");
        prioridades.add(3,"Prioridad terminal");

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prioridades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_prioridadTarea.setAdapter(adapter);
    }

    /**
     * Obtiene los datos de los empleados y los pasa a dos arrayLists.
     * En un array "empleados" guarda los objetos completos.
     * En un array "empleadosNombres" guarda los nombres pora poder usarlos en el spinnerEmpleados
     */
    private void setEventListenerEmpleados(){
        eventListenerEmpleados = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Departamento departamento= snapshot.getValue(Departamento.class);
                //Se recorre la lista de miembros y se extraen los datos de cada uno
                try {
                    ArrayList<String> miembrosDpt= departamento.getMiembrosDepartamento();
                    for (int i = 0; i < miembrosDpt.size(); i++) {
                        String uid = miembrosDpt.get(i);
                        dbReference.child("Empleados").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Empleado empld = snapshot.getValue(Empleado.class);
                                empleados.add(empld);
                                empleadosNombres.add(empld.getNombreEmpleado() +" "+ empld.getApellidoEmpleado());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } catch (NullPointerException e){

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

    }

    /**
     * Crea una tarea a partir de los datos proporcionados y lo añade a la base de datos
     */
    private void crearTarea(){
        proyecto.setShowMenu(false);

        String nombre = et_nombreTarea.getText().toString();
        String descripcion = et_descripcionTarea.getText().toString();
        if(!uidEncargado.equals("-1")){
            if (!TextUtils.isEmpty(nombre)&&!TextUtils.isEmpty(descripcion)) {
                Sprint sprint1=sprint;
                proyecto.eliminarSprint(sprint1);

                //Se crea la tarea y se guarda
                String tid = UUID.randomUUID().toString();
                Date fechaCreacion= new Date();

                Tarea tarea= new Tarea(tid,nombre, descripcion,tipo,prioridad, uidEncargado,fechaCreacion);
                tarea.setEstadoTarea(0);

                sprint1.nuevaTarea(tarea);
                proyecto.nuevoSprint(sprint1);
                proyecto.ordenarSprints();

                dbReference.child("Proyectos").child(pid).setValue(proyecto);

                finish();
                Toast.makeText(getApplicationContext(), "Tarea añadido", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "¡Debe rellenar todos los campos!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "¡Debe seleccionar un encargado!", Toast.LENGTH_LONG).show();
        }

    }
}