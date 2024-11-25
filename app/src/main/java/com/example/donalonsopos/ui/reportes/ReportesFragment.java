package com.example.donalonsopos.ui.reportes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.CompraDaoImpl;
import com.example.donalonsopos.data.DAO.MovimientoProductoDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DAO.VentaDaoImpl;

public class ReportesFragment extends Fragment {

    private Spinner spReportes;
    private Spinner spVentas;
    private Spinner spCompras;
    private Spinner spProductos;
    private TextView tvFechaInicio;
    private TextView tvFechaFin;
    private Button btnGenerarReporte;

    // Variables para almacenar la selección actual
    private String reporteSeleccionado = "";
    private String subReporteSeleccionado = "";

    public ReportesFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reportes, container, false);

        spReportes = view.findViewById(R.id.spReportes);
        spVentas = view.findViewById(R.id.spVentas);
        spCompras = view.findViewById(R.id.spCompras);
        spProductos = view.findViewById(R.id.spProductos);
        tvFechaInicio = view.findViewById(R.id.tvFechaInicio);
        tvFechaFin = view.findViewById(R.id.tvFechaFin);
        btnGenerarReporte = view.findViewById(R.id.btnGenerarReporte);

        ArrayAdapter<CharSequence> adapterReportes = ArrayAdapter.createFromResource(getContext(),
                R.array.modulo_reportes, android.R.layout.simple_spinner_item);
        adapterReportes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReportes.setAdapter(adapterReportes);

        spReportes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reporteSeleccionado = parent.getItemAtPosition(position).toString();
                spVentas.setVisibility(View.GONE);
                spCompras.setVisibility(View.GONE);
                spProductos.setVisibility(View.GONE);
                tvFechaInicio.setVisibility(View.GONE);
                tvFechaFin.setVisibility(View.GONE);

                switch (reporteSeleccionado) {
                    case "Ventas":
                        spVentas.setVisibility(View.VISIBLE);
                        break;
                    case "Compras":
                        spCompras.setVisibility(View.VISIBLE);
                        break;
                    case "Productos":
                        spProductos.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                reporteSeleccionado = "";
            }
        });

        setupSecondarySpinner(spVentas, R.array.ventas_reportes);
        setupSecondarySpinner(spCompras, R.array.compras_reportes);
        setupSecondarySpinner(spProductos, R.array.productos_reportes);

        btnGenerarReporte.setOnClickListener(v -> {
            generarReporte();
        });

        return view;
    }

    private void setupSecondarySpinner(Spinner spinner, int arrayResource) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subReporteSeleccionado = parent.getItemAtPosition(position).toString();
                if (subReporteSeleccionado.contains("fecha")) {
                    tvFechaInicio.setVisibility(View.VISIBLE);
                    tvFechaFin.setVisibility(View.VISIBLE);
                } else {
                    tvFechaInicio.setVisibility(View.GONE);
                    tvFechaFin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subReporteSeleccionado = "";
            }
        });
    }

    private void generarReporte() {
        if (reporteSeleccionado.isEmpty() || subReporteSeleccionado.isEmpty()) {
            Toast.makeText(getContext(), "Por favor seleccione un tipo de reporte", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (subReporteSeleccionado) {
            case "Ventas por fecha":
                generarReporteVentasPorFecha();
                break;
            case "Ventas por Clientes":
                generarReporteVentasPorClientes();
                break;
            case "Compras por fecha":
                generarReporteComprasPorFecha();
                break;
            case "Compras por proveedor":
                generarReporteComprasPorProveedor();
                break;
            case "Movimientos de productos por fecha":
                generarReporteMovimientosPorFecha();
                break;
            case "Movimientos por producto":
                generarReporteMovimientosPorProducto();
                break;
            case "Productos en Inventario":
                generarReporteProducto();
                break;
            default:
                Toast.makeText(getContext(), "Opción no válida", Toast.LENGTH_SHORT).show();
                break;
        }
    }

// Funciones específicas para cada tipo de reporte

    private void generarReporteVentasPorFecha() {
        VentaDaoImpl ventaDao = new VentaDaoImpl(requireContext());
        ReporteVenta reporteVentas = new ReporteVenta(getContext());
        reporteVentas.crearReporteVentas(ventaDao.select());
        ventaDao.close();
        Toast.makeText(getContext(), "Reporte de ventas generado", Toast.LENGTH_SHORT).show();
    }

    private void generarReporteVentasPorClientes() {
        VentaDaoImpl ventaDao = new VentaDaoImpl(requireContext());
        ReporteVenta reporteVentas = new ReporteVenta(getContext());
        reporteVentas.crearReporteVentas(ventaDao.select());
        ventaDao.close();
        Toast.makeText(getContext(), "Reporte de ventas generado", Toast.LENGTH_SHORT).show();
    }

    private void generarReporteComprasPorFecha() {
        CompraDaoImpl compraDao = new CompraDaoImpl(requireContext());
        ReporteCompra reporteCompras = new ReporteCompra(getContext());
        reporteCompras.crearInformeCompras(compraDao.select());
        compraDao.close();
        Toast.makeText(getContext(), "Reporte de compras generado", Toast.LENGTH_SHORT).show();
    }

    private void generarReporteComprasPorProveedor() {
        CompraDaoImpl compraDao = new CompraDaoImpl(requireContext());
        ReporteCompra reporteCompras = new ReporteCompra(getContext());
        reporteCompras.crearInformeCompras(compraDao.select());
        compraDao.close();
        Toast.makeText(getContext(), "Reporte de compras generado", Toast.LENGTH_SHORT).show();
    }

    private void generarReporteMovimientosPorFecha() {
        MovimientoProductoDaoImpl movimientoDao = new MovimientoProductoDaoImpl(requireContext());
        ReporteMovimientoProducto reporteMovimientos = new ReporteMovimientoProducto(getContext());
        reporteMovimientos.crearReporteMovimientoProducto(movimientoDao.select());
        movimientoDao.close();
        Toast.makeText(getContext(), "Reporte de movimientos generado", Toast.LENGTH_SHORT).show();
    }

    private void generarReporteMovimientosPorProducto() {
        MovimientoProductoDaoImpl movimientoDao = new MovimientoProductoDaoImpl(requireContext());
        ReporteMovimientoProducto reporteMovimientos = new ReporteMovimientoProducto(getContext());
        reporteMovimientos.crearReporteMovimientoProducto(movimientoDao.select());
        movimientoDao.close();
        Toast.makeText(getContext(), "Reporte de movimientos generado", Toast.LENGTH_SHORT).show();
    }

    private void generarReporteProducto() {
        ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
        ReporteProducto reporteProducto = new ReporteProducto(getContext());
        reporteProducto.crearReporteProducto(productoDao.select());
        productoDao.close();
        Toast.makeText(getContext(), "Reporte de productos generado", Toast.LENGTH_SHORT).show();
    }
}

