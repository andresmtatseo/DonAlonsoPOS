package com.example.donalonsopos.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donalonsopos.databinding.ActivityMenuLateralBinding;

public class MenuLateral extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.donalonsopos.databinding.ActivityMenuLateralBinding binding = ActivityMenuLateralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuLateral.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        if (id == R.id.nav_inicio) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new InicioFragment()).commit();
        } else if (id == R.id.nav_ventas) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new VentasFragment()).commit();
        }  else if (id == R.id.nav_productos) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new ProductosFragment()).commit();
        }  else if (id == R.id.nav_compras) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new ComprasFragment()).commit();
        }  else if (id == R.id.nav_clientes) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new ClientesFragment()).commit();
        }  else if (id == R.id.nav_proveedores) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new ProveedoresFragment()).commit();
        }  else if (id == R.id.nav_usuarios) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new UsuariosFragment()).commit();
        }  else if (id == R.id.nav_reportes) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new ReportesFragment()).commit();
        }  else if (id == R.id.nav_ayuda) {
            fm.beginTransaction().replace(R.id.nav_host_fragment_content_menu_lateral, new AyudaFragment()).commit();
        }

        return true;
    }
}