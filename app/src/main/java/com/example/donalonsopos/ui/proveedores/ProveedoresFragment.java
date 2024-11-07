package com.example.donalonsopos.ui.proveedores;

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
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProveedoresFragment extends Fragment {

    public static final String KEY_PROVEEDOR = "proveedor";
    private static final String FILTRO_CEDULA = "Cedula";
    private static final String FILTRO_NOMBRE = "Nombre";

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private AdaptadorViewProveedor adaptador;
    private List<Proveedor> proveedores = new ArrayList<>();
    private List<Proveedor> proveedoresFiltrados = new ArrayList<>();
    private String filtroActual = FILTRO_CEDULA;

    public ProveedoresFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proveedores, container, false);

        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupSwipeRefresh(view);
        setupSearchView(view);
        setupFilterButton(view);
        cargarProveedores();

        return view;
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            AgregarProveedor dialog = new AgregarProveedor();
            dialog.show(getChildFragmentManager(), "agregar_proveedor");
        });
    }

    private void setupRecyclerView(View view) {
        lista = view.findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new AdaptadorViewProveedor(requireContext(), proveedoresFiltrados, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Proveedor proveedorSeleccionado = proveedoresFiltrados.get(position);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.detallesProveedorFragment, createBundleWithProveedor(proveedorSeleccionado));
            }
        }, new OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(requireContext(), "Click largo en proveedor", Toast.LENGTH_SHORT).show();
            }
        });

        lista.setAdapter(adaptador);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupSwipeRefresh(View view) {
        SwipeRefreshLayout swipeToRefresh = view.findViewById(R.id.swipeRefreshLayout);
        swipeToRefresh.setOnRefreshListener(() -> {
            cargarProveedores();
            adaptador.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            Toast.makeText(requireContext(), "Proveedores actualizados", Toast.LENGTH_SHORT).show();
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
                    proveedoresFiltrados.clear();
                    proveedoresFiltrados.addAll(proveedores);
                    adaptador.notifyDataSetChanged();
                } else {
                    filtrarProveedores(newText);
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
            builder.setTitle("Filtrar Proveedores");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_proveedores, null);
            builder.setView(dialogView);

            RadioGroup radioGroupFiltros = dialogView.findViewById(R.id.radioGroupFiltros);

            builder.setPositiveButton("Aplicar", (dialog, which) -> {
                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();
                if (selectedId == R.id.rbFiltrarPorCedula) {
                    filtroActual = FILTRO_CEDULA;
                } else if (selectedId == R.id.rbFiltrarPorNombre) {
                    filtroActual = FILTRO_NOMBRE;
                }
                tvFiltro.setText("Por " + filtroActual);
                SearchView searchView = view.findViewById(R.id.searchView);
                filtrarProveedores(searchView.getQuery().toString());
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void filtrarProveedores(String textoBusqueda) {
        proveedoresFiltrados.clear();
        String query = textoBusqueda.toLowerCase();

        for (Proveedor proveedor : proveedores) {
            switch (filtroActual) {
                case FILTRO_CEDULA:
                    String cedula = proveedor.getCedula().toLowerCase();
                    if (cedula.contains(query)) {
                        proveedoresFiltrados.add(proveedor);
                    }
                    break;
                case FILTRO_NOMBRE:
                    if (proveedor.getNombre().toLowerCase().contains(query)) {
                        proveedoresFiltrados.add(proveedor);
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged();
    }

    private void cargarProveedores() {
        proveedores.clear();
        proveedores.add(new Proveedor(1, "J-12345678", "Distribuidora XYZ", "Calle 1", "1234567890", "xyz@mail.com", true));
        proveedores.add(new Proveedor(2, "J-87654321", "Suministros ABC", "Calle 2", "0987654321", "abc@mail.com", true));
        proveedoresFiltrados.clear();
        proveedoresFiltrados.addAll(proveedores);
    }

    private Bundle createBundleWithProveedor(Proveedor proveedor) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PROVEEDOR, proveedor);
        return bundle;
    }
}
