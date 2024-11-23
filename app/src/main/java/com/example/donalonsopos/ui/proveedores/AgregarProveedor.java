package com.example.donalonsopos.ui.proveedores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.ProveedorDaoImpl;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.util.Utils;

public class AgregarProveedor extends DialogFragment {

    private EditText etRif, etNombre, etTelefono, etDireccion, etEmail;
    private Spinner spTipoCedula;
    private Button btnGuardar, btnLimpiar;
    private ImageButton btnCerrar;

    public AgregarProveedor() {
        // Constructor público requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_proveedor, container, false);

        initializeViews(view);
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        etRif = view.findViewById(R.id.etCedulaCliente);
        spTipoCedula = view.findViewById(R.id.spTipoCedula);
        etNombre = view.findViewById(R.id.etNombre);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        etEmail = view.findViewById(R.id.etEmail);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
        btnCerrar = view.findViewById(R.id.btnCerrar);
    }

    private void setupListeners() {
        btnGuardar.setOnClickListener(v -> guardarProveedor());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
        btnCerrar.setOnClickListener(v -> dismiss());
    }

    private void guardarProveedor() {
        String rif = etRif.getText().toString().trim();
        String tipoCedula = spTipoCedula.getSelectedItem().toString();
        String rifCompleto = tipoCedula + "-" + rif;
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (!Utils.validateRequiredField(etRif, "La Cedula/RIF es obligatorio") ||
                !Utils.validateRequiredField(etNombre, "El nombre es obligatorio")) {
            return;
        }

        if (!Utils.validatePositiveNumberField(etRif, "La Cedula/RIF debe ser un número válido")) {
            return;
        }

        if (!telefono.isEmpty() && !Utils.validatePhoneNumberField(etTelefono, "El teléfono no es válido")) {
            return;
        }

        if (!direccion.isEmpty() && !Utils.validateAddressField(etDireccion, "La dirección no es válida")) {
            return;
        }

        if (!email.isEmpty() && !Utils.validateEmailField(etEmail, "El correo electrónico no es válido")) {
            return;
        }

        Proveedor proveedor = new Proveedor(rifCompleto, nombre, direccion, telefono, email);
        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl(requireContext());
        proveedorDao.insert(proveedor);
        proveedorDao.close();
        Toast.makeText(getContext(), "Proveedor registrado con éxito", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void limpiarCampos() {
        etRif.setText("");
        etNombre.setText("");
        etTelefono.setText("");
        etDireccion.setText("");
        etEmail.setText("");
    }
}
