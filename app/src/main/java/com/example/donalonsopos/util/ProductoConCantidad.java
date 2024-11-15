package com.example.donalonsopos.util;

import com.example.donalonsopos.data.DTO.Producto;

import java.io.Serializable;

public class ProductoConCantidad implements Serializable {
    private Producto producto;
    private int cantidad;

    public ProductoConCantidad(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
