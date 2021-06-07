package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

/**
 * Permite editar un Empleado
 */
public class EditarEmpleadoActivity extends AppCompatActivity {
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;
    StorageReference storageReference;
    private FirebaseAuth mAuthAdmin, mAuthWorker;

    private ArrayList<Departamento> departamentos=new ArrayList<>();
    private ArrayList<String> departamentosNombres=new ArrayList<>();
    private Departamento departamento;
    private Empleado empleado;
    private String uid,eid;
    private String idDepartamento, idAntiguoDepartamento;
    Uri imagenUri;

    private Spinner spinnerDepartamento;
    private Button btn_registrar;
    private EditText et_nombre, et_apellidos, et_dni, et_email;
    private ImageView iv_imagenEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_empleado);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Editar empleado");

        //Se recogen los datos del empleado a editar
        recogerEmpleado();

        //Inicialización variables del layout
        spinnerDepartamento= findViewById(R.id.spinner_departamentos);
        btn_registrar= findViewById(R.id.btn_registrarEmpleado);
        et_nombre=findViewById(R.id.et_nombreEmpleado);
        et_apellidos=findViewById(R.id.et_apellidoEmpleado);
        et_dni=findViewById(R.id.et_dniEmpleado);
        et_email=findViewById(R.id.et_emailEmpleado);
        iv_imagenEmpleado=findViewById(R.id.iv_imagenEmpleado);

        btn_registrar.setText("Editar");
        et_email.setEnabled(false);

        //Se rellenan los campos con los datos originales del empleado
        et_nombre.setText(empleado.getNombreEmpleado());
        et_apellidos.setText(empleado.getApellidoEmpleado());
        et_dni.setText(empleado.getNifEmpleado());
        et_email.setText(empleado.getEmailEmpleado());

        //Inicialización de firebaseDatabase
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        setEventListener();
        dbReference.child(eid).child("Departamentos").addValueEventListener(eventListener);

        //Se coloca el departamento actual el primero
        if(empleado.getIdDepartamento()!=null && !empleado.getIdDepartamento().equals("-1")) {
            dbReference.child(eid).child("Departamentos").child(empleado.getIdDepartamento()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Departamento departamento1 = snapshot.getValue(Departamento.class);
                    departamentos.add(0, departamento1);
                    departamentosNombres.add(0, departamento1.getNombreDepartamento());

                    //Se añade la opción de dejar sin departamento
                    Departamento dpt= new Departamento("-1","Sin Departamento", eid);
                    departamentosNombres.add(dpt.getNombreDepartamento());
                    departamentos.add(dpt);

                    rellenarSpinnerDepartamento();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else{
            //Se añade la opción de dejar sin departamento
            Departamento dpt= new Departamento("-1","Sin Departamento", eid);
            departamentosNombres.add(dpt.getNombreDepartamento());
            departamentos.add(dpt);

            rellenarSpinnerDepartamento();
        }

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
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEmpleado();
            }
        });

        //Abrir galería para seleccionar imagen
        iv_imagenEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirGaleriaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(abrirGaleriaIntent,1000);
            }
        });

        //Imagen de perfil
        StorageReference perfilRef= storageReference.child("users/"+uid+"/profile.jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(iv_imagenEmpleado);
            }
        });
    }

    /**
     * Obtiene el empleado a editar que se ha enviado a través de un intent
     */
    private void recogerEmpleado(){
        Intent intent = getIntent();
        empleado = (Empleado) intent.getSerializableExtra("empleado");
        uid= empleado.getUid();
        eid=empleado.getIdEmpresa();
        idAntiguoDepartamento=empleado.getIdDepartamento();
    }

    //Método que se activará cuando la imagen de pérfil se pulse y se abra la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                 imagenUri= data.getData();
                 Picasso.get().load(imagenUri).into(iv_imagenEmpleado);
            }
        }
    }

    /**
     * Sube la imagen a la base de datos y la muestra
     * @param imagenUri
     */
    private void subirImagenFirebase(Uri imagenUri){
        try {
            StorageReference archivoRef = storageReference.child("users/" + uid + "/profile.jpg");
            archivoRef.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    archivoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("Añadido correctamente a la bd");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    }
            });
        }catch (java.lang.IllegalArgumentException e){}
    }

    /**
     * Rellena el spinner de los departamentos
     */
    private void rellenarSpinnerDepartamento(){
        ArrayAdapter<String> departamentoAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departamentosNombres);
        departamentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartamento.setAdapter(departamentoAdapter);
    }

    /**
     * Edita el empleado con los datos introducidos por el usuario
     * Extrae los datos del layout y los introduce en la base de datos
     */
    private void actualizarEmpleado(){
        String email = et_email.getText().toString();
        String nombre = et_nombre.getText().toString();
        String apellido = et_apellidos.getText().toString();
        String dni=et_dni.getText().toString();

        //Se crea el perfil de usuario y se envía a la base de datos
        Empleado empleadoObjeto= new Empleado(uid,nombre,apellido,eid);
        empleadoObjeto.setEmailEmpleado(email);
        empleadoObjeto.setNifEmpleado(dni);
        empleadoObjeto.setIdDepartamento(idDepartamento);

        //Si el empleado se edita sin departamento se añade sin más a la base de datos
        if(idDepartamento.equals("-1")){
            dbReference.child(eid).child("Empleados").child(uid).setValue(empleadoObjeto);
            //Se actualiza la imagen
            subirImagenFirebase(imagenUri);
            Toast.makeText(getApplicationContext(), "Empleado editado", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            //Se obtiene el departamento, se edita y se devuelve a la bd
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(eid);
            dbReference.child("Departamentos").child(idDepartamento).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Se añade al departamento
                    Departamento departamento = snapshot.getValue(Departamento.class);
                    departamento.aniadirEmpleado(uid);

                    dbReference.child("Departamentos").child(idDepartamento).setValue(departamento);
                    dbReference.child("Empleados").child(uid).setValue(empleadoObjeto);

                    if(!idAntiguoDepartamento.equals(idDepartamento)&&!idAntiguoDepartamento.equals("-1")) {
                        //Se obtiene el antiguo departamento, se edita eliminando el empleado y se devuelve a la bd
                        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(eid);
                        dbReference.child("Departamentos").child(idAntiguoDepartamento).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Se elimina del departamento
                                Departamento departamento = snapshot.getValue(Departamento.class);
                                departamento.eliminarEmpleado(uid);

                                if(departamento.getUidJefeDepartamento().equals(uid)){
                                    departamento.setUidJefeDepartamento("-1");
                                }

                                dbReference.child("Departamentos").child(idAntiguoDepartamento).setValue(departamento);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                    //Se actualiza la imagen
                    subirImagenFirebase(imagenUri);
                    Toast.makeText(getApplicationContext(), "Empleado editado", Toast.LENGTH_SHORT).show();
                    finish();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {  }
            });
        }
    }


    /**
     * Establece el EventListener para extraer los datos de los departamentos de la base de datos
     */
    private void setEventListener(){
        eventListener= new ValueEventListener() {
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


}