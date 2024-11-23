package com.example.donalonsopos.ui.ventas;

import static com.example.donalonsopos.ui.productos.ProductosFragment.KEY_PRODUCTO;
import static com.example.donalonsopos.ui.ventas.VentasFragment.KEY_VENTA;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.ClienteDaoImpl;
import com.example.donalonsopos.data.DAO.DetallesVentaDaoImpl;
import com.example.donalonsopos.data.DAO.MovimientoProductoDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DAO.VentaDaoImpl;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.data.DTO.MovimientoProducto;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.util.ConfirmDialog;
import com.example.donalonsopos.util.ProductoConCantidad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetallesVenta extends Fragment {

    private Venta ventaSeleccionada;
    private ConfirmDialog confirmDialog;
    private AdaptadorViewProductoSeleccionado adaptador;
    private TextView tvNumeroVentaContenido, tvFechaContenido, tvMetodoPagoContenido, tvNumeroTransaccionContenido, tvTotalContenido, tvCedulaContenido, tvNombreContenido, tvApellidoContenido, tvDireccionContenido;
    private RecyclerView lista;
    private Button btnAnular;

    private String fechaInicio, fechaFin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public DetallesVenta() {

    }

    public static DetallesVenta newInstance(Venta venta) {
        DetallesVenta fragment = new DetallesVenta();
        Bundle args = new Bundle();
        args.putSerializable(KEY_VENTA, venta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ventaSeleccionada = (Venta) getArguments().getSerializable(KEY_VENTA);
        }
        confirmDialog = new ConfirmDialog(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_venta, container, false);

        if (ventaSeleccionada == null) {
            Toast.makeText(getContext(), "No se ha seleccionado una venta.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return view;
        }

        initializeViews(view);
        displayProductDetails();

        return view;
    }

    private void initializeViews(View view) {
        tvNumeroVentaContenido = view.findViewById(R.id.tvNumeroVentaContenido);
        tvFechaContenido = view.findViewById(R.id.tvFechaContenido);
        tvMetodoPagoContenido = view.findViewById(R.id.tvMetodoPagoContenido);
        tvNumeroTransaccionContenido = view.findViewById(R.id.tvNumeroTransaccionContenido);
        tvTotalContenido = view.findViewById(R.id.tvTotalContenido);
        btnAnular = view.findViewById(R.id.btnAnular);

        // cliente
        tvCedulaContenido = view.findViewById(R.id.tvCedulaContenido);
        tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        tvApellidoContenido = view.findViewById(R.id.tvApellidoContenido);
        tvDireccionContenido = view.findViewById(R.id.tvDireccionContenido);

        // detallesVenta
        lista = view.findViewById(R.id.lista);
        setupRecyclerView(view);

        btnAnular.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void displayProductDetails() {
        if (ventaSeleccionada == null) return;

        tvNumeroVentaContenido.setText(String.valueOf(ventaSeleccionada.getIdVenta()));
        tvFechaContenido.setText(String.valueOf(ventaSeleccionada.getFechaVenta()));
        tvMetodoPagoContenido.setText(ventaSeleccionada.getMetodoPago());
        tvNumeroTransaccionContenido.setText(String.valueOf(ventaSeleccionada.getNumeroTransaccion()));
        tvTotalContenido.setText("$" + String.valueOf(ventaSeleccionada.getTotal()));

        ClienteDaoImpl clienteDao = new ClienteDaoImpl(requireContext());
        Cliente cliente = clienteDao.findById(ventaSeleccionada.getIdCliente());
        if (cliente != null) {
            tvCedulaContenido.setText(cliente.getCedula());
            tvNombreContenido.setText(cliente.getNombre());
            tvApellidoContenido.setText(cliente.getApellido());
            tvDireccionContenido.setText(cliente.getDireccion());
        }
        clienteDao.close();
    }

    private void setupRecyclerView(View view) {
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener detalles de la venta
        List<com.example.donalonsopos.data.DTO.DetallesVenta> detallesVentas = new ArrayList<>();
        DetallesVentaDaoImpl detalleVentaDao = new DetallesVentaDaoImpl(requireContext());
        detallesVentas.addAll(detalleVentaDao.selectByIdVenta(ventaSeleccionada.getIdVenta()));
        detalleVentaDao.close();

        // Crear una lista para los productos seleccionados
        List<ProductoConCantidad> productosSeleccionados = new ArrayList<>();

        // Obtener productos para cada idProducto en los detalles de venta
        ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
        for (com.example.donalonsopos.data.DTO.DetallesVenta detalle : detallesVentas) {
            // Obtener producto por idProducto
            Producto producto = productoDao.findById(detalle.getIdProducto());

            if (producto != null) {
                // Crear un ProductoConCantidad para almacenar el producto y la cantidad
                ProductoConCantidad productoConCantidad = new ProductoConCantidad(producto, detalle.getCantidad());
                productosSeleccionados.add(productoConCantidad);
            }
        }
        productoDao.close();

        // Crear el adaptador y asignarlo al RecyclerView
        adaptador = new AdaptadorViewProductoSeleccionado(requireContext(), productosSeleccionados);
        lista.setAdapter(adaptador);
    }


    private void showDeleteConfirmationDialog() {
        confirmDialog.showConfirmationDialog("Anular", "¿Estás seguro de anular esta Venta?", () -> {
            // Inicializa las clases necesarias
            VentaDaoImpl ventaDao = new VentaDaoImpl(requireContext());
            DetallesVentaDaoImpl detallesVentaDao = new DetallesVentaDaoImpl(requireContext());
            ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
            MovimientoProductoDaoImpl movimientoProductoDao = new MovimientoProductoDaoImpl(requireContext());

            // Eliminar la venta
            int rowsDeleted = ventaDao.delete(ventaSeleccionada.getIdVenta());
            if (rowsDeleted > 0) {
                // Recupera los detalles de la venta anulada
                List<com.example.donalonsopos.data.DTO.DetallesVenta> detallesVentas = detallesVentaDao.selectByIdVenta(ventaSeleccionada.getIdVenta());

                // Reponer las cantidades de los productos y registrar el movimiento
                for (com.example.donalonsopos.data.DTO.DetallesVenta detalle : detallesVentas) {
                    Producto producto = productoDao.findById(detalle.getIdProducto());
                    if (producto != null) {
                        // Reponer la cantidad vendida
                        int cantidadRestaurada = producto.getCantidadActual() + detalle.getCantidad();
                        producto.setCantidadActual(cantidadRestaurada);

                        // Actualizar el producto con la nueva cantidad
                        productoDao.update(producto);

                        // Insertar el movimiento de producto (reposición)
                        MovimientoProducto movimiento = new MovimientoProducto(
                                producto.getIdProducto(),
                                0,
                                "Entrada",
                                0,
                                detalle.getCantidad(),
                                dateFormat.format(new Date()),
                                "Devoluciones o Reemplazos"
                        );
                        movimientoProductoDao.insert(movimiento);
                    }
                }

                // Cerrar conexiones a bases de datos
                detallesVentaDao.close();
                productoDao.close();
                movimientoProductoDao.close();

                // Mostrar mensaje de éxito
                Toast.makeText(getContext(), "Venta anulada y stock repuesto con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error al anular la venta", Toast.LENGTH_SHORT).show();
            }

            // Navegar hacia atrás
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }


}