package com.example.donalonsopos.ui.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Usuario;
import com.example.donalonsopos.util.ConfirmDialog;

import static com.example.donalonsopos.ui.usuarios.UsuariosFragment.KEY_USUARIO;

public class DetallesUsuario extends Fragment {

    private ConfirmDialog confirmDialog;
    private Usuario usuarioSeleccionado;
    private TextView tvUsernameContenido, tvCedulaContenido, tvRolContenido, tvNombreContenido, tvApellidoContenido;
    private Button btnEditar, btnEliminar;

    public DetallesUsuario() {
        // Constructor vacío requerido
    }

    public static DetallesUsuario newInstance(Usuario usuario) {
        DetallesUsuario fragment = new DetallesUsuario();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuarioSeleccionado = (Usuario) getArguments().getSerializable(KEY_USUARIO);
        }
        confirmDialog = new ConfirmDialog(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_usuario, container, false);

        // Verificación de usuario seleccionado
        if (usuarioSeleccionado == null) {
            Toast.makeText(getContext(), "No se ha seleccionado un usuario.", Toast.LENGTH_SHORT).show();

            // Navegación segura hacia atrás usando NavController
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
            return view;
        }

        inicializarVistas(view);
        mostrarDatosUsuario();

        return view;
    }

    private void inicializarVistas(View view) {
        tvUsernameContenido = view.findViewById(R.id.tvUsernameContenido);
        tvCedulaContenido = view.findViewById(R.id.tvCedulaContenido);
        tvRolContenido = view.findViewById(R.id.tvRolContenido);
        tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        tvApellidoContenido = view.findViewById(R.id.tvApellidoContenido);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnEliminar = view.findViewById(R.id.btnEliminar);

        btnEditar.setOnClickListener(v -> navegarAEditarUsuario());
        btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacionEliminar());
    }

    private void mostrarDatosUsuario() {
        if (usuarioSeleccionado == null) return;

        tvUsernameContenido.setText(usuarioSeleccionado.getUsername());
        tvCedulaContenido.setText(String.valueOf(usuarioSeleccionado.getCedula()));
        tvRolContenido.setText(String.valueOf(usuarioSeleccionado.getRol()));
        tvNombreContenido.setText(usuarioSeleccionado.getNombre());
        tvApellidoContenido.setText(usuarioSeleccionado.getApellido());
    }

    private void navegarAEditarUsuario() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
        navController.navigate(R.id.EditarUsuarioFragment, crearBundleConUsuario(usuarioSeleccionado));
    }

    private void mostrarDialogoConfirmacionEliminar() {
        confirmDialog.showConfirmationDialog("Eliminar", "¿Estás seguro de eliminar este usuario?", () -> {
            Toast.makeText(getContext(), "Usuario eliminado con éxito", Toast.LENGTH_SHORT).show();

            // Navegación segura hacia atrás usando NavController después de confirmar la eliminación
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }

    private Bundle crearBundleConUsuario(Usuario usuario) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_USUARIO, usuario);
        return bundle;
    }
}