package com.example.scrummanager.Controladores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.example.scrummanager.Vistas.VerEmpleadoActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorEmpleadosDpt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Empleado> empleados;
    private Context contexto;
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;
    StorageReference storageReference;
    Fragment fragment;

    public AdaptadorEmpleadosDpt(ArrayList<Empleado> empleados, Context contexto) {
        this.empleados = empleados;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se infla la View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleado_dpt, parent, false);
        //Se crea el ViewHolder
        return new AdaptadorEmpleadosDpt.AdaptadorEmpleadosDptViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Empleado empleado = empleados.get(position);

        String nombreEmpleado = empleado.getNombreEmpleado();
        String apellidoEmpleado = empleado.getApellidoEmpleado();
        String nombre= nombreEmpleado + " " + apellidoEmpleado;

        //Se incluye el empleado en el layout
        ((AdaptadorEmpleadosDptViewHolder) holder).tv_nombreEmpleadoDpt.setText(nombre);
        if(empleado.getNivelJerarquia()<=2){
            ((AdaptadorEmpleadosDptViewHolder) holder).tv_nombreEmpleadoDpt.setTextColor(contexto.getResources().getColor(R.color.teal_700));
        }

        //Imagen de perfil
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference perfilRef= storageReference.child("users/"+empleado.getUid()+"/profile.jpg");
        perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into((( AdaptadorEmpleadosDpt.AdaptadorEmpleadosDptViewHolder) holder).iv_fotoEmpleadoDpt);
            }
        });
        //Si se hace un click simple se muestra el contenido
        ((AdaptadorEmpleadosDpt.AdaptadorEmpleadosDptViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(contexto, VerEmpleadoActivity.class);
                intent.putExtra("empleado",empleado);
                contexto.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return empleados.size();
        }catch (NullPointerException e){
            return 0;
        }
    }

    public class AdaptadorEmpleadosDptViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_nombreEmpleadoDpt;
        private ImageView iv_fotoEmpleadoDpt;

        public AdaptadorEmpleadosDptViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreEmpleadoDpt = itemView.findViewById(R.id.tv_nombreEmpleadoDpt);
            iv_fotoEmpleadoDpt= itemView.findViewById(R.id.iv_fotoEmpleadoDpt);

        }
    }
}
