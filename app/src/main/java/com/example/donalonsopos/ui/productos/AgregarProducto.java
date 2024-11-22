package com.example.donalonsopos.ui.productos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.CategoriaDaoImpl;
import com.example.donalonsopos.data.DAO.ProductoDaoImpl;
import com.example.donalonsopos.data.DTO.Categoria;
import com.example.donalonsopos.data.DTO.Producto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarProducto extends DialogFragment {

    private EditText etNombreProducto, etPrecio, etCantidadMinima, etDescripcion;
    private Spinner spCategoria;
    private Button btnGuardar, btnLimpiar, btnTomarFoto, btnSeleccionarImagen;
    private ImageButton btnCerrar;
    private static final int REQUEST_CAMERA = 1001;
    private static final int SELECT_FILE = 1002;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private ImageView ivFotoProducto;
    private byte[] imagenProducto; // Variable para almacenar la imagen como byte array.

    public AgregarProducto() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_producto, container, false);

        initializeViews(view);
        setupListeners();

        return view;
    }

    private void initializeViews(View view) {
        etNombreProducto = view.findViewById(R.id.etNombreProducto);
        etPrecio = view.findViewById(R.id.etPrecio);
        etCantidadMinima = view.findViewById(R.id.etCantidadMinima);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        spCategoria = view.findViewById(R.id.spCategoria);
        ivFotoProducto = view.findViewById(R.id.ivFotoProducto);
        btnTomarFoto = view.findViewById(R.id.btnTomarFoto);
        btnSeleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
        btnCerrar = view.findViewById(R.id.btnCerrar);

        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, cargarCategorias());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }

    private void setupListeners() {
        btnTomarFoto.setOnClickListener(v -> verificarPermisoCamara());
        btnSeleccionarImagen.setOnClickListener(v -> seleccionarImagen());
        btnGuardar.setOnClickListener(v -> guardarProducto());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
        btnCerrar.setOnClickListener(v -> dismiss());
    }

    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            abrirCamara();
        }
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            try {
                startActivityForResult(intent, REQUEST_CAMERA);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al intentar abrir la cámara: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "No se encontró ninguna aplicación de cámara en este dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(getContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivFotoProducto.setImageBitmap(photo); // Mostrar la foto en el ImageView
                imagenProducto = convertirBitmapABytes(photo); // Convertir a byte array
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                ivFotoProducto.setImageURI(selectedImageUri); // Mostrar la imagen seleccionada
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    imagenProducto = convertirBitmapABytes(bitmap); // Convertir a byte array
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private byte[] convertirBitmapABytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void guardarProducto() {
        String nombre = etNombreProducto.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String cantidadMinima = etCantidadMinima.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        Categoria categoriaSeleccionada = (Categoria) spCategoria.getSelectedItem();
        int idCategoria = categoriaSeleccionada != null ? categoriaSeleccionada.getIdCategoria() : -1;

        if (nombre.isEmpty()) {
            etNombreProducto.setError("El nombre del producto es obligatorio");
            return;
        }

        if (precio.isEmpty()) {
            etPrecio.setError("El precio es obligatorio");
            return;
        }
        double precioDouble;
        try {
            precioDouble = Double.parseDouble(precio);
            if (precioDouble <= 0) {
                etPrecio.setError("El precio debe ser mayor que cero");
                return;
            }
        } catch (NumberFormatException e) {
            etPrecio.setError("El precio debe ser un número válido");
            return;
        }

        if (cantidadMinima.isEmpty()) {
            etCantidadMinima.setError("La cantidad mínima es obligatoria");
            return;
        }
        int cantidadMinimaInt;
        try {
            cantidadMinimaInt = Integer.parseInt(cantidadMinima);
            if (cantidadMinimaInt <= 0) {
                etCantidadMinima.setError("La cantidad mínima debe ser mayor que cero");
                return;
            }
        } catch (NumberFormatException e) {
            etCantidadMinima.setError("La cantidad mínima debe ser un número válido");
            return;
        }

        Producto producto = new Producto(idCategoria, nombre, precioDouble, imagenProducto, descripcion, 0, cantidadMinimaInt);
        ProductoDaoImpl productoDao = new ProductoDaoImpl(requireContext());
        productoDao.insert(producto);
        productoDao.close();

        Toast.makeText(getContext(), "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private List<Categoria> cargarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl(requireContext());
        categorias.addAll(categoriaDao.select());
        categoriaDao.close();
        return categorias;
    }

    private void limpiarCampos() {
        etNombreProducto.setText("");
        etPrecio.setText("");
        etCantidadMinima.setText("");
        etDescripcion.setText("");
        spCategoria.setSelection(0);
        ivFotoProducto.setImageResource(R.drawable.icono_camara);
        imagenProducto = null; // Resetear la imagen
    }
}
