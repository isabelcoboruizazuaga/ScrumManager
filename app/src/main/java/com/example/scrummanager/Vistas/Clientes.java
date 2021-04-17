package com.example.scrummanager.Vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Controladores.AdaptadorClientes;
import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.R;

import java.util.ArrayList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Controladores.AdaptadorClientes;
import com.example.scrummanager.Modelos.Cliente;
import com.example.scrummanager.R;

import java.util.ArrayList;

public class Clientes extends Fragment {
     private RecyclerView recView;
     private ArrayList<Cliente> clientes;
     private Cliente cliente;

    public Clientes() {
        // Required empty public constructor
    }

    public static Clientes newInstance() {
        Clientes fragment = new Clientes();
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
        return inflater.inflate(R.layout.fragment_clientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        super.onViewCreated(view, savedInstanceState);
        //Inicialización del recycler view
        recView= view.findViewById(R.id.rv_clientes);
        //Creación del layout y asignación al recycler
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recView.setLayoutManager(layoutManager);

        clientes =new ArrayList<Cliente>();
        cliente = new Cliente("1","Cliente1", "Apellido", "cli@email");
        clientes.add(cliente);
        cliente = new Cliente("1","Cliente2", "Apellido", "cli@email");
        clientes.add(cliente);
        cliente = new Cliente("1","Cliente3", "Apellido", "cli@email");
        clientes.add(cliente);
        cliente = new Cliente("1","Cliente4", "Apellido", "cli@email");
        clientes.add(cliente);
        cliente = new Cliente("1","Cliente5", "Apellido", "cli@email");
        clientes.add(cliente);
        cliente = new Cliente("1","Cliente6", "Apellido", "cli@email");
        clientes.add(cliente);
        cliente = new Cliente("1","Cliente7", "Apellido", "cli@email");
        clientes.add(cliente);

        AdaptadorClientes adaptadorClientes= new AdaptadorClientes(clientes,getContext());
        recView.setAdapter(adaptadorClientes);
    }
}