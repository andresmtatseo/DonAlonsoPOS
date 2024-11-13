package com.example.donalonsopos.ui.compras;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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