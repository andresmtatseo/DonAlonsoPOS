package com.example.donalonsopos.ui.compras;

import static com.example.donalonsopos.ui.compras.ComprasFragment.KEY_COMPRA;
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
import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.util.ConfirmDialog;

public class DetallesCompra extends Fragment {

    private Compra compraSeleccionada;
    private ConfirmDialog confirmDialog;

    private TextView tvNumeroCompraContenido, tvFechaContenido, tvMetodoPagoContenido, tvNumeroTransaccionContenido, tvTotalContenido;
    private Button btnAnular;

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

        // Falta datos proveedor y detalles compra (productos)

        btnAnular.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void displayProductDetails() {
        if (compraSeleccionada == null) return;

        tvNumeroCompraContenido.setText(String.valueOf(compraSeleccionada.getIdCompra()));
        tvFechaContenido.setText(String.valueOf(compraSeleccionada.getFechaCompra()));
        tvMetodoPagoContenido.setText(compraSeleccionada.getMetodoPago());
        tvNumeroTransaccionContenido.setText(String.valueOf(compraSeleccionada.getNumeroFactura()));
        tvTotalContenido.setText(String.valueOf(compraSeleccionada.getTotal()));
    }

    private void showDeleteConfirmationDialog() {
        confirmDialog.showConfirmationDialog("Anular", "¿Estás seguro de anular esta Compra?", () -> {
            Toast.makeText(getContext(), "Compra anulada con éxito", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.popBackStack();
        });
    }

    private Bundle createBundleWithCompra(Compra Compra) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_VENTA, Compra);
        return bundle;
    }
}