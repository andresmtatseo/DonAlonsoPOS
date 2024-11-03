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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.entities.Cliente;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ClientesFragment extends Fragment {

    private static final String KEY_CLIENTE = "cliente";
    private static final String FILTRO_CEDULA = "Cedula";
    private static final String FILTRO_NOMBRECOMPLETO = "Nombre Completo";

    private RecyclerView lista;
    private AdaptadorViewCliente adaptador;
    private List<Cliente> clientes = new ArrayList<>();
    private List<Cliente> clientesFiltrados = new ArrayList<>();

    private String filtroActual = FILTRO_CEDULA;

    public ClientesFragment() { }

    public static ClientesFragment newInstance(String param1, String param2) {
        ClientesFragment fragment = new ClientesFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
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
                    filterClientes(newText);
                }
                return true;
            }
        });
    }

    private void setupFilterButton(View view) {
        ImageButton ibFiltro = view.findViewById(R.id.ibFiltro);
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

                SearchView searchView = view.findViewById(R.id.searchView);
                filterClientes(searchView.getQuery().toString());
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void filterClientes(String textoBusqueda) {
        clientesFiltrados.clear();
        String query = textoBusqueda.toLowerCase();

        for (Cliente cliente : clientes) {
            switch (filtroActual) {
                case FILTRO_CEDULA:
                    if (String.valueOf(cliente.getCedula()).contains(query)) {
                        clientesFiltrados.add(cliente);
                    }
                    break;
                case FILTRO_NOMBRECOMPLETO:
                    if (cliente.getNombre().toLowerCase().contains(query) || cliente.getApellido().toLowerCase().contains(query) ) {
                        clientesFiltrados.add(cliente);
                    }
                    break;
            }
        }

        adaptador.notifyDataSetChanged();
    }

    private void cargarClientes() {
        clientes.clear();
        clientes.add(new Cliente(1, "V-12345678", "Juan", "Pérez", "Calle 1", "1234567890", 1));
        clientes.add(new Cliente(2, "V-87654321", "Maria", "Gonzalez", "Calle 2", "0987654321", 1));
        clientes.add(new Cliente(3, "V-22334455", "Carlos", "Ramirez", "Avenida Principal #45", "04141234567", 1));
        clientes.add(new Cliente(4, "V-33445566", "Ana", "Fernandez", "Calle Bolivar #120", "04147894567", 1));
        clientes.add(new Cliente(5, "V-44556677", "Luis", "Martinez", "Urbanización La Paz", "04145678901", 1));
        clientes.add(new Cliente(6, "V-55667788", "Elena", "Sanchez", "Calle Sol #10", "04241236589", 1));
        clientes.add(new Cliente(7, "V-66778899", "Jorge", "Lopez", "Sector Los Olivos", "04149876543", 1));
        clientes.add(new Cliente(8, "V-77889900", "Rosa", "Gomez", "Calle Libertad #250", "04141230021", 1));
        clientes.add(new Cliente(9, "V-88990011", "Pedro", "Garcia", "Avenida Urdaneta #18", "04241567890", 1));
        clientes.add(new Cliente(10, "V-99001122", "Martha", "Rodriguez", "Calle Fuego #42", "04147654321", 1));
        clientes.add(new Cliente(11, "V-10111213", "Sofia", "Diaz", "Urbanización El Valle", "04248901234", 1));
        clientes.add(new Cliente(12, "V-12131415", "Andres", "Moreno", "Calle Central #75", "04146543210", 1));
        clientes.add(new Cliente(13, "V-13141516", "Lucia", "Perez", "Sector Las Palmas", "04246789012", 1));
        clientes.add(new Cliente(14, "V-14151617", "Fernando", "Rivas", "Avenida Sucre", "04147890123", 1));
        clientes.add(new Cliente(15, "V-15161718", "Patricia", "Acosta", "Calle Maracaibo #36", "04248917654", 1));
        clientes.add(new Cliente(16, "V-16171819", "Javier", "Mejia", "Calle Carabobo", "04141234678", 1));
        clientes.add(new Cliente(17, "V-17181920", "Alejandra", "Mendoza", "Sector El Paraiso", "04241237895", 1));
        clientes.add(new Cliente(18, "V-18192021", "Diego", "Torres", "Calle Lara", "04142345678", 1));
        clientes.add(new Cliente(19, "V-19202122", "Gabriela", "Campos", "Avenida Norte #90", "04241239012", 1));
        clientes.add(new Cliente(20, "V-20212223", "Victor", "Silva", "Sector El Trigal", "04149875432", 1));
        clientesFiltrados.clear();
        clientesFiltrados.addAll(clientes);
    }

    private Bundle createBundleWithCliente(Cliente cliente) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CLIENTE, cliente);
        return bundle;
    }
}
