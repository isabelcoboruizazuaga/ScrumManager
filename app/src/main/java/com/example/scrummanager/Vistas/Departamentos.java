package com.example.scrummanager.Vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.scrummanager.Controladores.AdaptadorDepartamentos;
import com.example.scrummanager.Modelos.Departamento;
import com.example.scrummanager.R;

import java.util.ArrayList;

public class Departamentos extends Fragment {
   private RecyclerView recView;
   private ArrayList<Departamento> departamentos;
   private Departamento departamento;

    public Departamentos() {
        // Required empty public constructor
    }

    public static Departamentos newInstance() {
        Departamentos fragment = new Departamentos();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_departamentos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recView= view.findViewById(R.id.rv_departamentos);
        GridLayoutManager gridlayoutManager= new GridLayoutManager(getContext(),2);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(gridlayoutManager);

        departamentos=new ArrayList<Departamento>();
        departamento= new Departamento("id01","dep1","eid");
        departamentos.add(departamento);
        departamento= new Departamento("id02","dep2","eid");
        departamentos.add(departamento);
        departamento= new Departamento("id03","dep3","eid");
        departamentos.add(departamento);
        departamento= new Departamento("id4","dep4","eid");
        departamentos.add(departamento);
        departamento= new Departamento("asda","dep5","eid");
        departamentos.add(departamento);
        departamento= new Departamento("sad","dep5","eid");
        departamentos.add(departamento);
        departamento= new Departamento("ad","dep6","eid");
        departamentos.add(departamento);

        AdaptadorDepartamentos adaptadorDepartamentos= new AdaptadorDepartamentos(departamentos,getContext());
        recView.setAdapter(adaptadorDepartamentos);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Se ocultan las opciones del menú que no pertenecen a este fragment
        MenuItem item=menu.findItem(R.id.menuCosaInutil);
        if(item!=null)
            item.setVisible(false);
        item=menu.findItem(R.id.menuAnEmp);
        if(item!=null)
            item.setVisible(false);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAnDept:
                startActivity(new Intent(getContext(), NuevoDepartamentoActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}