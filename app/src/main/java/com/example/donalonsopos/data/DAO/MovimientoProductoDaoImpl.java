package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.MovimientoProducto;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MovimientoProductoDaoImpl {
    private static final String TAG = "MovimientoProductoDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public MovimientoProductoDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "movimientoProducto";
    private static final String COLUMN_IDMOVIMIENTO = "idMovimiento";
    private static final String COLUMN_IDPRODUCTO = "idProducto";
    private static final String COLUMN_IDUSUARIO = "idUsuario";
    private static final String COLUMN_TIPOMOVIMIENTO = "tipoMovimiento";
    private static final String COLUMN_REFERENCIA = "referencia";
    private static final String COLUMN_CANTIDAD = "cantidad";
    private static final String COLUMN_FECHAMOVIMIENTO = "fechaMovimiento";
    private static final String COLUMN_DESCRIPCION = "descripcion";

    // Obtener todos los movimientos de productos
    public List<MovimientoProducto> select() {
        List<MovimientoProducto> movimientos = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    MovimientoProducto movimiento = new MovimientoProducto(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDMOVIMIENTO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDPRODUCTO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDUSUARIO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPOMOVIMIENTO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REFERENCIA)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHAMOVIMIENTO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                    );
                    movimientos.add(movimiento);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar movimientos de productos: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return movimientos;
    }

    // Insertar movimiento de producto
    public long insert(MovimientoProducto movimientoProducto) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDPRODUCTO, movimientoProducto.getIdProducto());
            values.put(COLUMN_IDUSUARIO, movimientoProducto.getIdUsuario());
            values.put(COLUMN_TIPOMOVIMIENTO, movimientoProducto.getTipoMovimiento());
            values.put(COLUMN_REFERENCIA, movimientoProducto.getReferencia());
            values.put(COLUMN_CANTIDAD, movimientoProducto.getCantidad());
            values.put(COLUMN_FECHAMOVIMIENTO, movimientoProducto.getFechaMovimiento());
            values.put(COLUMN_DESCRIPCION, movimientoProducto.getDescripcion());

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar movimiento de producto: ", e);
        }
        return id;
    }

    // Obtener movimiento por ID
    public MovimientoProducto findById(int idMovimiento) {
        MovimientoProducto movimiento = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IDMOVIMIENTO + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(idMovimiento)});

            if (cursor.moveToFirst()) {
                movimiento = new MovimientoProducto(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDMOVIMIENTO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDPRODUCTO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDUSUARIO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPOMOVIMIENTO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REFERENCIA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHAMOVIMIENTO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                );
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al buscar movimiento de producto por ID: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return movimiento;
    }

    // Eliminar movimiento por ID
    public int delete(int idMovimiento) {
        int rows = 0;
        try {
            rows = db.delete(TABLE_NAME, COLUMN_IDMOVIMIENTO + " = ?", new String[]{String.valueOf(idMovimiento)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar movimiento de producto: ", e);
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