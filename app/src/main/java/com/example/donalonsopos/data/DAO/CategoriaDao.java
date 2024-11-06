package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.Categoria;
import java.util.List;

public interface CategoriaDao {
    long insert(Categoria categoria);
    Categoria get(int id);
    List<Categoria> getAll();
    int update(Categoria categoria);
    int delete(int id);
}