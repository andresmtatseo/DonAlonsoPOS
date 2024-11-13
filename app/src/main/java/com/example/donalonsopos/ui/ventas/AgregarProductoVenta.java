package com.example.donalonsopos.ui.ventas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.ui.productos.AdaptadorViewProducto;
import com.example.donalonsopos.ui.productos.AgregarProducto;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class AgregarProductoVenta extends Fragment {

    public static final String KEY_PRODUCTO = "producto";

    private static final String FILTRO_ID = "ID";
    private static final String FILTRO_NOMBRE = "Nombre";
    private static final String FILTRO_CATEGORIA = "Categoría";
    private String filtroActual = FILTRO_NOMBRE;

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private AdaptadorViewProductoVenta adaptador;
    private ArrayList<Producto> productos = new ArrayList<>();
    private ArrayList<Producto> productosFiltrados = new ArrayList<>();
    private int idCategoriaSeleccionada = -1;

    public AgregarProductoVenta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_producto_venta, container, false);

        setupRecyclerView(view);
        setupSearchView(view);
        setupFilterButton(view);

        cargarProductos();

        return view;
    }

    private void setupRecyclerView(View view) {
        lista = view.findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new GridLayoutManager(getContext(), 5));

        adaptador = new AdaptadorViewProductoVenta(requireContext(), productosFiltrados, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Producto productoSeleccionado = productosFiltrados.get(position);
                Toast.makeText(requireContext(), "Producto seleccionado: " + productoSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();
            }
        }, new OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(requireContext(), "Click largo", Toast.LENGTH_SHORT).show();
            }
        });

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

    private ArrayList<Producto> cargarProductos() {
        productos.clear();
        productos.add(new Producto(55, 1, "Helado 5L Fresa", 10, 10.55, "imagen_fresa.png"));
        productos.add(new Producto(35, 2, "Helado 5L Mantecado", 100, 105, "imagen_mantecado.png"));
        productos.add(new Producto(45, 3, "Manzana Roja", 200, 1.25, "imagen_manzana.png"));
        productos.add(new Producto(65, 4, "Dulce de Leche", 250, 3.25, "imagen_dulcedeleche.png"));
        productos.add(new Producto(66, 5, "Banana", 150, 0.85, "imagen_banana.png"));
        // (Agrega más productos según necesidad)

        productosFiltrados.clear();
        productosFiltrados.addAll(productos);

        return productos;
    }

    private Bundle createBundleWithProducto(Producto producto) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCTO, producto);
        return bundle;
    }
}