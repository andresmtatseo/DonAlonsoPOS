package com.example.donalonsopos.ui.ventas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.util.ProductoConCantidad;

import java.util.HashMap;
import java.util.List;

public class AdaptadorViewProductoVenta extends RecyclerView.Adapter<AdaptadorViewProductoVenta.ViewHolder> {

    private final Context context;
    private final List<Producto> productos;
    private final List<ProductoConCantidad> productosSeleccionados;  // Recibe productos seleccionados
    private final HashMap<Integer, Integer> cantidadesSeleccionadas = new HashMap<>();

    public AdaptadorViewProductoVenta(Context context, List<Producto> productos, List<ProductoConCantidad> productosSeleccionados) {
        this.context = context;
        this.productos = productos;
        this.productosSeleccionados = productosSeleccionados;

        // Inicializar el HashMap con las cantidades de los productos seleccionados previamente
        for (ProductoConCantidad productoConCantidad : productosSeleccionados) {
            cantidadesSeleccionadas.put(productoConCantidad.getProducto().getIdProducto(), productoConCantidad.getCantidad());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_agregar_producto_venta, parent, false);
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
        private final Button btnIncrementar;
        private final Button btnDecrementar;
        private final TextView tvCantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idProducto = itemView.findViewById(R.id.tvIdProductoContenido);
            idCategoria = itemView.findViewById(R.id.tvCaregoriaContenido);
            nombre = itemView.findViewById(R.id.tvNombreContenido);
            precio = itemView.findViewById(R.id.tvPrecioContenido);
            imagen = itemView.findViewById(R.id.ivImagen);
            btnIncrementar = itemView.findViewById(R.id.btnIncrementar);
            btnDecrementar = itemView.findViewById(R.id.btnDecrementar);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
        }

        public void bind(Producto producto) {
            int id = producto.getIdProducto();
            idProducto.setText(String.valueOf(id));
            idCategoria.setText(String.valueOf(producto.getIdCategoria()));
            nombre.setText(producto.getNombre());
            precio.setText(String.format("%.2f", producto.getPrecio()));
            imagen.setImageResource(R.drawable.icono_producto_sin_foto); // Usa Glide o Picasso si lo prefieres

            // Obtener la cantidad seleccionada o inicializar en 0 usando idProducto
            final int[] cantidadSeleccionada = {cantidadesSeleccionadas.getOrDefault(id, 0)};
            tvCantidad.setText(String.valueOf(cantidadSeleccionada[0]));

            // Incrementar cantidad
            btnIncrementar.setOnClickListener(v -> {
                cantidadSeleccionada[0] = cantidadesSeleccionadas.getOrDefault(id, 0);
                if (cantidadSeleccionada[0] < producto.getCantidadActual()) {
                    cantidadSeleccionada[0]++; // Incrementamos la cantidad
                    cantidadesSeleccionadas.put(id, cantidadSeleccionada[0]); // Actualizamos el HashMap
                    tvCantidad.setText(String.valueOf(cantidadSeleccionada[0])); // Actualizamos el TextView
                } else {
                    Toast.makeText(context, "No puedes seleccionar más de la cantidad disponible", Toast.LENGTH_SHORT).show();
                }
            });

            // Decrementar cantidad
            btnDecrementar.setOnClickListener(v -> {
                cantidadSeleccionada[0] = cantidadesSeleccionadas.getOrDefault(id, 0);
                if (cantidadSeleccionada[0] > 0) {
                    cantidadSeleccionada[0]--; // Decrementamos la cantidad
                    cantidadesSeleccionadas.put(id, cantidadSeleccionada[0]); // Actualizamos el HashMap
                    tvCantidad.setText(String.valueOf(cantidadSeleccionada[0])); // Actualizamos el TextView
                } else {
                    Toast.makeText(context, "La cantidad no puede ser menor a 0", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Método para obtener la cantidad seleccionada de un producto por su id
    public int getCantidadSeleccionada(int idProducto) {
        return cantidadesSeleccionadas.getOrDefault(idProducto, 0);
    }

    // Método para obtener todas las cantidades seleccionadas
    public HashMap<Integer, Integer> getCantidadesSeleccionadas() {
        return cantidadesSeleccionadas;
    }
}
