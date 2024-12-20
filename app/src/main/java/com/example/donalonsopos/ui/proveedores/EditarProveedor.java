package com.example.donalonsopos.ui.proveedores;

import static com.example.donalonsopos.ui.proveedores.ProveedoresFragment.KEY_PROVEEDOR;
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
import com.example.donalonsopos.data.DAO.ProveedorDaoImpl;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.util.Utils;

public class EditarProveedor extends Fragment {

    private Proveedor proveedorSeleccionado;
    private EditText etCedula, etNombre, etTelefono, etDireccion, etEmail;
    private Spinner spTipoCedula;
    private Button btnActualizar;

    public EditarProveedor() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proveedorSeleccionado = (Proveedor) getArguments().getSerializable(KEY_PROVEEDOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_proveedor, container, false);

        // Inicialización de vistas
        etCedula = view.findViewById(R.id.etCedulaCliente);
        spTipoCedula = view.findViewById(R.id.spTipoCedula);
        etNombre = view.findViewById(R.id.etNombre);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        etEmail = view.findViewById(R.id.etEmail);
        btnActualizar = view.findViewById(R.id.btnGuardar);

        if (proveedorSeleccionado != null) {
            // Dividir la cédula en tipo y número
            String[] cedulaPartes = proveedorSeleccionado.getCedula().split("-", 2);
            if (cedulaPartes.length == 2) {
                String tipoCedula = cedulaPartes[0];
                String numeroCedula = cedulaPartes[1];
                setSpinnerSelection(requireContext(), spTipoCedula, tipoCedula, R.array.tipo_cedula);
                etCedula.setText(numeroCedula);
            }
            etNombre.setText(proveedorSeleccionado.getNombre());
            etTelefono.setText(proveedorSeleccionado.getTelefono());
            etDireccion.setText(proveedorSeleccionado.getDireccion());
            etEmail.setText(proveedorSeleccionado.getEmail());

            // Manejo de valores nulos o vacíos
            if (proveedorSeleccionado.getTelefono().isEmpty()) {
                etTelefono.setHint("No tiene teléfono");
            }
            if (proveedorSeleccionado.getDireccion().isEmpty()) {
                etDireccion.setHint("No tiene dirección");
            }
        }

        // Configurar el botón de actualización
        btnActualizar.setOnClickListener(v -> validarYActualizarProveedor());

        return view;
    }

    private void validarYActualizarProveedor() {
        String cedula = etCedula.getText().toString().trim();
        String tipoCedula = String.valueOf(spTipoCedula.getSelectedItem());
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // Validaciones de campos obligatorios
        if (!Utils.validateRequiredField(etCedula, "La cédula del proveedor es obligatoria") ||
                !Utils.validateRequiredField(etNombre, "El nombre del proveedor es obligatorio")) {
            return;
        }

        // Validación de la cédula: debe ser un número mayor que cero
        if (!Utils.validatePositiveNumberField(etCedula, "La cédula debe ser un número mayor que cero")) {
            return;
        }

        // Validación de teléfono (si está presente, validamos el formato)
        if (!telefono.isEmpty() && !Utils.validatePhoneNumberField(etTelefono, "El teléfono no es válido")) {
            return;
        }

        // Validación de dirección (si está presente, validamos el formato)
        if (!direccion.isEmpty() && !Utils.validateAddressField(etDireccion, "La dirección no es válida")) {
            return;
        }

        // Validación de email (si está presente, validamos el formato)
        if (!email.isEmpty() && !Utils.validateEmailField(etEmail, "El email no es válido")) {
            return;
        }

        // Si no hay errores, actualizamos el proveedor
        String cedulaCompleta = tipoCedula + "-" + cedula;
        proveedorSeleccionado.setCedula(cedulaCompleta);
        proveedorSeleccionado.setNombre(nombre);

        // Actualizar teléfono, dirección y email solo si están presentes
        if (!telefono.isEmpty()) {
            proveedorSeleccionado.setTelefono(telefono);
        }
        if (!direccion.isEmpty()) {
            proveedorSeleccionado.setDireccion(direccion);
        }
        if (!email.isEmpty()) {
            proveedorSeleccionado.setEmail(email);
        }

        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl(requireContext());
        int row = proveedorDao.update(proveedorSeleccionado);
        proveedorDao.close();
        if (row == 0) {
            Toast.makeText(getContext(), "No se pudo actualizar el proveedor", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Proveedor actualizado con éxito", Toast.LENGTH_SHORT).show();

        // Volver a la vista anterior
        requireActivity().onBackPressed();
    }
}