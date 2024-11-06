package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.Usuario;
import java.util.List;

public interface UsuarioDao {
    long insert(Usuario usuario);
    Usuario getById(int id);
    List<Usuario> getAll();
    int update(Usuario usuario);
    int delete(int id);
    Usuario getByUsername(String username);
}
