package com.example.donalonsopos.ui.compras;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.util.ProductoConCantidad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AgregarCompra extends Fragment {

    private EditText etCedulaProveedor, etNumeroComprobante;
    private Spinner spTipoCedula, spMetodoPago;
    private ImageButton ibBuscar;
    private TextView tvNombreProveedorContenido, tvTelefonoProveedorContenido, tvDireccionProveedorContenido, tvTotalVentaContenido;
    private RecyclerView rvProductosSeleccionados;
    private Button btnConfirmar, btnLimpiar;

    public AgregarCompra() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_compra, container, false);
        initializeViews(view);
        setupFloatingActionButton(view);
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        etCedulaProveedor = view.findViewById(R.id.etCedulaProveedor);
        etNumeroComprobante = view.findViewById(R.id.etNumeroComprobante);
        spTipoCedula = view.findViewById(R.id.spTipoCedula);
        spMetodoPago = view.findViewById(R.id.spMetodoPago);
        ibBuscar = view.findViewById(R.id.ibBuscar);
        tvNombreProveedorContenido = view.findViewById(R.id.tvNombreProveedorContenido);
        tvTelefonoProveedorContenido = view.findViewById(R.id.tvTelefonoProveedorContenido);
        tvDireccionProveedorContenido = view.findViewById(R.id.tvDireccionProveedorContenido);
        tvTotalVentaContenido = view.findViewById(R.id.tvTotalVentaContenido);
        rvProductosSeleccionados = view.findViewById(R.id.rvProductosSeleccionados);
        btnConfirmar = view.findViewById(R.id.btnConfirmar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregarProducto);

        // Evento clic para agregar productos
        btnAgregar.setOnClickListener(v -> {
            // Obtener el bundle actual con los productos seleccionados y datos de la venta
            Bundle bundle = getArguments();
            List<ProductoConCantidad> productosSeleccionados = null;
            Venta venta = null;
            Cliente cliente = null;

            // Recuperar los datos del bundle si existen
            if (bundle != null) {
                productosSeleccionados = (List<ProductoConCantidad>) bundle.getSerializable("productosSeleccionados");
                venta = (Venta) bundle.getSerializable("venta");
                cliente = (Cliente) bundle.getSerializable("cliente");
            }

            // Inicializar productos seleccionados si es nulo
            if (productosSeleccionados == null) {
                productosSeleccionados = new ArrayList<>();
            }

            // Si no hay una venta previa, crear una nueva
            if (venta == null) {
                String tipoCedula = spTipoCedula.getSelectedItem() != null
                        ? spTipoCedula.getSelectedItem().toString().trim()
                        : "";
                String cedula = etCedulaProveedor.getText().toString().trim();
                String cedulaCompleta = tipoCedula + "-" + cedula;

                // Simulo la búsqueda del id del cliente
                int id = 5;
                String metodoPago = spMetodoPago.getSelectedItem() != null
                        ? spMetodoPago.getSelectedItem().toString().trim()
                        : "";

                int numeroComprobante;
                try {
                    numeroComprobante = Integer.parseInt(etNumeroComprobante.getText().toString().trim());
                } catch (NumberFormatException e) {
                    etNumeroComprobante.setError("Ingrese un número válido");
                    numeroComprobante = 0; // Valor por defecto si ocurre un error
                }

                cliente = new Cliente(id, cedulaCompleta);
                venta = new Venta(cliente.getIdCliente(), metodoPago, numeroComprobante);
            }

            // Crear un nuevo bundle para pasar los datos al siguiente fragmento
            Bundle nuevoBundle = new Bundle();

            // Asegurarnos de pasar correctamente los productos seleccionados
            nuevoBundle.putSerializable("productosSeleccionados", new ArrayList<>(productosSeleccionados));
            nuevoBundle.putSerializable("venta", venta);
            nuevoBundle.putSerializable("cliente", cliente);

            // Navegar al fragmento AgregarProductoVenta y pasar el nuevo bundle
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.navigate(R.id.agregarProductoCompraFragment, nuevoBundle);
        });
    }

    private void setupListeners() {
        btnConfirmar.setOnClickListener(v -> guardarCompra());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
    }

    private void guardarCompra() {
        String cedula = etCedulaProveedor.getText().toString().trim();
        String numeroComprobante = etNumeroComprobante.getText().toString().trim();
        String tipoCedula = spTipoCedula.getSelectedItem() != null ? spTipoCedula.getSelectedItem().toString().trim() : "";
        String metodoPago = spMetodoPago.getSelectedItem() != null ? spMetodoPago.getSelectedItem().toString().trim() : "";

        // productos seleccionados
        if (cedula.isEmpty() || numeroComprobante.isEmpty() || tipoCedula.isEmpty() || metodoPago.isEmpty() ) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rvProductosSeleccionados.getAdapter() == null || rvProductosSeleccionados.getAdapter().getItemCount() == 0) {
            Toast.makeText(getContext(), "Por favor, seleccione al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí agregamos la lógica para guardar el producto (en base de datos o en memoria)
        Toast.makeText(getContext(), "Compra guardada correctamente", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    private void limpiarCampos() {
        etCedulaProveedor.setText("");
        etNumeroComprobante.setText("");
        spTipoCedula.setSelection(0);
        spMetodoPago.setSelection(0);
        tvNombreProveedorContenido.setText("");
        tvTelefonoProveedorContenido.setText("");
        tvDireccionProveedorContenido.setText("");
        rvProductosSeleccionados.setAdapter(null);
        tvTotalVentaContenido.setText("");
    }
}