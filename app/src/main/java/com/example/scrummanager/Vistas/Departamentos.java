package com.example.scrummanager.Vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recView.setLayoutManager(gridlayoutManager);

        departamentos=new ArrayList<Departamento>();
        departamento= new Departamento("id01","dep1");
        departamentos.add(departamento);
        departamento= new Departamento("id02","dep2");
        departamentos.add(departamento);
        departamento= new Departamento("id03","dep3");
        departamentos.add(departamento);
        departamento= new Departamento("id4","dep4");
        departamentos.add(departamento);
        departamento= new Departamento("asda","dep5");
        departamentos.add(departamento);
        departamento= new Departamento("sad","dep5");
        departamentos.add(departamento);
        departamento= new Departamento("ad","dep6");
        departamentos.add(departamento);

        AdaptadorDepartamentos adaptadorDepartamentos= new AdaptadorDepartamentos(departamentos,getContext());
        recView.setAdapter(adaptadorDepartamentos);
    }
}