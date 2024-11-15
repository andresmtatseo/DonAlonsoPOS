package com.example.donalonsopos.ui.ventas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.util.ProductoConCantidad;

import java.util.List;

public class AdaptadorViewProductoSeleccionado extends RecyclerView.Adapter<AdaptadorViewProductoSeleccionado.ViewHolder> {

    private final Context context;
    private final List<ProductoConCantidad> productosSeleccionados;

    public AdaptadorViewProductoSeleccionado(Context context, List<ProductoConCantidad> productosSeleccionados) {
        this.context = context;
        this.productosSeleccionados = productosSeleccionados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_producto_seleccionado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductoConCantidad productoConCantidad = productosSeleccionados.get(position);
        holder.bind(productoConCantidad);
    }

    @Override
    public int getItemCount() {
        return productosSeleccionados.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNombreProducto;
        private final TextView tvCantidadSeleccionada;
        private final TextView tvPrecioUnitario;
        private final TextView tvTotalProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvCantidadSeleccionada = itemView.findViewById(R.id.tvCantidadSeleccionada);
            tvPrecioUnitario = itemView.findViewById(R.id.tvPrecioUnitario);
            tvTotalProducto = itemView.findViewById(R.id.tvTotalProducto);
        }

        public void bind(ProductoConCantidad productoConCantidad) {
            Producto producto = productoConCantidad.getProducto();
            int cantidad = productoConCantidad.getCantidad();

            tvNombreProducto.setText(producto.getNombre());
            tvCantidadSeleccionada.setText("Cantidad: " + cantidad);
            tvPrecioUnitario.setText("Precio: $" + String.format("%.2f", producto.getPrecio()));
            tvTotalProducto.setText("Total: $" + String.format("%.2f", producto.getPrecio() * cantidad));
        }
    }
}

