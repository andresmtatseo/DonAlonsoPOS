package com.example.donalonsopos.ui.productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DTO.Categoria; // Asegúrate de tener acceso a la clase Categoria
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdaptadorViewProducto extends RecyclerView.Adapter<AdaptadorViewProducto.ViewHolder> {

    private final Context context;
    private final List<Producto> productos;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;
    private final Map<Integer, String> categoriaMap; // Mapa para asociar ID con nombre de categoría

    public AdaptadorViewProducto(Context context, List<Producto> productos, List<Categoria> categorias, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.context = context;
        this.productos = productos;
        this.listener = listener;
        this.longListener = longClickListener;

        // Crear el mapa de categorías
        this.categoriaMap = new HashMap<>();
        for (Categoria categoria : categorias) {
            categoriaMap.put(categoria.getIdCategoria(), categoria.getNombre());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_producto, parent, false);
        return new ViewHolder(view, listener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        if (producto != null) {
            holder.bind(producto);
        }
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView idProducto;
        private final TextView idCategoria;
        private final TextView nombre;
        private final TextView precio;
        private final ImageView imagen;
        private final TextView cantidadActual;
        private final OnItemClickListener listener;
        private final OnItemLongClickListener longListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(itemView);
            this.idProducto = itemView.findViewById(R.id.tvIdProducto);
            this.idCategoria = itemView.findViewById(R.id.tvIdCaregoria);
            this.nombre = itemView.findViewById(R.id.tvNombre);
            this.precio = itemView.findViewById(R.id.tvPrecioMenu);
            this.imagen = itemView.findViewById(R.id.ivImagen);
            this.cantidadActual = itemView.findViewById(R.id.tvCantidadActual);
            this.listener = listener;
            this.longListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Producto producto) {
            idProducto.setText("ID: " + producto.getIdProducto());

            // Obtener el nombre de la categoría usando el mapa
            String categoriaNombre = categoriaMap.get(producto.getIdCategoria());
            if (categoriaNombre != null) {
                idCategoria.setText("Categoría: " + categoriaNombre);
            } else {
                idCategoria.setText("Categoría no encontrada");
            }

            nombre.setText(producto.getNombre());
            precio.setText(String.format("Precio: %.2f", producto.getPrecio()));
            cantidadActual.setText("Cant. Disponible: " + producto.getCantidadActual());
            imagen.setImageResource(R.drawable.icono_producto_sin_foto); // Considera usar Glide o Picasso aquí
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            longListener.onLongClick(view, getAdapterPosition());
            return true;
        }
    }
}
