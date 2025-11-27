package com.tiendaonline.dao.interfaces;

import com.tiendaonline.model.Cliente;
import java.util.List;

public interface ClienteDAO {

    Cliente getClientePorEmail(String email);

    List<Cliente> getTodosLosClientes();

    List<Cliente> getClientesEstandar();

    List<Cliente> getClientesPremium();

    void anadirCliente(Cliente cliente);
}