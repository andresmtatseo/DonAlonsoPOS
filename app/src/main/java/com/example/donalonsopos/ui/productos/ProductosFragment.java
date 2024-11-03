package com.example.donalonsopos.ui.productos;

import android.annotation.SuppressLint;
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
import android.widget.SearchView;
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

    private static final String KEY_PRODUCTO = "producto"; // Clave del Bundle
    private RecyclerView lista;
    private AdaptadorViewProducto adaptador;
    private ArrayList<Producto> productos;
    private ArrayList<Producto> productosFiltrados;

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
        // Recuperar argumentos si existen
        //if (getArguments() != null) {
            // Puedes recuperar parámetros aquí si es necesario
       // }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        productos = cargarProductos();         // Cargar la lista completa
        productosFiltrados = new ArrayList<>(productos);  // Inicialmente, todos los productos están en productosFiltrados

        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupSwipeRefresh(view);
        setupSearchView(view);  // Configura el SearchView

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
                Producto productoSeleccionado = productosFiltrados.get(position);  // Obtener el producto filtrado
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
            // Simulación de actualización de productos
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
                // Opcional: puedes manejar aquí cuando se envía el texto
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarProductos(newText);  // Llama al método de filtrado cada vez que cambia el texto
                return true;
            }
        });
    }

    private void filtrarProductos(String textoBusqueda) {
        productosFiltrados.clear();

        if (textoBusqueda.isEmpty()) {
            productosFiltrados.addAll(productos);  // Si el texto está vacío, mostrar todos los productos
        } else {
            for (Producto producto : productos) {
                // Filtra productos cuyo nombre contenga el texto de búsqueda (ignorando mayúsculas/minúsculas)
                if (producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                    productosFiltrados.add(producto);
                }
            }
        }
        adaptador.notifyDataSetChanged();  // Notificar al adaptador sobre los cambios
    }

    private Bundle createBundleWithProducto(Producto producto) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCTO, producto);
        return bundle;
    }

    private ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(new Producto(55, 6, "Helado 5L Fresa", 10, 10.55, "Producto 1"));
        productos.add(new Producto(35, 5, "Helado 5L Mantecado", 100, 105, "Producto 2"));
        productos.add(new Producto(55, 6, "Helado 5L Chocolate", 10, 10.55, "Producto 1"));
        productos.add(new Producto(35, 5, "Helado 5L Oreo", 100, 105, "Producto 2"));
        return productos;
    }
}
