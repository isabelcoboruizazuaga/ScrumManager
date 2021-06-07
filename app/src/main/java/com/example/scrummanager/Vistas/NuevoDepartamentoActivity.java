package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.scrummanager.Controladores.AdaptadorEmpleados;
import com.example.scrummanager.Controladores.AdaptadorEmpleadosDpt;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.UUID;

/**
 * Permite crear un departamento e introducirlo en la base de datos de la empresa
 */
public class NuevoDepartamentoActivity extends AppCompatActivity {
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;
    StorageReference storageReference;

    private ArrayList<Empleado> empleados;
    private ArrayList<String> empleadosNombres;
    private String uidJefeDpt;
    private String eid;
    private Uri imagenUri;
    String did= UUID.randomUUID().toString();
    private Empleado empleado;

    private Spinner spinnerEmpleado;
    private Button btn_crear;
    private EditText et_nombre;
    private ImageView iv_imagenDepartamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_departamento);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Crear departamento");

        //Inicialización variables del layout
        spinnerEmpleado= findViewById(R.id.spinner_empleados);
        btn_crear= findViewById(R.id.btn_addDepartamento);
        et_nombre=findViewById(R.id.et_nombreDepartamento);
        iv_imagenDepartamento=findViewById(R.id.iv_imagenDepartamento);

        //Obtención del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        eid= sp.getString("eid","-1");

        //Inicialización de firebaseDatabase
        database= FirebaseDatabase.getInstance();
        dbReference = database.getReference().child(eid);
        storageReference= FirebaseStorage.getInstance().getReference();

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

        //Al clickar en la imagen se selecciona en la galería
        iv_imagenDepartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirGaleriaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(abrirGaleriaIntent,1000);
            }
        });


        //Cargar la imagen de departamento
        StorageReference perfilRef= storageReference.child("departments/"+did+"/cover.jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(iv_imagenDepartamento);
            }
        });
    }

    //Método que se activará cuando la imagen de pérfil se pulse y se abra la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                imagenUri= data.getData();
                Picasso.get().load(imagenUri).into(iv_imagenDepartamento);
            }
        }
    }

    /**
     * Sube la imagen a la base de datos y la muestra
     * @param imagenUri
     */
    private void subirImagenFirebase(Uri imagenUri){
        try {
            StorageReference archivoRef = storageReference.child("departments/" + did + "/cover.jpg");
            archivoRef.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    archivoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Si se sube correctamente se accede a la base de datos y la carga en el layout
                            Picasso.get().load(uri).into(iv_imagenDepartamento);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "errooor", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (java.lang.IllegalArgumentException e){}
    }

    /**
     * Crea un departamento con los datos introducidos por el usuario
     * Extrae los datos del layout y los introduce en la base de datos
     * @param v
     */
    private void crearDepartamento(View v){
        String nombre= et_nombre.getText().toString();
        Departamento departamento= new Departamento(did,nombre,eid);

        //Si el departamento se edita sin jefe se añade sin más a la base de datos
        if(uidJefeDpt.equals("-1")){
            dbReference.child("Departamentos").child(did).setValue(departamento);
            //Se sube la imagen a la base de datos
            subirImagenFirebase(imagenUri);
            Toast.makeText(getApplicationContext(), "Departamento creado", Toast.LENGTH_SHORT).show();

            finish();
        }else {
            //Se obtiene el empleado a añadir
            dbReference.child("Empleados").child(uidJefeDpt).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Empleado empleado = snapshot.getValue(Empleado.class);
                        //Si el empleado es jefe de otro departamento no puede añadirse como jefe del nuevo
                        if(empleado.getNivelJerarquia()==2 && !empleado.getIdDepartamento().equals(did)) {
                            Toast.makeText(getApplicationContext(), "Este empleado ya es jefe de otro departamento", Toast.LENGTH_SHORT).show();
                        }else{
                            String antiguoDpt= empleado.getIdDepartamento();
                            //Si el empleado pertenecía a otro departamento
                            if(!antiguoDpt.equals(did)) {
                                if(!antiguoDpt.equals("-1")&&!antiguoDpt.equals(did)) {
                                    //Se elimina el jefe como jefe de anterior departamento
                                    dbReference.child("Departamentos").child(antiguoDpt).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //Se elimina del departamento
                                            Departamento departamento = snapshot.getValue(Departamento.class);
                                            departamento.setUidJefeDepartamento("-1");
                                            dbReference.child("Departamentos").child(antiguoDpt).setValue(departamento);

                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                                //se añade al nuevo
                                departamento.aniadirEmpleado(empleado.getUid());
                            }
                            //Se actualiza el departamento del empleado en la base de datos
                            empleado.setIdDepartamento(did);
                            empleado.setNivelJerarquia(2);
                            dbReference.child("Empleados").child(uidJefeDpt).setValue(empleado);

                            //Se introduce el empleado al departamento y se añade a la bd
                            departamento.setUidJefeDepartamento(uidJefeDpt);
                            dbReference.child("Departamentos").child(did).setValue(departamento);
                            Toast.makeText(getApplicationContext(), "Departamento creado", Toast.LENGTH_SHORT).show();

                            //Se sube la imagen a la base de datos
                            subirImagenFirebase(imagenUri);

                            finish();
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

    /**
     * Rellena las opciones del spinner de empleados
     */
    private void rellenarSpinnerEmpleados(Empleado empleado){
        ArrayAdapter<String> empleadoAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empleadosNombres);
        empleadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmpleado.setAdapter(empleadoAdapter);
    }

    /**
     * Crea el EventListener para extraer los empleados de la base de datos
     */
    private void setEventListener(){
        eventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot xempleado : snapshot.getChildren() ){
                    //Se añaden los empleados a la lista
                    empleado = xempleado.getValue(Empleado.class);
                    empleados.add(empleado);
                    empleadosNombres.add(empleado.getNombreEmpleado() +" " +empleado.getApellidoEmpleado());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

}