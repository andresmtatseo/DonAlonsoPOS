package com.example.donalonsopos.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.entities.Producto;

import java.util.ArrayList;

public class AdaptadorCustom extends RecyclerView.Adapter<AdaptadorCustom.ViewHolder> {

    private Context context = null;
    private ArrayList<Producto> productos = null;

    public AdaptadorCustom(Context context, ArrayList<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public AdaptadorCustom.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_producto, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorCustom.ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        if (producto != null) {
            holder.idProducto.setText("ID: " + producto.getIdProducto());
            holder.idCategoria.setText("Categoria: " + producto.getIdCategoria());
            holder.nombre.setText(producto.getNombre());
            holder.precio.setText("Precio: " + String.valueOf(producto.getPrecio()));
            holder.cantidadActual.setText("Cant. Disponible: " + String.valueOf(producto.getCantidadActual()));

            // Ajusta la imagen según sea necesario (puedes dejar la imagen por defecto o establecer una personalizada)
            holder.imagen.setImageResource(R.drawable.icono_producto_sin_foto);
        }
    }


    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView = null;
        TextView idProducto = null;
        TextView idCategoria = null;
        TextView nombre = null;
        TextView precio = null;
        ImageView imagen = null;
        TextView cantidadActual = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            idProducto = itemView.findViewById(R.id.tvIdProducto);
            idCategoria = itemView.findViewById(R.id.tvIdCaregoria);
            nombre = itemView.findViewById(R.id.tvNombre);
            precio = itemView.findViewById(R.id.tvPrecioMenu);
            imagen = itemView.findViewById(R.id.ivImagen);
            cantidadActual = itemView.findViewById(R.id.tvCantidadActual);
        }
    }

}
