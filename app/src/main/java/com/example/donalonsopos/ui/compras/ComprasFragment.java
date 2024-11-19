package com.example.donalonsopos.ui.compras;

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
import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComprasFragment extends Fragment {

    public static final String KEY_COMPRA = "compra";

    private static final String FILTRO_ID_PROVEEDOR = "ID Proveedor";
    private static final String FILTRO_FECHA = "Fecha";
    private String filtroActual = FILTRO_ID_PROVEEDOR;

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private AdaptadorViewCompra adaptador;
    private List<Compra> compras = new ArrayList<>();
    private List<Compra> comprasFiltradas = new ArrayList<>();

    private String fechaInicio, fechaFin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ComprasFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);

        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupSwipeRefresh(view);
        setupSearchView(view);
        setupFilterButton(view);

        cargarCompras();

        return view;
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
            navController.navigate(R.id.agregarCompraFragment);
        });
    }

    private void setupRecyclerView(View view) {
        lista = view.findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new AdaptadorViewCompra(requireContext(), comprasFiltradas, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Compra compraSeleccionada = comprasFiltradas.get(position);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.detallesCompraFragment, createBundleWithCompra(compraSeleccionada));
            }
        }, new OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(requireContext(), "Click largo en compra", Toast.LENGTH_SHORT).show();
            }
        });

        lista.setAdapter(adaptador);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupSwipeRefresh(View view) {
        SwipeRefreshLayout swipeToRefresh = view.findViewById(R.id.swipeRefreshLayout);
        swipeToRefresh.setOnRefreshListener(() -> {
            cargarCompras();
            adaptador.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            Toast.makeText(requireContext(), "Compras actualizadas", Toast.LENGTH_SHORT).show();
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
                if (newText.isEmpty()) {
                    comprasFiltradas.clear();
                    comprasFiltradas.addAll(compras);
                    adaptador.notifyDataSetChanged();
                } else {
                    filtrarCompras(newText);
                }
                return true;
            }
        });
    }

    private void setupFilterButton(View view) {
        ibFiltro = view.findViewById(R.id.ibFiltro);
        tvFiltro = view.findViewById(R.id.tvFiltro);
        tvFiltro.setText("Por " + filtroActual);

        ibFiltro.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Filtrar Compras");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_compras, null);
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

                if (selectedId == R.id.rbFiltrarPorIdProveedor) {
                    filtroActual = FILTRO_ID_PROVEEDOR;
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

                tvFiltro.setText("Por " + filtroActual);
                SearchView searchView = requireView().findViewById(R.id.searchView);
                filtrarCompras(searchView.getQuery().toString());
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


    private void filtrarCompras(String textoBusqueda) {
        comprasFiltradas.clear();
        String query = textoBusqueda.toLowerCase().trim();

        for (Compra compra : compras) {
            switch (filtroActual) {
                case FILTRO_ID_PROVEEDOR:
                    if (String.valueOf(compra.getIdProveedor()).contains(query)) {
                        comprasFiltradas.add(compra);
                    }
                    break;
                case FILTRO_FECHA:
                    try {
                        Date inicio = dateFormat.parse(fechaInicio);
                        Date fin = dateFormat.parse(fechaFin);
                        Date fechaCompra = compra.getFechaCompra();

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

                        Calendar calCompra = Calendar.getInstance();
                        calCompra.setTime(fechaCompra);

                        // Comparar solo las fechas (sin hora)
                        if (!calCompra.before(calInicio) && !calCompra.after(calFin)) {
                            comprasFiltradas.add(compra);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged();
    }


    private void cargarCompras() {
        compras.clear();
        compras.add(new Compra(1, 1001, new Date(), "Efectivo", "F001", 1500.75f, true));
        compras.add(new Compra(2, 1002, new Date(), "Tarjeta", "F002", 2000.00f, true));
        // Agrega más compras según sea necesario
        comprasFiltradas.clear();
        comprasFiltradas.addAll(compras);
    }

    private Bundle createBundleWithCompra(Compra compra) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_COMPRA, compra);
        return bundle;
    }
}
