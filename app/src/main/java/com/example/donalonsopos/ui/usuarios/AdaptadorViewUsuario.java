package com.example.donalonsopos.ui.usuarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.entities.Usuario;
import com.example.donalonsopos.util.OnItemClickListener;
import com.example.donalonsopos.util.OnItemLongClickListener;

import java.util.List;

public class AdaptadorViewUsuario extends RecyclerView.Adapter<AdaptadorViewUsuario.ViewHolder> {

    private final Context context;
    private final List<Usuario> usuarios;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longListener;

    public AdaptadorViewUsuario(Context context, List<Usuario> usuarios, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.context = context;
        this.usuarios = usuarios;
        this.listener = listener;
        this.longListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_usuario, parent, false);
        return new ViewHolder(view, listener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        if (usuario != null) {
            holder.bind(usuario);
        }
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView cedula;
        private final TextView nombre;
        private final TextView apellido;
        private final TextView rol;
        private final TextView estado;
        private final OnItemClickListener listener;
        private final OnItemLongClickListener longListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(itemView);
            this.cedula = itemView.findViewById(R.id.tvCedula);
            this.nombre = itemView.findViewById(R.id.tvNombre);
            this.apellido = itemView.findViewById(R.id.tvApellido);
            this.rol = itemView.findViewById(R.id.tvRol);
            this.estado = itemView.findViewById(R.id.tvEstado);
            this.listener = listener;
            this.longListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Usuario usuario) {
            cedula.setText("Cédula: " + usuario.getCedula());
            nombre.setText("Nombre: " + usuario.getNombre());
            apellido.setText("Apellido: " + usuario.getApellido());
            rol.setText("Rol: " + usuario.getRol());
            estado.setText("Estado: " + (usuario.getIsActive() ? "Activo" : "Inactivo"));
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
