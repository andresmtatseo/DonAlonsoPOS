package com.example.donalonsopos.ui.ventas;

import static com.example.donalonsopos.ui.productos.ProductosFragment.KEY_PRODUCTO;
import static com.example.donalonsopos.ui.ventas.VentasFragment.KEY_VENTA;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.util.ConfirmDialog;

public class DetallesVenta extends Fragment {

    private Venta ventaSeleccionada;
    private ConfirmDialog confirmDialog;

    private TextView tvNumeroVentaContenido, tvFechaContenido, tvMetodoPagoContenido, tvNumeroTransaccionContenido, tvTotalContenido;
    private Button btnAnular;

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

        // Falta datos clientes y detallesVenta (productos)

        btnAnular.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void displayProductDetails() {
        if (ventaSeleccionada == null) return;

        tvNumeroVentaContenido.setText(String.valueOf(ventaSeleccionada.getIdVenta()));
        tvFechaContenido.setText(String.valueOf(ventaSeleccionada.getFechaVenta()));
        tvMetodoPagoContenido.setText(ventaSeleccionada.getMetodoPago());
        tvNumeroTransaccionContenido.setText(String.valueOf(ventaSeleccionada.getNumeroTransaccion()));
        tvTotalContenido.setText(String.valueOf(ventaSeleccionada.getTotal()));
    }

    private void showDeleteConfirmationDialog() {
        confirmDialog.showConfirmationDialog("Anular", "¿Estás seguro de anular esta Venta?", () -> {
            Toast.makeText(getContext(), "Venta anulada con éxito", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }

    private Bundle createBundleWithVenta(Venta venta) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_VENTA, venta);
        return bundle;
    }
}