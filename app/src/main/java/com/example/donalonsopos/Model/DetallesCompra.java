package com.example.donalonsopos.Model;

public class DetallesCompra {
    private int idDetallesCompra;
    private int idCompra;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private String fechaExpiracion;

    public int getIdDetallesCompra() {
        return idDetallesCompra;
    }

    public void setIdDetallesCompra(int idDetallesCompra) {
        this.idDetallesCompra = idDetallesCompra;
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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}