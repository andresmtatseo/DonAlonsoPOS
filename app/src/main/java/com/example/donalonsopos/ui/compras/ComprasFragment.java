package com.example.donalonsopos.ui.compras;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

            builder.setPositiveButton("Aplicar", (dialog, which) -> {
                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();

                if (selectedId == R.id.rbFiltrarPorIdProveedor) {
                    filtroActual = FILTRO_ID_PROVEEDOR;
                } else if (selectedId == R.id.rbFiltrarPorFecha) {
                    filtroActual = FILTRO_FECHA;
                }
                tvFiltro.setText("Por " + filtroActual);
                SearchView searchView = view.findViewById(R.id.searchView);
                filtrarCompras(searchView.getQuery().toString());
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
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
                    if (compra.getFechaCompra().toString().toLowerCase().contains(query)) {
                        comprasFiltradas.add(compra);
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
