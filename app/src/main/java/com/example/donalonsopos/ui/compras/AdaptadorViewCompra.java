package com.example.donalonsopos.ui.compras;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdaptadorViewCompra extends RecyclerView.Adapter<AdaptadorViewCompra.ViewHolder> {

    private final Context context;
    private final List<Compra> compras;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;

    public AdaptadorViewCompra(Context context, List<Compra> compras, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.context = context;
        this.compras = compras;
        this.listener = listener;
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_compra, parent, false);
        return new ViewHolder(view, listener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Compra compra = compras.get(position);
        if (compra != null) {
            holder.bind(compra);
        }
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView tvIdCompra;
        private final TextView tvIdProveedor;
        private final TextView tvFechaCompra;
        private final TextView tvMetodoPago;
        private final TextView tvNumeroFactura;
        private final TextView tvTotal;
        private final OnItemClickListener listener;
        private final OnItemLongClickListener longListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(itemView);
            this.tvIdCompra = itemView.findViewById(R.id.tvIdCompra);
            this.tvIdProveedor = itemView.findViewById(R.id.tvIdProveedor);
            this.tvFechaCompra = itemView.findViewById(R.id.tvFechaCompra);
            this.tvMetodoPago = itemView.findViewById(R.id.tvMetodoPago);
            this.tvNumeroFactura = itemView.findViewById(R.id.tvNumeroFactura);
            this.tvTotal = itemView.findViewById(R.id.tvTotal);
            this.listener = listener;
            this.longListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Compra compra) {
            tvIdCompra.setText("ID: " + compra.getIdCompra());
            tvIdProveedor.setText("ID Proveedor: " + compra.getIdProveedor());

            tvFechaCompra.setText("Fecha: " + compra.getFechaCompra());

            tvMetodoPago.setText("Método de Pago: " + compra.getMetodoPago());
            tvNumeroFactura.setText("N° Factura: " + compra.getNumeroFactura());
            tvTotal.setText("Total: $" + String.format(Locale.getDefault(), "%.2f", compra.getTotal()));
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
