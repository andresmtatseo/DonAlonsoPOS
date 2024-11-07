package com.example.donalonsopos.ui.productos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.util.Utils;

public class AgregarProducto extends DialogFragment {

    private EditText etNombreProducto, etPrecio, etCantidadMinima, etDescripcion;
    private Spinner spCategoria;
    private Button btnGuardar, btnLimpiar;
    private ImageButton btnCerrar;

    public AgregarProducto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_producto, container, false);

        initializeViews(view);
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        etNombreProducto = view.findViewById(R.id.etNombreProducto);
        etPrecio = view.findViewById(R.id.etPrecio);
        etCantidadMinima = view.findViewById(R.id.etCantidadMinima);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        spCategoria = view.findViewById(R.id.spCategoria);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
        btnCerrar = view.findViewById(R.id.btnCerrar);
    }

    private void setupListeners() {
        btnGuardar.setOnClickListener(v -> guardarProducto());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
        btnCerrar.setOnClickListener(v -> dismiss());
    }

    private void guardarProducto() {
        String nombre = etNombreProducto.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String cantidadMinima = etCantidadMinima.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spCategoria.getSelectedItem() != null ? spCategoria.getSelectedItem().toString().trim() : "";

        if (nombre.isEmpty()) {
            etNombreProducto.setError("El nombre del producto es obligatorio");
            return;
        }

        if (precio.isEmpty()) {
            etPrecio.setError("El precio es obligatorio");
            return;
        }
        try {
            double precioDouble = Double.parseDouble(precio);
            if (precioDouble <= 0) {
                etPrecio.setError("El precio debe ser mayor que cero");
                return;
            }
        } catch (NumberFormatException e) {
            etPrecio.setError("El precio debe ser un número válido");
            return;
        }

        if (cantidadMinima.isEmpty()) {
            etCantidadMinima.setError("La cantidad mínima es obligatoria");
            return;
        }
        try {
            int cantidadMinimaInt = Integer.parseInt(cantidadMinima);
            if (cantidadMinimaInt <= 0) {
                etCantidadMinima.setError("La cantidad mínima debe ser mayor que cero");
                return;
            }
        } catch (NumberFormatException e) {
            etCantidadMinima.setError("La cantidad mínima debe ser un número válido");
            return;
        }

        // Validar la categoría (aunque el spinner debería tener opciones válidas)
        if (categoria.isEmpty()) {
            Toast.makeText(getContext(), "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí agregamos la lógica para guardar el producto (en base de datos o en memoria)
        Toast.makeText(getContext(), "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void limpiarCampos() {
        etNombreProducto.setText("");
        etPrecio.setText("");
        etCantidadMinima.setText("");
        etDescripcion.setText("");
        spCategoria.setSelection(0);
    }
}