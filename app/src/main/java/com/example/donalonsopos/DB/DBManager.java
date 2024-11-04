package com.example.donalonsopos.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    // Usuario table
    public static final String TABLA_USUARIO = "usuario";
    public static final String USUARIO_ID = "idUsuario";
    public static final String USUARIO_USERNAME = "username";
    public static final String USUARIO_PASSWORD = "password";
    public static final String USUARIO_ROL = "rol";
    public static final String USUARIO_CEDULA = "cedula";
    public static final String USUARIO_NOMBRE = "nombre";
    public static final String USUARIO_APELLIDO = "apellido";
    public static final String USUARIO_ISACTIVE = "isActive";

    // Cliente table
    public static final String TABLA_CLIENTE = "cliente";
    public static final String CLIENTE_ID = "idCliente";
    public static final String CLIENTE_CEDULA = "cedula";
    public static final String CLIENTE_NOMBRE = "nombre";
    public static final String CLIENTE_APELLIDO = "apellido";
    public static final String CLIENTE_DIRECCION = "direccion";
    public static final String CLIENTE_TELEFONO = "telefono";
    public static final String CLIENTE_ISACTIVE = "isActive";

    // Producto table
    public static final String TABLA_PRODUCTO = "producto";
    public static final String PRODUCTO_ID = "idProducto";
    public static final String PRODUCTO_IDCATEGORIA = "idCategoria";
    public static final String PRODUCTO_NOMBRE = "nombre";
    public static final String PRODUCTO_PRECIO = "precio";
    public static final String PRODUCTO_IMAGENURL = "imagenURL";
    public static final String PRODUCTO_DESCRIPCION = "descripcion";
    public static final String PRODUCTO_CANTIDADACTUAL = "cantidadActual";
    public static final String PRODUCTO_CANTIDADMINIMA = "cantidadMinima";
    public static final String PRODUCTO_ISACTIVE = "isActive";

    // Categoria table
    public static final String TABLA_CATEGORIA = "categoria";
    public static final String CATEGORIA_ID = "idCategoria";
    public static final String CATEGORIA_NOMBRE = "nombre";

    // Venta table
    public static final String TABLA_VENTA = "venta";
    public static final String VENTA_ID = "idVenta";
    public static final String VENTA_IDUSUARIO = "idUsuario";
    public static final String VENTA_IDCLIENTE = "idCliente";
    public static final String VENTA_FECHA = "fechaVenta";
    public static final String VENTA_METODOPAGO = "metodoPago";
    public static final String VENTA_NUMEROTRANSACCION = "numeroTransaccion";
    public static final String VENTA_FECHAPAGO = "fechaPago";
    public static final String VENTA_TOTAL = "total";
    public static final String VENTA_ISACTIVE = "isActive";

    // DetallesVenta table
    public static final String TABLA_DETALLESVENTA = "detallesVenta";
    public static final String DETALLESVENTA_ID = "idDetallesVenta";
    public static final String DETALLESVENTA_IDVENTA = "idVenta";
    public static final String DETALLESVENTA_IDPRODUCTO = "idProducto";
    public static final String DETALLESVENTA_CANTIDAD = "cantidad";
    public static final String DETALLESVENTA_PRECIOUNITARIO = "precioUnitario";

    // Proveedor table
    public static final String TABLA_PROVEEDOR = "proveedor";
    public static final String PROVEEDOR_ID = "idProveedor";
    public static final String PROVEEDOR_CEDULA = "cedula";
    public static final String PROVEEDOR_NOMBRE = "nombre";
    public static final String PROVEEDOR_DIRECCION = "direccion";
    public static final String PROVEEDOR_TELEFONO = "telefono";
    public static final String PROVEEDOR_EMAIL = "email";
    public static final String PROVEEDOR_ISACTIVE = "isActive";

    // Compra table
    public static final String TABLA_COMPRA = "compra";
    public static final String COMPRA_ID = "idCompra";
    public static final String COMPRA_IDPROVEEDOR = "idProveedor";
    public static final String COMPRA_FECHA = "fechaCompra";
    public static final String COMPRA_METODOPAGO = "metodoPago";
    public static final String COMPRA_NUMEROFACTURA = "numeroFactura";
    public static final String COMPRA_TOTAL = "total";
    public static final String COMPRA_ISACTIVE = "isActive";

    // DetallesCompra table
    public static final String TABLA_DETALLESCOMPRA = "detallesCompra";
    public static final String DETALLESCOMPRA_ID = "idDetallesCompra";
    public static final String DETALLESCOMPRA_IDCOMPRA = "idCompra";
    public static final String DETALLESCOMPRA_IDPRODUCTO = "idProducto";
    public static final String DETALLESCOMPRA_CANTIDAD = "cantidad";
    public static final String DETALLESCOMPRA_PRECIOUNITARIO = "precioUnitario";
    public static final String DETALLESCOMPRA_FECHAEXPIRACION = "fechaExpiracion";

    // MovimientoProducto table
    public static final String TABLA_MOVIMIENTOPRODUCTO = "movimientoProducto";
    public static final String MOVIMIENTOPRODUCTO_ID = "idMovimiento";
    public static final String MOVIMIENTOPRODUCTO_IDPRODUCTO = "idProducto";
    public static final String MOVIMIENTOPRODUCTO_IDUSUARIO = "idUsuario";
    public static final String MOVIMIENTOPRODUCTO_TIPOMOVIMIENTO = "tipoMovimiento";
    public static final String MOVIMIENTOPRODUCTO_REFERENCIA = "referencia";
    public static final String MOVIMIENTOPRODUCTO_CANTIDAD = "cantidad";
    public static final String MOVIMIENTOPRODUCTO_FECHAMOVIMIENTO = "fechaMovimiento";
    public static final String MOVIMIENTOPRODUCTO_DESCRIPCION = "descripcion";

    // ProductoProveedor table
    public static final String TABLA_PRODUCTOPROVEEDOR = "productoProveedor";
    public static final String PRODUCTOPROVEEDOR_IDPRODUCTO = "idProducto";
    public static final String PRODUCTOPROVEEDOR_IDPROVEEDOR = "idProveedor";

    private DBConexion _conexion;
    private SQLiteDatabase _basededatos;

    public DBManager(Context context) {
        _conexion= new DBConexion(context);
    }

    public DBManager open() throws SQLException{
        _basededatos = _conexion.getWritableDatabase();
        return this;
    }

    public void close(){
        _conexion.close();
    }


}
