package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class Proveedor implements Serializable {
    // Atributos
    private int idProveedor;
    private String cedula;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private boolean isActive;

    // Constructores
    public Proveedor(int anInt, String string, String cursorString, String s, String string1, String cursorString1) {
    }

    public Proveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Proveedor(int idProveedor, String cedula) {
        this.idProveedor = idProveedor;
        this.cedula = cedula;
    }

    public Proveedor(int idProveedor, String cedula, String nombre, String direccion, String telefono, String email, boolean isActive) {
        this.idProveedor = idProveedor;
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.isActive = isActive;
    }

    // Métodos
    public int getIdProveedor() {
        return idProveedor;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
