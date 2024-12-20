package com.example.donalonsopos.ui.ventas;

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
import com.example.donalonsopos.data.DAO.DetallesVentaDaoImpl;
import com.example.donalonsopos.data.DAO.MovimientoProductoDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DAO.VentaDaoImpl;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.data.DTO.DetallesVenta;
import com.example.donalonsopos.data.DTO.MovimientoProducto;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.ui.clientes.AgregarCliente;
import com.example.donalonsopos.util.ConfirmDialog;
import com.example.donalonsopos.util.ProductoConCantidad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AgregarVenta extends Fragment {

    private EditText etCedulaCliente, etNumeroComprobante;
    private Spinner spTipoCedula, spMetodoPago;
    private ImageButton ibBuscar;
    private TextView tvNombreClienteContenido, tvApellidoClienteContenido, tvDireccionClienteContenido, tvTotalVentaContenido;
    private AdaptadorViewProductoSeleccionado adaptador;
    private RecyclerView rvProductosSeleccionados;
    private Button btnConfirmar, btnLimpiar;
    private ConfirmDialog confirmDialog;

    private String fechaInicio, fechaFin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public AgregarVenta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_venta, container, false);

        setupFloatingActionButton(view);
        initializeViews(view);
        setupRecyclerView(view);
        setupListeners();

        confirmDialog = new ConfirmDialog(requireContext());

        return view;
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
                tvTotalVentaContenido.setText("Total: $" + String.format("%.2f", totalVenta));
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
                String cedula = etCedulaCliente.getText().toString().trim();
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

                // Crear cliente con datos de cédula
                cliente = new Cliente(id, cedulaCompleta);

                // Obtener los valores de los campos, aunque puedan estar vacíos
                String nombre = tvNombreClienteContenido.getText().toString().trim();
                String apellido = tvApellidoClienteContenido.getText().toString().trim();
                String direccion = tvDireccionClienteContenido.getText().toString().trim();

                // Establecer los valores del cliente, aunque estén vacíos
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setDireccion(direccion);

                // Crear la venta con los datos de cliente y método de pago
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
            navController.navigate(R.id.agregarProductoVentaFragment, nuevoBundle);
        });
    }



    private void initializeViews(View view) {
        etCedulaCliente = view.findViewById(R.id.etCedulaCliente);
        etNumeroComprobante = view.findViewById(R.id.etNumeroComprobante);
        spTipoCedula = view.findViewById(R.id.spTipoCedula);
        spMetodoPago = view.findViewById(R.id.spMetodoPago);
        ibBuscar = view.findViewById(R.id.ibBuscar);
        tvNombreClienteContenido = view.findViewById(R.id.tvNombreClienteContenido);
        tvApellidoClienteContenido = view.findViewById(R.id.tvApellidoClienteContenido);
        tvDireccionClienteContenido = view.findViewById(R.id.tvDireccionClienteContenido);
        tvTotalVentaContenido = view.findViewById(R.id.tvTotalVentaContenido);
        rvProductosSeleccionados = view.findViewById(R.id.rvProductosSeleccionados);
        btnConfirmar = view.findViewById(R.id.btnConfirmar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);

        // Recuperar datos del bundle si existen
        Bundle bundle = getArguments();
        if (bundle != null) {
            Cliente cliente = (Cliente) bundle.getSerializable("cliente");
            Venta venta = (Venta) bundle.getSerializable("venta");

            if (cliente != null) {
                // Dividir la cédula en tipo y número si corresponde
                String[] cedulaPartes = cliente.getCedula().split("-", 2);
                if (cedulaPartes.length == 2) {
                    String tipoCedula = cedulaPartes[0];
                    String numeroCedula = cedulaPartes[1];
                    setSpinnerSelection(requireContext(), spTipoCedula, tipoCedula, R.array.tipo_cedula); // Establecer el tipo de cédula
                    etCedulaCliente.setText(numeroCedula); // Establecer el número de cédula
                }
                // Mostrar información del cliente
                tvNombreClienteContenido.setText(cliente.getNombre());
                tvApellidoClienteContenido.setText(cliente.getApellido());
                tvDireccionClienteContenido.setText(cliente.getDireccion());
            }

            if (venta != null) {
                // Configurar método de pago y número de comprobante
                setSpinnerSelection(requireContext(), spMetodoPago, venta.getMetodoPago(), R.array.metodo_pago); // Establecer el método de pago
                etNumeroComprobante.setText(String.valueOf(venta.getNumeroTransaccion()));

                // Mostrar el campo de comprobante solo si aplica
                if (venta.getMetodoPago().equals("Pago Movil") || venta.getMetodoPago().equals("Zelle")) {
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

    private void setupListeners() {
        ibBuscar.setOnClickListener(v -> buscarCliente());
        btnConfirmar.setOnClickListener(v -> guardarVenta());
        btnLimpiar.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void buscarCliente() {
        String tipoCedula = String.valueOf(spTipoCedula.getSelectedItem());
        String cedula = String.valueOf(etCedulaCliente.getText());
        String cedulaCompleta = tipoCedula + "-" + cedula;

        ClienteDaoImpl clienteDao = new ClienteDaoImpl(requireContext());
        Cliente cliente = clienteDao.findByCedula(cedulaCompleta);
        if (cliente != null) {
            tvNombreClienteContenido.setText(cliente.getNombre());
            tvApellidoClienteContenido.setText(cliente.getApellido());
            tvDireccionClienteContenido.setText(cliente.getDireccion());
        } else {
            showNotFoundDialog();
        }
        clienteDao.close();
    }

    private void guardarVenta() {
        String tipoCedula = String.valueOf(spTipoCedula.getSelectedItem());
        String cedula = etCedulaCliente.getText().toString().trim();
        String cedulaCompleta = tipoCedula + "-" + cedula;
        String numeroComprobante = etNumeroComprobante.getText().toString().trim();
        String metodoPago = spMetodoPago.getSelectedItem() != null
                ? spMetodoPago.getSelectedItem().toString().trim()
                : "";

        boolean hayError = false;

        // Validaciones
        if (cedula.isEmpty()) {
            etCedulaCliente.setError("Este campo es obligatorio");
            hayError = true;
        } else {
            etCedulaCliente.setError(null);
        }

        ClienteDaoImpl clienteDao = new ClienteDaoImpl(requireContext());
        Cliente cliente = clienteDao.findByCedula(cedulaCompleta);
        clienteDao.close();

        if (cliente == null) {
            showNotFoundDialog();
            hayError = true;
        }

        if (etNumeroComprobante.isEnabled() && etNumeroComprobante.getVisibility() == View.VISIBLE) {
            if (numeroComprobante.isEmpty()) {
                etNumeroComprobante.setError("Este campo es obligatorio");
                hayError = true;
            } else {
                etNumeroComprobante.setError(null);
            }
        }

        if (rvProductosSeleccionados.getAdapter() == null || rvProductosSeleccionados.getAdapter().getItemCount() == 0) {
            Toast.makeText(getContext(), "Por favor, seleccione al menos un producto", Toast.LENGTH_SHORT).show();
            hayError = true;
        }

        int numeroComprobanteInt = 0;
        try {
            numeroComprobanteInt = Integer.parseInt(numeroComprobante);
        } catch (NumberFormatException e) {
            etNumeroComprobante.setError("El número de comprobante debe ser un número entero");
            hayError = true;
        }

        if (hayError) {
            return;
        }

        float totalVenta = 0;
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<ProductoConCantidad> productosSeleccionados = (List<ProductoConCantidad>) bundle.getSerializable("productosSeleccionados");
            if (productosSeleccionados != null && !productosSeleccionados.isEmpty()) {
                // Calcular el total de la venta
                for (ProductoConCantidad producto : productosSeleccionados) {
                    totalVenta += producto.getProducto().getPrecio() * producto.getCantidad();
                }
            }
        }

        // Guardar la venta y obtener el ID generado
        VentaDaoImpl ventaDao = new VentaDaoImpl(requireContext());
        Venta nuevaVenta = new Venta(0, cliente.getIdCliente(), dateFormat.format(new Date()), metodoPago, numeroComprobanteInt, dateFormat.format(new Date()), totalVenta);
        long idVentaGenerado = ventaDao.insert(nuevaVenta);
        ventaDao.close();

        if (idVentaGenerado > 0) {
            // Guardar los productos seleccionados como detalles de la venta
            if (bundle != null) {
                List<ProductoConCantidad> productosSeleccionados = (List<ProductoConCantidad>) bundle.getSerializable("productosSeleccionados");
                if (productosSeleccionados != null && !productosSeleccionados.isEmpty()) {
                    DetallesVentaDaoImpl detalleVentaDao = new DetallesVentaDaoImpl(requireContext());
                    MovimientoProductoDaoImpl movimientoProductoDao = new MovimientoProductoDaoImpl(requireContext());
                    ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());

                    for (ProductoConCantidad producto : productosSeleccionados) {
                        // Guardar el detalle de la venta
                        detalleVentaDao.insert(new DetallesVenta((int) idVentaGenerado,
                                producto.getProducto().getIdProducto(),
                                producto.getCantidad(),
                                (float) producto.getProducto().getPrecio()));

                        // Descontar la cantidad vendida del inventario
                        Producto productoInventario = productoDao.findById(producto.getProducto().getIdProducto());
                        if (productoInventario != null) {
                            int cantidadRestante = productoInventario.getCantidadActual() - producto.getCantidad();
                            productoInventario.setCantidadActual(cantidadRestante);

                            // Actualizar el producto en la base de datos
                            productoDao.update(productoInventario);

                            // Registrar el movimiento en el inventario (venta)
                            MovimientoProducto movimiento = new MovimientoProducto(
                                    productoInventario.getIdProducto(),
                                    0,
                                    "Salida",
                                    0,
                                    producto.getCantidad(),
                                    dateFormat.format(new Date()),
                                    "Venta realizada"
                            );
                            movimientoProductoDao.insert(movimiento);
                        }
                    }
                    detalleVentaDao.close();
                    movimientoProductoDao.close();
                    productoDao.close();
                }
            }

            Toast.makeText(getContext(), "Venta guardada correctamente", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
            limpiarCampos();
        } else {
            Toast.makeText(getContext(), "Error al guardar la venta", Toast.LENGTH_SHORT).show();
        }
    }



    private void showDeleteConfirmationDialog() {
        confirmDialog.showConfirmationDialog("Limpiar", "¿Estás seguro de limpiar todo el contenido?", () -> {
            Toast.makeText(getContext(), "Los campos fueron limpiados", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        });
    }

    private void showNotFoundDialog() {
        confirmDialog.showConfirmationDialog("Cliente no encontrado", "El cliente no existe en la base de datos, ¿desea agregarlo?", () -> {
            AgregarCliente dialog = new AgregarCliente();
            dialog.show(getChildFragmentManager(), "agregar_cliente");
        });
    }

    private void limpiarCampos() {
        etCedulaCliente.setText("");
        etNumeroComprobante.setText("");
        spTipoCedula.setSelection(0);
        spMetodoPago.setSelection(0);
        tvNombreClienteContenido.setText("");
        tvApellidoClienteContenido.setText("");
        tvDireccionClienteContenido.setText("");
        tvTotalVentaContenido.setText("");
        if (rvProductosSeleccionados.getAdapter() != null) {
            rvProductosSeleccionados.setAdapter(null);
        }
        // Limpiar el bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundle.clear();
        }
    }
}
