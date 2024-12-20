package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.DetallesVenta;
import java.util.List;

public interface DetallesVentaDao {
    long insert(DetallesVenta detallesVenta);
    DetallesVenta get(int id);
    List<DetallesVenta> getAll();
    List<DetallesVenta> getAllByVentaId(int ventaId);
    int update(DetallesVenta detallesVenta);
    int delete(int id);
}