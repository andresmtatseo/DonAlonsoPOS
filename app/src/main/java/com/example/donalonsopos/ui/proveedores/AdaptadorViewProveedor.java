package com.example.donalonsopos.ui.proveedores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;

import java.util.List;

public class AdaptadorViewProveedor extends RecyclerView.Adapter<AdaptadorViewProveedor.ViewHolder> {

    private final Context context;
    private final List<Proveedor> proveedores;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;

    public AdaptadorViewProveedor(Context context, List<Proveedor> proveedores, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.context = context;
        this.proveedores = proveedores;
        this.listener = listener;
        this.longListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_proveedor, parent, false);
        return new ViewHolder(view, listener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proveedor proveedor = proveedores.get(position);
        if (proveedor != null) {
            holder.bind(proveedor);
        }
    }

    @Override
    public int getItemCount() {
        return proveedores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView cedula;
        private final TextView nombre;
        //private final TextView direccion;
        private final TextView telefono;
        private final TextView email;
        private final OnItemClickListener listener;
        private final OnItemLongClickListener longListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(itemView);
            this.cedula = itemView.findViewById(R.id.tvCedula);
            this.nombre = itemView.findViewById(R.id.tvNombre);
            //this.direccion = itemView.findViewById(R.id.tvDireccion);
            this.telefono = itemView.findViewById(R.id.tvRol);
            this.email = itemView.findViewById(R.id.tvApellido);
            this.listener = listener;
            this.longListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Proveedor proveedor) {
            cedula.setText("Cédula/RIF: " + proveedor.getCedula());
            nombre.setText("Nombre: " + proveedor.getNombre());
            //direccion.setText("Dirección: " + proveedor.getDireccion());
            telefono.setText("Teléfono: " + proveedor.getTelefono());
            email.setText("Email: " + proveedor.getEmail());
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
