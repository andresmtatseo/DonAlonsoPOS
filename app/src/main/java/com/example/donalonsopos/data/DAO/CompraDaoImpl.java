package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.Compra;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class CompraDaoImpl {
    private static final String TAG = "CompraDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public CompraDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "compra";
    private static final String COLUMN_ID = "idCompra";
    private static final String COLUMN_ID_PROVEEDOR = "idProveedor";
    private static final String COLUMN_FECHA_COMPRA = "fechaCompra";
    private static final String COLUMN_METODO_PAGO = "metodoPago";
    private static final String COLUMN_NUMERO_FACTURA = "numeroFactura";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_ISACTIVE = "isActive";

    // Obtener todas las compras activas
    public List<Compra> select() {
        List<Compra> compras = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Compra compra = new Compra(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_PROVEEDOR)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_COMPRA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_METODO_PAGO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMERO_FACTURA)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_TOTAL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ISACTIVE))
                    );
                    compras.add(compra);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar compras: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return compras;
    }

    // Insertar compra
    public long insert(Compra compra) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID_PROVEEDOR, compra.getIdProveedor());
            values.put(COLUMN_FECHA_COMPRA, compra.getFechaCompra());
            values.put(COLUMN_METODO_PAGO, compra.getMetodoPago());
            values.put(COLUMN_NUMERO_FACTURA, compra.getNumeroFactura());
            values.put(COLUMN_TOTAL, compra.getTotal());
            values.put(COLUMN_ISACTIVE, 1); // Siempre insertar como activo

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar compra: ", e);
        }
        return id;
    }

    // Actualizar compra
    public int update(Compra compra) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID_PROVEEDOR, compra.getIdProveedor());
            values.put(COLUMN_FECHA_COMPRA, compra.getFechaCompra());
            values.put(COLUMN_METODO_PAGO, compra.getMetodoPago());
            values.put(COLUMN_NUMERO_FACTURA, compra.getNumeroFactura());
            values.put(COLUMN_TOTAL, compra.getTotal());

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(compra.getIdCompra())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar compra: ", e);
        }
        return rows;
    }

    // Eliminar compra (Soft Delete)
    public int delete(int idCompra) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 0); // Marcar como inactivo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idCompra)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar compra: ", e);
        }
        return rows;
    }

    // Reactivar compra (opcional)
    public int reactivate(int idCompra) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 1); // Marcar como activo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idCompra)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al reactivar compra: ", e);
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

//.