package com.example.donalonsopos.ui.usuarios;

import static com.example.donalonsopos.ui.usuarios.UsuariosFragment.KEY_USUARIO;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Usuario;
import com.example.donalonsopos.util.Utils;

public class EditarUsuario extends Fragment {

    private Usuario usuarioSeleccionado;
    private EditText etNombreUsuario, etCedula, etNombre, etApellido;
    private Spinner spinnerRol;
    private Button btnActualizar;

    public EditarUsuario() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuarioSeleccionado = (Usuario) getArguments().getSerializable(KEY_USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_usuario, container, false);

        // Inicialización de vistas
        etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        etCedula = view.findViewById(R.id.etCedulaCliente);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        spinnerRol = view.findViewById(R.id.spinnerRol);
        btnActualizar = view.findViewById(R.id.btnGuardar);

        // Cargar datos del usuario
        if (usuarioSeleccionado != null) {
            etNombreUsuario.setText(usuarioSeleccionado.getUsername());
            etCedula.setText(String.valueOf(usuarioSeleccionado.getCedula()));
            etNombre.setText(usuarioSeleccionado.getNombre());
            etApellido.setText(usuarioSeleccionado.getApellido());

            // Si el rol del usuario no está seleccionado, elige un valor por defecto
            // Esto se debe implementar según el contexto de los roles
            // spinnerRol.setSelection(getRolPosition(usuarioSeleccionado.getRol()));
        }

        // Configurar el botón de actualización
        btnActualizar.setOnClickListener(v -> validarYActualizarUsuario());

        return view;
    }

    private void validarYActualizarUsuario() {
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        int rol = spinnerRol.getSelectedItemPosition();

        // Validaciones de campos obligatorios
        if (!Utils.validateRequiredField(etNombreUsuario, "El nombre de usuario es obligatorio") ||
                !Utils.validateRequiredField(etCedula, "La cédula es obligatoria") ||
                !Utils.validateRequiredField(etNombre, "El nombre es obligatorio") ||
                !Utils.validateRequiredField(etApellido, "El apellido es obligatorio")) {
            return;
        }

        // Validación de la cédula: debe ser un número mayor que cero
        if (!Utils.validatePositiveNumberField(etCedula, "La cédula debe ser un número mayor que cero")) {
            return;
        }

        // Si no hay errores, actualizamos el usuario
        usuarioSeleccionado.setUsername(nombreUsuario);
        usuarioSeleccionado.setCedula(cedula);
        usuarioSeleccionado.setNombre(nombre);
        usuarioSeleccionado.setApellido(apellido);
        usuarioSeleccionado.setRol(rol);

        Toast.makeText(getContext(), "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}
