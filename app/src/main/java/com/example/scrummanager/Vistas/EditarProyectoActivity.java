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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Permite editar un proyecto que se le pasa como parte de un intent
 */
public class EditarProyectoActivity extends AppCompatActivity {
    private DatabaseReference dbReference;
    private ValueEventListener eventListenerDepartamentos,eventListenerClientes ;
    StorageReference storageReference;

    private ArrayList<Departamento> departamentos=new ArrayList<>();
    private ArrayList<Cliente> clientes=new ArrayList<>();
    private ArrayList<String> departamentosNombres=new ArrayList<>(),clientesNombres=new ArrayList<>();
    private String eid,nifCliente,idDepartamento, pid, idAntiguoDepartamento,idAntiguoCliente;
    private Proyecto proyecto;

    private Spinner spinnerDepartamento,spinnerClientes;
    private Button btn_aniadir;
    private EditText et_nombre, et_FechaInicioProyecto, et_FechaFinProyecto, et_presupuestoProyecto, et_especificacionesProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_proyecto);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Editar proyecto");

        //Se recogen los datos del proyecto a editar
        recogerProyecto();
        ArrayList <Date>fechas= proyecto.getFechasProyecto();

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

        //Inicialización del layout
        spinnerDepartamento= findViewById(R.id.spinner_departamentosProy);
        spinnerClientes= findViewById(R.id.spinner_clientesProy);
        btn_aniadir= findViewById(R.id.btn_editarProyecto);
        et_nombre=findViewById(R.id.et_nombreProyecto);
        et_FechaInicioProyecto=findViewById(R.id.et_FechaInicioProyecto);
        et_FechaFinProyecto=findViewById(R.id.et_FechaFinProyecto);
        et_presupuestoProyecto=findViewById(R.id.et_presupuestoProyecto);
        et_especificacionesProyecto=findViewById(R.id.et_especificacionesProyecto);

        et_FechaInicioProyecto.setText(fechaInicio);
        et_FechaFinProyecto.setText(fechaFin);
        et_nombre.setText(proyecto.getNombreProyecto());
        et_presupuestoProyecto.setText(proyecto.getPresupuesto());
        et_especificacionesProyecto.setText(proyecto.getEspecificacionesProyecto());

        //Inicialización de Firebase
        dbReference= FirebaseDatabase.getInstance().getReference().child(eid);
        storageReference= FirebaseStorage.getInstance().getReference();

        //Extracción de los datos del spinner departamentos
        controlInicialRellenarDepartamentos();
        setEventListenerDepartamentos();
        dbReference.child("Departamentos").addValueEventListener(eventListenerDepartamentos);

        //Extracción de los datos del spinner clientes
        controlInicialRellenarClientes();
        setEventListenerClientes();
        dbReference.child("Clientes").addValueEventListener(eventListenerClientes);

        //Control de los spinners
        spinnerDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //La posición del nombre de departamento equivale a la de la lista de objetos departamento
                int posNombre =  spinnerDepartamento.getSelectedItemPosition();
                //Se asigna la id del departamento de esa posición para poder introducirlo en el nuevo empleado
                idDepartamento= departamentos.get(posNombre).getIdDepartamento();
            }
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });

        spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //La posición del nombre de departamento equivale a la de la lista de objetos departamento
                int posNombre =  spinnerClientes.getSelectedItemPosition();
                //Se asigna la id del departamento de esa posición para poder introducirlo en el nuevo empleado
                nifCliente= clientes.get(posNombre).getNifCliente();
            }
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }
        });

        btn_aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearProyecto();
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
        idAntiguoDepartamento=proyecto.getDid();
        idAntiguoCliente=proyecto.getCliente();
    }

    /**
     * Extrae de la base de datos los datos del departamento actual de un proyecto y los guarda en el array duplicando los datos para que sea distinguible
     * Llama al método que rellena el spinner
     */
    private void controlInicialRellenarDepartamentos(){
        //Se coloca el departamento actual el primero
        dbReference.child("Departamentos").child(proyecto.getDid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Departamento departamento1 = snapshot.getValue(Departamento.class);
                departamentos.add(0, departamento1);
                departamentosNombres.add(0, departamento1.getNombreDepartamento());

                //Se indica que es el spinner de departamentos separando ademas el actual del resto
                Departamento dpt= new Departamento("-1","Departamentos", eid);
                departamentosNombres.add(1,dpt.getNombreDepartamento());
                departamentos.add(1,dpt);

                rellenarSpinnerDepartamentos();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Extrae de la base de datos los datos del cliente actual de un proyecto y los guarda en el array duplicando los datos para que sea distinguible
     * Llama al método que rellena el spinner
     */
    private void controlInicialRellenarClientes(){
        //Se coloca el departamento actual el primero
        dbReference.child("Clientes").child(proyecto.getCliente()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cliente cliente = snapshot.getValue(Cliente.class);
                clientes.add(0, cliente);
                clientesNombres.add(0,cliente.getNombreCliente()+" "+cliente.getApellidoCliente());

                Cliente cli= new Cliente( "-1", "Clientes", "","",eid);
                clientesNombres.add(1,cli.getNombreCliente()+""+cli.getApellidoCliente());
                clientes.add(1,cli);

                rellenarSpinnerClientes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Este método rellena el spinner de los departamentos con un array de nombres presente en la clase
     */
    private void rellenarSpinnerDepartamentos(){
        ArrayAdapter<String> departamentoAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departamentosNombres);
        departamentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartamento.setAdapter(departamentoAdapter);
    }


    private Departamento departamento;
    /**
     * Obtiene los datos de los departamentos y los pasa a dos arrays.
     * En un array "departamentos" guarda los objetos completos.
     * En un array "departamentosNombres" guarda los nombres pora poder usarlos en el spinnerDepartamentos.
     */
    private void setEventListenerDepartamentos(){
        eventListenerDepartamentos= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot xempleado : snapshot.getChildren() ){
                    //Se añaden los empleados a la lista
                    departamento = xempleado.getValue(Departamento.class);
                    departamentos.add(departamento);
                    departamentosNombres.add(departamento.getNombreDepartamento());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    /**
     * Este método rellena el spinner de los clientes con un array de nombres presente en la clase
     */
    private void rellenarSpinnerClientes(){
        ArrayAdapter<String> clienteAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientesNombres);
        clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(clienteAdapter);
    }


    private Cliente cliente;
    /**
     * Obtiene los datos de los clientes y los pasa a dos arrays.
     * En un array "clientes" guarda los objetos completos.
     * En un array "clientesNombres" guarda los nombres pora poder usarlos en el spinnerDepartamentos.
     */
    private void setEventListenerClientes(){
        eventListenerClientes= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot xclientes : snapshot.getChildren() ){
                    //Se añaden los clientes a la lista
                    cliente = xclientes.getValue(Cliente.class);
                    clientes.add(cliente);
                    clientesNombres.add(cliente.getNombreCliente()+" "+cliente.getApellidoCliente());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    /**
     * Crea un proyecto a partir de los datos proporcionados y lo añade a la base de datos
     */
    private void crearProyecto(){
        //Solo se actúa si se ha seleccionado un cliente o un departamento
        if(!idDepartamento.equals("-1") && !nifCliente.equals("-1")){
            String fechaInicio = et_FechaInicioProyecto.getText().toString();
            String fechaFinal = et_FechaFinProyecto.getText().toString();
            String nombre = et_nombre.getText().toString();
            String presupuesto = et_presupuestoProyecto.getText().toString();
            String especificaciones= et_especificacionesProyecto.getText().toString();

            Date fechaIni=parseDate(fechaInicio);
            Date fechaFin= parseDate(fechaFinal);
            //Si las fechas son nulas no son correctas, lo mismo sucede si la de fin es menor que la de inicia
            if(fechaFin!=null&& fechaIni!=null){
                if(fechaFin.compareTo(fechaIni)==1) {
                    if (!TextUtils.isEmpty(nombre)) {
                        //Se actualiza el proyecto y se guarda
                        ArrayList<Date> fechasProyecto = new ArrayList<>();
                        fechasProyecto.add(0, fechaIni);
                        fechasProyecto.add(1, fechaFin);

                        proyecto.setNombreProyecto(nombre);
                        proyecto.setCliente(nifCliente);
                        proyecto.setDid(idDepartamento);
                        proyecto.setEspecificacionesProyecto(especificaciones);
                        proyecto.setFechasProyecto(fechasProyecto);
                        proyecto.setPresupuesto(presupuesto);

                        dbReference.child("Proyectos").child(pid).setValue(proyecto);

                        finish();
                        Toast.makeText(getApplicationContext(), "Proyecto editado", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"¡La fecha de fin debe ser mayor que la de inicio!",Toast.LENGTH_LONG).show();

                }
            }else{
                Toast.makeText(getApplicationContext(),"¡Debe introducir dos fechas en formato correcto!",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"¡El proyecto debe tener un cliente y un departamento válidos!",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Convierte una fecha pasada en string a tipo Date
     * @param date String
     * @return date Date
     */
    public static Date parseDate(String date) {
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