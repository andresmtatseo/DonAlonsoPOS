package com.example.donalonsopos.ui.compras;

import static com.example.donalonsopos.ui.compras.ComprasFragment.KEY_COMPRA;
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
import com.example.donalonsopos.data.DAO.CompraDaoImpl;
import com.example.donalonsopos.data.DAO.DetallesCompraDaoImpl;
import com.example.donalonsopos.data.DAO.DetallesVentaDaoImpl;
import com.example.donalonsopos.data.DAO.MovimientoProductoDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DAO.ProveedorDaoImpl;
import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.data.DTO.DetallesVenta;
import com.example.donalonsopos.data.DTO.MovimientoProducto;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.ui.ventas.AdaptadorViewProductoSeleccionado;
import com.example.donalonsopos.util.ConfirmDialog;
import com.example.donalonsopos.util.ProductoConCantidad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetallesCompra extends Fragment {

    private Compra compraSeleccionada;
    private ConfirmDialog confirmDialog;

    private TextView tvNumeroCompraContenido, tvFechaContenido, tvMetodoPagoContenido, tvNumeroTransaccionContenido, tvTotalContenido, tvCedulaContenido, tvNombreContenido, tvTelefonoContenido, tvDireccionContenido;
    private RecyclerView rvProductosSeleccionados;
    private Button btnAnular;
    private AdaptadorViewProductoSeleccionado adaptador;

    private String fechaInicio, fechaFin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public DetallesCompra() {

    }

    public static DetallesCompra newInstance(Compra compra) {
        DetallesCompra fragment = new DetallesCompra();
        Bundle args = new Bundle();
        args.putSerializable(KEY_COMPRA, compra);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            compraSeleccionada = (Compra) getArguments().getSerializable(KEY_COMPRA);
        }

        confirmDialog = new ConfirmDialog(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_compra, container, false);

        if (compraSeleccionada == null) {
            Toast.makeText(getContext(), "No se ha seleccionado una compra.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return view;
        }

        initializeViews(view);
        displayProductDetails();

        return view;
    }

    private void initializeViews(View view) {
        tvNumeroCompraContenido = view.findViewById(R.id.tvNumeroCompraContenido);
        tvFechaContenido = view.findViewById(R.id.tvFechaContenido);
        tvMetodoPagoContenido = view.findViewById(R.id.tvMetodoPagoContenido);
        tvNumeroTransaccionContenido = view.findViewById(R.id.tvNumeroTransaccionContenido);
        tvTotalContenido = view.findViewById(R.id.tvTotalContenido);
        btnAnular = view.findViewById(R.id.btnAnular);
        // Proveedor
        tvCedulaContenido = view.findViewById(R.id.tvCedulaContenido);
        tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        tvTelefonoContenido = view.findViewById(R.id.tvTelefonoContenido);
        tvDireccionContenido = view.findViewById(R.id.tvDireccionContenido);
        // Detalles de la venta
        rvProductosSeleccionados = view.findViewById(R.id.rvProductosSeleccionados);

        btnAnular.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void displayProductDetails() {
        // Compra
        if (compraSeleccionada == null) return;
        tvNumeroCompraContenido.setText(String.valueOf(compraSeleccionada.getIdCompra()));
        tvFechaContenido.setText(String.valueOf(compraSeleccionada.getFechaCompra()));
        tvMetodoPagoContenido.setText(compraSeleccionada.getMetodoPago());
        tvNumeroTransaccionContenido.setText(String.valueOf(compraSeleccionada.getNumeroFactura()));
        tvTotalContenido.setText(String.valueOf(compraSeleccionada.getTotal()));
        // Proveedor
        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl(requireContext());
        Proveedor proveedorSeleccionado = proveedorDao.findById(compraSeleccionada.getIdProveedor());
        if (proveedorSeleccionado != null) {
            tvCedulaContenido.setText(proveedorSeleccionado.getCedula());
            tvNombreContenido.setText(proveedorSeleccionado.getNombre());
            tvTelefonoContenido.setText(proveedorSeleccionado.getTelefono());
            tvDireccionContenido.setText(proveedorSeleccionado.getDireccion());
        } else {
            Toast.makeText(getContext(), "Proveedor no encontrado", Toast.LENGTH_SHORT).show();
        }
        proveedorDao.close();
        // Detalles de la compra
        DetallesCompraDaoImpl detallesCompraDao = new DetallesCompraDaoImpl(requireContext());
        List<com.example.donalonsopos.data.DTO.DetallesCompra> detallesCompras = detallesCompraDao.selectByIdCompra(compraSeleccionada.getIdCompra());
        if (detallesCompras != null) {
            setupRecyclerView(detallesCompras);
        } else {
            Toast.makeText(getContext(), "Detalles de la Compra no encontrados", Toast.LENGTH_SHORT).show();
        }
        detallesCompraDao.close();
    }

    private void setupRecyclerView(List<com.example.donalonsopos.data.DTO.DetallesCompra> detallesCompras) {
        rvProductosSeleccionados.setHasFixedSize(true);
        rvProductosSeleccionados.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear una lista para los productos seleccionados
        List<ProductoConCantidad> productosSeleccionados = new ArrayList<>();

        ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
        for (com.example.donalonsopos.data.DTO.DetallesCompra detalle : detallesCompras) {
            Producto producto = productoDao.findById(detalle.getIdProducto());
            if (producto != null) {
                ProductoConCantidad productoConCantidad = new ProductoConCantidad(producto, detalle.getCantidad());
                productosSeleccionados.add(productoConCantidad);
            }
        }
        productoDao.close();

        // Crear el adaptador y asignarlo al RecyclerView
        adaptador = new AdaptadorViewProductoSeleccionado(requireContext(), productosSeleccionados);
        rvProductosSeleccionados.setAdapter(adaptador);
    }

    private void showDeleteConfirmationDialog() {
        confirmDialog.showConfirmationDialog("Anular", "¿Estás seguro de anular esta Compra?", () -> {
            // Inicializa las clases necesarias
            CompraDaoImpl compraDao = new CompraDaoImpl(requireContext());
            DetallesCompraDaoImpl detallesCompraDao = new DetallesCompraDaoImpl(requireContext());
            ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
            MovimientoProductoDaoImpl movimientoProductoDao = new MovimientoProductoDaoImpl(requireContext());

            // Eliminar la compra
            int rowsDeleted = compraDao.delete(compraSeleccionada.getIdCompra());
            if (rowsDeleted > 0) {
                // Recupera los detalles de la compra anulada
                List<com.example.donalonsopos.data.DTO.DetallesCompra> detallesCompras = detallesCompraDao.selectByIdCompra(compraSeleccionada.getIdCompra());

                // Reponer las cantidades de los productos y registrar el movimiento
                for (com.example.donalonsopos.data.DTO.DetallesCompra detalle : detallesCompras) {
                    Producto producto = productoDao.findById(detalle.getIdProducto());
                    if (producto != null) {
                        // Reponer la cantidad comprada
                        int cantidadRestaurada = producto.getCantidadActual() - detalle.getCantidad();
                        producto.setCantidadActual(cantidadRestaurada);

                        // Actualizar el producto con la nueva cantidad
                        productoDao.update(producto);

                        // Insertar el movimiento de producto (reposición)
                        MovimientoProducto movimiento = new MovimientoProducto(
                                producto.getIdProducto(),
                                0,
                                "Salida",
                                0,
                                detalle.getCantidad(),
                                dateFormat.format(new Date()),
                                "Devoluciones o Reemplazos"
                        );
                        movimientoProductoDao.insert(movimiento);
                    }
                }

                // Cerrar conexiones a bases de datos
                detallesCompraDao.close();
                productoDao.close();
                movimientoProductoDao.close();

                // Mostrar mensaje de éxito
                Toast.makeText(getContext(), "Compra anulada y stock restaurado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error al anular la compra", Toast.LENGTH_SHORT).show();
            }

            // Navegar hacia atrás
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }

}