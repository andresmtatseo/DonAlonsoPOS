package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.Proveedor;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class ProveedorDaoImpl {
    private static final String TAG = "ProveedorDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public ProveedorDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "proveedor";
    private static final String COLUMN_ID = "idProveedor";
    private static final String COLUMN_CEDULA = "cedula";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_DIRECCION = "direccion";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ISACTIVE = "isActive";

    // Obtener todos los proveedores activos
    public List<Proveedor> select() {
        List<Proveedor> proveedores = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Proveedor proveedor = new Proveedor(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                    );
                    proveedores.add(proveedor);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar proveedores: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return proveedores;
    }

    public Proveedor findByCedula(String cedula) {
        Proveedor proveedor = null;
        Cursor cursor = null;
        try {
            // Consulta SQL para obtener un proveedor por cédula
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CEDULA + " = ? AND " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, new String[]{cedula}); // Usamos ? para evitar inyecciones SQL

            if (cursor.moveToFirst()) {
                // Si se encuentra el proveedor, lo asignamos
                proveedor = new Proveedor(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                );
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar proveedor por cédula: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return proveedor; // Retorna el proveedor encontrado o null si no se encuentra
    }

    // Insertar proveedor
    public long insert(Proveedor proveedor) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CEDULA, proveedor.getCedula());
            values.put(COLUMN_NOMBRE, proveedor.getNombre());
            values.put(COLUMN_DIRECCION, proveedor.getDireccion());
            values.put(COLUMN_TELEFONO, proveedor.getTelefono());
            values.put(COLUMN_EMAIL, proveedor.getEmail());
            values.put(COLUMN_ISACTIVE, 1); // Siempre insertar como activo

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar proveedor: ", e);
        }
        return id;
    }

    // Actualizar proveedor
    public int update(Proveedor proveedor) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CEDULA, proveedor.getCedula());
            values.put(COLUMN_NOMBRE, proveedor.getNombre());
            values.put(COLUMN_DIRECCION, proveedor.getDireccion());
            values.put(COLUMN_TELEFONO, proveedor.getTelefono());
            values.put(COLUMN_EMAIL, proveedor.getEmail());

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(proveedor.getIdProveedor())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar proveedor: ", e);
        }
        return rows;
    }

    // Eliminar proveedor (Soft Delete)
    public int delete(int idProveedor) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 0); // Marcar como inactivo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idProveedor)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar proveedor: ", e);
        }
        return rows;
    }

    // Reactivar proveedor (opcional)
    public int reactivate(int idProveedor) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 1); // Marcar como activo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idProveedor)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al reactivar proveedor: ", e);
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
