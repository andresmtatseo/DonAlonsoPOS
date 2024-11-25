package com.example.donalonsopos.ui.reportes;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.donalonsopos.R;

public class ReportesFragment extends Fragment {

    private Spinner spinner;

    public ReportesFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        View view = inflater.inflate(R.layout.fragment_reportes, container, false);

        spinner = view.findViewById(R.id.spinner_r);

        // Configurar el Spinner con opciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.modulo_reportes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Configurar el comportamiento del Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                switch (selected) {
                    case "Clientes":
                        showDialog(new DialogFiltrosClientesFragment());
                        break;
                    case "Productos":
                        showDialog(new DialogFiltrosProductosFragment());
                        break;
                    case "Ventas":
                        showDialog(new DialogFiltrosVentasFragment());
                        break;
                    case "Compras":
                        showDialog(new DialogFiltrosComprasFragment());
                        break;
                    case "Proveedores":
                        showDialog(new DialogFiltrosProveedoresFragment());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        return view;
    }

    private void showDialog(DialogFragment dialogFragment) {
        dialogFragment.show(getParentFragmentManager(), "dialog");
    }
}

