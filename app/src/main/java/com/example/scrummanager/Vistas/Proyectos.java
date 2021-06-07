package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scrummanager.Controladores.AdaptadorProyectos;
import com.example.scrummanager.Modelos.Proyecto;
import com.example.scrummanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Una subclase de {@link Fragment} simple.
 * Usa el metodo {@link Proyectos#newInstance} para
 * crear una instancia del fragmento.
 * Muestra los proyectos de la empresa
 */
public class Proyectos extends Fragment {
    private RecyclerView recView;
    private ArrayList<Proyecto> proyectos;
    private Proyecto proyecto;
    private Fragment fragmento;
    private ValueEventListener eventListenerProyectos;

    /**
     * Constructor vacío por defecto
     */
    public Proyectos() {
    }

    /**
     * Usa este metodo para crear una nueva instancia de
     * este fragmento
     * @return una nueva instancia del fragmento Proyectos
     */
    public static Proyectos newInstance() {
        Proyectos fragment = new Proyectos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proyectos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        proyectos = new ArrayList<Proyecto>();
        fragmento = this;
        //Recuperación del id de empresa
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String eid = sp.getString("eid", "-1");

        //Inicializaciónd el RecyclerView
        recView = view.findViewById(R.id.rv_proyectos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recView.setLayoutManager(layoutManager);

        //Obtención de los datos
        setEventListenerClientes();
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(eid).child("Proyectos");
        dbReference.addValueEventListener(eventListenerProyectos);
    }

    /**
     * Establece el Event Listener de los clientes y rellena el Recycler View con la lista de los clientes
     */
    private void setEventListenerClientes() {
        eventListenerProyectos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proyectos.clear();
                for (DataSnapshot x : snapshot.getChildren()) {
                    //Se añaden los proyectos a la lista
                    proyecto = x.getValue(Proyecto.class);
                    proyectos.add(proyecto);

                    //Se rellena el recycler view
                    AdaptadorProyectos adaptadorProyectos = new AdaptadorProyectos(proyectos, getContext(), fragmento);
                    recView.setAdapter(adaptadorProyectos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    /**
     * Gestiona las acciones del menú del NavigationDrawer
     * @param item : MenuItem elemento del menu seleccionado
     * @return true si se ha seleccionado un elemente
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAnCli:
                startActivity(new Intent(getContext(), NuevoClienteActivity.class));
                break;
            case R.id.menuAnDept:
                startActivity(new Intent(getContext(), NuevoDepartamentoActivity.class));
                break;
            case R.id.menuAnEmp:
                startActivity(new Intent(getContext(), NuevoEmpleadoActivity.class));
                break;
            case R.id.menuAnProy:
                startActivity(new Intent(getContext(), NuevoProyectoActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gestiona los elementos que serán visibles en el menú
     * @param menu: Menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Se ocultan las opciones del menú que no pertenecen a este fragment
        MenuItem item = menu.findItem(R.id.menuAnTarea);
        if (item != null)
            item.setVisible(false);
        item = menu.findItem(R.id.menuAnSprint);
        if (item != null)
            item.setVisible(false);
    }
}