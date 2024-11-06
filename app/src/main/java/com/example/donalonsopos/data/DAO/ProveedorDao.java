package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.Proveedor;
import java.util.List;

public interface ProveedorDao {
    long insert(Proveedor proveedor);
    Proveedor get(int id);
    List<Proveedor> getAll();
    int update(Proveedor proveedor);
    int delete(int id);
}
