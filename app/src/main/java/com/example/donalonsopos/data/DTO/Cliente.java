package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class Cliente implements Serializable {
    // Atributos
    private int idCliente;
    private String cedula;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private boolean isActive;

    // Constructores
    public Cliente() {
    }

    public Cliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Cliente(int idCliente, String cedula) {
        this.idCliente = idCliente;
        this.cedula = cedula;
    }

    public Cliente(int idCliente, String cedula, String nombre) {
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public Cliente(int idCliente, String cedula, String nombre, String apellido, String direccion, String telefono, boolean isActive) {
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.isActive = isActive;
    }

    // Métodos
    public int getIdCliente() {
        return idCliente;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
