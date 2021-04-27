package com.example.scrummanager.Vistas;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scrummanager.R;

public class InicialFragment extends Fragment {

    public InicialFragment() {
        // Required empty public constructor
    }


    public static InicialFragment newInstance(String param1, String param2) {
        InicialFragment fragment = new InicialFragment();
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
        return inflater.inflate(R.layout.fragment_inicial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*SharedPreferences preferences = getContext().getPreferences(MODE_PRIVATE);
        boolean skipWelcome = preferences.getBoolean("skipWelcome", false);

        if (skipWelcome) {
            this.finish();
        }*/
    }
}