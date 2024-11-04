package com.example.donalonsopos.DAO;

import java.util.List;

public interface MovimientoProductoDao {
    long insert(MovimientoProducto movimientoProducto);
    MovimientoProducto get(int id);
    List<MovimientoProducto> getAll();
    List<MovimientoProducto> getAllByProductoId(int productoId);
    int update(MovimientoProducto movimientoProducto);
    int delete(int id);
}
