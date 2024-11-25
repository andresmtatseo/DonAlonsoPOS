package com.example.donalonsopos.ui.reportes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import com.example.donalonsopos.R;
import com.itextpdf.text.pdf.*;

public class ReportesFragment extends Fragment {

    private Spinner spReportes;
    private Spinner spVentas;
    private Spinner spCompras;
    private Spinner spProductos;
    private TextView tvFechaInicio;
    private TextView tvFechaFin;
    private Button btnGenerarReporte;

    public ReportesFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        View view = inflater.inflate(R.layout.fragment_reportes, container, false);

        spReportes = view.findViewById(R.id.spReportes);
        spVentas = view.findViewById(R.id.spVentas);
        spCompras = view.findViewById(R.id.spCompras);
        spProductos = view.findViewById(R.id.spProductos);
        tvFechaInicio = view.findViewById(R.id.tvFechaInicio);
        tvFechaFin = view.findViewById(R.id.tvFechaFin);
        btnGenerarReporte = view.findViewById(R.id.btnGenerarReporte);

        // Configurar el Spinner principal con opciones
        ArrayAdapter<CharSequence> adapterReportes = ArrayAdapter.createFromResource(getContext(),
                R.array.modulo_reportes, android.R.layout.simple_spinner_item);
        adapterReportes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReportes.setAdapter(adapterReportes);

        // Configurar el comportamiento del Spinner principal
        spReportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                spVentas.setVisibility(View.GONE);
                spCompras.setVisibility(View.GONE);
                spProductos.setVisibility(View.GONE);
                tvFechaInicio.setVisibility(View.GONE);
                tvFechaFin.setVisibility(View.GONE);

                switch (selected) {
                    case "Ventas":
                        spVentas.setVisibility(View.VISIBLE);
                        break;
                    case "Compras":
                        spCompras.setVisibility(View.VISIBLE);
                        break;
                    case "Productos":
                        spProductos.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Configurar el comportamiento de los Spinners secundarios
        setupSecondarySpinner(spVentas, R.array.ventas_reportes);
        setupSecondarySpinner(spCompras, R.array.compras_reportes);
        setupSecondarySpinner(spProductos, R.array.productos_reportes);

        return view;
    }

    private void setupSecondarySpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.contains("fecha")) {
                    tvFechaInicio.setVisibility(View.VISIBLE);
                    tvFechaFin.setVisibility(View.VISIBLE);
                } else {
                    tvFechaInicio.setVisibility(View.GONE);
                    tvFechaFin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }
}

