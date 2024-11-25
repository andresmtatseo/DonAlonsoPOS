package com.example.donalonsopos.ui.usuarios;

import static com.example.donalonsopos.ui.usuarios.UsuariosFragment.KEY_USUARIO;
import static com.example.donalonsopos.util.Utils.setSpinnerSelection;

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
import com.example.donalonsopos.data.DAO.UsuarioDaoImpl;
import com.example.donalonsopos.data.DTO.Usuario;
import com.example.donalonsopos.util.Utils;

public class EditarUsuario extends Fragment {

    private Usuario usuarioSeleccionado;
    private EditText etNombreUsuario, etCedula, etNombre, etApellido, etContrasena;
    private Spinner spinnerRol, spTipoCedula;
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
        etContrasena = view.findViewById(R.id.etContrasena);
        spinnerRol = view.findViewById(R.id.spinnerRol);
        spTipoCedula = view.findViewById(R.id.spTipoCedula4);
        btnActualizar = view.findViewById(R.id.btnGuardar);


        // Cargar datos del usuario
        if (usuarioSeleccionado != null) {
            etNombreUsuario.setText(usuarioSeleccionado.getUsername());
            //etCedula.setText(String.valueOf(usuarioSeleccionado.getCedula()));
            etNombre.setText(usuarioSeleccionado.getNombre());
            etApellido.setText(usuarioSeleccionado.getApellido());
            etContrasena.setText(usuarioSeleccionado.getPassword());
            String rol = String.valueOf(usuarioSeleccionado.getRol());
            setSpinnerSelection(requireContext(), spinnerRol, rol, R.array.roles);

            // Dividir la cédula en tipo y número
            String[] cedulaPartes = usuarioSeleccionado.getCedula().split("-", 2);
            if (cedulaPartes.length == 2) {
                String tipoCedula = cedulaPartes[0];
                String numeroCedula = cedulaPartes[1];
                setSpinnerSelection(requireContext(), spTipoCedula, tipoCedula, R.array.tipo_cedula);
                etCedula.setText(numeroCedula);
            }

        }

        // Configurar el botón de actualización
        btnActualizar.setOnClickListener(v -> validarYActualizarUsuario());

        return view;
    }

    private void validarYActualizarUsuario() {
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String cedula = etCedula.getText().toString().trim();
        String tipoCedula = String.valueOf(spTipoCedula.getSelectedItem());
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        int rol = spinnerRol.getSelectedItemPosition();

        // Validaciones de campos obligatorios
        if (!Utils.validateRequiredField(etNombreUsuario, "El nombre de usuario es obligatorio") ||
                !Utils.validateRequiredField(etCedula, "La cédula es obligatoria") ||
                !Utils.validateRequiredField(etNombre, "El nombre es obligatorio") ||
                !Utils.validateRequiredField(etApellido, "El apellido es obligatorio") ||
                !Utils.validateRequiredField(etContrasena, "La contraseña es obligatoria")) {
            return;
        }

        // Validación de la cédula: debe ser un número mayor que cero
        if (!Utils.validatePositiveNumberField(etCedula, "La cédula debe ser un número mayor que cero")) {
            return;
        }

        // Si no hay errores, actualizamos el usuario
        usuarioSeleccionado.setUsername(nombreUsuario);
        usuarioSeleccionado.setPassword(etContrasena.getText().toString());
        String cedulaCompleta = tipoCedula + "-" + cedula;
        usuarioSeleccionado.setCedula(cedulaCompleta);
        usuarioSeleccionado.setNombre(nombre);
        usuarioSeleccionado.setApellido(apellido);
        usuarioSeleccionado.setRol(rol);

        UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl(requireContext());
        usuarioDao.update(usuarioSeleccionado);
        usuarioDao.close();

        Toast.makeText(getContext(), "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }
}
