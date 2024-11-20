package com.example.donalonsopos.ui.clientes;

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
import com.example.donalonsopos.data.DAO.ClienteDaoImpl;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ClientesFragment extends Fragment {

    public static final String KEY_CLIENTE = "cliente";

    private static final String FILTRO_CEDULA = "cedula";
    private static final String FILTRO_NOMBRECOMPLETO = "nombre";
    private String filtroActual = FILTRO_CEDULA;

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private AdaptadorViewCliente adaptador;
    private List<Cliente> clientes = new ArrayList<>();
    private List<Cliente> clientesFiltrados = new ArrayList<>();

    public ClientesFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupSwipeRefresh(view);
        setupSearchView(view);
        setupFilterButton(view);

        cargarClientes();

        return view;
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            AgregarCliente dialog = new AgregarCliente();
            dialog.show(getChildFragmentManager(), "agregar_cliente");
        });
    }

    private void setupRecyclerView(View view) {
        lista = view.findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new AdaptadorViewCliente(requireContext(), clientesFiltrados, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Cliente clienteSeleccionado = clientesFiltrados.get(position);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.detallesClienteFragment, createBundleWithCliente(clienteSeleccionado));
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
            // Aquí debería refrescar los datos reales, por ejemplo, llamando a una base de datos
            cargarClientes();
            adaptador.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            Toast.makeText(requireContext(), "Clientes actualizados", Toast.LENGTH_SHORT).show();
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
                    clientesFiltrados.clear();
                    clientesFiltrados.addAll(clientes); // Restaurar lista completa si la búsqueda está vacía
                    adaptador.notifyDataSetChanged();
                } else {
                    filtrarClientes(newText);
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
            builder.setTitle("Filtrar Clientes");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_clientes, null);
            builder.setView(dialogView);

            RadioGroup radioGroupFiltros = dialogView.findViewById(R.id.radioGroupFiltros);

            builder.setPositiveButton("Aplicar", (dialog, which) -> {
                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();

                if (selectedId == R.id.rbFiltrarPorCedula) {
                    filtroActual = FILTRO_CEDULA;
                } else if (selectedId == R.id.rbFiltrarPorNombreApellido) {
                    filtroActual = FILTRO_NOMBRECOMPLETO;
                }
                tvFiltro.setText("Por " + filtroActual);
                SearchView searchView = view.findViewById(R.id.searchView);
                filtrarClientes(searchView.getQuery().toString());
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void filtrarClientes(String textoBusqueda) {
        clientesFiltrados.clear();
        String query = textoBusqueda.toLowerCase().trim();  // Limpiar espacios en blanco extra

        for (Cliente cliente : clientes) {
            switch (filtroActual) {
                case FILTRO_CEDULA:
                    // Buscar la cédula sin importar el prefijo (V-, J-, etc.)
                    String cedula = cliente.getCedula().toLowerCase();
                    if (cedula.contains(query)) {
                        clientesFiltrados.add(cliente);
                    }
                    break;
                case FILTRO_NOMBRECOMPLETO:
                    // Combina nombre y apellido en una sola cadena
                    String nombreCompleto = (cliente.getNombre() + " " + cliente.getApellido()).toLowerCase();

                    // Verifica si la búsqueda contiene la cadena del nombre completo
                    if (nombreCompleto.contains(query)) {
                        clientesFiltrados.add(cliente);
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged();
    }

    private void cargarClientes() {
        clientes.clear();
        ClienteDaoImpl clienteDao = new ClienteDaoImpl(requireContext());
        clientes.addAll(clienteDao.select());
        clienteDao.close();
        clientesFiltrados.clear();
        clientesFiltrados.addAll(clientes);
    }

    private Bundle createBundleWithCliente(Cliente cliente) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CLIENTE, cliente);
        return bundle;
    }
}