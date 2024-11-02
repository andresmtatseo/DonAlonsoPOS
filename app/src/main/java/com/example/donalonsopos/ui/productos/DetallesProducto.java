package com.example.donalonsopos.ui.productos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.donalonsopos.R;
import com.example.donalonsopos.data.entities.Producto;

public class DetallesProducto extends Fragment {

    private static final String ARG_PRODUCTO = "producto";  // Define el nuevo parámetro

    private Producto productoSeleccionado;  // Variable para almacenar el producto

    public DetallesProducto() {
        // Constructor vacío requerido
    }

    /**
     * Método factory para crear una nueva instancia de DetallesProducto,
     * pasando el objeto Producto como argumento.
     *
     * @param producto El producto a mostrar en los detalles.
     * @return Una nueva instancia del fragment DetallesProducto.
     */
    public static DetallesProducto newInstance(Producto producto) {
        DetallesProducto fragment = new DetallesProducto();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCTO, producto);  // Añadir el producto al Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productoSeleccionado = (Producto) getArguments().getSerializable(ARG_PRODUCTO);  // Recupera el producto
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_producto, container, false);

        // Aquí puedes acceder a `productoSeleccionado` para mostrar sus detalles en la interfaz
        // Ejemplo: actualizar un TextView con el nombre del producto, etc.

        return view;
    }
}
