package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.Modelos.Empresa;
import com.example.scrummanager.NavigationDrawerActivity;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegistrarseFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;

    Button bt_register;
    private EditText et_nombreEmpresa;
    private EditText et_email;
    private EditText et_password;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrarse, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicialización variables del menú
        et_nombreEmpresa= (EditText)getView().findViewById(R.id.et_nombreEmpresa);
        et_email = (EditText)getView().findViewById(R.id.et_email);
        et_password = (EditText)getView().findViewById(R.id.et_password);
        bt_register= getView().findViewById(R.id.bt_register);

        //Initialización de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Firebase database initialization
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        view.findViewById(R.id.tv_iniciarSesion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RegistrarseFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    //Método ejecutado cuando el usuario pulse el botón de registrar
    private void createNewAccount(){
        String nombreEmpresa = et_nombreEmpresa.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        //El usuario se registra sólo si ha rellenado los tres campos
        if(!TextUtils.isEmpty(nombreEmpresa)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //si no hay error al crear la cuenta se redirige a la actividad principal
                            if (task.isSuccessful()) {
                                //Se le envía un correo de verificación
                                FirebaseUser user = mAuth.getCurrentUser();
                                verifyEmail(user);

                                //Se crea la empresa
                                String eid=UUID.randomUUID().toString();
                                Empresa empresa= new Empresa(eid, nombreEmpresa);
                                dbReference.child(eid).child("NombreEmpresa").setValue(nombreEmpresa);

                                //Se crea el perfil de usuario y se envía a la base de datos
                                String uid = user.getUid();
                                Empleado userObject= new Empleado(uid,email.substring(0,email .indexOf("@")),"");
                                dbReference.child(eid).child("Empleados").child(uid).setValue(userObject);

                                updateUI(user);
                            } else {
                                //Si falla el usuario es informado
                                updateUI(null);
                            }
                        }
                    });
        }

    }

    //Redirige al usuario o muestra un error
    private void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(getContext(),"succes",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), NavigationDrawerActivity.class));
        }else{
            Toast. makeText(getContext(),"error",Toast. LENGTH_SHORT).show();
        }

    }
    //Método que envía el email de verificación
    private void verifyEmail(FirebaseUser user){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "succes", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),"error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}