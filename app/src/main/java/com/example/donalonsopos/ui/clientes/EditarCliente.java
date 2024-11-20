package com.example.donalonsopos.ui.clientes;

import static com.example.donalonsopos.ui.clientes.ClientesFragment.KEY_CLIENTE;
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
import com.example.donalonsopos.data.DAO.ClienteDao;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.util.Utils;

public class EditarCliente extends Fragment {

    private Cliente clienteSeleccionado;
    private EditText etCedula, etNombre, etApellido, etTelefono, etDireccion;
    private Spinner spTipoCedula;
    private Button btnActualizar;

    public EditarCliente() {
        // Constructor vacío requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clienteSeleccionado = (Cliente) getArguments().getSerializable(KEY_CLIENTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_cliente, container, false);

        // Inicialización de vistas
        spTipoCedula = view.findViewById(R.id.spTipoCedula);
        etCedula = view.findViewById(R.id.etCedulaCliente);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDireccion = view.findViewById(R.id.etDireccion);
        btnActualizar = view.findViewById(R.id.btnActualizar);

        // Cargar datos del cliente
        if (clienteSeleccionado != null) {
            // Dividir la cédula en tipo y número
            String[] cedulaPartes = clienteSeleccionado.getCedula().split("-", 2);
            if (cedulaPartes.length == 2) {
                String tipoCedula = cedulaPartes[0];
                String numeroCedula = cedulaPartes[1];
                setSpinnerSelection(requireContext(), spTipoCedula, tipoCedula, R.array.tipo_cedula);
                etCedula.setText(numeroCedula);
            }
            etNombre.setText(clienteSeleccionado.getNombre());
            etApellido.setText(clienteSeleccionado.getApellido());
            etTelefono.setText(clienteSeleccionado.getTelefono());
            etDireccion.setText(clienteSeleccionado.getDireccion());

            // Manejo de valores nulos o vacíos
            if (clienteSeleccionado.getTelefono().isEmpty()) {
                etTelefono.setHint("No tiene teléfono");
            }

            if (clienteSeleccionado.getDireccion().isEmpty()) {
                etDireccion.setHint("No tiene dirección");
            }
        }

        // Configurar el botón de actualización
        btnActualizar.setOnClickListener(v -> validarYActualizarCliente());

        return view;
    }

    private void validarYActualizarCliente() {
        String tipoCedula = String.valueOf(spTipoCedula.getSelectedItem());
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();

        // Validaciones de campos obligatorios
        if (!Utils.validateRequiredField(etCedula, "La cédula del cliente es obligatoria") ||
                !Utils.validateRequiredField(etNombre, "El nombre del cliente es obligatorio") ||
                !Utils.validateRequiredField(etApellido, "El apellido del cliente es obligatorio")) {
            return;
        }

        // Validación de la cédula: debe ser un número mayor que cero
        if (!Utils.validatePositiveNumberField(etCedula, "La cédula debe ser un número válido")) {
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

        // Si no hay errores, actualizamos el cliente
        String cedulaCompleta = tipoCedula + "-" + cedula;
        clienteSeleccionado.setCedula(cedulaCompleta);
        clienteSeleccionado.setNombre(nombre);
        clienteSeleccionado.setApellido(apellido);

        // Actualizar teléfono y dirección solo si están presentes
        if (!telefono.isEmpty()) {
            clienteSeleccionado.setTelefono(telefono);
        }
        if (!direccion.isEmpty()) {
            clienteSeleccionado.setDireccion(direccion);
        }

        ClienteDao clienteDao = new ClienteDao(requireContext());
        clienteDao.update(clienteSeleccionado);
        clienteDao.close();

        Toast.makeText(getContext(), "Cliente actualizado con éxito", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }

}
