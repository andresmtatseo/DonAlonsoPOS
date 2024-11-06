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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;

public class EditarProducto extends Fragment {

    private static final String KEY_PRODUCTO = "producto";
    private Producto producto;

    private EditText etNombreProducto, etPrecio, etDescripcion, etCantidadMinima;
    private Spinner spCategoria;

    public EditarProducto() {
        // Required empty public constructor
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

        // Verificar y cargar los datos del producto en los campos
        if (producto != null) {
            etNombreProducto.setText(producto.getNombre());
            etPrecio.setText(String.valueOf(producto.getPrecio()));
            etDescripcion.setText(producto.getDescripcion());
            etCantidadMinima.setText(String.valueOf(producto.getCantidadMinima()));
            // Configura el spinner spCategoria aquí, si tienes un adaptador para categorías
        }

        // Configurar el botón de actualización
        Button btnActualizar = view.findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(v -> validarYActualizarProducto());

        return view;
    }

    // Método para validar los campos y actualizar el producto
    private void validarYActualizarProducto() {
        String nombre = etNombreProducto.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String cantidadMinimaStr = etCantidadMinima.getText().toString().trim();

        // Validar campos vacíos
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
        if (TextUtils.isEmpty(descripcion)) {
            etDescripcion.setError("La descripción es requerida");
            etDescripcion.requestFocus();
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

        // Si todas las validaciones pasan, puedes actualizar el producto
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setCantidadMinima(cantidadMinima);

        // Aquí puedes llamar a tu lógica para actualizar el producto, como guardarlo en la base de datos

        Toast.makeText(getContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();

        // Volver a la pantalla anterior o realizar otra acción
        requireActivity().onBackPressed();
    }
}
