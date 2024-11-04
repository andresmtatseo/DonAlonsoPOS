package com.example.donalonsopos.data.entities;

import java.io.Serializable;

public class Categoria implements Serializable {
    // Atributos
    private int idCategoria;
    private String nombre;

    // Constructores
    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    // Metodos
    public int getIdCategoria() {
        return idCategoria;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
