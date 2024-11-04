package com.example.donalonsopos.DAO;

import com.example.donalonsopos.Model.Venta;
import java.util.List;

public interface VentaDao {
    long insert(Venta venta);
    Venta get(int id);
    List<Venta> getAll();
    int update(Venta venta);
    int delete(int id);
}
