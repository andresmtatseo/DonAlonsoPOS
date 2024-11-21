package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.ProductoProveedor;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class ProductoProveedorDaoImpl {
    private static final String TAG = "ProductoProveedorDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public ProductoProveedorDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "productoProveedor";
    private static final String COLUMN_IDPRODUCTO = "idProducto";
    private static final String COLUMN_IDPROVEEDOR = "idProveedor";

    // Obtener todos los productos y proveedores relacionados
    public List<ProductoProveedor> select() {
        List<ProductoProveedor> productoProveedores = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    ProductoProveedor productoProveedor = new ProductoProveedor(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDPRODUCTO)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDPROVEEDOR))
                    );
                    productoProveedores.add(productoProveedor);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar producto-proveedor: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productoProveedores;
    }

    // Insertar relación producto-proveedor
    public long insert(ProductoProveedor productoProveedor) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDPRODUCTO, productoProveedor.getIdProducto());
            values.put(COLUMN_IDPROVEEDOR, productoProveedor.getIdProveedor());

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar producto-proveedor: ", e);
        }
        return id;
    }

    // Eliminar relación producto-proveedor
    public int delete(int idProducto, int idProveedor) {
        int rows = 0;
        try {
            rows = db.delete(TABLE_NAME, COLUMN_IDPRODUCTO + " = ? AND " + COLUMN_IDPROVEEDOR + " = ?",
                    new String[]{String.valueOf(idProducto), String.valueOf(idProveedor)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar producto-proveedor: ", e);
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
