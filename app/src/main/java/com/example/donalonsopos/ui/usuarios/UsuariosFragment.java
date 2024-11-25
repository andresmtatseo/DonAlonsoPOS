package com.example.donalonsopos.ui.usuarios;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.UsuarioDaoImpl;
import com.example.donalonsopos.data.DTO.Usuario;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UsuariosFragment extends Fragment {

    public static final String KEY_USUARIO = "usuario";

    private static final String FILTRO_CEDULA = "Cedula";
    private static final String FILTRO_NOMBRECOMPLETO = "Nombre y Apellido";
    private static final String FILTRO_ROL = "Rol";
    private String filtroActual = FILTRO_CEDULA;
    private int rolSeleccionado = -1;

    private RecyclerView lista;
    private TextView tvFiltro;
    private ImageButton ibFiltro;
    private AdaptadorViewUsuario adaptador;
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Usuario> usuariosFiltrados = new ArrayList<>();

    public UsuariosFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        setupFloatingActionButton(view);
        setupRecyclerView(view);
        setupSwipeRefresh(view);
        setupSearchView(view);
        setupFilterButton(view);

        cargarUsuarios();

        return view;
    }

    private void setupFloatingActionButton(View view) {
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            AgregarUsuario dialog = new AgregarUsuario();
            dialog.show(getChildFragmentManager(), "agregar_usuario");
        });
    }

    private void setupRecyclerView(View view) {
        lista = view.findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new AdaptadorViewUsuario(requireContext(), usuariosFiltrados, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Usuario usuarioSeleccionado = usuariosFiltrados.get(position);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.detallesUsuarioFragment, createBundleWithUsuario(usuarioSeleccionado));
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
            cargarUsuarios();
            adaptador.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            Toast.makeText(requireContext(), "Usuarios actualizados", Toast.LENGTH_SHORT).show();
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
                filtrarUsuarios(newText);
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
            builder.setTitle("Filtrar Usuarios");

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtros_usuarios, null);
            builder.setView(dialogView);

            RadioGroup radioGroupFiltros = dialogView.findViewById(R.id.radioGroupFiltros);
            Spinner spinnerRoles = dialogView.findViewById(R.id.spinnerRoles);

            ArrayList<String> roles = new ArrayList<>();
            roles.add("Administrador");
            roles.add("Encargado/a");
            roles.add("Cajero/a");

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, roles);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRoles.setAdapter(spinnerAdapter);

            radioGroupFiltros.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rbFiltrarPorRol) {
                    spinnerRoles.setVisibility(View.VISIBLE);
                } else {
                    spinnerRoles.setVisibility(View.GONE);
                }
            });

            builder.setPositiveButton("Aplicar", (dialog, which) -> {
                int selectedId = radioGroupFiltros.getCheckedRadioButtonId();

                if (selectedId == R.id.rbFiltrarPorCedula) {
                    filtroActual = FILTRO_CEDULA;
                } else if (selectedId == R.id.rbFiltrarPorNombre) {
                    filtroActual = FILTRO_NOMBRECOMPLETO;
                } else if (selectedId == R.id.rbFiltrarPorRol) {
                    filtroActual = FILTRO_ROL;
                    switch (spinnerRoles.getSelectedItem().toString()) {
                        case "Administrador":
                            rolSeleccionado = 1;
                            break;
                        case "Encargado":
                            rolSeleccionado = 2;
                            break;
                        case "Vendedor":
                            rolSeleccionado = 3;
                            break;
                        default:
                            rolSeleccionado = -1;
                    }
                }
                tvFiltro.setText("Por " + filtroActual);
                String textoBusqueda = ((SearchView) view.findViewById(R.id.searchView)).getQuery().toString();
                filtrarUsuarios(textoBusqueda);
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void filtrarUsuarios(String textoBusqueda) {
        usuariosFiltrados.clear();
        String query = textoBusqueda.toLowerCase().trim(); // Limpiar espacios

        for (Usuario usuario : usuarios) {
            switch (filtroActual) {
                case FILTRO_CEDULA:
                    String cedula = usuario.getCedula().toLowerCase();
                    if (cedula.contains(query)) {
                        usuariosFiltrados.add(usuario);
                    }
                    break;
                case FILTRO_NOMBRECOMPLETO:
                    String nombreCompleto = (usuario.getNombre() + " " + usuario.getApellido()).toLowerCase();
                    if (nombreCompleto.contains(query)) {
                        usuariosFiltrados.add(usuario);
                    }
                    break;
                case FILTRO_ROL:
                    if (usuario.getRol() == rolSeleccionado && usuario.getNombre().toLowerCase().contains(query)) {
                        usuariosFiltrados.add(usuario);
                    }
                    break;
            }
        }
        adaptador.notifyDataSetChanged();
    }

    private void cargarUsuarios() {
        usuarios.clear();
        UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl(requireContext());
        usuarios.addAll(usuarioDao.select());
        usuarioDao.close();
        usuariosFiltrados.clear();
        usuariosFiltrados.addAll(usuarios);
    }

    private Bundle createBundleWithUsuario(Usuario usuario) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_USUARIO, usuario);
        return bundle;
    }
}
