package com.example.donalonsopos.ui.usuarios;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.donalonsopos.R;
import com.example.donalonsopos.util.Utils;

public class AgregarUsuario extends DialogFragment {

    private EditText etNombreUsuario, etCedula, etNombre, etApellido;
    private Spinner spinnerRol;
    private Button btnGuardar, btnLimpiar;

    public AgregarUsuario() {
        // Constructor público requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_usuario, container, false);

        initializeViews(view);
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        etCedula = view.findViewById(R.id.etCedula);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        spinnerRol = view.findViewById(R.id.spinnerRol);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
    }

    private void setupListeners() {
        btnGuardar.setOnClickListener(v -> guardarUsuario());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
    }

    private void guardarUsuario() {
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();

        // Obtener el rol seleccionado en el Spinner
        String rol = spinnerRol.getSelectedItem() != null ? spinnerRol.getSelectedItem().toString() : "";

        // Validar campos obligatorios
        if (!Utils.validateRequiredField(etNombreUsuario, "El nombre de usuario es obligatorio") ||
                !Utils.validateRequiredField(etCedula, "La cédula es obligatoria") ||
                !Utils.validateRequiredField(etNombre, "El nombre es obligatorio") ||
                !Utils.validateRequiredField(etApellido, "El apellido es obligatorio") ||
                rol.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios, incluido el rol", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato de cédula
        if (!Utils.validatePositiveNumberField(etCedula, "La cédula debe ser un número mayor que cero")) {
            return;
        }

        // Lógica para guardar el usuario
        // Aquí puedes agregar el código para guardar los datos en la base de datos o donde corresponda

        Toast.makeText(getContext(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
        dismiss();  // Cerrar el formulario
    }


    private void limpiarCampos() {
        etNombreUsuario.setText("");
        etCedula.setText("");
        etNombre.setText("");
        etApellido.setText("");
    }
}