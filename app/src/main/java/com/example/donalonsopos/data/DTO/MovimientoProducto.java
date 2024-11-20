package com.example.donalonsopos.data.DTO;

import java.io.Serializable;
import java.util.Date;

public class MovimientoProducto implements Serializable {
    // Atributos
    private int idMovimiento;
    private int idProducto;
    private int idUsuario;
    private String tipoMovimiento;
    private int referencia;
    private int cantidad;
    private Date fechaMovimiento;
    private String descripcion;

    // Constructores
    public MovimientoProducto(int anInt, int cursorInt, int idUsuario, String string, int referencia, int cantidad, String cursorString, String descripcion) {
    }

    public MovimientoProducto(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public MovimientoProducto(int idMovimiento, int idProducto, int idUsuario, String tipoMovimiento, int referencia, int cantidad, Date fechaMovimiento, String descripcion) {
        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.referencia = referencia;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.descripcion = descripcion;
    }

    // Métodos
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getReferencia() {
        return referencia;
    }

    public void setReferencia(int referencia) {
        this.referencia = referencia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
