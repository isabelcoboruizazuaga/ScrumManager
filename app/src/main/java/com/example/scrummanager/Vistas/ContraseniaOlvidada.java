package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrummanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

/**
 * Una subclase de {@link Fragment} simple.
 * Usa el metodo {@link InicioSesionFragment#newInstance} para
 * crear una instancia del fragmento.
 * Permite recuperar una contraseña olvidada
*/
public class ContraseniaOlvidada extends Fragment {
    FirebaseAuth auth;
    public ContraseniaOlvidada() {
        // Required empty public constructor
    }

    public static ContraseniaOlvidada newInstance() {
        ContraseniaOlvidada fragment = new ContraseniaOlvidada();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contrasenia_olvidada, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtEmail= view.findViewById(R.id.et_email);
        view.findViewById(R.id.btn_enviarContrasenia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString();
                send(email);
            }
        });
    }

    /**
     * Onclick listener del botón de enviar un correo para restablecer la contraseña
     * @param email
     */
    public void send(String email) {

        if(!TextUtils.isEmpty(email)) {
            auth.sendPasswordResetEmail(email).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    NavHostFragment.findNavController(ContraseniaOlvidada.this)
                            .navigate(R.id.action_contraseniaOlvidada_to_FirstFragment);
                }
            }).addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al enviar el email", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}