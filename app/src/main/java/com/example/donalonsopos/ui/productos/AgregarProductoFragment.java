package com.example.donalonsopos.ui.productos;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        // Obtener la referencia al botón Guardar
        Button btnGuardar = view.findViewById(R.id.btnGuardar);

        // Configurar el OnClickListener para el botón
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 1. Obtener los valores de los campos del formulario
                // Ejemplo:
                EditText nombreProducto = view.findViewById(R.id.etNombreProducto);
                String nombre = nombreProducto.getText().toString();
                // ... obtener otros valores del formulario

                // 2. Validar los datos ingresados
                // Ejemplo:
                if (nombre.isEmpty()) {
                    nombreProducto.setError("El nombre del producto es obligatorio");
                    return;
                }
                // ... otras validaciones

                // 3. Guardar el nuevo producto (en una base de datos, etc.)
                // ... tu lógica para guardar el producto

                // 4. Cerrar el DialogFragment
                dismiss();
            }
        });

        return view;
    }
}