package com.example.donalonsopos.DAO;

import com.example.donalonsopos.Model.Producto;
import java.util.List;

public interface ProductoDao {
    long insert(Producto producto);
    Producto get(int id);
    List<Producto> getAll();
    int update(Producto producto);
    int delete(int id);
}
