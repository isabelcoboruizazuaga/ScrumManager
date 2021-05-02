package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.Modelos.Empresa;
import com.example.scrummanager.NavigationDrawerActivity;
import com.example.scrummanager.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class InicioSesionFragment extends Fragment {
    private static final int RC_GOOGLE_API = 1;
    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    private boolean primeraVez;
    Button bt_login;
    SignInButton bt_googleSingIn;
    EditText et_correo, et_contrasena,et_idEmpresa;
    FirebaseAuth mAuth;
    GoogleSignInClient client_google;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicialización items del layout
        bt_login = getView().findViewById(R.id.bt_iniciarSesion);
        bt_googleSingIn = getView().findViewById(R.id.botonGoogle);
        et_correo = getView().findViewById(R.id.et_email);
        et_contrasena = getView().findViewById(R.id.et_password);
        et_idEmpresa= getView().findViewById(R.id.et_idEmpresa);

        //Inicialización firebase
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();

        //Obtención de las preferencias
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String eid= sp.getString("eid","-1");
        if(eid.equals("-1")){
            primeraVez= true;
            et_idEmpresa.setVisibility(View.VISIBLE);
        }else{
            primeraVez=false;
            et_idEmpresa.setVisibility(View.INVISIBLE);
        }

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = et_correo.getText().toString();
                String contrasena = et_contrasena.getText().toString();
                String idEmpresa= et_idEmpresa.getText().toString();

                if(!TextUtils.isEmpty(correo)&& !TextUtils.isEmpty(contrasena)) {
                    //Si es la primera vez que se inicia sesión se necesitará el id de empresa, si no no es necesario
                    if(!TextUtils.isEmpty(idEmpresa) && primeraVez){
                        //Se añade el id de empresa a las preferencias para no tener que recuperarlo más
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor edit_pref = prefs.edit();
                        edit_pref.putString("eid", idEmpresa);
                        edit_pref.commit();

                        //Si es correcto pasa a la actividad principal
                        if (iniciaSesion(correo, contrasena)) {
                            Toast.makeText(getContext(), idEmpresa, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), NavigationDrawerActivity.class));
                        }
                    }else {
                        //Si es correcto pasa a la actividad principal
                        if (iniciaSesion(correo, contrasena)) {
                            Toast.makeText(getContext(), eid +"aaaaa", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), NavigationDrawerActivity.class));
                        }
                    }
                }
            }
        });

        //Al pulsar el texto de registro pasa a ese fragment
        view.findViewById(R.id.tv_registrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(InicioSesionFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        //Ajustes del botón google
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client_google = GoogleSignIn.getClient(getContext(), gso);

        mAuth = FirebaseAuth.getInstance();

        //Al pulsar el botón pasa al método de actividad
        bt_googleSingIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent singIntent = client_google.getSignInIntent();
                startActivityForResult(singIntent, RC_GOOGLE_API);
            }
        });
    }

    Boolean exito = false;
    //Si el inicio de sesión es correcto devuelve true
    public boolean iniciaSesion(String correo, String contrasena){
        //TODO CARGAR EMPRESAID
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        exito = task.isSuccessful();
                    }
                }
        );
        return exito;
    }

    //Método que se activa tras pulsar el botón de google
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Comprueba que haya sido activado por el botón
        if(requestCode == RC_GOOGLE_API){
            Task<GoogleSignInAccount> gTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = gTask.getResult(ApiException.class);
                if(account != null){
                    mAuth.signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null));
                    if(mAuth.getCurrentUser() != null){
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();

                        //Si el usuario existe no lo modifica, si no existe añade los datos nuevos
                        dbReference.child("ad96328d-ce92-4da7-88cf-b7ff81d0e6d3").child("Empleados").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    startActivity(new Intent(getContext(),NavigationDrawerActivity.class));
                                    Toast.makeText(getContext(),"ok", Toast.LENGTH_LONG).show();
                                } else {
                                    //Se crea la empresa
                                    String eid="ad96328d-ce92-4da7-88cf-b7ff81d0e6d3";//UUID.randomUUID().toString();
                                    Empresa empresa= new Empresa(eid, "Empresisa");
                                    dbReference.child(eid).child("NombreEmpresa").setValue("Empresisa");


                                    //Se añade a las preferencias para no tener que recuperarlo más
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                    SharedPreferences.Editor edit_pref = prefs.edit();
                                    edit_pref.putString("eid", eid);
                                    edit_pref.commit();

                                    //String uid, String nombreEmpleado, String apellidoEmpleado, String idEmpresa
                                    Empleado userObject= new Empleado(uid,user.getDisplayName(),"",eid);

                                    dbReference.child(eid).child("Empleados").child(uid).setValue(userObject);
                                    startActivity(new Intent(getContext(),NavigationDrawerActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                } else{
                    Toast.makeText(getContext(),"error", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}