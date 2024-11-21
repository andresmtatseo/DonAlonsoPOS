package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.Venta;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class VentaDaoImpl {
    private static final String TAG = "VentaDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public VentaDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "venta";
    private static final String COLUMN_ID = "idVenta";
    private static final String COLUMN_IDUSUARIO = "idUsuario";
    private static final String COLUMN_IDCLIENTE = "idCliente";
    private static final String COLUMN_FECHAVENTA = "fechaVenta";
    private static final String COLUMN_METODOPAGO = "metodoPago";
    private static final String COLUMN_NUMEROTRANSACCION = "numeroTransaccion";
    private static final String COLUMN_FECHAPAGO = "fechaPago";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_ISACTIVE = "isActive";

    // Obtener todas las ventas activas
    public List<Venta> select() {
        List<Venta> ventas = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Venta venta = new Venta(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDUSUARIO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDCLIENTE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHAVENTA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_METODOPAGO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMEROTRANSACCION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHAPAGO)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_TOTAL))
                    );
                    ventas.add(venta);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar ventas: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ventas;
    }

    // Insertar venta
    public long insert(Venta venta) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDUSUARIO, venta.getIdUsuario());
            values.put(COLUMN_IDCLIENTE, venta.getIdCliente());
            values.put(COLUMN_FECHAVENTA, venta.getFechaVenta());
            values.put(COLUMN_METODOPAGO, venta.getMetodoPago());
            values.put(COLUMN_NUMEROTRANSACCION, venta.getNumeroTransaccion());
            values.put(COLUMN_FECHAPAGO, venta.getFechaPago());
            values.put(COLUMN_TOTAL, venta.getTotal());
            values.put(COLUMN_ISACTIVE, 1); // Siempre insertar como activo

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar venta: ", e);
        }
        return id;
    }

    // Actualizar venta
    public int update(Venta venta) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDUSUARIO, venta.getIdUsuario());
            values.put(COLUMN_IDCLIENTE, venta.getIdCliente());
            values.put(COLUMN_FECHAVENTA, venta.getFechaVenta());
            values.put(COLUMN_METODOPAGO, venta.getMetodoPago());
            values.put(COLUMN_NUMEROTRANSACCION, venta.getNumeroTransaccion());
            values.put(COLUMN_FECHAPAGO, venta.getFechaPago());
            values.put(COLUMN_TOTAL, venta.getTotal());

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(venta.getIdVenta())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar venta: ", e);
        }
        return rows;
    }

    // Eliminar venta (Soft Delete)
    public int delete(int idVenta) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 0); // Marcar como inactivo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idVenta)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar venta: ", e);
        }
        return rows;
    }

    // Reactivar venta (opcional)
    public int reactivate(int idVenta) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 1); // Marcar como activo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idVenta)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al reactivar venta: ", e);
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
