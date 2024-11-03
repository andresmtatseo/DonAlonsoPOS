package com.example.donalonsopos.ui.productos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.donalonsopos.R;
import com.example.donalonsopos.data.entities.Producto;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass for displaying products.
 */
public class ProductosFragment extends Fragment {

    private static final String KEY_PRODUCTO = "producto";
    private RecyclerView lista;
    private AdaptadorViewProducto adaptador;
    private ArrayList<Producto> productos;
    private ArrayList<Producto> productosFiltrados;

    private String filtroActual = "Nombre"; // Filtro por defecto
    private int idCategoriaSeleccionada = -1; // Almacena la idCategoría seleccionada

    public ProductosFragment() {
        // Constructor vacío requerido
    }

    public static ProductosFragment newInstance(String param1, String param2) {
        ProductosFragment fragment = new ProductosFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        productos = cargarProductos();         // Cargar la lista completa
        productosFiltrados = new ArrayList<>(productos);  // Inicialmente, todos los productos están en productosFiltrados

        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupSwipeRefresh(view);
        setupSearchView(view);
        setupFilterButton(view);

        return view;
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            AgregarProducto dialog = new AgregarProducto();
            dialog.show(getChildFragmentManager(), "agregar_producto");
        });
    }

    private void setupRecyclerView(View view) {
        lista = view.findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new AdaptadorViewProducto(requireContext(), productosFiltrados, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Producto productoSeleccionado = productosFiltrados.get(position);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.detallesProductoFragment, createBundleWithProducto(productoSeleccionado));
            }
        }, new OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(requireContext(), "Click largo", Toast.LENGTH_SHORT).show();
            }
        });

        lista.setAdapter(adaptador);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupSwipeRefresh(View view) {
        SwipeRefreshLayout swipeToRefresh = view.findViewById(R.id.swipeRefreshLayout);
        swipeToRefresh.setOnRefreshListener(() -> {
            Toast.makeText(requireContext(), "Actualizando productos...", Toast.LENGTH_SHORT).show();
            adaptador.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
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
                filtrarProductos(newText);  // Llama al método de filtrado cada vez que cambia el texto
                return true;
            }
        });
    }

    private void setupFilterButton(View view) {
        ImageButton ibFiltro = view.findViewById(R.id.ibFiltro);
        ibFiltro.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Filtrar Productos");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_productos, null);
            builder.setView(dialogView);

            RadioGroup radioGroupFiltros = dialogView.findViewById(R.id.radioGroupFiltros);
            Spinner spinnerCategorias = dialogView.findViewById(R.id.spinnerCategorias);

            // Opciones para el Spinner de categorías
            ArrayList<String> categorias = new ArrayList<>();
            categorias.add("Lácteos");
            categorias.add("Frutas");
            categorias.add("Helados");
            categorias.add("Dulces");

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categorias);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategorias.setAdapter(spinnerAdapter);

            // Configurar visibilidad del Spinner según la selección del RadioGroup
            radioGroupFiltros.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rbFiltrarPorCategoria) {
                    spinnerCategorias.setVisibility(View.VISIBLE);
                } else {
                    spinnerCategorias.setVisibility(View.GONE);
                }
            });

            builder.setPositiveButton("Aplicar", (dialog, which) -> {
                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();

                // Definir el filtro actual y aplicar filtro específico
                if (selectedId == R.id.rbFiltrarPorId) {
                    filtroActual = "ID";
                } else if (selectedId == R.id.rbFiltrarPorNombre) {
                    filtroActual = "Nombre";
                } else if (selectedId == R.id.rbFiltrarPorCategoria) {
                    filtroActual = "Categoría";
                    // Asignar id de categoría según la selección
                    switch (spinnerCategorias.getSelectedItem().toString()) {
                        case "Lácteos":
                            idCategoriaSeleccionada = 1; // Suponiendo idCategoría para Lácteos
                            break;
                        case "Frutas":
                            idCategoriaSeleccionada = 2; // Suponiendo idCategoría para Frutas
                            break;
                        case "Helados":
                            idCategoriaSeleccionada = 3; // Suponiendo idCategoría para Helados
                            break;
                        case "Dulces":
                            idCategoriaSeleccionada = 4; // Suponiendo idCategoría para Dulces
                            break;
                        default:
                            idCategoriaSeleccionada = -1; // No seleccionado
                    }
                }

                // Llamar a `filtrarProductos` para aplicar el filtro con el texto actual en el SearchView
                SearchView searchView = view.findViewById(R.id.searchView);
                String textoBusqueda = searchView.getQuery().toString();
                filtrarProductos(textoBusqueda);
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void filtrarProductos(String textoBusqueda) {
        productosFiltrados.clear();

        for (Producto producto : productos) {
            switch (filtroActual) {
                case "ID":
                    if (String.valueOf(producto.getIdProducto()).contains(textoBusqueda)) {
                        productosFiltrados.add(producto);
                    }
                    break;
                case "Nombre":
                    if (producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        productosFiltrados.add(producto);
                    }
                    break;
                case "Categoría":
                    if (producto.getIdCategoria() == idCategoriaSeleccionada &&
                            producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        productosFiltrados.add(producto);
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged(); // Actualizar el adaptador después de filtrar
    }

    private Bundle createBundleWithProducto(Producto producto) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCTO, producto);
        return bundle;
    }

    private ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(new Producto(55, 1, "Helado 5L Fresa", 10, 10.55, "imagen_fresa.png"));
        productos.add(new Producto(35, 2, "Helado 5L Mantecado", 100, 105, "imagen_mantecado.png"));
        productos.add(new Producto(45, 3, "Manzana Roja", 200, 1.25, "imagen_manzana.png"));
        productos.add(new Producto(65, 4, "Dulce de Leche", 250, 3.25, "imagen_dulcedeleche.png"));
        return productos;
    }
}
