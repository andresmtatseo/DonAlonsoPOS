package com.example.donalonsopos.ui.reportes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.donalonsopos.R;


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
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_item);

        seleccion.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configurar el comportamiento del Spinner

        seleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                switch (selected) {
                    case "Ventas": showDialog(new DialogFiltrosVentasFragment());
                    break;
                    case "Productos": // Muestra el diálogo para productos showDialog(new DialogFiltrosProductosFragment()); break;
                    case "Clientes": showDialog(new DialogFiltrosVentasFragment());
                    break; } }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            };

        // Configurar el botón para generar el reporte



            });
        }

            private void showDialog(DialogFragment dialogFragment) {
                dialogFragment.show(getSupportFragmentManager(), "dialog"); }


    }


