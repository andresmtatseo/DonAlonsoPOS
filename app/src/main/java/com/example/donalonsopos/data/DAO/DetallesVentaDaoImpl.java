package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.DetallesVenta;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DetallesVentaDaoImpl {
    private static final String TAG = "DetallesVentaDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public DetallesVentaDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "detallesVenta";
    private static final String COLUMN_ID = "idDetallesVenta";
    private static final String COLUMN_IDVENTA = "idVenta";
    private static final String COLUMN_IDPRODUCTO = "idProducto";
    private static final String COLUMN_CANTIDAD = "cantidad";
    private static final String COLUMN_PRECIOUNITARIO = "precioUnitario";

    // Obtener todos los detalles de venta
    public List<DetallesVenta> select() {
        List<DetallesVenta> detallesVentas = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    DetallesVenta detallesVenta = new DetallesVenta(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDVENTA)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDPRODUCTO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRECIOUNITARIO))
                    );
                    detallesVentas.add(detallesVenta);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar detalles de venta: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return detallesVentas;
    }

    // Insertar detalles de venta
    public long insert(DetallesVenta detallesVenta) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDVENTA, detallesVenta.getIdVenta());
            values.put(COLUMN_IDPRODUCTO, detallesVenta.getIdProducto());
            values.put(COLUMN_CANTIDAD, detallesVenta.getCantidad());
            values.put(COLUMN_PRECIOUNITARIO, detallesVenta.getPrecioUnitario());

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar detalles de venta: ", e);
        }
        return id;
    }

    // Actualizar detalles de venta
    public int update(DetallesVenta detallesVenta) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDVENTA, detallesVenta.getIdVenta());
            values.put(COLUMN_IDPRODUCTO, detallesVenta.getIdProducto());
            values.put(COLUMN_CANTIDAD, detallesVenta.getCantidad());
            values.put(COLUMN_PRECIOUNITARIO, detallesVenta.getPrecioUnitario());

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(detallesVenta.getIdDetallesVenta())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar detalles de venta: ", e);
        }
        return rows;
    }

    // Eliminar detalles de venta
    public int delete(int idDetallesVenta) {
        int rows = 0;
        try {
            rows = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(idDetallesVenta)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar detalles de venta: ", e);
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
