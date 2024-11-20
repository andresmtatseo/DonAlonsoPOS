package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private static final String SQL_SELECT = "SELECT idCliente, cedula, nombre, apellido, direccion, telefono FROM cliente";
    private static final String SQL_INSERT = "INSERT INTO cliente (cedula, nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE cliente SET cedula = ?, nombre = ?, apellido = ?, direccion = ?, telefono = ? WHERE idCliente = ?";
    private static final String SQL_DELETE = "DELETE FROM cliente WHERE idCliente = ?";

    private Connection conexion;

    public ClienteDao(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Cliente> select() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    public int insert(Cliente cliente) throws SQLException {
        int rows = 0;
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_INSERT)) {
            stmt.setString(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getApellido());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getTelefono());
            rows = stmt.executeUpdate();
        }
        return rows;
    }

    public int update(Cliente cliente) throws SQLException {
        int rows = 0;
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_UPDATE)) {
            stmt.setString(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getApellido());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getTelefono());
            stmt.setInt(6, cliente.getIdCliente());
            rows = stmt.executeUpdate();
        }
        return rows;
    }

    public int delete(int idCliente) throws SQLException {
        int rows = 0;
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_DELETE)) {
            stmt.setInt(1, idCliente);
            rows = stmt.executeUpdate();
        }
        return rows;
    }
}
