package com.example.donalonsopos.data.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        indices = {@Index(value = "cedula", unique = true)} // Asegura que cedula sea unica
)
public class UsuarioEntity {

    @PrimaryKey
    @NonNull
    public int idUsuario;

    @NonNull
    public String username;

    @NonNull
    public String password;

    @NonNull
    public int rol;

    @NonNull
    public  String cedula;

    @NonNull
    public String nombre;

    @NonNull
    public String apellido;

    public boolean isActive = true;


}
