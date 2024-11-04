package com.example.donalonsopos.data.entities;

import java.io.Serializable;
import java.util.Date;

public class Compra implements Serializable {
    // Atributos
    private int idCompra;
    private int idProveedor;
    private Date fechaCompra;
    private String metodoPago;
    private String numeroFactura;
    private float total;
    private boolean isActive;

    // Constructores
    public Compra() {
    }

    public Compra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Compra(int idCompra, int idProveedor, Date fechaCompra, String metodoPago, String numeroFactura, float total, boolean isActive) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.fechaCompra = fechaCompra;
        this.metodoPago = metodoPago;
        this.numeroFactura = numeroFactura;
        this.total = total;
        this.isActive = isActive;
    }

    // Métodos
    public int getIdCompra() {
        return idCompra;
    }


    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
