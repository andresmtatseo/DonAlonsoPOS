package com.example.donalonsopos.DAO;

import com.example.donalonsopos.Model.Cliente;
import java.util.List;

public interface ClienteDao {
    long insert(Cliente cliente);
    Cliente get(int id);
    List<Cliente> getAll();
    int update(Cliente cliente);
    int delete(int id);
}
