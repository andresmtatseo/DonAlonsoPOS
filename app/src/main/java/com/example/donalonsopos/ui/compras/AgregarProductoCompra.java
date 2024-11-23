package com.example.donalonsopos.ui.compras;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.ui.productos.AgregarProducto;
import com.example.donalonsopos.ui.ventas.AdaptadorViewProductoVenta;
import com.example.donalonsopos.util.ProductoConCantidad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AgregarProductoCompra extends Fragment {

    private static final String FILTRO_ID = "ID";
    private static final String FILTRO_NOMBRE = "Nombre";
    private static final String FILTRO_CATEGORIA = "Categoría";
    private String filtroActual = FILTRO_NOMBRE;

    private static final String KEY_PRODUCTOS_SELECCIONADOS = "productosSeleccionados";
    private static final String KEY_COMPRA = "compra";
    private static final String KEY_PROVEEDOR = "proveedor";

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private Button btnQuitarProductos, btnContinuar;
    private AdaptadorViewProductoVenta adaptador;
    private ArrayList<Producto> productos = new ArrayList<>();
    private ArrayList<Producto> productosFiltrados = new ArrayList<>();
    private ArrayList<ProductoConCantidad> productosSeleccionados = new ArrayList<>(); // Lista de productos con cantidad seleccionada
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
            List<ProductoConCantidad> productosRecibidos = (List<ProductoConCantidad>) getArguments().getSerializable(KEY_PRODUCTOS_SELECCIONADOS);
            if (productosRecibidos != null) {
                productosSeleccionados.addAll(productosRecibidos); // Agregar los productos ya seleccionados
            }

            compra = (Compra) getArguments().getSerializable(KEY_COMPRA);
            proveedor = (Proveedor) getArguments().getSerializable(KEY_PROVEEDOR);
        }

        initializeViews(view);
        setupFloatingActionButton(view);
        setupListeners();
        setupRecyclerView(view);
        setupSearchView(view);
        setupFilterButton(view);
        cargarProductos();

        return view;
    }

    private void initializeViews(View view) {
        btnContinuar = view.findViewById(R.id.btnContinuar);
        btnQuitarProductos = view.findViewById(R.id.btnQuitarProductos);
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar3);
        btnAgregar.setOnClickListener(v -> {
            AgregarProducto dialog = new AgregarProducto();
            dialog.show(getChildFragmentManager(), "agregar_producto");
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
        adaptador = new AdaptadorViewProductoVenta(requireContext(), productosFiltrados, productosSeleccionados, cargarCategorias());
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
                if (selectedId == R.id.rbFiltrarPorCedula) {
                    filtroActual = FILTRO_ID;
                } else if (selectedId == R.id.rbFiltrarPorNombre) {
                    filtroActual = FILTRO_NOMBRE;
                } else if (selectedId == R.id.rbFiltrarPorCategoria) {
                    filtroActual = FILTRO_CATEGORIA;
                    // Asignar id de categoría según la selección
                    switch (spinnerCategorias.getSelectedItem().toString()) {
                        case "Lácteos":
                            idCategoriaSeleccionada = 1;
                            break;
                        case "Frutas":
                            idCategoriaSeleccionada = 2;
                            break;
                        case "Helados":
                            idCategoriaSeleccionada = 3;
                            break;
                        case "Dulces":
                            idCategoriaSeleccionada = 4;
                            break;
                        default:
                            idCategoriaSeleccionada = -1;
                    }
                }

                tvFiltro.setText("Por " + filtroActual);
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
        // Obtener las cantidades seleccionadas desde el adaptador
        HashMap<Integer, Integer> cantidadesSeleccionadas = adaptador.getCantidadesSeleccionadas();

        // Lista para almacenar los productos seleccionados con cantidades mayores a 0
        List<ProductoConCantidad> productosSeleccionados = new ArrayList<>();

        // Crear un mapa para la búsqueda rápida de productos por idProducto
        Map<Integer, Producto> productosPorId = new HashMap<>();
        for (Producto producto : productos) {
            productosPorId.put(producto.getIdProducto(), producto);
        }

        // Recorrer el HashMap y agregar los productos con cantidad seleccionada mayor a 0
        for (Map.Entry<Integer, Integer> entry : cantidadesSeleccionadas.entrySet()) {
            int idProducto = entry.getKey();
            int cantidad = entry.getValue();

            if (cantidad > 0) {
                Producto producto = productosPorId.get(idProducto);
                if (producto != null) {
                    productosSeleccionados.add(new ProductoConCantidad(producto, cantidad));
                }
            }
        }

        // Limpiar la lista de productos seleccionados previa, si ya existe
        this.productosSeleccionados.clear();  // Eliminar productos seleccionados anteriores

        // Agregar los nuevos productos seleccionados a la lista
        this.productosSeleccionados.addAll(productosSeleccionados);

        // Pasa los productos seleccionados al siguiente fragmento
        Bundle nuevoBundle = new Bundle();
        nuevoBundle.putSerializable(KEY_PRODUCTOS_SELECCIONADOS, new ArrayList<>(productosSeleccionados));
        nuevoBundle.putSerializable(KEY_COMPRA, compra);
        nuevoBundle.putSerializable(KEY_PROVEEDOR, proveedor);

        // Navegar al siguiente fragmento
        NavController navController = Navigation.findNavController(v);
        navController.popBackStack();
        navController.navigate(R.id.agregarCompraFragment, nuevoBundle);
    }


    private void quitarProductos() {
        HashMap<Integer, Integer> productosSeleccionados = adaptador.getCantidadesSeleccionadas();
        productosSeleccionados.clear();
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