package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.ProductoProveedor;
import java.util.List;

public interface ProductoProveedorDao {
    long insert(ProductoProveedor productoProveedor);
    ProductoProveedor get(int productoId, int proveedorId);
    List<ProductoProveedor> getAllByProductoId(int productoId);
    List<ProductoProveedor> getAllByProveedorId(int proveedorId);
    int update(ProductoProveedor productoProveedor);
    int delete(int productoId, int proveedorId);
}