package com.example.donalonsopos.ui.clientes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.util.ConfirmDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallesCliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallesCliente extends Fragment {

    private static final String ARG_CLIENTE = "cliente";
    private ConfirmDialog confirmDialog;
    private Cliente clienteSeleccionado;
    private static final String KEY_CLIENTE = "cliente";

    public DetallesCliente() {
        // Required empty public constructor
    }

    public static DetallesCliente newInstance(Cliente cliente) {
        DetallesCliente fragment = new DetallesCliente();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENTE, cliente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clienteSeleccionado = (Cliente) getArguments().getSerializable(ARG_CLIENTE);
        }

        confirmDialog = new ConfirmDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_cliente, container, false);

        TextView tvCedulaContenido = view.findViewById(R.id.tvCedulaContenido);
        TextView tvNombreContenido = view.findViewById(R.id.tvNombreContenido);
        TextView tvApellidoContenido = view.findViewById(R.id.tvApellidoContenido);
        TextView tvDireccionContenido = view.findViewById(R.id.tvDireccionContenido);

        tvCedulaContenido.setText(String.valueOf(clienteSeleccionado.getCedula()));
        tvNombreContenido.setText(clienteSeleccionado.getNombre());
        tvApellidoContenido.setText(clienteSeleccionado.getApellido());
        tvDireccionContenido.setText(clienteSeleccionado.getDireccion());

        Button btnEliminar = view.findViewById(R.id.btnEliminar);
        Button btnEditar = view.findViewById(R.id.btnEditar);


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                navController.navigate(R.id.EditarClienteFragment, createBundleWithProducto(clienteSeleccionado));
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.showConfirmationDialog("Eliminar", "¿Estás seguro de eliminar este cliente?", () -> {
                    Toast.makeText(getContext(), "Se elimino el cliente.", Toast.LENGTH_SHORT);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu_lateral);
                    navController.navigate(R.id.nav_clientes);
                });
            }
        });

        return view;
    }

    private Bundle createBundleWithProducto(Cliente cliente) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CLIENTE, cliente);
        return bundle;
    }
}