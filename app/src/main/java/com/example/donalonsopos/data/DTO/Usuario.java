package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class Usuario implements Serializable {
    // Atributos
    private int idUsuario;
    private String username;
    private String password;
    private int rol;
    private String cedula;
    private String nombre;
    private String apellido;
    private boolean isActive;

    // Constructores
    public Usuario() {
    }

    public Usuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(int idUsuario, String username, String password, int rol, String cedula, String nombre, String apellido, boolean isActive) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.isActive = isActive;
    }

    // Métodos
    public int getIdUsuario() {
        return idUsuario;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
