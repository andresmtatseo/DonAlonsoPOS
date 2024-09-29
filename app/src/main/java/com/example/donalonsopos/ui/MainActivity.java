package com.example.donalonsopos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donalonsopos.R;
import com.example.donalonsopos.util.ConfirmDialog;

public class MainActivity extends AppCompatActivity {

    private ConfirmDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();  // Este método debe ser implementado en Java
        setContentView(R.layout.activity_main);

        // Inicialización de botones
        Button btnIniciar = findViewById(R.id.btnIniciar); // Botón Iniciar
        Button btnSalir = findViewById(R.id.btnSalir);  // Botón Salir

        // Inicializar confirmDialog
        confirmDialog = new ConfirmDialog(this);

        // Eventos
        btnIniciar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuLateral.class);
            startActivity(intent);
        });

        btnSalir.setOnClickListener(v -> mostrarConfirmacionSalir());
    }

    // Llamar al popup de confirmación
    private void mostrarConfirmacionSalir() {
        confirmDialog.showConfirmationDialog(
                "Confirmación",
                "¿Estás seguro de que quieres salir?",
                this::finish  // Cerrar la actividad si el usuario confirma
        );
    }

    // Implementación del método enableEdgeToEdge (deberás definirlo si no está en la clase base)
    private void enableEdgeToEdge() {
        // Lógica para habilitar el edge-to-edge si es necesario
    }
}