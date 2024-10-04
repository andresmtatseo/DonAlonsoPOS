package com.example.donalonsopos.ui.productos;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.donalonsopos.R;

public class AgregarProductoFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgregarProductoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarProductoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarProductoFragment newInstance(String param1, String param2) {
        AgregarProductoFragment fragment = new AgregarProductoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_agregar_producto, container, false);

        // Obtener la referencia al botón
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnLimpiar = view.findViewById(R.id.btnLimpiar);
        ImageButton btnCerrar = view.findViewById(R.id.btnCerrar);

        // Configurar el OnClickListener para el botón
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNombreProducto = view.findViewById(R.id.etNombreProducto);
                EditText etPrecio = view.findViewById(R.id.etPrecio);
                EditText etCantidadMinima = view.findViewById(R.id.etCantidadMinima);
                Spinner spCategoria = view.findViewById(R.id.spCategoria);

                String nombre = etNombreProducto.getText().toString();
                String precio = etPrecio.getText().toString();
                String cantidadMinima = etCantidadMinima.getText().toString();
                // Obtener la categoría seleccionada del Spinner
//                String categoria = spCategoria.getSelectedItem().toString();

                // Validar nombre (no vacío)
                if (nombre.isEmpty()) {
                    etNombreProducto.setError("El nombre del producto es obligatorio");
                    return;
                }

                // Validar precio (no vacío y numérico positivo)
                if(precio.isEmpty()) {
                    etPrecio.setError("El precio es obligatorio");
                    return;
                }
                try {
                    double precioDouble = Double.parseDouble(precio);
                    if (precioDouble <= 0) {
                        etPrecio.setError("El precio debe ser mayor que cero");
                        return;
                    }
                } catch (NumberFormatException e) {
                    etPrecio.setError("El precio debe ser un número válido");
                    return;
                }

                // Validar cantidad mínima (no vacío y numérico positivo)
                if (cantidadMinima.isEmpty()) {
                    etCantidadMinima.setError("La cantidad mínima es obligatoria");
                    return;
                }
                try {
                    int cantidadMinimaInt = Integer.parseInt(cantidadMinima);
                    if (cantidadMinimaInt <= 0) {
                        etCantidadMinima.setError("La cantidad mínima debe ser mayor que cero");
                    }
                } catch (NumberFormatException e) {
                    etCantidadMinima.setError("La cantidad mínima debe ser un número válido");
                }

//                // Validar categoría (novacía) - Asegúrate de que el Spinner tenga al menos una opción
//                if (categoria.isEmpty()) {
//                    // Puedes mostrar un mensaje de error de alguna forma,
//                    // por ejemplo, con un Toast o un Snackbar
//                    Toast.makeText(getContext(), "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // ... continuar con el guardado si las validaciones son correctas ...

                dismiss();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNombreProducto = view.findViewById(R.id.etNombreProducto);
                EditText etPrecio = view.findViewById(R.id.etPrecio);
                EditText etCantidadMinima = view.findViewById(R.id.etCantidadMinima);
                EditText etDescripcion = view.findViewById(R.id.etDescripcion);
                Spinner spCategoria = view.findViewById(R.id.spCategoria);

                // Limpiar campos EditText
                etNombreProducto.setText("");etPrecio.setText("");
                etCantidadMinima.setText("");
                etDescripcion.setText("");

                // Restablecer Spinner a la primera opción (si es necesario)
                spCategoria.setSelection(0);
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
        });

        return view;
    }
}