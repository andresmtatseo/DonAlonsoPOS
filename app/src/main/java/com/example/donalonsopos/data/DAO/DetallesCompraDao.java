package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.DetallesCompra;
import java.util.List;

public interface DetallesCompraDao {
    long insert(DetallesCompra detallesCompra);
    DetallesCompra get(int id);
    List<DetallesCompra> getAll();
    List<DetallesCompra> getAllByCompraId(int compraId);
    int update(DetallesCompra detallesCompra);
    int delete(int id);
}