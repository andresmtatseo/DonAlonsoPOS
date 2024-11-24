package com.example.donalonsopos.ui.compras;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.DetallesCompra;
import com.example.donalonsopos.data.DTO.Producto;

import java.util.List;

public class AdaptadorViewProductoSeleccionadoCompra extends RecyclerView.Adapter<AdaptadorViewProductoSeleccionadoCompra.ViewHolder> {

    private final Context context;
    private final List<DetallesCompra> detallesCompras;
    private final List<Producto> productos;

    public AdaptadorViewProductoSeleccionadoCompra(Context context, List<DetallesCompra> detallesCompras, List<Producto> productos) {
        this.context = context;
        this.detallesCompras = detallesCompras;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_producto_seleccionado_compra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetallesCompra detallesCompra = detallesCompras.get(position);
        holder.bind(detallesCompra, productos);
    }

    @Override
    public int getItemCount() {
        return detallesCompras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNombreProducto;
        private final TextView tvPrecioUnitario;
        private final TextView tvTotalProducto;
        private final EditText etCostoUnitario;
        private final EditText etCantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioUnitario = itemView.findViewById(R.id.tvPrecioUnitario);
            tvTotalProducto = itemView.findViewById(R.id.tvTotalProducto);
            etCostoUnitario = itemView.findViewById(R.id.etCostoUnitario);
            etCantidad = itemView.findViewById(R.id.etCantidad);
        }

        public void bind(DetallesCompra detallesCompra, List<Producto> productos) {
            Producto producto = null;
            for (Producto p : productos) {
                if (p.getIdProducto() == detallesCompra.getIdProducto()) {
                    producto = p;
                    break;
                }
            }

            if (producto != null) {
                tvNombreProducto.setText(producto.getNombre());
                tvPrecioUnitario.setText("$" + String.format("%.2f", producto.getPrecio()));
            } else {
                tvNombreProducto.setText("Producto no encontrado");
                tvPrecioUnitario.setText("-");
            }

            // Mostrar valores actuales
            etCostoUnitario.setText(String.valueOf(detallesCompra.getPrecioUnitario()));
            etCantidad.setText(String.valueOf(detallesCompra.getCantidad()));
            actualizarTotal(detallesCompra.getCantidad(), detallesCompra.getPrecioUnitario());

            etCostoUnitario.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().isEmpty()) {
                        try {
                            float costoUnitario = Float.parseFloat(s.toString());
                            if (costoUnitario < 0) {
                                Toast.makeText(itemView.getContext(), "El costo unitario no puede ser menor que 0", Toast.LENGTH_SHORT).show();
                                etCostoUnitario.setText("0");
                            } else {
                                detallesCompra.setPrecioUnitario(costoUnitario);
                                actualizarTotal(detallesCompra.getCantidad(), costoUnitario);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(itemView.getContext(), "Por favor, introduce un valor válido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            etCantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().isEmpty()) {
                        try {
                            int cantidad = Integer.parseInt(s.toString());
                            if (cantidad < 0) {
                                Toast.makeText(itemView.getContext(), "La cantidad no puede ser menor que 0", Toast.LENGTH_SHORT).show();
                                etCantidad.setText("0");
                            } else {
                                detallesCompra.setCantidad(cantidad);
                                actualizarTotal(cantidad, detallesCompra.getPrecioUnitario());
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(itemView.getContext(), "Por favor, introduce un valor válido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        private void actualizarTotal(int cantidad, double costoUnitario) {
            tvTotalProducto.setText("$" + String.format("%.2f", cantidad * costoUnitario));
        }
    }
}
