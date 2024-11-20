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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Categoria;
import com.example.donalonsopos.util.Utils;

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

    public AgregarProducto() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Llamamos a la función cargarCategorias() y llenamos el Spinner
        List<Categoria> categorias = cargarCategorias();
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }


    private void setupListeners() {
        btnTomarFoto.setOnClickListener(v -> abrirCamara());
        btnSeleccionarImagen.setOnClickListener(v -> seleccionarImagen());
        btnGuardar.setOnClickListener(v -> guardarProducto());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());
        btnCerrar.setOnClickListener(v -> dismiss());
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivFotoProducto.setImageBitmap(photo);  // Mostrar la foto en el ImageView
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                ivFotoProducto.setImageURI(selectedImageUri);  // Mostrar la imagen seleccionada
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Verificamos si el permiso de cámara ya ha sido otorgado
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no se ha concedido, solicitamos el permiso
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // Verificamos si el permiso fue concedido
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso fue concedido, puedes abrir la cámara
                abrirCamara();
            } else {
                // El permiso fue denegado, muestra un mensaje al usuario
                Toast.makeText(getContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarProducto() {
        String nombre = etNombreProducto.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String cantidadMinima = etCantidadMinima.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        // Obtener el id de la categoría seleccionada
        Categoria categoriaSeleccionada = (Categoria) spCategoria.getSelectedItem();
        int categoriaId = categoriaSeleccionada != null ? categoriaSeleccionada.getIdCategoria() : -1;

        if (nombre.isEmpty()) {
            etNombreProducto.setError("El nombre del producto es obligatorio");
            return;
        }

        if (precio.isEmpty()) {
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

        if (cantidadMinima.isEmpty()) {
            etCantidadMinima.setError("La cantidad mínima es obligatoria");
            return;
        }
        try {
            int cantidadMinimaInt = Integer.parseInt(cantidadMinima);
            if (cantidadMinimaInt <= 0) {
                etCantidadMinima.setError("La cantidad mínima debe ser mayor que cero");
                return;
            }
        } catch (NumberFormatException e) {
            etCantidadMinima.setError("La cantidad mínima debe ser un número válido");
            return;
        }

        // Validar la categoría (aunque el spinner debería tener opciones válidas)
        if (categoriaId == -1) {
            Toast.makeText(getContext(), "Debes seleccionar una categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí agregarías la lógica para guardar el producto con el id de la categoría
        // Por ejemplo:
        // Producto producto = new Producto(nombre, precioDouble, cantidadMinimaInt, descripcion, categoriaId);
        // guardarEnBaseDeDatos(producto);

        Toast.makeText(getContext(), "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
        dismiss();
    }


    private List<Categoria> cargarCategorias() {
        // Simulamos categorías obtenidas desde la base de datos
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria(1, "Categoria 1"));
        categorias.add(new Categoria(2, "Categoria 2"));
        categorias.add(new Categoria(3, "Categoria 3"));
        categorias.add(new Categoria(4, "Categoria 4"));
        return categorias;
    }


    private void limpiarCampos() {
        etNombreProducto.setText("");
        etPrecio.setText("");
        etCantidadMinima.setText("");
        etDescripcion.setText("");
        spCategoria.setSelection(0);
        ivFotoProducto.setImageResource(R.drawable.icono_camara);
    }
}
