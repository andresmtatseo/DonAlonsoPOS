package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.Categoria;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoImpl {
    private static final String TAG = "CategoriaDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public CategoriaDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "categoria";
    private static final String COLUMN_IDCATEGORIA = "idCategoria";
    private static final String COLUMN_NOMBRE = "nombre";

    // Obtener todas las categorías
    public List<Categoria> select() {
        List<Categoria> categorias = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Categoria categoria = new Categoria(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDCATEGORIA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                    );
                    categorias.add(categoria);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar categorías: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return categorias;
    }

    // Insertar nueva categoría
    public long insert(Categoria categoria) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOMBRE, categoria.getNombre());

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar categoría: ", e);
        }
        return id;
    }

    // Obtener categoría por ID
    public Categoria findById(int idCategoria) {
        Categoria categoria = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IDCATEGORIA + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(idCategoria)});

            if (cursor.moveToFirst()) {
                categoria = new Categoria(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IDCATEGORIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                );
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al buscar categoría por ID: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return categoria;
    }

    // Eliminar categoría por ID
    public int delete(int idCategoria) {
        int rows = 0;
        try {
            rows = db.delete(TABLE_NAME, COLUMN_IDCATEGORIA + " = ?", new String[]{String.valueOf(idCategoria)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar categoría: ", e);
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
