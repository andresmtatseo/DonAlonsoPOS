package com.example.donalonsopos.ui.ventas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdaptadorViewVenta extends RecyclerView.Adapter<AdaptadorViewVenta.ViewHolder> {

    private final Context context;
    private final List<Venta> ventas;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;

    public AdaptadorViewVenta(Context context, List<Venta> ventas, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.context = context;
        this.ventas = ventas;
        this.listener = listener;
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_venta, parent, false);
        return new ViewHolder(view, listener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venta venta = ventas.get(position);
        if (venta != null) {
            holder.bind(venta);
        }
    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView tvIdVenta;
        private final TextView tvIdCliente;
        private final TextView tvFechaVenta;
        private final TextView tvMetodoPago;
        private final TextView tvNumeroTransaccion;
        private final TextView tvTotal;
        private final OnItemClickListener listener;
        private final OnItemLongClickListener longListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(itemView);
            this.tvIdVenta = itemView.findViewById(R.id.tvIdVenta);
            this.tvIdCliente = itemView.findViewById(R.id.tvIdCliente);
            this.tvFechaVenta = itemView.findViewById(R.id.tvFechaVenta);
            this.tvMetodoPago = itemView.findViewById(R.id.tvMetodoPago);
            this.tvNumeroTransaccion = itemView.findViewById(R.id.tvNumeroTransaccion);
            this.tvTotal = itemView.findViewById(R.id.tvTotal);
            this.listener = listener;
            this.longListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Venta venta) {
            tvIdVenta.setText("ID: " + venta.getIdVenta());
            tvIdCliente.setText("ID Cliente: " + venta.getIdCliente());

            tvFechaVenta.setText("Fecha Venta: " + venta.getFechaVenta());

            tvMetodoPago.setText("Método de Pago: " + venta.getMetodoPago());
            tvNumeroTransaccion.setText("N° Transacción: " + venta.getNumeroTransaccion());
            tvTotal.setText("Total: $" + String.format(Locale.getDefault(), "%.2f", venta.getTotal()));
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
