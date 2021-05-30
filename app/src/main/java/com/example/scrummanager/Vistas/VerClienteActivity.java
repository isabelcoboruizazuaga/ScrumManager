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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.Modelos.Departamento;
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

import java.util.ArrayList;

public class VerClienteActivity extends AppCompatActivity {
    StorageReference storageReference;
    private Cliente cliente;
    private String nif,eid;
    private Button btn_registrar;
    private TextView tv_nombre, tv_apellidos, tv_telefono, tv_email,tv_dni,tv_tipo;
    private ImageView iv_imagenCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cliente);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Editar cliente");

        //Se recogen los datos del cliente a editar
        Intent intent = getIntent();
        cliente = (Cliente) intent.getSerializableExtra("cliente");
        nif= cliente.getNifCliente();
        eid= cliente.getIdEmpresa();

        //Inicialización variables del
        tv_nombre=findViewById(R.id.tv_nombreCliente);
        tv_apellidos=findViewById(R.id.tv_apellidosCliente);
        tv_email=findViewById(R.id.tv_emailCliente);
        tv_telefono=findViewById(R.id.tv_telefonoCliente);
        tv_dni=findViewById(R.id.tv_dniCliente);
        tv_tipo=findViewById(R.id.tv_tipoCliente);
        iv_imagenCliente=findViewById(R.id.iv_imagenCliente);

        //Se rellenan los campos con los datos originales del empleado
        tv_nombre.setText(cliente.getNombreCliente());
        tv_apellidos.setText(cliente.getApellidoCliente());
        tv_dni.setText(cliente.getNifCliente());
        tv_email.setText(cliente.getEmailCliente());
        tv_telefono.setText(cliente.getTelefonoCliente());

        //Imagen de perfil
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference perfilRef= storageReference.child("clients/"+nif+"/profile.jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(iv_imagenCliente);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ver_clientes, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editarCliente:
                Intent intent= new Intent(getApplicationContext(), EditarClienteActivity.class);
                intent.putExtra("cliente",cliente);
                startActivity(intent);
                return true;

            case R.id.action_borrarCliente:
                borrarConfirmacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void borrarConfirmacion() {
        //Inicialización
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(VerClienteActivity.this);
        alertDialogBu.setTitle("Borrar");
        alertDialogBu.setMessage("¿Seguro que quiere eliminar a " + cliente.getNombreCliente() +" "+ cliente.getApellidoCliente() +"? Esta acción no se puede deshacer");

        //Opción positiva
        alertDialogBu.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                borrarCliente();
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
    public void borrarCliente() {
        //Inicialización de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference().child(eid);

        dbReference.child("Clientes").child(nif).removeValue();

        Toast.makeText(getApplicationContext(), cliente.getNombreCliente() +" "+cliente.getApellidoCliente() + " borrado", Toast.LENGTH_LONG).show();

        finish();
    }
}