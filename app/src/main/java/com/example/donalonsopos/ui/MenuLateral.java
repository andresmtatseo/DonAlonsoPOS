package com.example.donalonsopos.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import com.example.donalonsopos.R;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donalonsopos.databinding.ActivityMenuLateralBinding;

import com.example.donalonsopos.util.ConfirmDialog;
import com.google.android.material.navigation.NavigationView;

public class MenuLateral extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ConfirmDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.donalonsopos.databinding.ActivityMenuLateralBinding binding = ActivityMenuLateralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuLateral.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio) // Puedes agregar más elementos si es necesario
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configurar ConfirmDialog
        confirmDialog = new ConfirmDialog(this);

        // En el onCreate de MenuLateral.java
        navigationView.setNavigationItemSelectedListener(item -> {
            // Cerrar el menú lateral después de seleccionar una opción
            drawer.closeDrawer(GravityCompat.START);

            // Verificar si se ha seleccionado la opción de cerrar sesión
            if (item.getItemId() == R.id.nav_cerrar_sesion) {
                mostrarConfirmacionSalir();
                return true; // Retornamos 'true' para indicar que la opción fue manejada
            }

            // Verificar las otras opciones con 'if'
            if (item.getItemId() == R.id.nav_inicio) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_inicio);
            } else if (item.getItemId() == R.id.nav_ventas) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_ventas);
            } else if (item.getItemId() == R.id.nav_clientes) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_clientes);
            } else if (item.getItemId() == R.id.nav_productos) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_productos);
            } else if (item.getItemId() == R.id.nav_compras) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_compras);
            } else if (item.getItemId() == R.id.nav_reportes) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_reportes);
            } else if (item.getItemId() == R.id.nav_proveedores) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_proveedores);
            } else if (item.getItemId() == R.id.nav_usuarios) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_usuarios);
            } else if (item.getItemId() == R.id.nav_ayuda) {
                Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral)
                        .navigate(R.id.nav_ayuda);
            } else {
                return false; // Si no se maneja alguna opción, retornamos 'false'
            }

            return true; // Retornamos 'true' si hemos manejado alguna de las opciones
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void mostrarConfirmacionSalir() {
        // Mostrar el cuadro de confirmación antes de cerrar sesión
        confirmDialog.showConfirmationDialog(
                "Confirmación",
                "¿Estás seguro de que quieres cerrar sesión?",
                this::cerrarSesion
        );
    }

    private void cerrarSesion() {
        // Limpiar la sesión (SharedPreferences, Firebase, etc.)
        // En este caso, usamos SharedPreferences como ejemplo, si usas Firebase, puedes hacer FirebaseAuth.getInstance().signOut();

        SharedPreferences sharedPreferences = getSharedPreferences("mi_sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Eliminar todos los datos guardados
        editor.apply();

        // Navegar a la pantalla de inicio de sesión (MainActivity en este caso)
        Intent intent = new Intent(MenuLateral.this, MainActivity.class);  // Redirigir a la pantalla de login
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Limpiar la pila de actividades
        startActivity(intent);  // Iniciar la actividad de login
        finish();  // Finalizar la actividad actual (MenuLateral o la que esté gestionando el menú lateral)
    }
}
