package com.example.donalonsopos.data.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConexion extends SQLiteOpenHelper {

    private static final String DB_NAME = "DonAlonsoDB";
    private static final int DB_VERSION = 2;


    public DBConexion(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Usuario
        String CREATE_USUARIO_TABLE = "CREATE TABLE " + DBManager.TABLA_USUARIO + "("
                + DBManager.USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.USUARIO_USERNAME + " TEXT NOT NULL,"
                + DBManager.USUARIO_PASSWORD + " TEXT NOT NULL,"
                + DBManager.USUARIO_ROL + " TEXT NOT NULL,"
                + DBManager.USUARIO_CEDULA + " TEXT UNIQUE NOT NULL,"
                + DBManager.USUARIO_NOMBRE + " TEXT NOT NULL,"
                + DBManager.USUARIO_APELLIDO + " TEXT NOT NULL,"
                + DBManager.USUARIO_ISACTIVE + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_USUARIO_TABLE);

        //Cliente
        String CREATE_CLIENTE_TABLE = "CREATE TABLE " + DBManager.TABLA_CLIENTE + "("
                + DBManager.CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.CLIENTE_CEDULA + " TEXT UNIQUE  NOT NULL,"
                + DBManager.CLIENTE_NOMBRE + " TEXT  NOT NULL,"
                + DBManager.CLIENTE_APELLIDO + " TEXT  NOT NULL,"
                + DBManager.CLIENTE_DIRECCION + " TEXT,"
                + DBManager.CLIENTE_TELEFONO + " TEXT,"
                + DBManager.CLIENTE_ISACTIVE + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_CLIENTE_TABLE);

        String CREATE_VENTA_TABLE = "CREATE TABLE " + DBManager.TABLA_VENTA + "("
                + DBManager.VENTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.VENTA_FECHA + " TEXT NOT NULL,"
                + DBManager.VENTA_METODOPAGO + " TEXT NOT NULL,"
                + DBManager.VENTA_NUMEROTRANSACCION + " TEXT,"
                + DBManager.VENTA_FECHAPAGO + " TEXT,"
                + DBManager.VENTA_TOTAL + " REAL NOT NULL,"
                + DBManager.VENTA_IDUSUARIO + " INTEGER NOT NULL,"
                + DBManager.VENTA_IDCLIENTE + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + DBManager.VENTA_IDUSUARIO + ") REFERENCES " + DBManager.TABLA_USUARIO + "(" + DBManager.USUARIO_ID + "), "
                + "FOREIGN KEY (" + DBManager.VENTA_IDCLIENTE + ") REFERENCES " + DBManager.TABLA_CLIENTE + "(" + DBManager.CLIENTE_ID + "));";
        db.execSQL(CREATE_VENTA_TABLE);

        String CREATE_CATEGORIA_TABLE = "CREATE TABLE " + DBManager.TABLA_CATEGORIA + "("
                + DBManager.CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.CATEGORIA_NOMBRE + " TEXT NOT NULL);";
        db.execSQL(CREATE_CATEGORIA_TABLE);

        String CREATE_PRODUCTO_TABLE = "CREATE TABLE " + DBManager.TABLA_PRODUCTO + "("
                + DBManager.PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.PRODUCTO_NOMBRE + " TEXT NOT NULL,"
                + DBManager.PRODUCTO_PRECIO + " REAL NOT NULL,"
                + DBManager.PRODUCTO_IMAGENURL + " BLOB,"
                + DBManager.PRODUCTO_DESCRIPCION + " TEXT,"
                + DBManager.PRODUCTO_CANTIDADACTUAL + " INTEGER NOT NULL,"
                + DBManager.PRODUCTO_CANTIDADMINIMA + " INTEGER NOT NULL,"
                + DBManager.PRODUCTO_ISACTIVE + " INTEGER NOT NULL,"
                + DBManager.PRODUCTO_IDCATEGORIA + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + DBManager.PRODUCTO_IDCATEGORIA + ") REFERENCES " + DBManager.TABLA_CATEGORIA + "(" + DBManager.CATEGORIA_ID + "));";
        db.execSQL(CREATE_PRODUCTO_TABLE);

        String CREATE_PROVEEDOR_TABLE = "CREATE TABLE " + DBManager.TABLA_PROVEEDOR + "("
                + DBManager.PROVEEDOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.PROVEEDOR_CEDULA + " TEXT UNIQUE NOT NULL,"
                + DBManager.PROVEEDOR_NOMBRE + " TEXT NOT NULL,"
                + DBManager.PROVEEDOR_DIRECCION + " TEXT,"
                + DBManager.PROVEEDOR_TELEFONO + " TEXT,"
                + DBManager.PROVEEDOR_EMAIL + " TEXT,"
                + DBManager.PROVEEDOR_ISACTIVE + " INTEGER NOT NULL);";
        db.execSQL(CREATE_PROVEEDOR_TABLE);

        String CREATE_COMPRA_TABLE = "CREATE TABLE " + DBManager.TABLA_COMPRA + "("
                + DBManager.COMPRA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.COMPRA_FECHA + " TEXT NOT NULL,"
                + DBManager.COMPRA_METODOPAGO + " TEXT NOT NULL,"
                + DBManager.COMPRA_NUMEROFACTURA + " TEXT,"
                + DBManager.COMPRA_TOTAL + " REAL NOT NULL,"
                + DBManager.COMPRA_ISACTIVE + " INTEGER NOT NULL,"
                + DBManager.COMPRA_IDPROVEEDOR + " INTEGER,"
                + "FOREIGN KEY (" + DBManager.COMPRA_IDPROVEEDOR + ") REFERENCES " + DBManager.TABLA_PROVEEDOR + "(" + DBManager.PROVEEDOR_ID + "));";
        db.execSQL(CREATE_COMPRA_TABLE);

        String CREATE_DETALLESCOMPRA_TABLE = "CREATE TABLE " + DBManager.TABLA_DETALLESCOMPRA + "("
                + DBManager.DETALLESCOMPRA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.DETALLESCOMPRA_CANTIDAD + " INTEGER NOT NULL,"
                + DBManager.DETALLESCOMPRA_PRECIOUNITARIO + " REAL NOT NULL,"
                + DBManager.DETALLESCOMPRA_FECHAEXPIRACION + " TEXT,"
                + DBManager.DETALLESCOMPRA_IDCOMPRA + " INTEGER NOT NULL,"
                + DBManager.DETALLESCOMPRA_IDPRODUCTO + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + DBManager.DETALLESCOMPRA_IDCOMPRA + ") REFERENCES " + DBManager.TABLA_COMPRA + "(" + DBManager.COMPRA_ID + "), "
                + "FOREIGN KEY (" + DBManager.DETALLESCOMPRA_IDPRODUCTO + ") REFERENCES " + DBManager.TABLA_PRODUCTO + "(" + DBManager.PRODUCTO_ID + "));";
        db.execSQL(CREATE_DETALLESCOMPRA_TABLE);

        String CREATE_DETALLESVENTA_TABLE = "CREATE TABLE " + DBManager.TABLA_DETALLESVENTA + "("
                + DBManager.DETALLESVENTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.DETALLESVENTA_CANTIDAD + " INTEGER NOT NULL,"
                + DBManager.DETALLESVENTA_PRECIOUNITARIO + " REAL NOT NULL,"
                + DBManager.DETALLESVENTA_IDVENTA + " INTEGER NOT NULL,"
                + DBManager.DETALLESVENTA_IDPRODUCTO + " REAL NOT NULL,"
                + "FOREIGN KEY (" + DBManager.DETALLESVENTA_IDVENTA + ") REFERENCES " + DBManager.TABLA_VENTA + "(" + DBManager.VENTA_ID + "), "
                + "FOREIGN KEY (" + DBManager.DETALLESVENTA_IDPRODUCTO + ") REFERENCES " + DBManager.TABLA_PRODUCTO + "(" + DBManager.PRODUCTO_ID + "));";
        db.execSQL(CREATE_DETALLESVENTA_TABLE);

        String CREATE_PRODUCTOPROVEEDOR_TABLE = "CREATE TABLE " + DBManager.TABLA_PRODUCTOPROVEEDOR + "("
                + DBManager.PRODUCTOPROVEEDOR_IDPRODUCTO + " INTEGER NOT NULL,"
                + DBManager.PRODUCTOPROVEEDOR_IDPROVEEDOR + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + DBManager.PRODUCTOPROVEEDOR_IDPRODUCTO + ") REFERENCES " + DBManager.TABLA_PRODUCTO + "(" + DBManager.PRODUCTO_ID + "), "
                + "FOREIGN KEY (" + DBManager.PRODUCTOPROVEEDOR_IDPROVEEDOR + ") REFERENCES " + DBManager.TABLA_PROVEEDOR + "(" + DBManager.PROVEEDOR_ID + "));";
        db.execSQL(CREATE_PRODUCTOPROVEEDOR_TABLE);

        String CREATE_MOVIMIENTOPRODUCTO_TABLE = "CREATE TABLE " + DBManager.TABLA_MOVIMIENTOPRODUCTO + "("
                + DBManager.MOVIMIENTOPRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBManager.MOVIMIENTOPRODUCTO_TIPOMOVIMIENTO + " TEXT NOT NULL,"
                + DBManager.MOVIMIENTOPRODUCTO_REFERENCIA + " INTEGER,"
                + DBManager.MOVIMIENTOPRODUCTO_CANTIDAD + " INTEGER NOT NULL,"
                + DBManager.MOVIMIENTOPRODUCTO_FECHAMOVIMIENTO + " TEXT NOT NULL,"
                + DBManager.MOVIMIENTOPRODUCTO_DESCRIPCION + " TEXT,"
                + DBManager.MOVIMIENTOPRODUCTO_IDPRODUCTO + " INTEGER NOT NULL,"
                + DBManager.MOVIMIENTOPRODUCTO_IDUSUARIO + " INTEGER,"
                + "FOREIGN KEY (" + DBManager.MOVIMIENTOPRODUCTO_IDPRODUCTO + ") REFERENCES " + DBManager.TABLA_PRODUCTO + "(" + DBManager.PRODUCTO_ID + "), "
                + "FOREIGN KEY (" + DBManager.MOVIMIENTOPRODUCTO_IDUSUARIO + ") REFERENCES " + DBManager.TABLA_USUARIO + "(" + DBManager.USUARIO_ID + "));";
        db.execSQL(CREATE_MOVIMIENTOPRODUCTO_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_DETALLESVENTA);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_VENTA);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_COMPRA);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_DETALLESCOMPRA);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_CATEGORIA);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_PROVEEDOR);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_MOVIMIENTOPRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_PRODUCTOPROVEEDOR);
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.TABLA_USUARIO);
        onCreate(db);
    }
}
