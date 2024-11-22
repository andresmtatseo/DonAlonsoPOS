package com.example.donalonsopos.ui.productos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.CategoriaDaoImpl;
import com.example.donalonsopos.data.DTO.Categoria;

import java.util.List;

public class CategoriasFragment extends Fragment {

    private Spinner spCategoria;
    private EditText etNombreCategoria;
    private Button btnCrear, btnEditar, btnEliminar;

    private CategoriaDaoImpl categoriaDao;

    public CategoriasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        // Inicializamos las vistas
        spCategoria = view.findViewById(R.id.spCategoria);
        etNombreCategoria = view.findViewById(R.id.etNombreCategoria);
        btnCrear = view.findViewById(R.id.btnCrear);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnEliminar = view.findViewById(R.id.btnEliminar);

        categoriaDao = new CategoriaDaoImpl(requireContext());

        // Cargar categorías
        cargarCategorias();

        // Configurar botones
        btnCrear.setOnClickListener(v -> crearCategoria());
        btnEditar.setOnClickListener(v -> editarCategoria());
        btnEliminar.setOnClickListener(v -> eliminarCategoria());

        return view;
    }

    private void cargarCategorias() {
        List<Categoria> categorias = categoriaDao.select();
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }

    private void crearCategoria() {
        String nombreCategoria = etNombreCategoria.getText().toString().trim();

        if (nombreCategoria.isEmpty()) {
            etNombreCategoria.setError("El nombre de la categoría es obligatorio");
            return;
        }

        Categoria categoria = new Categoria(nombreCategoria);
        categoriaDao.insert(categoria);

        Toast.makeText(requireContext(), "Categoría creada", Toast.LENGTH_SHORT).show();
        etNombreCategoria.setText(""); // Limpiar el campo
        cargarCategorias(); // Recargar categorías en el spinner
    }

    private void editarCategoria() {
        Categoria categoriaSeleccionada = (Categoria) spCategoria.getSelectedItem();
        if (categoriaSeleccionada == null) {
            Toast.makeText(requireContext(), "Seleccione una categoría para editar", Toast.LENGTH_SHORT).show();
            return;
        }

        String nuevoNombre = etNombreCategoria.getText().toString().trim();
        if (nuevoNombre.isEmpty()) {
            etNombreCategoria.setError("El nombre de la categoría es obligatorio");
            return;
        }

        categoriaSeleccionada.setNombre(nuevoNombre);
        categoriaDao.update(categoriaSeleccionada); // Método para actualizar la categoría

        Toast.makeText(requireContext(), "Categoría editada", Toast.LENGTH_SHORT).show();
        etNombreCategoria.setText(""); // Limpiar el campo
        cargarCategorias(); // Recargar categorías en el spinner
    }

    private void eliminarCategoria() {
        Categoria categoriaSeleccionada = (Categoria) spCategoria.getSelectedItem();
        if (categoriaSeleccionada == null) {
            Toast.makeText(requireContext(), "Seleccione una categoría para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        categoriaDao.delete(categoriaSeleccionada.getIdCategoria());

        Toast.makeText(requireContext(), "Categoría eliminada", Toast.LENGTH_SHORT).show();
        cargarCategorias(); // Recargar categorías en el spinner
    }
}
