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
import com.example.donalonsopos.data.DTO.Cliente;
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
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
        }, (view1, position) -> Toast.makeText(requireContext(), "Click largo en venta", Toast.LENGTH_SHORT).show());
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
            TextView tvFechaInicio = dialogView.findViewById(R.id.tvFechaInicio);
            TextView tvFechaFin = dialogView.findViewById(R.id.tvFechaFin);

            // Mostrar u ocultar los selectores de fechas
            radioGroupFiltros.setOnCheckedChangeListener((group, checkedId) -> {
                boolean isFechaFiltro = checkedId == R.id.rbFiltrarPorFecha;
                tvFechaInicio.setVisibility(isFechaFiltro ? View.VISIBLE : View.GONE);
                tvFechaFin.setVisibility(isFechaFiltro ? View.VISIBLE : View.GONE);
            });

            tvFechaInicio.setOnClickListener(v1 -> seleccionarFecha(true, tvFechaInicio));
            tvFechaFin.setOnClickListener(v1 -> seleccionarFecha(false, tvFechaFin));

            builder.setPositiveButton("Aplicar", (dialog, which) -> {
                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();

                if (selectedId == R.id.rbFiltrarPorIdVenta) {
                    filtroActual = FILTRO_ID_VENTA;
                } else if (selectedId == R.id.rbFiltrarPorIdCedula) {
                    filtroActual = FILTRO_CEDULA_RIF;
                } else if (selectedId == R.id.rbFiltrarPorFecha) {
                    filtroActual = FILTRO_FECHA;

                    // Validar que fechaInicio y fechaFin no sean inválidas
                    try {
                        if (fechaInicio != null && fechaFin != null) {
                            Date inicio = dateFormat.parse(fechaInicio);
                            Date fin = dateFormat.parse(fechaFin);

                            if (inicio.after(fin)) {
                                Toast.makeText(requireContext(), "La fecha desde no puede ser mayor que la fecha hasta", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Toast.makeText(requireContext(), "Selecciona ambas fechas para aplicar el filtro", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Error al validar las fechas", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                tvFiltroVentas.setText("Por " + filtroActual);
                SearchView searchView = requireView().findViewById(R.id.searchView);
                filtrarVentas(searchView.getQuery().toString());
            });


            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void seleccionarFecha(boolean esFechaInicio, TextView textView) {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar fechaSeleccionada = Calendar.getInstance();
                    fechaSeleccionada.set(year, month, dayOfMonth);

                    // Validar que la fecha seleccionada no sea mayor a la fecha actual
                    if (fechaSeleccionada.after(Calendar.getInstance())) {
                        Toast.makeText(requireContext(), "No puedes seleccionar una fecha futura", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String fechaFormateada = dateFormat.format(fechaSeleccionada.getTime());

                    if (esFechaInicio) {
                        fechaInicio = fechaFormateada;
                    } else {
                        fechaFin = fechaFormateada;
                    }
                    textView.setText(fechaFormateada);
                },
                anio, mes, dia
        );
        datePickerDialog.show();
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
                    // simular busqueda en bdd
                    Cliente cliente = new Cliente(5, "V-30465183");
                    if (String.valueOf(venta.getIdCliente()).contains(query)) {
                        ventasFiltradas.add(venta);
                    }
                    break;
                case FILTRO_FECHA:
                    try {
                        Date inicio = dateFormat.parse(fechaInicio);
                        Date fin = dateFormat.parse(fechaFin);
                        Date fechaVenta = dateFormat.parse(venta.getFechaVenta());

                        // Usar Calendar para comparar solo las fechas (ignorando hora)
                        Calendar calInicio = Calendar.getInstance();
                        calInicio.setTime(inicio);
                        calInicio.set(Calendar.HOUR_OF_DAY, 0);
                        calInicio.set(Calendar.MINUTE, 0);
                        calInicio.set(Calendar.SECOND, 0);
                        calInicio.set(Calendar.MILLISECOND, 0);

                        Calendar calFin = Calendar.getInstance();
                        calFin.setTime(fin);
                        calFin.set(Calendar.HOUR_OF_DAY, 23);
                        calFin.set(Calendar.MINUTE, 59);
                        calFin.set(Calendar.SECOND, 59);
                        calFin.set(Calendar.MILLISECOND, 999);

                        Calendar calVenta = Calendar.getInstance();
                        calVenta.setTime(fechaVenta);

                        // Comparar solo las fechas (sin hora)
                        if (!calVenta.before(calInicio) && !calVenta.after(calFin)) {
                            ventasFiltradas.add(venta);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged();
    }


    private void cargarVentas() {
        ventas.clear();
        ventas.add(new Venta(1, 5, 8, dateFormat.format(new Date()), "Efectivo", 3068484, dateFormat.format(new Date()), 100));
        ventas.add(new Venta(2, 1, 4, dateFormat.format(new Date()), "Tarjeta", 0, dateFormat.format(new Date()), 100));
        ventas.add(new Venta(3, 2, 3, dateFormat.format(new Date()), "Efectivo", 0, dateFormat.format(new Date()), 100));
        ventas.add(new Venta(4, 3, 2, dateFormat.format(new Date()), "Efectivo", 0, dateFormat.format(new Date()), 100));
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
