package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.Producto;
import java.util.List;

public interface ProductoDao {
    long insert(Producto producto);
    Producto get(int id);
    List<Producto> getAll();
    int update(Producto producto);
    int delete(int id);
}
