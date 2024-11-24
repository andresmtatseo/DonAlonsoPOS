package com.example.donalonsopos.ui.compras;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.CategoriaDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DTO.Categoria;
import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.data.DTO.DetallesCompra;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.ui.productos.AgregarProducto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class AgregarProductoCompra extends Fragment {

    private static final String FILTRO_ID = "ID";
    private static final String FILTRO_NOMBRE = "Nombre";
    private static final String FILTRO_CATEGORIA = "Categoría";
    private String filtroActual = FILTRO_NOMBRE;

    private static final String KEY_DETALLES_COMPRA = "detallesCompra";
    private static final String KEY_COMPRA = "compra";
    private static final String KEY_PROVEEDOR = "proveedor";

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private Button btnQuitarProductos, btnContinuar;
    private AdaptadorViewProductoCompra adaptador;
    private ArrayList<Producto> productos = new ArrayList<>();
    private ArrayList<Producto> productosFiltrados = new ArrayList<>();
    private ArrayList<DetallesCompra> detallesCompras;
    private int idCategoriaSeleccionada = -1;
    private Compra compra;
    private Proveedor proveedor;

    public AgregarProductoCompra() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_producto_compra, container, false);

        if (getArguments() != null) {
            detallesCompras = (ArrayList<DetallesCompra>) getArguments().getSerializable(KEY_DETALLES_COMPRA);
            if (detallesCompras == null) {
                detallesCompras = new ArrayList<>();  // Asegúrate de inicializar si es null
            }
            compra = (Compra) getArguments().getSerializable(KEY_COMPRA);
            proveedor = (Proveedor) getArguments().getSerializable(KEY_PROVEEDOR);
        }

        initializeViews(view);
        setupFloatingActionButton(view);
        setupSwipeRefresh(view);
        setupListeners();
        setupRecyclerView(view);
        setupSearchView(view);
        setupFilterButton(view);
        setupSpinnerCategorias(view);
        cargarProductos();

        return view;
    }

    private void initializeViews(View view) {
        btnContinuar = view.findViewById(R.id.btnContinuar);
        btnQuitarProductos = view.findViewById(R.id.btnQuitarProductos);
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            AgregarProducto dialog = new AgregarProducto();
            dialog.show(getChildFragmentManager(), "agregar_producto");
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupSwipeRefresh(View view) {
        SwipeRefreshLayout swipeToRefresh = view.findViewById(R.id.swipeRefreshLayout);
        swipeToRefresh.setOnRefreshListener(() -> {
            cargarProductos();
            adaptador.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            Toast.makeText(requireContext(), "Productos actualizadas", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        btnContinuar.setOnClickListener(v -> continuarCompra(v));
        btnQuitarProductos.setOnClickListener(v -> quitarProductos());
    }

    private void setupRecyclerView(View view) {
        lista = view.findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new GridLayoutManager(getContext(), 6));
        adaptador = new AdaptadorViewProductoCompra(requireContext(), productosFiltrados, detallesCompras, cargarCategorias());
        lista.setAdapter(adaptador);
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
                filtrarProductos(newText);
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
            builder.setTitle("Filtrar Productos");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_productos, null);
            builder.setView(dialogView);

            RadioGroup radioGroupFiltros = dialogView.findViewById(R.id.radioGroupFiltros);
            Spinner spinnerCategorias = dialogView.findViewById(R.id.spinnerCategorias);

            // Cargar categorías en el Spinner
            ArrayAdapter<Categoria> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, cargarCategorias());
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
                if (selectedId == R.id.rbFiltrarPorCedula) {
                    filtroActual = FILTRO_ID;
                } else if (selectedId == R.id.rbFiltrarPorNombre) {
                    filtroActual = FILTRO_NOMBRE;
                } else if (selectedId == R.id.rbFiltrarPorCategoria) {
                    filtroActual = FILTRO_CATEGORIA;

                    // Obtener el objeto Categoria seleccionado
                    Categoria categoriaSeleccionada = (Categoria) spinnerCategorias.getSelectedItem();
                    idCategoriaSeleccionada = categoriaSeleccionada != null ? categoriaSeleccionada.getIdCategoria() : -1;
                }

                // Actualizar la vista con el filtro seleccionado
                tvFiltro.setText("Por " + filtroActual);
                SearchView searchView = view.findViewById(R.id.searchView);
                String textoBusqueda = searchView.getQuery().toString();
                filtrarProductos(textoBusqueda);
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void setupSpinnerCategorias(View view) {
        // Usar dialogView para encontrar el Spinner dentro del diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_productos, null);
        Spinner spinnerCategorias = dialogView.findViewById(R.id.spinnerCategorias);
        if (spinnerCategorias == null) {
            Log.e("ProductosFragment", "El Spinner es null en setupSpinnerCategorias.");
            return;
        }

        ArrayAdapter<Categoria> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, cargarCategorias());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(spinnerAdapter);
    }

    private void filtrarProductos(String textoBusqueda) {
        productosFiltrados.clear();

        for (Producto producto : productos) {
            switch (filtroActual) {
                case FILTRO_ID:
                    if (String.valueOf(producto.getIdProducto()).contains(textoBusqueda)) {
                        productosFiltrados.add(producto);
                    }
                    break;
                case FILTRO_NOMBRE:
                    if (producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        productosFiltrados.add(producto);
                    }
                    break;
                case FILTRO_CATEGORIA:
                    if (producto.getIdCategoria() == idCategoriaSeleccionada &&
                            producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        productosFiltrados.add(producto);
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged(); // Actualizar el adaptador después de filtrar
    }

    private void continuarCompra(View v) {
        List<DetallesCompra> productosSeleccionados = adaptador.getDetallesCompras();

        // Comentar el clear si no deseas borrar los productos anteriores, pero necesitas evitar duplicados
        // this.detallesCompras.clear();

        // Agregar solo los productos que no están ya en detallesCompras
        for (DetallesCompra producto : productosSeleccionados) {
            boolean alreadyExists = false;

            // Verifica si el producto ya está en la lista
            for (DetallesCompra detalle : detallesCompras) {
                if (detalle.getIdProducto() == producto.getIdProducto()) {
                    alreadyExists = true;
                    break;
                }
            }

            // Solo agrega el producto si no está duplicado
            if (!alreadyExists) {
                this.detallesCompras.add(producto);
            }
        }

        Log.e("AgregarProductoCompra", "DetallesCompras antes de navegar: " + detallesCompras.size());

        Bundle nuevoBundle = new Bundle();
        nuevoBundle.putSerializable(KEY_DETALLES_COMPRA, new ArrayList<>(this.detallesCompras));
        nuevoBundle.putSerializable(KEY_COMPRA, compra);
        nuevoBundle.putSerializable(KEY_PROVEEDOR, proveedor);

        NavController navController = Navigation.findNavController(v);
        navController.popBackStack();
        navController.navigate(R.id.agregarCompraFragment, nuevoBundle);

        Log.e("AgregarProductoCompra fragment", "DetallesCompras en continuarCompra: " + detallesCompras.size());
    }


    private void quitarProductos() {
        adaptador.getDetallesCompras().clear();
        adaptador.notifyDataSetChanged();
        Toast.makeText(getContext(), "Productos eliminados correctamente", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<Producto> cargarProductos() {
        productos.clear();
        ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
        productos.addAll(productoDao.select());
        productoDao.close();
        productosFiltrados.clear();
        productosFiltrados.addAll(productos);
        adaptador.notifyDataSetChanged();

        return productos;
    }

    private List<Categoria> cargarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl(requireContext());
        categorias.addAll(categoriaDao.select());
        categoriaDao.close();
        return categorias;
    }

}