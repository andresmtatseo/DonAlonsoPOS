package com.example.donalonsopos.data.DTO;

import java.io.Serializable;
import java.util.Date;

public class Compra implements Serializable {
    // Atributos
    private int idCompra;
    private int idProveedor;
    private String fechaCompra;
    private String metodoPago;
    private String numeroFactura;
    private float total;
    private int isActive;

    // Constructores


    public Compra(int idCompra, int idProveedor, String metodoPago, String fechaCompra, String numeroFactura, float total, int isActive) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.metodoPago = metodoPago;
        this.fechaCompra = fechaCompra;
        this.numeroFactura = numeroFactura;
        this.isActive = isActive;
        this.total = total;
    }

    public Compra(int idCompra, int idProveedor, String fechaCompra, String metodoPago, String numeroFactura, float total) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.fechaCompra = fechaCompra;
        this.metodoPago = metodoPago;
        this.numeroFactura = numeroFactura;
        this.total = total;

    }

    public Compra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Compra(int idCompra, int idProveedor, String metodoPago) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.metodoPago = metodoPago;
    }

    public Compra(int idProveedor, String metodoPago, String numeroFactura) {
        this.idProveedor = idProveedor;
        this.metodoPago = metodoPago;
        this.numeroFactura = numeroFactura;
    }

    public Compra(int idCompra, int idProveedor, String metodoPago, String numeroFactura) {
        this.idCompra = idCompra;
        this.idProveedor = idProveedor;
        this.metodoPago = metodoPago;
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

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
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

}
