package com.example.donalonsopos.ui.reportes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.donalonsopos.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class ReportesFragment extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    Spinner seleccion;
    private Button btnGenerarReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reportes);


        seleccion = findViewById(R.id.spinner_r);
        btnGenerarReporte = findViewById(R.id.btnGenerarReporte);

        // Configurar el Spinner con opciones
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.spinner_reportes, android.R.layout.simple_spinner_item);

        seleccion.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configurar el comportamiento del Spinner

        seleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                switch (selected) {
                    case "Clientes": showDialog(new DialogFiltrosClientesFragment());
                    break;
                    case "Productos": // Muestra el diálogo para productos showDialog(new DialogFiltrosProductosFragment()); break;
                    case "Ventas": // Muestra el diálogo para ventas showDialog(new DialogFiltrosVentasFragment()); break; } }

        // Configurar el botón para generar el reporte



            }
        }

            private void showDialog(DialogFiltrosClientesFragment dialogFiltrosClientesFragment) {
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { // No hacer nada
                } });
    }

}
