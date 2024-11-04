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

    private static final String KEY_PROVEEDOR = "proveedor";
    private static final String FILTRO_CEDULA = "Cedula";
    private static final String FILTRO_NOMBRE = "Nombre";

    private RecyclerView lista;
    private AdaptadorViewProveedor adaptador;
    private List<Proveedor> proveedores = new ArrayList<>();
    private List<Proveedor> proveedoresFiltrados = new ArrayList<>();

    private String filtroActual = FILTRO_CEDULA;

    public ProveedoresFragment() { }

    public static ProveedoresFragment newInstance(String param1, String param2) {
        ProveedoresFragment fragment = new ProveedoresFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
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
                    filterProveedores(newText);
                }
                return true;
            }
        });
    }

    private void setupFilterButton(View view) {
        ImageButton ibFiltro = view.findViewById(R.id.ibFiltro);
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

                SearchView searchView = view.findViewById(R.id.searchView);
                filterProveedores(searchView.getQuery().toString());
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void filterProveedores(String textoBusqueda) {
        proveedoresFiltrados.clear();
        String query = textoBusqueda.toLowerCase();

        for (Proveedor proveedor : proveedores) {
            switch (filtroActual) {
                case FILTRO_CEDULA:
                    if (proveedor.getCedula().contains(query)) {
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
        proveedores.add(new Proveedor(3, "J-22334455", "Comercial Perez", "Avenida Principal #45", "04141234567", "abc@mail.com", true));
        proveedores.add(new Proveedor(4, "J-33445566", "Productos Vargas", "Calle Bolívar #120", "04147894567", "vargas@mail.com", true));
        proveedores.add(new Proveedor(5, "J-44556677", "Alimentos La Esperanza", "Urbanización La Paz", "04145678901", "esperanza@mail.com", true));
        proveedores.add(new Proveedor(6, "J-55667788", "Insumos Sánchez", "Calle Sol #10", "04241236589", "sanchez@mail.com", true));
        proveedores.add(new Proveedor(7, "J-66778899", "Distribuidora Los Olivos", "Sector Los Olivos", "04149876543", "olivos@mail.com", true));
        proveedores.add(new Proveedor(8, "J-77889900", "Comercial Libertad", "Calle Libertad #250", "04141230021", "libertad@mail.com", true));
        proveedores.add(new Proveedor(9, "J-88990011", "Productos Urdaneta", "Avenida Urdaneta #18", "04241567890", "urdaneta@mail.com", true));
        proveedores.add(new Proveedor(10, "J-99001122", "Distribuidora Central", "Calle Fuego #42", "04147654321", "central@mail.com", true));
        proveedores.add(new Proveedor(11, "J-10111213", "Alimentos El Valle", "Urbanización El Valle", "04248901234", "valle@mail.com", true));
        proveedores.add(new Proveedor(12, "J-12131415", "Insumos Las Palmas", "Sector Las Palmas", "04246789012", "palmas@mail.com", true));
        proveedores.add(new Proveedor(13, "J-13141516", "Comercial Sucre", "Avenida Sucre", "04147890123", "sucre@mail.com", true));
        proveedores.add(new Proveedor(14, "J-14151617", "Productos La Paz", "Calle Maracaibo #36", "04248917654", "lapaz@mail.com", true));
        proveedores.add(new Proveedor(15, "J-15161718", "Comercial Lara", "Calle Carabobo", "04141234678", "lara@mail.com", true));
        proveedores.add(new Proveedor(16, "J-16171819", "Distribuidora Paraiso", "Sector El Paraiso", "04241237895", "paraiso@mail.com", true));
        proveedores.add(new Proveedor(17, "J-17181920", "Insumos Norte", "Avenida Norte #90", "04241239012", "norte@mail.com", true));
        proveedores.add(new Proveedor(18, "J-18192021", "Productos Trigal", "Sector El Trigal", "04149875432", "trigal@mail.com", true));
        proveedoresFiltrados.clear();
        proveedoresFiltrados.addAll(proveedores);
    }

    private Bundle createBundleWithProveedor(Proveedor proveedor) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PROVEEDOR, proveedor);
        return bundle;
    }
}
