package com.example.donalonsopos.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DAO.UsuarioDaoImpl;
import com.example.donalonsopos.data.DTO.Usuario;
import com.example.donalonsopos.util.ConfirmDialog;

public class MainActivity extends AppCompatActivity {

    private ConfirmDialog confirmDialog;
    private EditText etUsuario;
    private EditText etContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de botones y campos
        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnSalir = findViewById(R.id.btnSalir);
        Button btnRecuperarContrasena = findViewById(R.id.btnRecuperarContrasena);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);

        // Inicializar confirmDialog
        confirmDialog = new ConfirmDialog(this);

        // Eventos
        btnIniciar.setOnClickListener(v -> {
            if (validarCredenciales(etUsuario.getText().toString().trim(), etContrasena.getText().toString().trim())) {
                Intent intent = new Intent(MainActivity.this, MenuLateral.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        btnSalir.setOnClickListener(v -> mostrarConfirmacionSalir());

        btnRecuperarContrasena.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            if (usuario.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, ingrese su nombre de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            mostrarConfirmacionRecuperarContrasena(usuario);
        });
    }

    private boolean validarCredenciales(String usuario, String contrasena) {
        // Comprobación directa para el usuario administrador
        if ("admin".equals(usuario) && "1234".equals(contrasena)) {
            return true;
        }

        UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl(this);
        boolean isAuthenticated = false;

        try {
            // Obtener todos los usuarios activos
            for (Usuario u : usuarioDao.select()) {
                if (u.getUsername().equals(usuario) && u.getPassword().equals(contrasena)) {
                    isAuthenticated = true;
                    break;
                }
            }
        } finally {
            usuarioDao.close();
        }

        return isAuthenticated;
    }


    private void mostrarConfirmacionSalir() {
        confirmDialog.showConfirmationDialog(
                "Confirmación",
                "¿Estás seguro de que quieres salir?",
                this::finish
        );
    }

    private void mostrarConfirmacionRecuperarContrasena(String usuario) {
        confirmDialog.showConfirmationDialog(
                "Recuperación de contraseña",
                "Se enviará una solicitud al administrador para recuperar la contraseña del usuario \"" + usuario + "\". ¿Desea continuar?",
                () -> {
                    enviarSolicitudRecuperacion(usuario);
                    Toast.makeText(MainActivity.this, "Solicitud enviada al administrador", Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void enviarSolicitudRecuperacion(String usuario) {
        UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl(this);
        Usuario user = null;

        try {
            for (Usuario u : usuarioDao.select()) {
                if (u.getUsername().equals(usuario)) {
                    user = u;
                    break;
                }
            }
        } finally {
            usuarioDao.close();
        }

        if (user != null) {
            String subject = "Solicitud de recuperación de contraseña";
            String body = "Se ha solicitado la recuperación de contraseña para el usuario: " + user.getUsername() + ".\n\n" +
                    "Por favor, tome las acciones necesarias para restablecer la contraseña de este usuario.";

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:andresmoreno2001@gmail.com")); // Dirección del administrador
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);

            try {
                startActivity(Intent.createChooser(emailIntent, "Enviar correo al administrador"));
                Toast.makeText(this, "El administrador ha sido notificado para el usuario: " + user.getNombre(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "No se pudo abrir la aplicación de correo. Verifique que tenga una instalada.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Usuario no encontrado en el sistema", Toast.LENGTH_SHORT).show();
        }
    }

}
