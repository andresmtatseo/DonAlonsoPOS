package com.example.donalonsopos.data.DTO;

import java.io.Serializable;

public class Venta implements Serializable {
    // Atributos
    private int idVenta;
    private int idUsuario;
    private int idCliente;
    private String fechaVenta; // Cambiado de Date a String
    private String metodoPago;
    private int numeroTransaccion;
    private String fechaPago; // Cambiado de Date a String
    private float total;
    private boolean isActive;

    // Constructores
    public Venta(int idVenta, int idUsuario, int idCliente, String fechaVenta, String metodoPago, int numeroTransaccion, String fechaPago, float total) {
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

    public Venta(int idVenta, int idUsuario, int idCliente, String fechaVenta, String metodoPago, int numeroTransaccion, String fechaPago) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
        this.metodoPago = metodoPago;
        this.numeroTransaccion = numeroTransaccion;
        this.fechaPago = fechaPago;
    }

    public Venta(int idVenta, int idUsuario, int idCliente, String fechaVenta, String metodoPago, int numeroTransaccion) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
        this.metodoPago = metodoPago;
        this.numeroTransaccion = numeroTransaccion;
    }

    public Venta(int idVenta, int idUsuario, int idCliente, String fechaVenta, String metodoPago) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
        this.metodoPago = metodoPago;
    }

    public Venta(int idUsuario, int idVenta, int idCliente, String fechaVenta) {
        this.idUsuario = idUsuario;
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
    }

    public Venta(int idVenta, int idUsuario, int idCliente) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
    }

    public Venta(int idVenta, int idUsuario) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
    }

    public Venta(int idVenta) {
        this.idVenta = idVenta;
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

    public String getFechaVenta() { // Cambiado a String
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) { // Cambiado a String
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

    public String getFechaPago() { // Cambiado a String
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) { // Cambiado a String
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
