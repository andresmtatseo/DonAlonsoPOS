package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class Producto implements Serializable {

    private int idProducto;
    private int idCategoria;
    private String nombre;
    private double precio;
    private byte[] imagenBlob;
    private String descripcion;
    private int cantidadActual;
    private int cantidadMinima;
    private int isActive;

    public Producto(int idCategoria, String nombre, double precio, byte[] imagenBlob, String descripcion, int cantidadMinima) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenBlob = imagenBlob;
        this.descripcion = descripcion;
        this.cantidadMinima = cantidadMinima;
    }

    public Producto(int idProducto, int idCategoria, String nombre, double precio, byte[] imagenBlob, String descripcion, int cantidadActual, int cantidadMinima) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenBlob = imagenBlob;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
    }

    public Producto(int idCategoria, String nombre, double precio, byte[] imagenBlob, String descripcion, int cantidadActual, int cantidadMinima) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenBlob = imagenBlob;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
    }

    public Producto(int idProducto, int idCategoria, String nombre, double precio, byte[] imagenBlob, String descripcion, int cantidadActual, int cantidadMinima, int isActive) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenBlob = imagenBlob;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
        this.isActive = isActive;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


    public byte[] getImagenBlob() {
        return imagenBlob;
    }

    public void setImagenBlob(byte[] imagenBlob) {
        this.imagenBlob = imagenBlob;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
