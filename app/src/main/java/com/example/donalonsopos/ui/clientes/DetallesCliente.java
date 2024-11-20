package com.example.donalonsopos.ui.clientes;

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
import com.example.donalonsopos.data.DAO.ClienteDao;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.util.ConfirmDialog;
import com.google.android.material.snackbar.Snackbar;

import static com.example.donalonsopos.ui.clientes.ClientesFragment.KEY_CLIENTE;

public class DetallesCliente extends Fragment {

    private ConfirmDialog confirmDialog;
    private Cliente clienteSeleccionado;
    private TextView tvCedulaContenido, tvNombreContenido, tvApellidoContenido, tvTelefonoContenido, tvDireccionContenido;
    private Button btnEditar, btnEliminar;

    public DetallesCliente() {
        // Constructor vacío requerido
    }

    public static DetallesCliente newInstance(Cliente cliente) {
        DetallesCliente fragment = new DetallesCliente();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CLIENTE, cliente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clienteSeleccionado = (Cliente) getArguments().getSerializable(KEY_CLIENTE);
        }
        confirmDialog = new ConfirmDialog(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cliente, container, false);

        // Verificación de cliente seleccionado
        if (clienteSeleccionado == null) {
            Toast.makeText(getContext(), "No se ha seleccionado un cliente.", Toast.LENGTH_SHORT).show();

            // Navegación segura hacia atrás usando NavController
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
            return view;
        }

        inicializarVistas(view);
        mostrarDatosCliente();

        return view;
    }

    private void inicializarVistas(View view) {
        tvCedulaContenido = view.findViewById(R.id.tvCedulaContenido);
        tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        tvApellidoContenido = view.findViewById(R.id.tvApellidoContenido);
        tvTelefonoContenido = view.findViewById(R.id.tvTelefonoContenido);
        tvDireccionContenido = view.findViewById(R.id.tvDireccionContenido);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnEliminar = view.findViewById(R.id.btnEliminar);

        btnEditar.setOnClickListener(v -> navegarAEditarCliente());
        btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacionEliminar());
    }

    private void mostrarDatosCliente() {
        if (clienteSeleccionado == null) return;

        tvCedulaContenido.setText(String.valueOf(clienteSeleccionado.getCedula()));
        tvNombreContenido.setText(clienteSeleccionado.getNombre());
        tvApellidoContenido.setText(clienteSeleccionado.getApellido());
        tvTelefonoContenido.setText(
                clienteSeleccionado.getTelefono() == null || clienteSeleccionado.getTelefono().isEmpty()
                        ? "No tiene teléfono"
                        : clienteSeleccionado.getTelefono()
        );
        tvDireccionContenido.setText(
                clienteSeleccionado.getDireccion() == null || clienteSeleccionado.getDireccion().isEmpty()
                        ? "No tiene dirección"
                        : clienteSeleccionado.getDireccion()
        );
    }

    private void navegarAEditarCliente() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
        navController.navigate(R.id.EditarClienteFragment, crearBundleConCliente(clienteSeleccionado));
    }

    private void mostrarDialogoConfirmacionEliminar() {
        confirmDialog.showConfirmationDialog("Eliminar", "¿Estás seguro de eliminar este cliente?", () -> {
            ClienteDao clienteDao = new ClienteDao(requireContext());
            clienteDao.delete(clienteSeleccionado.getIdCliente());
            clienteDao.close();

            Toast.makeText(getContext(), "Cliente eliminado con éxito", Toast.LENGTH_SHORT).show();

            // Navegación segura hacia atrás usando NavController después de confirmar la eliminación
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }

    private Bundle crearBundleConCliente(Cliente cliente) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CLIENTE, cliente);
        return bundle;
    }
}