package com.example.donalonsopos.ui.compras;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Categoria;
import com.example.donalonsopos.data.DTO.DetallesCompra;
import com.example.donalonsopos.data.DTO.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorViewProductoCompra extends RecyclerView.Adapter<AdaptadorViewProductoCompra.ViewHolder> {

    private final Context context;
    private final List<Producto> productos;
    private final List<DetallesCompra> detallesCompras;
    private final Map<Integer, String> categoriaMap;

    public AdaptadorViewProductoCompra(Context context, List<Producto> productos, List<DetallesCompra> detallesCompras, List<Categoria> categorias) {
        this.context = context;
        this.productos = productos;
        this.detallesCompras = detallesCompras != null ? detallesCompras : new ArrayList<>();

        // Crear el mapa de categorías
        this.categoriaMap = new HashMap<>();
        for (Categoria categoria : categorias) {
            categoriaMap.put(categoria.getIdCategoria(), categoria.getNombre());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_agregar_producto_compra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView idProducto;
        private final TextView idCategoria;
        private final TextView nombre;
        private final TextView precio;
        private final ImageView imagen;
        private final ImageButton btnAgregarProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idProducto = itemView.findViewById(R.id.tvIdProductoContenido);
            idCategoria = itemView.findViewById(R.id.tvCaregoriaContenido);
            nombre = itemView.findViewById(R.id.tvNombre);
            precio = itemView.findViewById(R.id.tvPrecioContenido);
            imagen = itemView.findViewById(R.id.ivImagen);
            btnAgregarProducto = itemView.findViewById(R.id.ibAgregarProducto);
        }

        public void bind(Producto producto) {
            int id = producto.getIdProducto();
            idProducto.setText(String.valueOf(id));

            // Obtener el nombre de la categoría usando el mapa
            String categoriaNombre = categoriaMap.get(producto.getIdCategoria());
            idCategoria.setText(categoriaNombre != null ? categoriaNombre : "Categoría no encontrada");

            nombre.setText(producto.getNombre());
            precio.setText(String.format("%.2f", producto.getPrecio()));
            imagen.setImageResource(R.drawable.icono_producto_sin_foto);

            // Verificar si el producto ya está en el carrito (detallesCompras)
            final boolean[] estaSeleccionado = {detallesCompras.stream()
                    .anyMatch(detalle -> detalle.getIdProducto() == id)};

            // Actualizar el estado del botón basado en la selección
            actualizarEstadoBoton(estaSeleccionado[0]);

            // Configurar el listener para agregar o quitar el producto
            btnAgregarProducto.setOnClickListener(v -> {
                // Recalcular si está seleccionado después de la acción
                if (estaSeleccionado[0]) {
                    // Si está seleccionado, quitar el producto del carrito
                    detallesCompras.removeIf(detalle -> detalle.getIdProducto() == id);
                    Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                } else {
                    // Si no está seleccionado, agregarlo al carrito solo si no existe
                    if (!detallesCompras.stream().anyMatch(detalle -> detalle.getIdProducto() == id)) {
                        Log.e("AgregarProductoCompra", "Producto seleccionado: " + producto.getIdProducto());
                        DetallesCompra nuevoDetalle = new DetallesCompra();
                        nuevoDetalle.setIdProducto(id);
                        detallesCompras.add(nuevoDetalle);
                        Log.e("AgregarProductoCompra", "DetallesCompras después de agregar: " + detallesCompras.size());
                        Toast.makeText(context, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
                    }
                }

                // Actualizar la variable `estaSeleccionado` después de la acción
                estaSeleccionado[0] = !estaSeleccionado[0];

                // Actualizar el estado visual del botón
                actualizarEstadoBoton(estaSeleccionado[0]);
            });
        }

        private void actualizarEstadoBoton(boolean seleccionado) {
            if (seleccionado) {
                btnAgregarProducto.setImageResource(R.drawable.icono_eliminar_del_carrito);
            } else {
                btnAgregarProducto.setImageResource(R.drawable.icono_anadir_al_carrito);
            }
        }
    }

    public List<DetallesCompra> getDetallesCompras() {
        Log.e("Adaptador", "DetallesCompras obtenidos: " + detallesCompras.size());
        return detallesCompras;
    }

    public boolean isProductoSeleccionado(int idProducto) {
        return detallesCompras.stream().anyMatch(detalle -> detalle.getIdProducto() == idProducto);
    }

}
