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
import com.example.scrummanager.NavigationDrawerActivity;
import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarseFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;

    Button bt_register;
    private EditText et_username;
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

        //Layout variables initialization
        et_username= (EditText)getView().findViewById(R.id.et_username);
        et_email = (EditText)getView().findViewById(R.id.et_email);
        et_password = (EditText)getView().findViewById(R.id.et_password);
        bt_register= getView().findViewById(R.id.bt_register);

        //Initialization of Firebase Authentication
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
    //This method will be launched when the user press the Register button
    private void createNewAccount(){
        String name = et_username.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        //If all the text fields are complete the user is register
        if(!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //if the account is created correctly the user is redirected to the main activity
                            if (task.isSuccessful()) {
                                //The user is sent a verification Email
                                FirebaseUser user = mAuth.getCurrentUser();
                                verifyEmail(user);

                                //The user is created and added to the database
                                String uid = user.getUid();
                                Empleado userObject= new Empleado(uid,name,email);
                                dbReference.child("User").child(uid).setValue(userObject);

                                updateUI(user);
                            } else {
                                //if it fails the user is informed
                                updateUI(null);
                            }
                        }
                    });
        }

    }

    //Method to redirect the user to the main activity. If the register failed it won't redirect the user
    private void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(getContext(),"succes",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), NavigationDrawerActivity.class));
        }else{
            Toast. makeText(getContext(),"error",Toast. LENGTH_SHORT).show();
        }

    }
    //Method for the verification of the user email
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