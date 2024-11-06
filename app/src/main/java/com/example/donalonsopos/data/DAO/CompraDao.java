package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.Compra;
import java.util.List;

public interface CompraDao {
    long insert(Compra compra);
    Compra get(int id);
    List<Compra> getAll();
    int update(Compra compra);
    int delete(int id);
}