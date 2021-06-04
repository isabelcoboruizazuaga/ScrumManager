package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Permite añadir un sprint al proyecto que se le pasa en el intent que inicia la actividad
 */
public class NuevoSprintActivity extends AppCompatActivity {
    private DatabaseReference dbReference;

    private String eid,nifCliente,idDepartamento, pid;
    private Proyecto proyecto;

    private EditText et_nombreSprint, et_FechaInicioSprint, et_FechaFinSprint, et_objetivosSprint;
    private Button btn_aniadir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_sprint);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Crear sprint");

        //Se recogen los datos del proyecto del sprint
        recogerProyecto();

        //Inicialización del layout
        et_nombreSprint= findViewById(R.id.et_nombreSprint);
        et_FechaInicioSprint= findViewById(R.id.et_FechaInicioSprint);
        et_FechaFinSprint= findViewById(R.id.et_FechaFinSprint);
        et_objetivosSprint=findViewById(R.id.et_objetivosSprint);
        btn_aniadir= findViewById(R.id.btn_addSprint);

        //Inicialización de Firebase
        dbReference= FirebaseDatabase.getInstance().getReference().child(eid);

        btn_aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearSprint();
            }
        });

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
     * Crea un sprint a partir de los datos proporcionados y lo añade a la base de datos
     */
    private void crearSprint(){
        proyecto.setShowMenu(false);

        String fechaInicio = et_FechaInicioSprint.getText().toString();
        String fechaFinal = et_FechaFinSprint.getText().toString();
        String nombre = et_nombreSprint.getText().toString();
        String objetivos = et_objetivosSprint.getText().toString();
        Date finProyecto= proyecto.getFechasProyecto().get(1);

        Date fechaIni = parseDate(fechaInicio);
        Date fechaFin = parseDate(fechaFinal);
        //Si las fechas son nulas no son correctas, lo mismo sucede si la de fin es menor que la de inicio o mayor que el fin de proyecto
        if (fechaFin != null && fechaIni != null) {
            if (fechaFin.compareTo(fechaIni) >0 && finProyecto.compareTo(fechaFin)>=0) {
                if (!TextUtils.isEmpty(nombre)) {
                    //Se crea el sprint y se guarda
                    ArrayList<Date> fechasSprint = new ArrayList<>();
                    fechasSprint.add(0, fechaIni);
                    fechasSprint.add(1, fechaFin);

                    String sid = UUID.randomUUID().toString();
                    Sprint sprint= new Sprint(sid, nombre, fechasSprint);
                    sprint.setObjetivoSprint(objetivos);

                    proyecto.nuevoSprint(sprint);

                    dbReference.child("Proyectos").child(pid).setValue(proyecto);

                    finish();
                    Toast.makeText(getApplicationContext(), "Sprint añadido", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "¡Debe rellenar el campo del nombre!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "¡La fecha de fin debe ser mayor que la de inicio y debe estar dentro del plazo del proyecto!", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(getApplicationContext(), "¡Debe introducir dos fechas en formato correcto!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Convierte una fecha pasada en string a tipo Date
     * @param date String
     * @return date Date
     */
    public Date parseDate(String date) {
        if(date.contains("/")){
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(date);
            } catch (ParseException e) {
                return null;
            }
        }else{
            if(date.contains("_")){
                try {
                    return new SimpleDateFormat("dd-MM-yyyy").parse(date);
                } catch (ParseException e) {
                    return null;
                }
            }else{
                return null;
            }
        }
    }
}