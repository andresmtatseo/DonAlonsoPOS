package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class ProductoProveedor implements Serializable {
    // Atributos
    private int idProducto;
    private int idProveedor;

    // Constructores
    public ProductoProveedor() {
    }

    public ProductoProveedor(int idProducto, int idProveedor) {
        this.idProducto = idProducto;
        this.idProveedor = idProveedor;
    }

    // Métodos
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
}
