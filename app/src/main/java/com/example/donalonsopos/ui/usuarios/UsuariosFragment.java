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
            roles.add("Encargado");
            roles.add("Vendedor");

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
        usuarios.add(new Usuario(1, "admin", "1234", 1, "V-11112222", "Andres", "Moreno", true));
        usuarios.add(new Usuario(2, "jane", "5678", 2, "V-33334444", "Jane", "Doe", true));
        usuarios.add(new Usuario(3, "amendez", "abcd", 1, "V-55556666", "Ana", "Mendez", true));
        usuarios.add(new Usuario(4, "rgomez", "qwerty", 2, "V-77778888", "Roberto", "Gomez", true));
        usuarios.add(new Usuario(5, "cgarcia", "pass123", 1, "V-99990000", "Carlos", "Garcia", true));
        usuarios.add(new Usuario(6, "mlopez", "letmein", 1, "V-22223333", "Maria", "Lopez", true));
        usuarios.add(new Usuario(7, "aperez", "secure!", 2, "V-44445555", "Alejandro", "Perez", true));
        usuarios.add(new Usuario(8, "mgonzalez", "hunter2", 1, "V-66667777", "Miguel", "Gonzalez", true));
        usuarios.add(new Usuario(9, "fhernandez", "admin", 2, "V-88889999", "Felipe", "Hernandez", true));
        usuarios.add(new Usuario(10, "lsanchez", "pass4321", 1, "V-10111213", "Laura", "Sanchez", true));
        usuarios.add(new Usuario(11, "dvasquez", "mypassword", 2, "V-12131415", "Daniel", "Vasquez", true));
        usuarios.add(new Usuario(12, "jguzman", "abc123", 1, "V-14151617", "Juan", "Guzman", true));
        usuarios.add(new Usuario(13, "vbustamante", "password1", 2, "V-16171819", "Valeria", "Bustamante", true));
        usuarios.add(new Usuario(14, "ocardenas", "changeme", 1, "V-18192021", "Oscar", "Cardenas", true));
        usuarios.add(new Usuario(15, "lmontes", "pass789", 2, "V-20212223", "Luis", "Montes", true));
        usuarios.add(new Usuario(16, "tfernandez", "login123", 1, "V-23242526", "Tania", "Fernandez", true));
        usuarios.add(new Usuario(17, "rperez", "test456", 2, "V-26272829", "Rosa", "Perez", true));
        usuarios.add(new Usuario(18, "gcabrera", "safepass", 1, "V-29303132", "Gabriel", "Cabrera", true));
        usuarios.add(new Usuario(19, "mfuentes", "topsecret", 2, "V-32333435", "Monica", "Fuentes", true));
        usuarios.add(new Usuario(20, "jmartinez", "access123", 1, "V-35363738", "Julio", "Martinez", true));
        usuarios.add(new Usuario(21, "eramirez", "xyz123", 2, "V-38394041", "Elena", "Ramirez", true));
        usuarios.add(new Usuario(22, "agomez", "welcome", 1, "V-41424344", "Andres", "Gomez", true));
        usuarios.add(new Usuario(23, "lruiz", "p4ssword", 2, "V-45464748", "Lucia", "Ruiz", true));
        usuariosFiltrados.clear();
        usuariosFiltrados.addAll(usuarios);
    }

    private Bundle createBundleWithUsuario(Usuario usuario) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_USUARIO, usuario);
        return bundle;
    }
}
