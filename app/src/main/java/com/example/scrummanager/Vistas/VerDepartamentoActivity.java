package com.example.scrummanager.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrummanager.Controladores.AdaptadorEmpleadosDpt;
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

import java.util.ArrayList;
import java.util.EventListener;

public class VerDepartamentoActivity extends AppCompatActivity {
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    StorageReference storageReference;

    private RecyclerView recView;
    private ArrayList<Empleado> empleados;
    ArrayList<String> miembrosDpt;
    private Departamento departamento;
    private String did,eid;

    private TextView tv_nombreDepartamento;
    private ImageView iv_imagenDepartamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_departamento);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Ver Departamento");

        //Se recogen los datos del departamento a editar
        Intent intent = getIntent();
        departamento = (Departamento) intent.getSerializableExtra("departamento");
        did = departamento.getIdDepartamento();
        miembrosDpt = departamento.getMiembrosDepartamento();

        //Inicialización variables del layout
        tv_nombreDepartamento = findViewById(R.id.tv_nombreDepartamento);
        iv_imagenDepartamento = findViewById(R.id.iv_imagenDepartamento);
        tv_nombreDepartamento.setText(departamento.getNombreDepartamento());

        //Obtención del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        eid = sp.getString("eid", "-1");

        //Inicialización de firebaseDatabase
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference().child(eid);
        storageReference = FirebaseStorage.getInstance().getReference();

        //Imagen de perfil
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference perfilRef = storageReference.child("departments/" + did + "/cover.jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(iv_imagenDepartamento);
            }
        });

        //Inicialización del recycler view
        recView = findViewById(R.id.rv_empleadosDpt);
        //Creación del layout y asignación al recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recView.setLayoutManager(layoutManager);

        //Extracción de los datos de empleados de la base de datos
        empleados = new ArrayList<>();
        //Se recorre la lista de miembros y se extraen los datos de cada uno para rellenar el recycler view
        try {
            for (int i = 0; i < miembrosDpt.size(); i++) {
                String uid = miembrosDpt.get(i);
                dbReference.child("Empleados").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Empleado empld = snapshot.getValue(Empleado.class);
                        empleados.add(empld);

                        AdaptadorEmpleadosDpt adaptadorEmpleados = new AdaptadorEmpleadosDpt(empleados, getApplicationContext());
                        recView.setAdapter(adaptadorEmpleados);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        } catch (NullPointerException e){

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ver_departamentos, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editarDepartamento:
                Intent intent= new Intent(getApplicationContext(), EditarDepartamentoActivity.class);
                intent.putExtra("departamento",departamento);
                startActivity(intent);
                return true;

            case R.id.action_borrarDepartamento:
                borrarDepartamento();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void borrarDepartamento(){
        //Se borra el departamento de la bd
        DatabaseReference dbReference=FirebaseDatabase.getInstance().getReference().child(eid);
        dbReference.child("Departamentos").child(departamento.getIdDepartamento()).removeValue();

        try {
            //Se accede a cada miembro en la bd
            for (int i = 0; i < miembrosDpt.size(); i++) {
                String uid = miembrosDpt.get(i);
                dbReference.child("Empleados").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Se actualiza para no pertenecer ya al departamento y no ser jefe si lo era
                        Empleado empld = snapshot.getValue(Empleado.class);
                        empld.setIdDepartamento("-1");
                        if(empld.getNivelJerarquia()==2){
                            empld.setNivelJerarquia(4);
                        }
                        //Se devuelve a la bd
                        dbReference.child("Empleados").child(uid).setValue(empld);

                        Toast.makeText(getApplicationContext(),"Departamento borrado",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Departamento borrado",Toast.LENGTH_SHORT);
        }
    }
}