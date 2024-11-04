package com.example.donalonsopos.data.entities;

import java.io.Serializable;
import java.util.Date;

public class DetallesCompra implements Serializable {
    // Atributos
    private int idDetallesCompra;
    private int idCompra;
    private int idProducto;
    private int cantidad;
    private float precioUnitario;
    private Date fechaExpiracion;

    // Constructores
    public DetallesCompra() {
    }

    public DetallesCompra(int idDetallesCompra) {
        this.idDetallesCompra = idDetallesCompra;
    }

    public DetallesCompra(int idDetallesCompra, int idCompra, int idProducto, int cantidad, float precioUnitario, Date fechaExpiracion) {
        this.idDetallesCompra = idDetallesCompra;
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fechaExpiracion = fechaExpiracion;
    }

    // Métodos
    public int getIdDetallesCompra() {
        return idDetallesCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
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

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
