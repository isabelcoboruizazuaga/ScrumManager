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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class NuevoClienteActivity extends AppCompatActivity {
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private ValueEventListener eventListener;
    StorageReference storageReference;

    private Cliente cliente;
    private  String eid, dni;
    private Uri imagenUri;
    private  ArrayList<String> tipos= new ArrayList<String>();
    private String tipo;

    private Spinner spinnerTipo;
    private Button btn_registrar;
    private EditText et_nombre, et_apellidos, et_telefono, et_email,et_dni;
    private ImageView iv_imagenCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Crear empleado");

        //Recuperación del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        eid= sp.getString("eid","-1");

        //Inicialización variables del layout
        spinnerTipo= findViewById(R.id.spinner_tipo);
        btn_registrar= findViewById(R.id.btn_registrarCliente);
        et_nombre=findViewById(R.id.et_nombreCliente);
        et_apellidos=findViewById(R.id.et_apellidoCliente);
        et_email=findViewById(R.id.et_emailCliente);
        et_telefono=findViewById(R.id.et_telefonoCliente);
        et_dni=findViewById(R.id.et_dniCliente);
        iv_imagenCliente=findViewById(R.id.iv_imagenCliente);

        //Inicialización de Firebase
        dbReference=FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        //Control del spinner
        rellenarSpinnerDepartamento();
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Se obtiene el tipo seleccionado
                int postipo =  spinnerTipo.getSelectedItemPosition();
                tipo= tipos.get(position);
            }
            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });

        //ON CLICK LISTENERS
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCliente();
            }
        });
        iv_imagenCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirGaleriaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(abrirGaleriaIntent,1000);
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
                Picasso.get().load(imagenUri).into(iv_imagenCliente);
            }
        }
    }

    //Método para subir la imagen a la database y cargarla en el layout
    private void subirImagenFirebase(Uri imagenUri){
        try {
            StorageReference archivoRef = storageReference.child("clients/" + dni + "/profile.jpg");
            archivoRef.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    archivoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Si se sube correctamente se accede a la base de datos y la carga en el layout
                            Picasso.get().load(uri).into(iv_imagenCliente);
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

    //Método para rellenar las opciones de tipo
    private void rellenarSpinnerDepartamento(){
        tipos.add(0,"Empresa");
        tipos.add(1,"Individual");
        ArrayAdapter<String> departamentoAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        departamentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(departamentoAdapter);
    }

    //Método ejecutado cuando el usuario pulse el botón de registrar
    private void crearCliente(){
        //String nombreEmpresa = et_nombreEmpresa.getText().toString();
        String email = et_email.getText().toString();
        String telefono = et_telefono.getText().toString();
        String nombre = et_nombre.getText().toString();
        String apellido = et_apellidos.getText().toString();
        dni= et_dni.getText().toString();

        //El usuario se registra sólo si ha rellenado los tres campos
        if(!TextUtils.isEmpty(nombre)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(dni)){
            if(dni.length()==9){
                //Se crea el perfil de usuario y se envía a la base de datos
                Cliente clienteObjeto= new Cliente(dni,nombre,apellido,email,eid);
                clienteObjeto.setTelefonoCliente(telefono);
                clienteObjeto.setTipoCliente(tipo);
                dbReference.child(eid).child("Clientes").child(dni).setValue(clienteObjeto);
                //Se sube la imagen
                subirImagenFirebase(imagenUri);
                finish();
            }else{
                Toast.makeText(this,"El nif debe ser válido",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Debe rellenar todos los campos, el apellido no es necesario",Toast.LENGTH_SHORT).show();
        }

    }
}