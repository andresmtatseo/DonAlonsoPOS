package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.DetallesCompra;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DetallesCompraDaoImpl {
    private static final String TAG = "DetallesCompraDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public DetallesCompraDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "detallesCompra";
    private static final String COLUMN_ID = "idDetallesCompra";
    private static final String COLUMN_IDCOMPRA = "idCompra";
    private static final String COLUMN_IDPRODUCTO = "idProducto";
    private static final String COLUMN_CANTIDAD = "cantidad";
    private static final String COLUMN_PRECIOUNITARIO = "precioUnitario";
    private static final String COLUMN_FECHAEXPIRACION = "fechaExpiracion";

    // Obtener todos los detalles de compra
    public List<DetallesCompra> select() {
        List<DetallesCompra> detallesCompras = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    DetallesCompra detallesCompra = new DetallesCompra(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDCOMPRA)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDPRODUCTO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRECIOUNITARIO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHAEXPIRACION)) // Guardar como String para facilidad
                    );
                    detallesCompras.add(detallesCompra);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar detalles de compra: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return detallesCompras;
    }

    // Insertar detalles de compra
    public long insert(DetallesCompra detallesCompra) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDCOMPRA, detallesCompra.getIdCompra());
            values.put(COLUMN_IDPRODUCTO, detallesCompra.getIdProducto());
            values.put(COLUMN_CANTIDAD, detallesCompra.getCantidad());
            values.put(COLUMN_PRECIOUNITARIO, detallesCompra.getPrecioUnitario());
            values.put(COLUMN_FECHAEXPIRACION, detallesCompra.getFechaExpiracion()); // Almacenar fecha como String (formato YYYY-MM-DD)

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar detalles de compra: ", e);
        }
        return id;
    }

    // Actualizar detalles de compra
    public int update(DetallesCompra detallesCompra) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDCOMPRA, detallesCompra.getIdCompra());
            values.put(COLUMN_IDPRODUCTO, detallesCompra.getIdProducto());
            values.put(COLUMN_CANTIDAD, detallesCompra.getCantidad());
            values.put(COLUMN_PRECIOUNITARIO, detallesCompra.getPrecioUnitario());
            values.put(COLUMN_FECHAEXPIRACION, detallesCompra.getFechaExpiracion()); // Actualizar fecha

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(detallesCompra.getIdDetallesCompra())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar detalles de compra: ", e);
        }
        return rows;
    }

    // Eliminar detalles de compra
    public int delete(int idDetallesCompra) {
        int rows = 0;
        try {
            rows = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(idDetallesCompra)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar detalles de compra: ", e);
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

//hola