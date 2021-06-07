package com.example.scrummanager.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Permite editar un Sprint que se le pasa por un intent
 */
public class EditarSprintActivity extends AppCompatActivity {private DatabaseReference dbReference;
    private String eid, pid, sid;
    private Proyecto proyecto;
    private Sprint sprint;

    private EditText et_nombreSprint, et_FechaInicioSprint, et_FechaFinSprint, et_objetivosSprint;
    private Button btn_editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_sprint);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Editar sprint");

        //Se recogen los datos del proyecto y del sprint
        recogerProyecto();
        recogerSprint();

        //Inicialización del layout
        et_nombreSprint= findViewById(R.id.et_nombreSprint);
        et_FechaInicioSprint= findViewById(R.id.et_FechaInicioSprint);
        et_FechaFinSprint= findViewById(R.id.et_FechaFinSprint);
        et_objetivosSprint=findViewById(R.id.et_objetivosSprint);
        btn_editar = findViewById(R.id.btn_addSprint);

        //Se rellena el layout con los datos extraídos
        String fechas[]=dateToString(sprint.getFechasSprint());
        et_nombreSprint.setText(sprint.getNombre());
        et_FechaInicioSprint.setText(fechas[0]);
        et_FechaFinSprint.setText(fechas[1]);
        et_objetivosSprint.setText(sprint.getObjetivoSprint());
        btn_editar.setText("Editar");


        //Inicialización de Firebase
        dbReference= FirebaseDatabase.getInstance().getReference().child(eid);

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarSprint();
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
     * Obtiene el sprint enviado por el intent que llamaba a la Activity
     */
    private void recogerSprint(){
        Intent intent = getIntent();
        sprint = (Sprint) intent.getSerializableExtra("sprint");
        sid= sprint.getIdSprint();
    }

    /**
     * Edita un sprint a partir de los datos proporcionados y lo añade a la base de datos
     */
    private void editarSprint(){
        proyecto.setShowMenu(false);
        sprint.setShowMenu(false);

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
                    Sprint sprint1=sprint;
                    proyecto.eliminarSprint(sprint1);

                    //Se crea el sprint y se guarda
                    ArrayList<Date> fechasSprint = new ArrayList<>();
                    fechasSprint.add(0, fechaIni);
                    fechasSprint.add(1, fechaFin);

                    sprint1.setNombre(nombre);
                    sprint1.setFechasSprint(fechasSprint);
                    sprint1.setObjetivoSprint(objetivos);

                    proyecto.nuevoSprint(sprint1);
                    proyecto.ordenarSprints();

                    dbReference.child("Proyectos").child(pid).setValue(proyecto);

                    finish();
                    Toast.makeText(getApplicationContext(), "Sprint editado", Toast.LENGTH_LONG).show();
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

    /**
     * Convierte las fechas de un ArrayList de dos fechas en string y las devuelve como array
     * @param fechas arrayList<Date> de donde se extrae la fecha de inicio (0) y la de fin (1)
     * @return  resultado String[] con las fechas como Strings
     */
    public String[] dateToString(ArrayList<Date> fechas){
        String fechaInicio;
        String fechaFin;
        //Conversión de la fecha al formato correcto
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            fechaInicio = formatter.format(fechas.get(0));
            fechaFin = formatter.format(fechas.get(1));
        }catch (Exception e){
            fechaInicio = "dd/MM/yyyy";
            fechaFin = "dd/MM/yyyy";
        }
        String resultado[]={fechaInicio,fechaFin};
        return resultado;
    }
}