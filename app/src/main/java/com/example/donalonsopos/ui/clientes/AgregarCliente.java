package com.example.donalonsopos.ui.clientes;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.ClienteDao;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.util.Utils;

public class AgregarCliente extends DialogFragment {

    private EditText etCedula, etNombre, etApellido, etTelefono, etDireccion;
    private Spinner spTipoCedula;
    private Button btnGuardar, btnLimpiar;
    private ImageButton btnCerrar;

    public AgregarCliente() {
        // Constructor público requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_cliente, container, false);

        initializeViews(view);
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        spTipoCedula = view.findViewById(R.id.spTipoCedula);
        etCedula = view.findViewById(R.id.etCedulaCliente);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
        btnCerrar = view.findViewById(R.id.btnCerrar);
    }

    private void setupListeners() {
        btnGuardar.setOnClickListener(v -> guardarCliente());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
        btnCerrar.setOnClickListener(v -> dismiss());
    }

    private void guardarCliente() {
        // Obtener los valores de los campos de entrada
        String tipoCedula = spTipoCedula.getSelectedItem().toString();
        String cedula = etCedula.getText().toString().trim();
        String cedulaCompleta = tipoCedula + "-" + cedula;
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();

        if (!Utils.validateRequiredField(etCedula, "La cédula es obligatoria") ||
                !Utils.validateRequiredField(etNombre, "El nombre es obligatorio") ||
                !Utils.validateRequiredField(etApellido, "El apellido es obligatorio")) {
            return;
        }

        if (!Utils.validatePositiveNumberField(etCedula, "La cédula debe ser un número válido")) {
            return;
        }

        if (!telefono.isEmpty() && !Utils.validatePhoneNumberField(etTelefono, "El teléfono no es válido")) {
            return;
        }

        if (!direccion.isEmpty() && !Utils.validateAddressField(etDireccion, "La dirección no es válida")) {
            return;
        }

        // Lógica para guardar el cliente
        ClienteDao clienteDao = new ClienteDao(requireContext());
        Cliente cliente = new Cliente(cedulaCompleta, nombre, apellido, direccion, telefono);
        clienteDao.insert(cliente);
        clienteDao.close();
        Toast.makeText(getContext(), "Cliente registrado con éxito", Toast.LENGTH_SHORT).show();
        dismiss();  // Cerrar el formulario o realizar alguna acción de salida
    }


    private void limpiarCampos() {
        etCedula.setText("");
        etNombre.setText("");
        etApellido.setText("");
        etTelefono.setText("");
        etDireccion.setText("");
    }
}
