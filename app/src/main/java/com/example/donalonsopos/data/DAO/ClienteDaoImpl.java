package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.Cliente;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class ClienteDaoImpl {
    private static final String TAG = "ClienteDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public ClienteDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "cliente";
    private static final String COLUMN_ID = "idCliente";
    private static final String COLUMN_CEDULA = "cedula";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDO = "apellido";
    private static final String COLUMN_DIRECCION = "direccion";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_ISACTIVE = "isActive";

    // Obtener todos los clientes activos
    public List<Cliente> select() {
        List<Cliente> clientes = new ArrayList<>();
        Cursor cursor = null;
        try {
            // Solo seleccionar clientes donde isActive = 1
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Cliente cliente = new Cliente(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))
                    );
                    clientes.add(cliente);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar clientes: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return clientes;
    }

    // Buscar cliente por cédula
    public Cliente findByCedula(String cedula) {
        Cliente cliente = null;
        Cursor cursor = null;
        try {
            // Query para buscar cliente activo con la cédula
            String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_CEDULA + " = ? AND " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, new String[]{cedula});

            if (cursor.moveToFirst()) {
                cliente = new Cliente(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))
                );
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al buscar cliente por cédula: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cliente;
    }

    // Insertar cliente
    public long insert(Cliente cliente) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CEDULA, cliente.getCedula());
            values.put(COLUMN_NOMBRE, cliente.getNombre());
            values.put(COLUMN_APELLIDO, cliente.getApellido());
            values.put(COLUMN_DIRECCION, cliente.getDireccion());
            values.put(COLUMN_TELEFONO, cliente.getTelefono());
            values.put(COLUMN_ISACTIVE, 1); // Siempre insertar como activo

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar cliente: ", e);
        }
        return id;
    }

    // Actualizar cliente
    public int update(Cliente cliente) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CEDULA, cliente.getCedula());
            values.put(COLUMN_NOMBRE, cliente.getNombre());
            values.put(COLUMN_APELLIDO, cliente.getApellido());
            values.put(COLUMN_DIRECCION, cliente.getDireccion());
            values.put(COLUMN_TELEFONO, cliente.getTelefono());

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(cliente.getIdCliente())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar cliente: ", e);
        }
        return rows;
    }

    // Eliminar cliente (Soft Delete)
    public int delete(int idCliente) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 0); // Marcar como inactivo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idCliente)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al marcar cliente como inactivo: ", e);
        }
        return rows;
    }

    // Reactivar cliente (opcional)
    public int reactivate(int idCliente) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 1); // Marcar como activo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idCliente)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al reactivar cliente: ", e);
        }
        return rows;
    }

    // Cerrar la conexión
    public void close() {
        if (dbManager != null) {
            dbManager.close();
        }
    }
}