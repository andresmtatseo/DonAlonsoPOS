package com.example.donalonsopos.ui.reportes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.donalonsopos.R;


public class ReportesFragment extends AppCompatActivity {
    Spinner seleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reportes);


        seleccion = findViewById(R.id.spinner_r); // Configurar el Spinner con opciones
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.spinner_reportes, android.R.layout.simple_spinner_item);

        seleccion.setAdapter(adapter);
        }
}