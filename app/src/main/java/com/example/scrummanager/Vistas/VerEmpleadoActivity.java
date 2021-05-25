package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Ver empleado");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ver_empleados, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editarEmpleado:
                Intent intent= new Intent(getApplicationContext(), EditarEmpleadoActivity.class);
                intent.putExtra("empleado",empleado);
                startActivity(intent);
                return true;

            case R.id.action_borrarEmpleado:
                borrarConfirmacion();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void borrarEmpleado(){
        String eid= empleado.getIdEmpresa();
        String uid= empleado.getUid();
        String did= empleado.getIdDepartamento();

        //Inicialización de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference().child(eid);

        //Obtengo el departamento al que pertenecía si lo hacía
        if(!did.equals("-1")){
            dbReference.child("Departamentos").child(did).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Departamento dpt= snapshot.getValue(Departamento.class);
                    //Si el empleado era jefe se deja el departamento sin jefe
                    if(empleado.getNivelJerarquia()<=2){
                        dpt.setUidJefeDepartamento("-1");
                    }
                    //Se elimina de la lista de miembros
                    dpt.eliminarEmpleado(uid);
                    //Se devuelve a la bd
                    dbReference.child("Departamentos").child(did).setValue(dpt);
                    //Se elimina el empleado
                    dbReference.child("Empleados").child(uid).removeValue();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }else{
            //Se elimina el empleado
            dbReference.child("Empleados").child(uid).removeValue();
        }
        Toast.makeText(getApplicationContext(), empleado.getNombreEmpleado() +" "+empleado.getApellidoEmpleado() + " borrado", Toast.LENGTH_LONG).show();
    }

    private void borrarConfirmacion() {
        //Inicialización
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(getApplicationContext());
        alertDialogBu.setTitle("Borrar");
        alertDialogBu.setMessage("¿Seguro que quiere eliminar a " + empleado.getNombreEmpleado() +"? Esta acción no se puede deshacer");

        //Opción positiva
        alertDialogBu.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                borrarEmpleado();
            }
        });
        //Opción negativa
        alertDialogBu.setNegativeButton("Cancelar·", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        //Creación del dialog
        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }

}