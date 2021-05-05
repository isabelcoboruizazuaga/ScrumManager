package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

public class NuevoEmpleadoActivity extends AppCompatActivity {
    private FirebaseAuth mAuthAdmin, mAuthWorker;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;
    StorageReference storageReference;

    private ArrayList<Departamento> departamentos;
    private ArrayList<String> departamentosNombres;
    private Empleado empleadoManager;
    private String idDepartamento;


    private Spinner spinnerDepartamento;
    private Button btn_registrar;
    private EditText et_nombre, et_apellidos, et_dni, et_email, et_contrasenia;
    private ImageView iv_imagenEmpleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_empleado);

        //Inicialización variables del layout
        spinnerDepartamento= findViewById(R.id.spinner_departamentos);
        btn_registrar= findViewById(R.id.btn_registrarEmpleado);
        et_nombre=findViewById(R.id.et_nombreEmpleado);
        et_apellidos=findViewById(R.id.et_apellidoEmpleado);
        et_dni=findViewById(R.id.et_dniEmpleado);
        et_email=findViewById(R.id.et_emailEmpleado);
        et_contrasenia=findViewById(R.id.et_contraseniaEmpleado);
        iv_imagenEmpleado=findViewById(R.id.iv_imagenEmpleado);

        //Initialización de Firebase
        mAuthAdmin = FirebaseAuth.getInstance();

        /*storageReference= FirebaseStorage.getInstance().getReference();*/

        //Configuración de la autenticación
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[https://scrummanager-edb30-default-rtdb.firebaseio.com/]")
                .setApiKey("AIzaSyDjlWFQsYGJFIlbVmSdu6-gNMu65Pu0YF8 ")
                .setApplicationId("scrummanager-edb30").build();
        //Asignación de la configuración para el usuario que vamos a crear
        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "ScrumManager");
            mAuthWorker = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuthWorker = FirebaseAuth.getInstance(FirebaseApp.getInstance("ScrumManager"));
        }

        //Firebase database initialization
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();

        //
        departamentos= new ArrayList<>();
        departamentosNombres= new ArrayList<>();
        Departamento dpt= new Departamento("dpt1","Dpt1", "empresa");
        departamentosNombres.add(dpt.getNombreDepartamento());
        departamentos.add(dpt);
        dpt= new Departamento("dpt2","dpt2", "empresa");
        departamentosNombres.add(dpt.getNombreDepartamento());
        departamentos.add(dpt);
        dpt= new Departamento("dpt3","dpt3", "empresa");
        departamentosNombres.add(dpt.getNombreDepartamento());
        departamentos.add(dpt);
        dpt= new Departamento("dpt4","dpt4", "empresa");
        departamentosNombres.add(dpt.getNombreDepartamento());
        departamentos.add(dpt);
        dpt= new Departamento("dpt5","dpt5", "empresa");
        departamentosNombres.add(dpt.getNombreDepartamento());
        departamentos.add(dpt);

        rellenarSpinnerDepartamento();

        spinnerDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {
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
                crearCuenta();
            }
        });

        /*iv_imagenEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirGaleriaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(abrirGaleriaIntent,1000);
            }
        });*/

        /*CARGAR IMAGEN PERFIL
        StorageReference storageReference;
        StorageReference perfilRef= storageReference.child("users/"+mAuthAdmin.getCurrentUser().getUid()+"profile.jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(iv_imagenEmpleado);
            }
        });*/
    }

    /*//Método que se activará cuando la imagen de pérfil se pulse y se abra la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imagenUri= data.getData();
                subirImagenFirebase(imagenUri);
            }
        }
    }

    //Método para subir la imagen a la database y cargarla en el layout
    private void subirImagenFirebase(Uri imagenUri){
        StorageReference archivoRef= storageReference.child("users/"+mAuthAdmin.getCurrentUser().getUid()+"/profile.jpg");
        archivoRef.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                archivoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Si se sube correctamente se accede a la base de datos y la carga en el layout
                        Picasso.get().load(uri).into(iv_imagenEmpleado);
                    }
                });
                Toast.makeText(getBaseContext(),"ok",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(),"errooor",Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    //Método para rellenar las opciones de departamento
    private void rellenarSpinnerDepartamento(){
        ArrayAdapter<String> departamentoAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departamentosNombres);
        departamentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartamento.setAdapter(departamentoAdapter);
    }

    //Método ejecutado cuando el usuario pulse el botón de registrar
    private void crearCuenta(){
        //String nombreEmpresa = et_nombreEmpresa.getText().toString();
        String email = et_email.getText().toString();
        String contrasenia = et_contrasenia.getText().toString();
        String nombre = et_nombre.getText().toString();
        String apellido = et_apellidos.getText().toString();

        //El usuario se registra sólo si ha rellenado los tres campos
        if(!TextUtils.isEmpty(nombre)&& !TextUtils.isEmpty(apellido)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(contrasenia)){
            if(contrasenia.length()>=6){
                mAuthWorker.createUserWithEmailAndPassword(email, contrasenia)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //si no hay error al crear la cuenta se redirige a la actividad principal
                                if (task.isSuccessful()) {
                                    //Se le envía un correo de verificación
                                    FirebaseUser user = mAuthWorker.getCurrentUser();
                                    verificarEmail(user);

                                    //Se añade a la empresa
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    String eid= sp.getString("eid","-1");

                                    //Se crea el perfil de usuario y se envía a la base de datos
                                    String uid = user.getUid();
                                    Empleado empleadoObjeto= new Empleado(uid,nombre,apellido,eid);
                                    empleadoObjeto.setEmailEmpleado(email);
                                    empleadoObjeto.setIdDepartamento(idDepartamento);
                                    dbReference.child(eid).child("Empleados").child(uid).setValue(empleadoObjeto);

                                    updateUI(user);
                                } else {
                                    //Si falla el usuario es informado
                                    updateUI(null);
                                }
                            }
                        });
            }else{
                Toast.makeText(this,"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Debe rellenar todos los campos",Toast.LENGTH_SHORT).show();
        }

    }

    //Redirige al usuario o muestra un error
    private void updateUI(FirebaseUser account){
        if(account != null){
            //Si es correcto sale de la cuenta creada para seguir en la cuenta de administrador
            mAuthWorker.signOut();
            this.finish();
            Toast.makeText(this,"Cuenta creada".toString(),Toast.LENGTH_SHORT).show();
        }else{
            Toast. makeText(this,"Error, es posible que el correo ya esté en uso",Toast. LENGTH_SHORT).show();
        }

    }
    //Método que envía el email de verificación
    private void verificarEmail(FirebaseUser user){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                }
            }
        });
    }


    //Database listener
    public void setEventListener(){
        eventListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //The data of the logged user are extracted
                //empleadoManager = dataSnapshot.getValue(Empleado.class);

                //The list of the received messages is gotten
                //empleadoManager.get;

                //Same with the sent messages
                //getCurrentUserSentMessages();

                //RV_creation();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException());
            }
        };
    }
}