package com.example.donalonsopos.ui.clientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;

import java.util.List;

public class AdaptadorViewCliente extends RecyclerView.Adapter<AdaptadorViewCliente.ViewHolder> {

    private final Context context;
    private final List<Cliente> clientes;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;

    public AdaptadorViewCliente(Context context, List<Cliente> clientes, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.context = context;
        this.clientes = clientes;
        this.listener = listener;
        this.longListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_cliente, parent, false);
        return new ViewHolder(view, listener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        if (cliente != null) {
            holder.bind(cliente);
        }
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //private final TextView idCliente;
        private final TextView cedula;
        private final TextView nombre;
        private final TextView apellido;
        //private final TextView direccion;
        private final TextView telefono;
        private final OnItemClickListener listener;
        private final OnItemLongClickListener longListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(itemView);
            //this.idCliente = itemView.findViewById(R.id.tvIdCliente);
            this.cedula = itemView.findViewById(R.id.tvCedula);
            this.nombre = itemView.findViewById(R.id.tvNombre);
            this.apellido = itemView.findViewById(R.id.tvApellido);
            //this.direccion = itemView.findViewById(R.id.tvDireccion);
            this.telefono = itemView.findViewById(R.id.tvRol);
            this.listener = listener;
            this.longListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Cliente cliente) {
            //idCliente.setText("ID: " + cliente.getIdCliente());
            cedula.setText("Cédula: " + cliente.getCedula());
            nombre.setText("Nombre: " + cliente.getNombre());
            apellido.setText("Apellido: " + cliente.getApellido());
            //direccion.setText("Dirección: " + cliente.getDireccion());
            telefono.setText("Teléfono: " + cliente.getTelefono());
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
