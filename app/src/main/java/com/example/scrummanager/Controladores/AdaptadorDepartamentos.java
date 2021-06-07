package com.example.scrummanager.Controladores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.Modelos.Empleado;
import com.example.scrummanager.R;
import com.example.scrummanager.Vistas.EditarClienteActivity;
import com.example.scrummanager.Vistas.EditarDepartamentoActivity;
import com.example.scrummanager.Vistas.EditarEmpleadoActivity;
import com.example.scrummanager.Vistas.VerClienteActivity;
import com.example.scrummanager.Vistas.VerDepartamentoActivity;
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

/**
 * Adaptador para rellenar Recycler Views con una lista de departamentos
 */
public class AdaptadorDepartamentos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Departamento> departamentos;
    private Context contexto;
    private final int MOSTRAR_MENU=1, OCULTAR_MENU=2;
    StorageReference storageReference;

    /**
     * Constructor del Adaptador
     * @param departamentos
     * @param contexto
     */
    public AdaptadorDepartamentos(ArrayList<Departamento> departamentos, Context contexto) {
        this.departamentos = departamentos;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==MOSTRAR_MENU ){
            //Se infla la View
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_departamento, parent, false);
            //Se crea el ViewHolder
            return new MenuViewHolder(v);
        }else{
            //Se infla la View
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_departamento, parent, false);
            //Se crea el ViewHolder
            return new AdaptadorDepartamentosViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Departamento departamento= departamentos.get(position);

        String nombreDepartamento= departamento.getNombreDepartamento();
        String did= departamento.getIdDepartamento();

        //Si se está mostrando el Departamento
        if(holder instanceof AdaptadorDepartamentosViewHolder){

            //Se incluye el departamento en el layout
            ((AdaptadorDepartamentosViewHolder) holder).tv_nombreDepartamento.setText(nombreDepartamento.toString());

            //Imagen de departamento
            storageReference= FirebaseStorage.getInstance().getReference();
            if((storageReference.child("departments/"+did+"/cover.jpg")).getDownloadUrl()!=null) {
                StorageReference perfilRef = storageReference.child("departments/" + did + "/cover.jpg");
                perfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(((AdaptadorDepartamentos.AdaptadorDepartamentosViewHolder) holder).iv_fotoDepartamento);
                    }
                });
            }

            //Un click simple muestra el departamento
            ((AdaptadorDepartamentosViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verDepartamento(departamento);
                }
            });

            //Si se mantiene pulsado se abre el menú de opciones
            ((AdaptadorDepartamentosViewHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageView image = ((AdaptadorDepartamentos.AdaptadorDepartamentosViewHolder) holder).iv_fotoDepartamento;
                    Picasso.get().cancelRequest(image);
                    mostrarMenu(position);
                    return true;
                }
            });

        }
        //Si se está mostrando el menú
        if(holder instanceof MenuViewHolder){
            //Se incluye el nombre del departamento en el layout
            ((MenuViewHolder) holder).tv_nombreDepartamento.setText(nombreDepartamento.toString());

            //Se asignan onClickListeners para las opciones del menú
            ((MenuViewHolder) holder).btn_atrasDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarMenu();
                }
            });
            ((MenuViewHolder) holder).btn_verDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verDepartamento(departamento);
                }
            });
            ((MenuViewHolder) holder).btn_editarDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editarDepartamento(departamento);
                }
            });
            ((MenuViewHolder) holder).btn_borrarDepartamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarConfirmacion(departamento);
                }
            });

            //Si se mantiene pulsado se cierra el menú de opciones
            ((MenuViewHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
        try{
            return departamentos.size();
        }catch (NullPointerException e){
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(departamentos.get(position).isShowMenu()){
            return MOSTRAR_MENU;
        }else {
            return OCULTAR_MENU;
        }
    }

    /**
     * ViewHolder del adaptador de departamentos, recoge el layout del item
     * Obtiene los items del layout indicado en el método onCreateViewHolder
     */
    public class AdaptadorDepartamentosViewHolder extends RecyclerView.ViewHolder{
        //items del layout
        private TextView tv_nombreDepartamento;
        private ImageView iv_fotoDepartamento;
        public AdaptadorDepartamentosViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto= itemView.getContext();

            //inicializacón de los elementos del layout
            tv_nombreDepartamento= itemView.findViewById(R.id.tv_nombreDepartamento);
            iv_fotoDepartamento= itemView.findViewById(R.id.iv_fotoDepartamento);

        }
    }

    /**
     * MenuViewHolder del adaptador de departamento, recoge el layout del menú del item
     * Obtiene los items del layout indicado en el método onCreateViewHolder
     */
    public class MenuViewHolder extends RecyclerView.ViewHolder{
        //items del layout
        private ImageButton btn_verDepartamento, btn_editarDepartamento,btn_atrasDepartamento, btn_borrarDepartamento;
        private TextView tv_nombreDepartamento;

        public MenuViewHolder(@NonNull View itemView){
            super(itemView);

            //Se le da al contexto el valor correcto
            contexto= itemView.getContext();

            //inicializacón de los elementos del layout
            btn_verDepartamento= itemView.findViewById(R.id.btn_verDepartamento);
            btn_editarDepartamento= itemView.findViewById(R.id.btn_editarDepartamento);
            btn_atrasDepartamento= itemView.findViewById(R.id.btn_atrasDepartamento);
            btn_borrarDepartamento= itemView.findViewById(R.id.btn_borrarDepartamento);
            tv_nombreDepartamento= itemView.findViewById(R.id.tv_nombreDepartamento);
        }
    }

    /**
     * Cambia a true el atributo de mostrar menú de un item seleccionado
     * @param position
     */
    public void mostrarMenu(int position) {
        for(int i=0; i<departamentos.size(); i++){
            departamentos.get(i).setShowMenu(false);
        }
        departamentos.get(position).setShowMenu(true);
        notifyDataSetChanged();
    }

    /**
     * Cambia a false el atributo de mostrar menú de todos los item
     */
    public void cerrarMenu() {
        for(int i=0; i<departamentos.size(); i++){
            departamentos.get(i).setShowMenu(false);
        }
        notifyDataSetChanged();
    }

    /**
     * Ejecuta la actividad de  Editar departamento pasándole el departamento seleccionado
     * @see EditarDepartamentoActivity
     * @param departamento
     */
    public void editarDepartamento(Departamento departamento){
        Intent intent= new Intent(contexto, EditarDepartamentoActivity.class);
        intent.putExtra("departamento",departamento);
        contexto.startActivity(intent);
    }

    /**
     * Ejecuta la actividad de Ver departamento pasándole el departamento seleccionado
     * @see VerDepartamentoActivity
     * @param departamento
     */
    public void verDepartamento(Departamento departamento){
        Intent intent= new Intent(contexto, VerDepartamentoActivity.class);
        intent.putExtra("departamento",departamento);
        contexto.startActivity(intent);
    }

    /**
     * Abre un alertDialog para confirmar si se desea borrar el departamento
     * @param departamento
     */
    private void borrarConfirmacion(Departamento departamento) {
        //Inicialización
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(contexto);
        alertDialogBu.setTitle("Borrar");
        alertDialogBu.setMessage("¿Seguro que quiere eliminar " + departamento.getNombreDepartamento() +"? Esta acción no se puede deshacer");

        //Opción positiva
        alertDialogBu.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                borrarDepartamento(departamento);
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

    /**
     * Elimina el cliente de la base de datos
     * @param departamento
     */
    public void borrarDepartamento(Departamento departamento){
        //Obtención del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(contexto);
        String eid = sp.getString("eid", "-1");

        //Se extraen los miembros del departamento
        ArrayList<String> miembrosdpt= departamento.getMiembrosDepartamento();

        //Se borra el departamento de la bd
        DatabaseReference dbReference=FirebaseDatabase.getInstance().getReference().child(eid);
        dbReference.child("Departamentos").child(departamento.getIdDepartamento()).removeValue();

        //Saltará al catch si no hay miembros
        try {
            //Se accede a cada miembro en la bd
            for (int i = 0; i < miembrosdpt.size(); i++) {
                String uid = miembrosdpt.get(i);
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

                        Toast.makeText(contexto,"Departamento borrado",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(contexto,"Departamento borrado",Toast.LENGTH_SHORT).show();
        }
    }

}
