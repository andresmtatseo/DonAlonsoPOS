package com.example.donalonsopos.ui.proveedores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.util.ConfirmDialog;

import static com.example.donalonsopos.ui.proveedores.ProveedoresFragment.KEY_PROVEEDOR;

public class DetallesProveedor extends Fragment {

    private ConfirmDialog confirmDialog;
    private Proveedor proveedorSeleccionado;
    private TextView tvCedulaContenido, tvNombreContenido, tvTelefonoContenido, tvDireccionContenido, tvEmailContenido;
    private Button btnEditar, btnEliminar;

    public DetallesProveedor() {
        // Constructor vacío requerido
    }

    public static DetallesProveedor newInstance(Proveedor proveedor) {
        DetallesProveedor fragment = new DetallesProveedor();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PROVEEDOR, proveedor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proveedorSeleccionado = (Proveedor) getArguments().getSerializable(KEY_PROVEEDOR);
        }
        confirmDialog = new ConfirmDialog(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_proveedor, container, false);

        // Verificación de proveedor seleccionado
        if (proveedorSeleccionado == null) {
            Toast.makeText(getContext(), "No se ha seleccionado un proveedor.", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
            return view;
        }

        inicializarVistas(view);
        mostrarDatosProveedor();

        return view;
    }

    private void inicializarVistas(View view) {
        tvCedulaContenido = view.findViewById(R.id.tvCedulaContenido);
        tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        tvTelefonoContenido = view.findViewById(R.id.tvTelefonoContenido);
        tvDireccionContenido = view.findViewById(R.id.tvDireccionContenido);
        tvEmailContenido = view.findViewById(R.id.tvEmailContenido);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnEliminar = view.findViewById(R.id.btnEliminar);

        btnEditar.setOnClickListener(v -> navegarAEditarProveedor());
        btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacionEliminar());
    }

    private void mostrarDatosProveedor() {
        if (proveedorSeleccionado == null) return;

        tvCedulaContenido.setText(String.valueOf(proveedorSeleccionado.getCedula()));
        tvNombreContenido.setText(proveedorSeleccionado.getNombre());
        tvTelefonoContenido.setText(
                proveedorSeleccionado.getTelefono() == null || proveedorSeleccionado.getTelefono().isEmpty()
                        ? "No tiene teléfono"
                        : proveedorSeleccionado.getTelefono()
        );
        tvDireccionContenido.setText(
                proveedorSeleccionado.getDireccion() == null || proveedorSeleccionado.getDireccion().isEmpty()
                        ? "No tiene dirección"
                        : proveedorSeleccionado.getDireccion()
        );
        tvEmailContenido.setText(
                proveedorSeleccionado.getEmail() == null || proveedorSeleccionado.getEmail().isEmpty()
                        ? "No tiene email"
                        : proveedorSeleccionado.getEmail()
        );
    }

    private void navegarAEditarProveedor() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
        navController.navigate(R.id.EditarProveedorFragment, crearBundleConProveedor(proveedorSeleccionado));
    }

    private void mostrarDialogoConfirmacionEliminar() {
        confirmDialog.showConfirmationDialog("Eliminar", "¿Estás seguro de eliminar este proveedor?", () -> {
            Toast.makeText(getContext(), "Proveedor eliminado con éxito", Toast.LENGTH_SHORT).show();

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }

    private Bundle crearBundleConProveedor(Proveedor proveedor) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PROVEEDOR, proveedor);
        return bundle;
    }
}
