package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
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

/**
 * Una subclase de {@link Fragment} simple.
 * Usa el metodo {@link InicioSesionFragment#newInstance} para
 * crear una instancia del fragmento.
 * Permite iniciar sesión en la aplicación
 */
public class InicioSesionFragment extends Fragment {
    Button bt_login;
    EditText et_correo, et_contrasena,et_idEmpresa;

    private DatabaseReference dbReference;
    private FirebaseDatabase database;
    FirebaseAuth mAuth;

    private boolean eidCorrecto, exito= false;

    /**
     * Constructor vacío por defecto
     */
    public InicioSesionFragment() {
    }

    /**
     * Usa este metodo para crear una nueva instancia de
     * este fragmento
     * @return una nueva instancia del fragmento InicioSesionFragment
     */
    public static InicioSesionFragment newInstance() {
        InicioSesionFragment fragment = new InicioSesionFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicialización items del layout
        bt_login = view.findViewById(R.id.bt_iniciarSesion);
        et_correo = view.findViewById(R.id.et_email);
        et_contrasena = view.findViewById(R.id.et_password);
        et_idEmpresa= view.findViewById(R.id.et_idEmpresa);

        //Inicialización de la base de datos
        database= FirebaseDatabase.getInstance();
        dbReference=database.getReference();

        boolean primeraVez= evaluaPreferencias();

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIniciarSesion(primeraVez);
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
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Recupera las preferencias y analiza si es la primera vez que se inicia sesión desde el dispositivo
     * @return primeraVez: boolean
     */
    private boolean evaluaPreferencias(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String eid= sp.getString("eid","-1");
        String email= sp.getString("email","");
        boolean primeraVez;

        if(eid.equals("-1")){
            primeraVez= true;
            et_idEmpresa.setVisibility(View.VISIBLE);
            return primeraVez;
        }else{
            primeraVez=false;
            et_idEmpresa.setText(eid);
            et_correo.setText(email);
            return primeraVez;
        }
    }

    /**
     * Se dirige a la aplicación o se redirige a guardar las preferencias según sea o no la primera vez
     * @param primeraVez : Boolean
     */
    private void clickIniciarSesion(Boolean primeraVez){
        String correo = et_correo.getText().toString();
        String contrasena = et_contrasena.getText().toString();
        String idEmpresa= et_idEmpresa.getText().toString();

        //Si es la primera vez que se inicia sesión se necesitará el id de empresa, si no no es necesario
        if(primeraVez){
            inicioPrimeraVez(correo,contrasena,idEmpresa);
        } else{
            //La contraseña y correo no deben estar vacíos
            if(!TextUtils.isEmpty(correo)&& !TextUtils.isEmpty(contrasena)) {
                //Si el inicio de sesión es correcto pasa a la actividad principal
                if (iniciaSesion(correo, contrasena)) {
                    startActivity(new Intent(getContext(), NavigationDrawerActivity.class));
                }
            }
        }
    }

    /**
     *Inicia sesión
     * @param correo  email del empleado que ha sido registrado
     * @param contrasena contraseña del empleado
     * @return exito: Boolean
     */
    public boolean iniciaSesion(String correo, String contrasena){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Se añade el id de empresa a las preferencias para no tener que recuperarlo más, igual con el correo
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor edit_pref = prefs.edit();
                        edit_pref.putString("email", correo);
                        edit_pref.commit();
                        exito = task.isSuccessful();
                    }
                }
        );
        return exito;
    }

    /**
     *
     * @param correo email del empleado que ha sido registrado
     * @param contrasena contraseña del empleado
     * @param idEmpresa id de la empresa en la que trabaja del empleado
     */
    private void inicioPrimeraVez(String correo, String contrasena, String idEmpresa){
        //La contraseña y correo no deben estar vacíos
        if(!TextUtils.isEmpty(correo)&& !TextUtils.isEmpty(contrasena)) {
            //Si el inicio de sesión es correcto comprueba que la empresa también lo sea
            if (iniciaSesion(correo, contrasena)) {
                if(!TextUtils.isEmpty(idEmpresa)){
                   String uid =FirebaseAuth.getInstance().getUid();
                    //Se comprueba que el id de empresa sea correcto y el usuario esté dentro
                    dbReference.child(idEmpresa).child("Empleados").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                //Se añade el id de empresa a las preferencias para no tener que recuperarlo más, igual con el correo
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                SharedPreferences.Editor edit_pref = prefs.edit();
                                edit_pref.putString("eid", idEmpresa);
                                edit_pref.putString("email", correo);
                                edit_pref.commit();

                                startActivity(new Intent(getContext(), NavigationDrawerActivity.class));
                            }else {
                                Toast.makeText(getContext(),"El id de empresa no es correcto",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("onDataChange", "Error!", databaseError.toException());
                        }
                    });
                }
            }else{
                Toast.makeText(getContext(),"Error al iniciar sesión",Toast.LENGTH_SHORT).show();
            }
        }
    }
}