package com.example.donalonsopos.ui.productos;

import static com.example.donalonsopos.ui.productos.ProductosFragment.KEY_PRODUCTO;

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
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.util.ConfirmDialog;

public class DetallesProducto extends Fragment {

    private ConfirmDialog confirmDialog;
    private Producto productoSeleccionado;
    private TextView tvNombreContenido, tvPrecioContenido, tvCantidadActualContenido, tvCantidadMinimaContenido, tvDescripcionContenido;
    private Button btnEditar, btnEliminar;

    public DetallesProducto() {
        // Constructor vacío requerido
    }

    public static DetallesProducto newInstance(Producto producto) {
        DetallesProducto fragment = new DetallesProducto();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PRODUCTO, producto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productoSeleccionado = (Producto) getArguments().getSerializable(KEY_PRODUCTO);
        }
        confirmDialog = new ConfirmDialog(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_producto, container, false);

        if (productoSeleccionado == null) {
            Toast.makeText(getContext(), "No se ha seleccionado un producto.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return view;
        }

        initializeViews(view);
        displayProductDetails();

        return view;
    }

    private void initializeViews(View view) {
        tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        tvPrecioContenido = view.findViewById(R.id.tvPrecioContenido);
        tvCantidadActualContenido = view.findViewById(R.id.tvCantidadActualContenido);
        tvCantidadMinimaContenido = view.findViewById(R.id.tvCantidadMinimaContenido);
        tvDescripcionContenido = view.findViewById(R.id.tvDescripcionContenido);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnEliminar = view.findViewById(R.id.btnEliminar);

        btnEditar.setOnClickListener(v -> navigateToEditProduct());
        btnEliminar.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void displayProductDetails() {
        if (productoSeleccionado == null) return;

        tvNombreContenido.setText(productoSeleccionado.getNombre());
        tvPrecioContenido.setText(String.valueOf(productoSeleccionado.getPrecio()));
        tvCantidadActualContenido.setText(String.valueOf(productoSeleccionado.getCantidadActual()));
        tvCantidadMinimaContenido.setText(String.valueOf(productoSeleccionado.getCantidadMinima()));
        tvDescripcionContenido.setText(
                productoSeleccionado.getDescripcion() == null || productoSeleccionado.getDescripcion().isEmpty()
                        ? "Sin descripción"
                        : productoSeleccionado.getDescripcion()
        );
    }

    private void navigateToEditProduct() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
        navController.navigate(R.id.EditarProductoFragment, createBundleWithProducto(productoSeleccionado));
    }

    private void showDeleteConfirmationDialog() {
        confirmDialog.showConfirmationDialog("Eliminar", "¿Estás seguro de eliminar este producto?", () -> {
            ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
            productoDao.delete(productoSeleccionado.getIdProducto());
            productoDao.close();
            Toast.makeText(getContext(), "Producto eliminado con éxito", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }

    private Bundle createBundleWithProducto(Producto producto) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCTO, producto);
        return bundle;
    }
}