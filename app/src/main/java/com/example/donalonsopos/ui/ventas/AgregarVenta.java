package com.example.donalonsopos.ui.ventas;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.util.ConfirmDialog;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.example.donalonsopos.util.ProductoConCantidad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AgregarVenta extends Fragment {

    private EditText etCedulaCliente, etNumeroComprobante;
    private Spinner spTipoCedula, spMetodoPago;
    private ImageButton ibBuscar;
    private TextView tvNombreClienteContenido, tvApellidoClienteContenido, tvDireccionClienteContenido, tvTotalVentaContenido;
    private AdaptadorViewProductoSeleccionado adaptador;
    private RecyclerView rvProductosSeleccionados;
    private Button btnConfirmar, btnLimpiar;
    private ConfirmDialog confirmDialog;

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
        btnAgregar.setOnClickListener(v -> {
            // Obtener el bundle actual con los productos seleccionados
            Bundle bundle = getArguments();
            List<ProductoConCantidad> productosSeleccionados = null;
            if (bundle != null) {
                productosSeleccionados = (List<ProductoConCantidad>) bundle.getSerializable("productosSeleccionados");
            }

            // Crear un nuevo bundle para pasar los productos al siguiente fragmento
            Bundle nuevoBundle = new Bundle();
            nuevoBundle.putSerializable("productosSeleccionados", (ArrayList<ProductoConCantidad>) productosSeleccionados);

            // Navegar al fragmento AgregarProductoVenta y pasar el bundle
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
        ibBuscar = view.findViewById(R.id.ibBuscar);
        btnConfirmar = view.findViewById(R.id.btnConfirmar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
    }

    private void setupListeners() {
        ibBuscar.setOnClickListener(v -> buscarCliente());
        btnConfirmar.setOnClickListener(v -> guardarVenta());
        btnLimpiar.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void buscarCliente() {
        String cedulaCliente = String.valueOf(etCedulaCliente.getText());
    }

    private void guardarVenta() {
        String cedula = etCedulaCliente.getText().toString().trim();
        String numeroComprobante = etNumeroComprobante.getText().toString().trim();
        String tipoCedula = spTipoCedula.getSelectedItem() != null ? spTipoCedula.getSelectedItem().toString().trim() : "";
        String metodoPago = spMetodoPago.getSelectedItem() != null ? spMetodoPago.getSelectedItem().toString().trim() : "";

        // productos seleccionados
        if (cedula.isEmpty() || numeroComprobante.isEmpty() || tipoCedula.isEmpty() || metodoPago.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rvProductosSeleccionados.getAdapter() == null || rvProductosSeleccionados.getAdapter().getItemCount() == 0) {
            Toast.makeText(getContext(), "Por favor, seleccione al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí agregamos la lógica para guardar el producto (en base de datos o en memoria)
        Toast.makeText(getContext(), "Venta guardada correctamente", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    private void showDeleteConfirmationDialog() {
        confirmDialog.showConfirmationDialog("Limpiar", "¿Estás seguro de limpiar todo el contenido?", () -> {
            Toast.makeText(getContext(), "Los campos fueron limpiados", Toast.LENGTH_SHORT).show();
            limpiarCampos();
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
        rvProductosSeleccionados.setAdapter(null);
    }
}
