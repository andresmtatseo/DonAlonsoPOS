package com.example.donalonsopos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.donalonsopos.R;
import com.example.donalonsopos.util.ConfirmDialog;

public class MainActivity extends AppCompatActivity {

    private ConfirmDialog confirmDialog;
    private EditText etUsuario;
    private EditText etContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de botones
        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnSalir = findViewById(R.id.btnSalir);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);

        // Inicializar confirmDialog
        confirmDialog = new ConfirmDialog(this);

        // Eventos
        btnIniciar.setOnClickListener(v -> {
            if (validarCredenciales(etUsuario.getText().toString(), etContrasena.getText().toString())) {
                Intent intent = new Intent(MainActivity.this, MenuLateral.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        btnSalir.setOnClickListener(v -> mostrarConfirmacionSalir());
    }

    private boolean validarCredenciales(String usuario, String contrasena) {
        // Aquí se pueden definir credenciales de prueba
        String usuarioCorrecto = "admin";
        String contrasenaCorrecta = "1234";
        return usuario.equals(usuarioCorrecto) && contrasena.equals(contrasenaCorrecta);
    }

    private void mostrarConfirmacionSalir() {
        confirmDialog.showConfirmationDialog(
                "Confirmación",
                "¿Estás seguro de que quieres salir?",
                this::finish
        );
    }

}
