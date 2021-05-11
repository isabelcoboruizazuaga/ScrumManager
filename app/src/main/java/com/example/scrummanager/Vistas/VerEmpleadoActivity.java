package com.example.scrummanager.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class VerEmpleadoActivity extends AppCompatActivity {
    private TextView tv_nombre, tv_apellidos, tv_dni, tv_email;
    private ImageView iv_imagenEmpleado;
    private Empleado empleado;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_empleado);

        //Se recogen los datos del empleado
        Intent intent = getIntent();
        empleado = (Empleado) intent.getSerializableExtra("empleado");
        String uid= empleado.getUid();

        //Inicialización variables del layout
        tv_nombre = findViewById(R.id.tv_nombreEmpleado);
        tv_apellidos =findViewById(R.id.tv_apellidoEmpleado);
        tv_dni =findViewById(R.id.tv_dniEmpleado);
        tv_email =findViewById(R.id.tv_emailEmpleado);
        iv_imagenEmpleado=findViewById(R.id.iv_imagenEmpleado);

        //Se rellenan los campos con los datos  del empleado
        tv_nombre.setText(empleado.getNombreEmpleado());
        tv_apellidos.setText(empleado.getApellidoEmpleado());
        tv_dni.setText(empleado.getNifEmpleado());
        tv_email.setText(empleado.getEmailEmpleado());

        //Imagen de perfil
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference perfilRef= storageReference.child("users/"+uid+"/profile.jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(iv_imagenEmpleado);
            }
        });
    }
}