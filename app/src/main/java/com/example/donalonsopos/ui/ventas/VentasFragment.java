package com.example.donalonsopos.ui.ventas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasFragment extends Fragment {

    public static final String KEY_VENTA = "venta";

    private static final String FILTRO_ID_VENTA = "ID Venta";
    private static final String FILTRO_CEDULA_RIF = "Cédula/RIF";
    private static final String FILTRO_FECHA = "Fecha";
    private String filtroActual = FILTRO_ID_VENTA;

    private RecyclerView listaVentas;
    private TextView tvFiltroVentas;
    private ImageButton ibFiltroVentas;
    private AdaptadorViewVenta adaptador;
    private List<Venta> ventas = new ArrayList<>();
    private List<Venta> ventasFiltradas = new ArrayList<>();

    private String fechaInicio, fechaFin;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);

        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupSwipeRefresh(view);
        setupSearchView(view);
        setupFilterButton(view);

        cargarVentas();

        return view;
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.navigate(R.id.agregarVentaFragment);
        });
    }


    private void setupRecyclerView(View view) {
        listaVentas = view.findViewById(R.id.lista);
        listaVentas.setHasFixedSize(true);
        listaVentas.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new AdaptadorViewVenta(requireContext(), ventasFiltradas, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Venta ventaSeleccionada = ventasFiltradas.get(position);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.detallesVentaFragment, createBundleWithVenta(ventaSeleccionada));
            }
        }, new OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(requireContext(), "Click largo en venta", Toast.LENGTH_SHORT).show();
            }
        });
        listaVentas.setAdapter(adaptador);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupSwipeRefresh(View view) {
        SwipeRefreshLayout swipeToRefresh = view.findViewById(R.id.swipeRefreshLayout);
        swipeToRefresh.setOnRefreshListener(() -> {
            cargarVentas();
            adaptador.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            Toast.makeText(requireContext(), "Ventas actualizadas", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSearchView(View view) {
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarVentas(newText);
                return true;
            }
        });
    }

    private void setupFilterButton(View view) {
        ibFiltroVentas = view.findViewById(R.id.ibFiltro);
        tvFiltroVentas = view.findViewById(R.id.tvFiltro);
        tvFiltroVentas.setText("Por " + filtroActual);

        ibFiltroVentas.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Filtrar Ventas");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_ventas, null);
            builder.setView(dialogView);

            RadioGroup radioGroupFiltros = dialogView.findViewById(R.id.radioGroupFiltros);

            builder.setPositiveButton("Aplicar", (dialog, which) -> {
                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();

                if (selectedId == R.id.rbFiltrarPorIdVenta) {
                    filtroActual = FILTRO_ID_VENTA;
                } else if (selectedId == R.id.rbFiltrarPorCedula) {
                    filtroActual = FILTRO_CEDULA_RIF;
                } else if (selectedId == R.id.rbFiltrarPorFecha) {
                    filtroActual = FILTRO_FECHA;
                }
                tvFiltroVentas.setText("Por " + filtroActual);
                SearchView searchView = view.findViewById(R.id.searchView);
                filtrarVentas(searchView.getQuery().toString());
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void filtrarVentas(String textoBusqueda) {
        ventasFiltradas.clear();
        String query = textoBusqueda.toLowerCase().trim();

        for (Venta venta : ventas) {
            switch (filtroActual) {
                case FILTRO_ID_VENTA:
                    if (String.valueOf(venta.getIdVenta()).contains(query)) {
                        ventasFiltradas.add(venta);
                    }
                    break;
                case FILTRO_CEDULA_RIF:
                    //if (venta.getCedulaRifCliente().toLowerCase().contains(query)) {
                    //    ventasFiltradas.add(venta);
                    // }
                    break;
                case FILTRO_FECHA:
                    if (fechaInicio != null && fechaFin != null) {
                        // Aquí va la lógica de comparación de fechas
                        // if (venta.getFechaVenta().after(fechaInicio) && venta.getFechaVenta().before(fechaFin))
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged();
    }

    private void cargarVentas() {
        ventas.clear();
        ventas.add(new Venta(1, 5, 8, new Date(), "Efectivo", 3068484, new Date(), 100));
        ventas.add(new Venta(2, 1, 4, new Date(), "Tarjeta", 0, new Date(), 100));
        ventas.add(new Venta(3, 2, 3, new Date(), "Efectivo", 0, new Date(), 100));
        ventas.add(new Venta(4, 3, 2, new Date(), "Efectivo", 0, new Date(), 100));
        ventasFiltradas.clear();
        ventasFiltradas.addAll(ventas);
        adaptador.notifyDataSetChanged();
    }

    private Bundle createBundleWithVenta(Venta venta) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_VENTA, venta);
        return bundle;
    }
}