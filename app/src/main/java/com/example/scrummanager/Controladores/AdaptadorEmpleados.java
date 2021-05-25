package com.example.scrummanager.Controladores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.example.scrummanager.Vistas.EditarEmpleadoActivity;
import com.example.scrummanager.Vistas.VerEmpleadoActivity;
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

public class AdaptadorEmpleados extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Empleado> empleados;
    private Context contexto;
    private final int MOSTRAR_MENU = 1, OCULTAR_MENU = 2;
    StorageReference storageReference;
    Fragment fragment;


    public AdaptadorEmpleados(ArrayList<Empleado> empleados, Context contexto, Fragment fragment) {
        this.empleados = empleados;
        this.contexto = contexto;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == MOSTRAR_MENU) {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_empleado, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorEmpleados.MenuViewHolder(v);
        } else {
            //Se infla la View
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empleado, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorEmpleados.AdaptadorEmpleadosViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Empleado empleado = empleados.get(position);

        String nombreEmpleado = empleado.getNombreEmpleado();
        String apellidoEmpleado = empleado.getApellidoEmpleado();
        String emailEmpleado = empleado.getEmailEmpleado();
        String departamentoEmpleado = empleado.getIdDepartamento();
        String eid= empleado.getIdEmpresa();

        //Si se está mostrando el Empleado
        if (holder instanceof AdaptadorEmpleadosViewHolder) {
            //Se obtiene el nombre de departamento y se muestra
           try {
               DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(eid).child("Departamentos").child(departamentoEmpleado);
               dbReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       //Se añaden el departamento
                       Departamento departamento = snapshot.getValue(Departamento.class);
                       try {
                           String did = departamento.getIdDepartamento();
                           if (departamentoEmpleado.equals("-1")) {
                               ((AdaptadorEmpleadosViewHolder) holder).tv_departamentoEmpleado.setText("Sin departamento");
                           } else {
                               String dptNombre = departamento.getNombreDepartamento();
                               ((AdaptadorEmpleadosViewHolder) holder).tv_departamentoEmpleado.setText(dptNombre);
                           }
                       } catch (Exception e) {
                           ((AdaptadorEmpleadosViewHolder) holder).tv_departamentoEmpleado.setText("Sin departamento");
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                   }
               });
           }catch (NullPointerException e){}

            //Se incluye el empleado en el layout
            ((AdaptadorEmpleadosViewHolder) holder).tv_nombreEmpleado.setText(nombreEmpleado);
            ((AdaptadorEmpleadosViewHolder) holder).tv_apellidoEmpleado.setText(apellidoEmpleado);
            ((AdaptadorEmpleadosViewHolder) holder).tv_emailEmpleado.setText(emailEmpleado);

            //Imagen de perfil
            storageReference= FirebaseStorage.getInstance().getReference();
            StorageReference perfilRef= storageReference.child("users/"+empleado.getUid()+"/profile.jpg");
            perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(((AdaptadorEmpleadosViewHolder) holder).iv_fotoEmpleado);
                }
            });

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorEmpleadosViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mostrarMenu(position);
                    return true;
                }
            });
            //Si se hace un click simple se muestra el contenido
            ((AdaptadorEmpleadosViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(contexto, VerEmpleadoActivity.class);
                    intent.putExtra("empleado",empleado);
                    contexto.startActivity(intent);
                }
            });


        }
        //Si se está mostrando el menú
        if (holder instanceof MenuViewHolder) {
            //Se asignan onClickListeners para las opciones del menú
            ((MenuViewHolder) holder).btn_atrasEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((MenuViewHolder) holder).btn_verEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verEmpleado(empleado);
                }
            });
            ((MenuViewHolder) holder).btn_editarEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarEmpleado(empleado);
                }
            });
            ((MenuViewHolder) holder).btn_borrarEmpleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarConfirmacion(v, empleado);
                }
            });

            //Si se mantiene pulsado se cierra el menú de opciones
            ((MenuViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    cerrarMenu();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        try {
            return empleados.size();
        }catch (NullPointerException e){
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (empleados.get(position).isShowMenu()) {
            return MOSTRAR_MENU;
        } else {
            return OCULTAR_MENU;
        }
    }

    public class AdaptadorEmpleadosViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private TextView tv_nombreEmpleado,tv_apellidoEmpleado,tv_emailEmpleado,tv_departamentoEmpleado;
        private ImageView iv_fotoEmpleado;

        public AdaptadorEmpleadosViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreEmpleado = itemView.findViewById(R.id.tv_nombreEmpleado);
            tv_apellidoEmpleado = itemView.findViewById(R.id.tv_apellidoEmpleado);
            tv_emailEmpleado = itemView.findViewById(R.id.tv_emailEmpleado);
            tv_departamentoEmpleado = itemView.findViewById(R.id.tv_departamentoEmpleado);
            iv_fotoEmpleado= itemView.findViewById(R.id.iv_fotoEmpleado);

        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        //items del layout
        private ImageButton btn_verEmpleado, btn_atrasEmpleado, btn_borrarEmpleado, btn_editarEmpleado;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto = itemView.getContext();

            //inicializacón de los elementos del layout
            btn_verEmpleado = itemView.findViewById(R.id.btn_verEmpleado);
            btn_atrasEmpleado = itemView.findViewById(R.id.btn_atrasEmpleado);
            btn_borrarEmpleado = itemView.findViewById(R.id.btn_borrarEmpleado);
            btn_editarEmpleado = itemView.findViewById(R.id.btn_editarEmpleado);
        }
    }

    public void mostrarMenu(int position) {
        for (int i = 0; i < empleados.size(); i++) {
            empleados.get(i).setShowMenu(false);
        }
        empleados.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }


    public boolean isMenuMostrado() {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).isShowMenu()) {
                return true;
            }
        }
        return false;
    }

    public void cerrarMenu() {
        for (int i = 0; i < empleados.size(); i++) {
            empleados.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    public void editarEmpleado(Empleado empleado) {
        Intent intent= new Intent(contexto, EditarEmpleadoActivity.class);
        intent.putExtra("empleado",empleado);
        contexto.startActivity(intent);
    }

    public void verEmpleado(Empleado empleado) {
        Intent intent= new Intent(contexto, VerEmpleadoActivity.class);
        intent.putExtra("empleado",empleado);
        contexto.startActivity(intent);
    }

    private void borrarConfirmacion(View view, Empleado empleado) {
        //Inicialización
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(contexto);
        alertDialogBu.setTitle("Borrar");
        alertDialogBu.setMessage("¿Seguro que quiere eliminar a " + empleado.getNombreEmpleado() +"? Esta acción no se puede deshacer");

        //Opción positiva
        alertDialogBu.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                borrarEmpleado(empleado);
            }
        });
        //Opción negativa
        alertDialogBu.setNegativeButton("Cancelar·", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(contexto, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        //Creación del dialog
        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }
    public void borrarEmpleado(Empleado empleado) {
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
        Toast.makeText(contexto, empleado.getNombreEmpleado() +" "+empleado.getApellidoEmpleado() + " borrado", Toast.LENGTH_LONG).show();
    }
}