package com.example.donalonsopos.ui.compras;

import static com.example.donalonsopos.util.Utils.setSpinnerSelection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.ClienteDaoImpl;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.ui.clientes.AgregarCliente;
import com.example.donalonsopos.ui.proveedores.AgregarProveedor;
import com.example.donalonsopos.ui.ventas.AdaptadorViewProductoSeleccionado;
import com.example.donalonsopos.util.ConfirmDialog;
import com.example.donalonsopos.util.ProductoConCantidad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AgregarCompra extends Fragment {

    private String KEY_PRODUCTOS_SELECCIONADOS = "productosSeleccionados";
    private String KEY_COMPRA = "compra";
    private String KEY_PROVEEDOR = "proveedor";

    private EditText etCedulaProveedor, etNumeroComprobante;
    private Spinner spTipoCedula, spMetodoPago;
    private ImageButton ibBuscar;
    private TextView tvNombreProveedorContenido, tvTelefonoProveedorContenido, tvDireccionProveedorContenido, tvTotalContenido;
    private RecyclerView rvProductosSeleccionados;
    private Button btnConfirmar, btnLimpiar;
    private AdaptadorViewProductoSeleccionado adaptador;
    private ConfirmDialog confirmDialog;

    public AgregarCompra() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_compra, container, false);

        initializeViews(view);
        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupListeners();

        confirmDialog = new ConfirmDialog(requireContext());

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
        tvTotalContenido = view.findViewById(R.id.tvTotalContenido);
        rvProductosSeleccionados = view.findViewById(R.id.rvProductosSeleccionados);
        btnConfirmar = view.findViewById(R.id.btnConfirmar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);

        // Recuperar datos del bundle si existen
        Bundle bundle = getArguments();
        if (bundle != null) {
            Proveedor proveedor = (Proveedor) bundle.getSerializable(KEY_PROVEEDOR);
            Compra compra = (Compra) bundle.getSerializable(KEY_COMPRA);

            if (proveedor != null) {
                // Dividir la cédula en tipo y número si corresponde
                String[] cedulaPartes = proveedor.getCedula().split("-", 2);
                if (cedulaPartes.length == 2) {
                    String tipoCedula = cedulaPartes[0];
                    String numeroCedula = cedulaPartes[1];
                    setSpinnerSelection(requireContext(), spTipoCedula, tipoCedula, R.array.tipo_cedula); // Establecer el tipo de cédula
                    etCedulaProveedor.setText(numeroCedula); // Establecer el número de cédula
                }
                // Mostrar información del cliente
                tvNombreProveedorContenido.setText(proveedor.getNombre());
                tvTelefonoProveedorContenido.setText(proveedor.getTelefono());
                tvDireccionProveedorContenido.setText(proveedor.getDireccion());
            }

            if (compra != null) {
                // Configurar método de pago y número de comprobante
                setSpinnerSelection(requireContext(), spMetodoPago, compra.getMetodoPago(), R.array.metodo_pago); // Establecer el método de pago
                etNumeroComprobante.setText(String.valueOf(compra.getNumeroFactura()));

                // Mostrar el campo de comprobante solo si aplica
                if (compra.getMetodoPago().equals("Pago Movil") || compra.getMetodoPago().equals("Zelle")) {
                    etNumeroComprobante.setEnabled(true);
                    etNumeroComprobante.setVisibility(View.VISIBLE);
                } else {
                    etNumeroComprobante.setEnabled(false);
                    etNumeroComprobante.setVisibility(View.GONE);
                }
            }
        }

        // Configuración del spinner de método de pago
        spMetodoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String metodoPagoSeleccionado = parentView.getItemAtPosition(position).toString();
                if (metodoPagoSeleccionado.equals("Pago Movil") || metodoPagoSeleccionado.equals("Zelle")) {
                    etNumeroComprobante.setEnabled(true);
                    etNumeroComprobante.setVisibility(View.VISIBLE);
                } else {
                    etNumeroComprobante.setEnabled(false);
                    etNumeroComprobante.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Si no se selecciona nada, se puede establecer el primer valor o realizar alguna otra acción
                spMetodoPago.setSelection(0);
            }
        });
    }

    private void setupRecyclerView(View view) {
        rvProductosSeleccionados.setHasFixedSize(true);
        rvProductosSeleccionados.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener los productos seleccionados con cantidades desde el Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<ProductoConCantidad> productosSeleccionados = (List<ProductoConCantidad>) bundle.getSerializable("productosSeleccionados");
            if (productosSeleccionados != null && !productosSeleccionados.isEmpty()) {
                // Asignar los productos seleccionados al RecyclerView
                adaptador = new AdaptadorViewProductoSeleccionado(requireContext(), productosSeleccionados);
                rvProductosSeleccionados.setAdapter(adaptador);

                // Calcular el total de la venta
                double totalVenta = 0;
                for (ProductoConCantidad producto : productosSeleccionados) {
                    totalVenta += producto.getProducto().getPrecio() * producto.getCantidad();
                }
                tvTotalContenido.setText("Total: $" + String.format("%.2f", totalVenta));
            }
        }
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregarProducto);

        // Evento clic para agregar productos
        btnAgregar.setOnClickListener(v -> {
            // Obtener el bundle actual con los productos seleccionados y datos de la venta
            Bundle bundle = getArguments();
            List<ProductoConCantidad> productosSeleccionados = null;
            Compra compra = null;
            Proveedor proveedor = null;

            // Recuperar los datos del bundle si existen
            if (bundle != null) {
                productosSeleccionados = (List<ProductoConCantidad>) bundle.getSerializable(KEY_PRODUCTOS_SELECCIONADOS);
                compra = (Compra) bundle.getSerializable(KEY_COMPRA);
                proveedor = (Proveedor) bundle.getSerializable(KEY_PROVEEDOR);
            }

            // Inicializar productos seleccionados si es nulo
            if (productosSeleccionados == null) {
                productosSeleccionados = new ArrayList<>();
            }

            // Si no hay una venta previa, crear una nueva
            if (compra == null) {
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
                    numeroComprobante = 0; // Valor por defecto si ocurre un error
                }

                proveedor = new Proveedor(id, cedulaCompleta);
                compra = new Compra(proveedor.getIdProveedor(), metodoPago, String.valueOf(numeroComprobante));
            }

            // Crear un nuevo bundle para pasar los datos al siguiente fragmento
            Bundle nuevoBundle = new Bundle();

            // Asegurarnos de pasar correctamente los productos seleccionados
            nuevoBundle.putSerializable(KEY_PRODUCTOS_SELECCIONADOS, new ArrayList<>(productosSeleccionados));
            nuevoBundle.putSerializable(KEY_COMPRA, compra);
            nuevoBundle.putSerializable(KEY_PROVEEDOR, proveedor);

            // Navegar al fragmento AgregarProductoVenta y pasar el nuevo bundle
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.navigate(R.id.agregarProductoCompraFragment, nuevoBundle);
        });
    }

    private void setupListeners() {
        ibBuscar.setOnClickListener(v -> buscarProveedor());
        btnConfirmar.setOnClickListener(v -> guardarCompra());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
    }

    private void buscarProveedor() {
        String tipoCedula = String.valueOf(spTipoCedula.getSelectedItem());
        String cedula = String.valueOf(etCedulaProveedor.getText());
        String cedulaCompleta = tipoCedula + "-" + cedula;

//        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl(requireContext());
//        Proveedor proveedor = proveedorDao.findByCedula(cedulaCompleta);
//        if (proveedor != null) {
//            tvNombreProveedorContenido.setText(proveedor.getNombre());
//            tvTelefonoProveedorContenido.setText(proveedor.getTelefono());
//            tvDireccionProveedorContenido.setText(proveedor.getDireccion());
//        } else {
//            showNotFoundDialog();
//        }
//        ProveedorDaoImpl.close();
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

    private void showNotFoundDialog() {
        confirmDialog.showConfirmationDialog("Proveedor no encontrado", "El proveedor no existe en la base de datos, ¿desea agregarlo?", () -> {
            AgregarProveedor dialog = new AgregarProveedor();
            dialog.show(getChildFragmentManager(), "agregar-proveedor");
        });
    }

    private void limpiarCampos() {
        etCedulaProveedor.setText("");
        etNumeroComprobante.setText("");
        spTipoCedula.setSelection(0);
        spMetodoPago.setSelection(0);
        tvNombreProveedorContenido.setText("");
        tvTelefonoProveedorContenido.setText("");
        tvDireccionProveedorContenido.setText("");
        if (rvProductosSeleccionados.getAdapter() != null) {
            rvProductosSeleccionados.setAdapter(null);
        }
        setArguments(null);
        tvTotalContenido.setText("");
        // Limpiar el bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundle.clear();
        }

    }


}