package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.Producto;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl {
    private static final String TAG = "ProductoDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public ProductoDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "producto";
    private static final String COLUMN_ID = "idProducto";
    private static final String COLUMN_ID_CATEGORIA = "idCategoria";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_PRECIO = "precio";
    private static final String COLUMN_IMAGEN_URL = "imagenURL"; // Ahora es un BLOB
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_CANTIDAD_ACTUAL = "cantidadActual";
    private static final String COLUMN_CANTIDAD_MINIMA = "cantidadMinima";
    private static final String COLUMN_ISACTIVE = "isActive";

    // Obtener todos los productos activos
    public List<Producto> select() {
        List<Producto> productos = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    byte[] imagenBlob = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN_URL));

                    Producto producto = new Producto(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_CATEGORIA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRECIO)),
                            imagenBlob, // Imagen en formato byte[]
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD_ACTUAL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD_MINIMA))
                    );
                    productos.add(producto);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar productos: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productos;
    }

    public Producto findById(int idProducto) {
        Producto producto = null;
        Cursor cursor = null;
        try {
            // Consulta SQL para obtener el producto por su ID
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ? AND " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, new String[]{String.valueOf(idProducto)});

            // Verifica si el cursor tiene resultados
            if (cursor != null && cursor.moveToFirst()) {
                byte[] imagenBlob = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN_URL));

                // Crear el objeto Producto con los datos obtenidos del cursor
                producto = new Producto(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_CATEGORIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRECIO)),
                        imagenBlob,  // Imagen en formato byte[]
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD_ACTUAL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CANTIDAD_MINIMA))
                );
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al buscar producto por ID: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return producto;  // Retorna el producto encontrado, o null si no se encuentra
    }

    // Insertar producto
    public long insert(Producto producto) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID_CATEGORIA, producto.getIdCategoria());
            values.put(COLUMN_NOMBRE, producto.getNombre());
            values.put(COLUMN_PRECIO, producto.getPrecio());
            values.put(COLUMN_IMAGEN_URL, producto.getImagenBlob()); // Imagen en formato byte[]
            values.put(COLUMN_DESCRIPCION, producto.getDescripcion());
            values.put(COLUMN_CANTIDAD_ACTUAL, producto.getCantidadActual());
            values.put(COLUMN_CANTIDAD_MINIMA, producto.getCantidadMinima());
            values.put(COLUMN_ISACTIVE, 1); // Siempre insertar como activo

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar producto: ", e);
        }
        return id;
    }

    // Actualizar producto
    public int update(Producto producto) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID_CATEGORIA, producto.getIdCategoria());
            values.put(COLUMN_NOMBRE, producto.getNombre());
            values.put(COLUMN_PRECIO, producto.getPrecio());
            values.put(COLUMN_IMAGEN_URL, producto.getImagenBlob()); // Imagen en formato byte[]
            values.put(COLUMN_DESCRIPCION, producto.getDescripcion());
            values.put(COLUMN_CANTIDAD_ACTUAL, producto.getCantidadActual());
            values.put(COLUMN_CANTIDAD_MINIMA, producto.getCantidadMinima());

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(producto.getIdProducto())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar producto: ", e);
        }
        return rows;
    }

    // Eliminar producto (Soft Delete)
    public int delete(int idProducto) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 0); // Marcar como inactivo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idProducto)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar producto: ", e);
        }
        return rows;
    }

    // Reactivar producto (opcional)
    public int reactivate(int idProducto) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 1); // Marcar como activo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idProducto)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al reactivar producto: ", e);
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
