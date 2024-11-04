package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class DetallesVenta implements Serializable {
    // Atributos
    private int idDetallesVenta;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private float precioUnitario;

    // Constructores
    public DetallesVenta() {
    }

    public DetallesVenta(int idDetallesVenta) {
        this.idDetallesVenta = idDetallesVenta;
    }

    public DetallesVenta(int idDetallesVenta, int idVenta, int idProducto, int cantidad, float precioUnitario) {
        this.idDetallesVenta = idDetallesVenta;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Métodos
    public int getIdDetallesVenta() {
        return idDetallesVenta;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
