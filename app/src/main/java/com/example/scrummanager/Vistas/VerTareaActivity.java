package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scrummanager.Controladores.AdaptadorTareas;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.Modelos.Tarea;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VerTareaActivity extends AppCompatActivity {
    private Tarea tarea;
    private String eid;

    private TextView tv_nombreTarea, tv_nombreEncargado,tv_descripcionTarea,tv_creacionTarea;
    private ImageView iv_imagenTarea;
    private LinearLayout fondoTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tarea);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Ver tarea");

        //Se recogen los datos de la tarea
        recogerTarea();

        //Inicialización del layout
        tv_nombreTarea= findViewById(R.id.tv_nombreTarea);
        tv_descripcionTarea= findViewById(R.id.tv_descripcionTarea);
        tv_nombreEncargado= findViewById(R.id.tv_nombreEncargado);
        tv_creacionTarea=findViewById(R.id.tv_creacionTarea);
        iv_imagenTarea= findViewById(R.id.iv_imagenTarea);
        fondoTarea= findViewById(R.id.fondoNombre);

        setImagenTarea(tarea.getTipoTarea());
        setFondoTarea(tarea.getPrioridadTarea());
        rellenarCampoNombreEncargado(tarea.getEncargadoTarea(),eid);
        setFecha(tarea.getFechaCreacion(), tarea.getEstadoTarea());
        tv_descripcionTarea.setText(tarea.getDescripcionTarea());
        tv_nombreTarea.setText(tarea.getNombreTarea());

    }
    /**
     * Obtiene la tarea a ver enviada por el intent que llamaba a la Activity
     */
    private void recogerTarea(){
        Intent intent = getIntent();
        tarea = (Tarea) intent.getSerializableExtra("tarea");
        eid = intent.getStringExtra("eid");
    }

    /**
     * Recoge el nombre del encargado de la base de datos y lo coloca en el TextView correspondiente
     * @param uid: String, identificador del usuario encargado
     * @param eid: Stting, identificador de empresa
     */
    private void rellenarCampoNombreEncargado(String uid, String eid){
        DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference().child(eid);
        dbReference.child("Empleados").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Empleado empleado = snapshot.getValue(Empleado.class);
                String nombreEm=empleado.getNombreEmpleado() + " "+empleado.getApellidoEmpleado();

                tv_nombreEncargado.setText(nombreEm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Pone la imagen basándose en el tipo de tarea que sea la tarea a crear
     * @param tipo:int -> 0 bug, 1 tarea, 3 otro
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
     * Pone el fondo  basándose en la prioridad de tarea que sea la tarea a crear
     * @param prioridad: int -> 0 baja, 1 normal, 2 alta, 3 terminal
     */
    private void setFondoTarea(int prioridad){
        int color=0;
        switch (prioridad){
            case 0:
                color=0xff00ccff;
                break;
            case 1:
                color=0xff1cc990;
                break;
            case 2:
                color=0xffec9879;
                break;
            case 3:
                color=0xffdf4620;
                break;
        }
        fondoTarea.setBackgroundColor(color);
    }

    /**
     * Rellena el textView de la fecha
     * @param creacion
     * @param estado: int -> 0 por hacer, 1 en progreso, 2 terminada
     */
    private void setFecha(Date creacion, int estado){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaCreacion = formatter.format(creacion);
        tv_creacionTarea.setText(fechaCreacion);

        int color=0;
        switch (estado){
            case 0:
                color=getResources().getColor(R.color.redNotOk);
                break;
            case 1:
                color=getResources().getColor(R.color.orangeMedium);
                break;
            case 2:
                color=getResources().getColor(R.color.greenOk);
                break;
        }
        tv_creacionTarea.setTextColor(color);

    }
}