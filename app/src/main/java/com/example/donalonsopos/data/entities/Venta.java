package com.example.donalonsopos.data.entities;

import java.io.Serializable;
import java.util.Date;

public class Venta implements Serializable {
    // Atributos
    private int idVenta;
    private int idUsuario;
    private int idCliente;
    private Date fechaVenta;
    private String metodoPago;
    private int numeroTransaccion;
    private Date fechaPago;
    private float total;
    private boolean isActive;

    // Constructores
    public Venta() {
    }

    public Venta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Venta(int idVenta, int idUsuario, int idCliente, Date fechaVenta, String metodoPago, int numeroTransaccion, Date fechaPago, float total, boolean isActive) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
        this.metodoPago = metodoPago;
        this.numeroTransaccion = numeroTransaccion;
        this.fechaPago = fechaPago;
        this.total = total;
        this.isActive = isActive;
    }

    // Métodos
    public int getIdVenta() {
        return idVenta;
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(int numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
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
