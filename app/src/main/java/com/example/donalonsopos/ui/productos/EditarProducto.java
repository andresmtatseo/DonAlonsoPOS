package com.example.donalonsopos.ui.productos;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;

public class EditarProducto extends Fragment {

    private static final String KEY_PRODUCTO = "producto";
    private Producto producto;

    private EditText etNombreProducto, etPrecio, etDescripcion, etCantidadMinima;
    private Button btnActualizar;
    private Spinner spCategoria;

    public EditarProducto() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            producto = (Producto) getArguments().getSerializable(KEY_PRODUCTO);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_producto, container, false);

        // Inicializar los EditText y Spinner
        etNombreProducto = view.findViewById(R.id.etNombreProducto);
        etPrecio = view.findViewById(R.id.etPrecio);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etCantidadMinima = view.findViewById(R.id.etCantidadMinima);
        spCategoria = view.findViewById(R.id.spCategoria);

        // Cargar datos del producto si existen
        if (producto != null) {
            etNombreProducto.setText(producto.getNombre());
            etPrecio.setText(String.valueOf(producto.getPrecio()));
            etDescripcion.setText(producto.getDescripcion());
            etCantidadMinima.setText(String.valueOf(producto.getCantidadMinima()));
            // Configurar spCategoria si tienes un adaptador de categorías
        }

        // Configurar el botón de actualización
        btnActualizar = view.findViewById(R.id.btnActualizarProducto);
        btnActualizar.setOnClickListener(v -> validarYActualizarProducto());

        return view;
    }

    // Método para validar campos y actualizar el producto
    private void validarYActualizarProducto() {
        String nombre = etNombreProducto.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String cantidadMinimaStr = etCantidadMinima.getText().toString().trim();

        // Validar campos vacíos obligatorios
        if (TextUtils.isEmpty(nombre)) {
            etNombreProducto.setError("El nombre es requerido");
            etNombreProducto.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(precioStr)) {
            etPrecio.setError("El precio es requerido");
            etPrecio.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(cantidadMinimaStr)) {
            etCantidadMinima.setError("La cantidad mínima es requerida");
            etCantidadMinima.requestFocus();
            return;
        }

        // Validar valores numéricos
        double precio;
        int cantidadMinima;
        try {
            precio = Double.parseDouble(precioStr);
            cantidadMinima = Integer.parseInt(cantidadMinimaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Precio o cantidad mínima inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar el producto
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);  // Descripción opcional, actualizada solo si se proporciona
        producto.setCantidadMinima(cantidadMinima);

        Toast.makeText(getContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();

        // Regresar a la pantalla anterior o realizar otra acción
        requireActivity().onBackPressed();
    }
}