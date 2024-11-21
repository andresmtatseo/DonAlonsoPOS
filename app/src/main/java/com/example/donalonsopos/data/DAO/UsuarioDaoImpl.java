package com.example.donalonsopos.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.donalonsopos.data.DTO.Usuario;
import com.example.donalonsopos.data.DB.DBManager;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoImpl {
    private static final String TAG = "UsuarioDao";
    private SQLiteDatabase db;
    private DBManager dbManager;

    public UsuarioDaoImpl(Context context) {
        try {
            dbManager = new DBManager(context);
            dbManager.open(); // Abrir la conexión
            db = dbManager.getDatabase(); // Usar el getter para obtener la base de datos
        } catch (SQLException e) {
            Log.e(TAG, "Error al abrir la base de datos: ", e);
        }
    }

    private static final String TABLE_NAME = "usuario";
    private static final String COLUMN_ID = "idUsuario";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROL = "rol";
    private static final String COLUMN_CEDULA = "cedula";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_APELLIDO = "apellido";
    private static final String COLUMN_ISACTIVE = "isActive";

    // Obtener todos los usuarios activos
    public List<Usuario> select() {
        List<Usuario> usuarios = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ISACTIVE + " = 1";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Usuario usuario = new Usuario(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO))
                    );
                    usuarios.add(usuario);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error al consultar usuarios: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return usuarios;
    }

    // Insertar usuario
    public long insert(Usuario usuario) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, usuario.getUsername());
            values.put(COLUMN_PASSWORD, usuario.getPassword());
            values.put(COLUMN_ROL, usuario.getRol());
            values.put(COLUMN_CEDULA, usuario.getCedula());
            values.put(COLUMN_NOMBRE, usuario.getNombre());
            values.put(COLUMN_APELLIDO, usuario.getApellido());
            values.put(COLUMN_ISACTIVE, 1); // Siempre insertar como activo

            id = db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Error al insertar usuario: ", e);
        }
        return id;
    }

    // Actualizar usuario
    public int update(Usuario usuario) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, usuario.getUsername());
            values.put(COLUMN_PASSWORD, usuario.getPassword());
            values.put(COLUMN_ROL, usuario.getRol());
            values.put(COLUMN_CEDULA, usuario.getCedula());
            values.put(COLUMN_NOMBRE, usuario.getNombre());
            values.put(COLUMN_APELLIDO, usuario.getApellido());

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(usuario.getIdUsuario())});
        } catch (SQLException e) {
            Log.e(TAG, "Error al actualizar usuario: ", e);
        }
        return rows;
    }

    // Eliminar usuario (Soft Delete)
    public int delete(int idUsuario) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 0); // Marcar como inactivo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idUsuario)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al eliminar usuario: ", e);
        }
        return rows;
    }

    // Reactivar usuario (opcional)
    public int reactivate(int idUsuario) {
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ISACTIVE, 1); // Marcar como activo

            rows = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(idUsuario)});
        } catch (SQLException e) {
            Log.e(TAG, "Error al reactivar usuario: ", e);
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
