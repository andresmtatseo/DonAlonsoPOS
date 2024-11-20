package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class Producto implements Serializable {
    // Atributos
    private int idProducto;
    private int idCategoria;
    private String nombre;
    private double precio;
    private String imagenURL;
    private String descripcion;
    private int cantidadActual;
    private int cantidadMinima;
    private int isActive;

    //Constructores
    public Producto(int anInt, int cursorInt, String string, float aFloat, String cursorString, String s, int i, int anInt1) {
    }

    public Producto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Producto(int idProducto, int idCategoria, String nombre, int cantidadActual, double precio, String imagenURL) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.cantidadActual = cantidadActual;
        this.precio = precio;
        this.imagenURL = imagenURL;
    }

    public Producto(int idProducto, int idCategoria, String nombre, double precio, String imagenURL, String descripcion, int cantidadActual, int cantidadMinima, int isActive) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenURL = imagenURL;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
        this.isActive = isActive;
    }

    //Metodos
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

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagen(String imagen) {
        this.imagenURL = imagen;
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
