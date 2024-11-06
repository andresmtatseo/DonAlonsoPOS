package com.example.donalonsopos.data.DAO;

import com.example.donalonsopos.data.DTO.Cliente;
import java.util.List;

public interface ClienteDao {
    long insert(Cliente cliente);
    Cliente get(int id);
    List<Cliente> getAll();
    int update(Cliente cliente);
    int delete(int id);
}
