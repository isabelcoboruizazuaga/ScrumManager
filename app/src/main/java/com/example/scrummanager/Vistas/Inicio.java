package com.example.scrummanager.Vistas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.Modelos.Sprint;
import com.example.scrummanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {

    public Inicio() {
        // Required empty public constructor
    }
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getArguments().getSerializable("sprint")!=null){
            Sprint sprint = (Sprint) getArguments().getSerializable("sprint");
            System.out.println(sprint.getNombre().toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }
}