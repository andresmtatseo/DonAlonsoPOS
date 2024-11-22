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
import com.example.donalonsopos.data.DAO.MovimientoProductoDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DTO.Categoria;
import com.example.donalonsopos.data.DTO.MovimientoProducto;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MovimientosFragment extends Fragment {

    public static final String KEY_PRODUCTO = "producto";

    private static final String FILTRO_ID = "ID";
    private static final String FILTRO_NOMBRE = "Nombre";
    private static final String FILTRO_CATEGORIA = "Categoría";
    private String filtroActual = FILTRO_NOMBRE;

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private AdaptadorViewProducto adaptador;
    private ArrayList<MovimientoProducto> movimientoProductos = new ArrayList<>();
    private ArrayList<MovimientoProducto> movimientoProductoFiltrados = new ArrayList<>();
    private int idCategoriaSeleccionada = -1;

    public MovimientosFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movimientos, container, false);

//        setupRecyclerView(view);
//        setupSwipeRefresh(view);
//        setupSearchView(view);
//        setupFilterButton(view);

        cargarMovimientos();

        return view;
    }

//    private void setupRecyclerView(View view) {
//        lista = view.findViewById(R.id.lista);
//        lista.setHasFixedSize(true);
//        lista.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        adaptador = new AdaptadorViewProducto(requireContext(), productosFiltrados, cargarCategorias(), new OnItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Producto productoSeleccionado = productosFiltrados.get(position);
//                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
//                navController.navigate(R.id.detallesProductoFragment, createBundleWithProducto(productoSeleccionado));
//            }
//        }, new OnItemLongClickListener() {
//            @Override
//            public void onLongClick(View view, int position) {
//                Toast.makeText(requireContext(), "Click largo", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        lista.setAdapter(adaptador);
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void setupSwipeRefresh(View view) {
//        SwipeRefreshLayout swipeToRefresh = view.findViewById(R.id.swipeRefreshLayout);
//        swipeToRefresh.setOnRefreshListener(() -> {
//            Toast.makeText(requireContext(), "Actualizando productos...", Toast.LENGTH_SHORT).show();
//            cargarProductos();
//            adaptador.notifyDataSetChanged();
//            swipeToRefresh.setRefreshing(false);
//        });
//    }
//
//    private void setupSearchView(View view) {
//        SearchView searchView = view.findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filtrarProductos(newText);
//                return true;
//            }
//        });
//    }
//
//    private void setupFilterButton(View view) {
//        ibFiltro = view.findViewById(R.id.ibFiltro);
//        tvFiltro = view.findViewById(R.id.tvFiltro);
//        tvFiltro.setText("Por " + filtroActual);
//
//        ibFiltro.setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//            builder.setTitle("Filtrar Productos");
//
//            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_productos, null);
//            builder.setView(dialogView);
//
//            RadioGroup radioGroupFiltros = dialogView.findViewById(R.id.radioGroupFiltros);
//            Spinner spinnerCategorias = dialogView.findViewById(R.id.spinnerCategorias);
//
//            // Cargar categorías en el Spinner
//            ArrayAdapter<Categoria> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, cargarCategorias());
//            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerCategorias.setAdapter(spinnerAdapter);
//
//            // Configurar visibilidad del Spinner según la selección del RadioGroup
//            radioGroupFiltros.setOnCheckedChangeListener((group, checkedId) -> {
//                if (checkedId == R.id.rbFiltrarPorCategoria) {
//                    spinnerCategorias.setVisibility(View.VISIBLE);
//                } else {
//                    spinnerCategorias.setVisibility(View.GONE);
//                }
//            });
//
//            builder.setPositiveButton("Aplicar", (dialog, which) -> {
//                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();
//
//                // Definir el filtro actual y aplicar filtro específico
//                if (selectedId == R.id.rbFiltrarPorCedula) {
//                    filtroActual = FILTRO_ID;
//                } else if (selectedId == R.id.rbFiltrarPorNombre) {
//                    filtroActual = FILTRO_NOMBRE;
//                } else if (selectedId == R.id.rbFiltrarPorCategoria) {
//                    filtroActual = FILTRO_CATEGORIA;
//
//                    // Obtener el objeto Categoria seleccionado
//                    Categoria categoriaSeleccionada = (Categoria) spinnerCategorias.getSelectedItem();
//                    idCategoriaSeleccionada = categoriaSeleccionada != null ? categoriaSeleccionada.getIdCategoria() : -1;
//                }
//
//                // Actualizar la vista con el filtro seleccionado
//                tvFiltro.setText("Por " + filtroActual);
//                SearchView searchView = view.findViewById(R.id.searchView);
//                String textoBusqueda = searchView.getQuery().toString();
//                filtrarProductos(textoBusqueda);
//            });
//
//            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
//            builder.create().show();
//        });
//    }
//
//    private void filtrarProductos(String textoBusqueda) {
//        productosFiltrados.clear();
//
//        for (Producto producto : productos) {
//            switch (filtroActual) {
//                case FILTRO_ID:
//                    if (String.valueOf(producto.getIdProducto()).contains(textoBusqueda)) {
//                        productosFiltrados.add(producto);
//                    }
//                    break;
//                case FILTRO_NOMBRE:
//                    if (producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
//                        productosFiltrados.add(producto);
//                    }
//                    break;
//                case FILTRO_CATEGORIA:
//                    if (producto.getIdCategoria() == idCategoriaSeleccionada &&
//                            producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
//                        productosFiltrados.add(producto);
//                    }
//                    break;
//            }
//        }
//
//        adaptador.notifyDataSetChanged(); // Actualizar el adaptador después de filtrar
//    }

    private ArrayList<MovimientoProducto> cargarMovimientos() {
        movimientoProductos.clear();
        MovimientoProductoDaoImpl movimientoProductoDao = new MovimientoProductoDaoImpl(requireContext());
        movimientoProductos.addAll(movimientoProductoDao.select());
        movimientoProductoDao.close();
        movimientoProductoFiltrados.clear();
        movimientoProductoFiltrados.addAll(movimientoProductos);
        adaptador.notifyDataSetChanged();

        return movimientoProductos;
    }

}