package com.example.donalonsopos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.donalonsopos.R;
import com.example.donalonsopos.ui.ayuda.AyudaFragment;
import com.example.donalonsopos.ui.clientes.ClientesFragment;
import com.example.donalonsopos.ui.compras.ComprasFragment;
import com.example.donalonsopos.ui.inicio.InicioFragment;
import com.example.donalonsopos.ui.productos.ProductosFragment;
import com.example.donalonsopos.ui.proveedores.ProveedoresFragment;
import com.example.donalonsopos.ui.reportes.ReportesFragment;
import com.example.donalonsopos.ui.usuarios.UsuariosFragment;
import com.example.donalonsopos.ui.ventas.VentasFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donalonsopos.databinding.ActivityMenuLateralBinding;

import com.example.donalonsopos.util.ConfirmDialog;

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

        confirmDialog = new ConfirmDialog(this);
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

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cerrar_sesion) {
            mostrarConfirmacionSalir();
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral));
    }

    private void mostrarConfirmacionSalir() {
        confirmDialog.showConfirmationDialog(
                "Confirmación",
                "¿Estás seguro de que quieres cerrar sesión?",
                this::cerrarSesion
        );
    }

    private void cerrarSesion() {
        Intent intent = new Intent(MenuLateral.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        startActivity(intent);
        finish(); // Finaliza la actividad actual
    }
}
