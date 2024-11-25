package com.example.donalonsopos.ui.productos;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.CategoriaDaoImpl;
import com.example.donalonsopos.data.DAO.MovimientoProductoDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DTO.Categoria;
import com.example.donalonsopos.data.DTO.MovimientoProducto;
import com.example.donalonsopos.data.DTO.Producto;

import java.util.Date;
import java.util.List;

public class EditarProducto extends Fragment {

    private static final String KEY_PRODUCTO = "producto";
    private Producto producto;

    private EditText etNombreProducto, etPrecio, etDescripcion, etCantidadMinima, etCantidadActual;
    private Button btnActualizar, btnHabilitarCantidadActual;
    private Spinner spCategoria, spMotivo;

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
        spMotivo = view.findViewById(R.id.spMotivo);
        etCantidadActual = view.findViewById(R.id.etCantidadActual);
        btnActualizar = view.findViewById(R.id.btnActualizarProducto);
        btnHabilitarCantidadActual = view.findViewById(R.id.btnHabilitarCantidadActual);

        etCantidadActual.setEnabled(false);
        spMotivo.setEnabled(false);

        // Cargar categorías desde la base de datos
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, cargarCategorias());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);

        // Cargar datos del producto si existen
        if (producto != null) {
            etNombreProducto.setText(producto.getNombre());
            etPrecio.setText(String.valueOf(producto.getPrecio()));
            etDescripcion.setText(producto.getDescripcion());
            etCantidadMinima.setText(String.valueOf(producto.getCantidadMinima()));
            etCantidadActual.setText(String.valueOf(producto.getCantidadActual()));
            // Setear la categoría seleccionada en el Spinner (suponiendo que el idCategoria es un índice válido)
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).getIdCategoria() == producto.getIdCategoria()) {
                    spCategoria.setSelection(i);
                    break;
                }
            }
        }

        btnActualizar.setOnClickListener(v -> validarYActualizarProducto());

        // Habilitar edición de cantidad actual y motivo
        btnHabilitarCantidadActual.setOnClickListener(v -> habilitarCamposEdicion());

        return view;
    }

    // Método para habilitar los campos para edición
    private void habilitarCamposEdicion() {
        // Habilitar el EditText y Spinner para edición
        etCantidadActual.setEnabled(true);
        spMotivo.setEnabled(true);
    }

    // Método para validar campos y actualizar el producto
    private void validarYActualizarProducto() {
        String nombre = etNombreProducto.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String cantidadMinimaStr = etCantidadMinima.getText().toString().trim();
        String cantidadActualStr = etCantidadActual.getText().toString().trim();
        int idCategoria = ((Categoria) spCategoria.getSelectedItem()).getIdCategoria(); // Obtener el id de la categoría seleccionada
        String motivo = spMotivo.getSelectedItem().toString(); // Obtener el motivo seleccionado en el Spinner

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
        if (TextUtils.isEmpty(cantidadActualStr)) {
            etCantidadActual.setError("La cantidad actual es requerida");
            etCantidadActual.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(motivo) || motivo.equals("Selecciona un motivo")) {
            Toast.makeText(getContext(), "Debe seleccionar un motivo", Toast.LENGTH_SHORT).show();
            spMotivo.requestFocus();
            return;
        }

        // Validar valores numéricos
        double precio;
        int cantidadMinima;
        int cantidadActual;
        try {
            precio = Double.parseDouble(precioStr);
            cantidadMinima = Integer.parseInt(cantidadMinimaStr);
            cantidadActual = Integer.parseInt(cantidadActualStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Precio o cantidad mínima inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determinar el tipo de movimiento
        String tipoMovimiento = "";
        int diferenciaCantidad = cantidadActual - producto.getCantidadActual();

        if (diferenciaCantidad > 0) {
            tipoMovimiento = "Entrada"; // Aumentó la cantidad, es un ingreso
        } else if (diferenciaCantidad < 0) {
            tipoMovimiento = "Salida"; // Disminuyó la cantidad, es un egreso
        }

        // Actualizar producto
        ProductoDaoImpl productoDao = new ProductoDaoImpl(getContext());
        productoDao.update(new Producto(producto.getIdProducto(), idCategoria, nombre, precio, producto.getImagenBlob(), descripcion, cantidadActual, cantidadMinima));
        productoDao.close();

        // Insertar movimiento producto
        MovimientoProductoDaoImpl movimientoProductoDao = new MovimientoProductoDaoImpl(getContext());
        movimientoProductoDao.insert(new MovimientoProducto(producto.getIdProducto(), 0, tipoMovimiento, 0, diferenciaCantidad, new Date().toString(), motivo));
        movimientoProductoDao.close();

        Toast.makeText(getContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();

        // Regresar a la pantalla anterior o realizar otra acción
        requireActivity().onBackPressed();
    }


    // Método para cargar las categorías desde la base de datos
    private List<Categoria> cargarCategorias() {
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl(getContext());
        List<Categoria> categorias = categoriaDao.select(); // Aquí obtenemos las categorías
        categoriaDao.close();
        return categorias;
    }
}
