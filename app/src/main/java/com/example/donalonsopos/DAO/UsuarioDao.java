package com.example.donalonsopos.DAO;

import com.example.donalonsopos.Model.Usuario;
import java.util.List;

public interface UsuarioDao {
    long insert(Usuario usuario);
    Usuario getById(int id);
    List<Usuario> getAll();
    int update(Usuario usuario);
    int delete(int id);
    Usuario getByUsername(String username);
}
