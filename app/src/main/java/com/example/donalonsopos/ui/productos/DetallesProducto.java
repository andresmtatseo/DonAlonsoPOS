package com.example.donalonsopos.ui.productos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.util.ConfirmDialog;

public class DetallesProducto extends Fragment {

    private static final String ARG_PRODUCTO = "producto";  // Define el nuevo parámetro
    private ConfirmDialog confirmDialog;
    private Producto productoSeleccionado;  // Variable para almacenar el producto
    private static final String KEY_PRODUCTO = "producto";

    public DetallesProducto() {
        // Constructor vacío requerido
    }

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

        confirmDialog = new ConfirmDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_producto, container, false);

        TextView tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        TextView tvPrecioContenido = view.findViewById(R.id.tvPrecioContenido);
        //TextView tvCostoContenido = view.findViewById(R.id.tvCostoContenido);
        TextView tvCantidadActualContenido = view.findViewById(R.id.tvCantidadActualContenido);
        TextView tvCantidadMinimaContenido = view.findViewById(R.id.tvCantidadMinimaContenido);
        TextView tvDescripcionContenido = view.findViewById(R.id.tvDescripcionContenido);

        tvNombreContenido.setText(productoSeleccionado.getNombre());
        tvPrecioContenido.setText(String.valueOf(productoSeleccionado.getPrecio()));
        //tvCostoContenido.setText(String.valueOf(productoSeleccionado.getCosto()));
        tvCantidadActualContenido.setText(String.valueOf(productoSeleccionado.getCantidadActual()));
        tvCantidadMinimaContenido.setText(String.valueOf(productoSeleccionado.getCantidadMinima()));
        tvDescripcionContenido.setText(productoSeleccionado.getDescripcion());

        Button btnEliminar = view.findViewById(R.id.btnEliminar);
        Button btnEditar = view.findViewById(R.id.btnEditar);


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.EditarProductoFragment, createBundleWithProducto(productoSeleccionado));
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.showConfirmationDialog("Eliminar", "¿Estás seguro de eliminar este producto?", () -> {
                    Toast.makeText(getContext(), "Se elimino el producto.", Toast.LENGTH_SHORT);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                    navController.navigate(R.id.nav_productos);
                });
            }
        });


        return view;
    }

    private Bundle createBundleWithProducto(Producto producto) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCTO, producto);
        return bundle;
    }
}
