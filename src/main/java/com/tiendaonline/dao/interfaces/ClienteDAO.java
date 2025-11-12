package main.java.com.tiendaonline.dao.interfaces;

import com.tiendaonline.model.Cliente;
import java.util.List;

public interface ClienteDAO {

    // Obtener un cliente por su email
    Cliente getClientePorEmail(String email) throws Exception;

    // Obtener todos los clientes
    List<Cliente> getTodosLosClientes() throws Exception;

    // Obtener solo clientes estándar
    List<Cliente> getClientesEstandar() throws Exception;

    // Obtener solo clientes premium
    List<Cliente> getClientesPremium() throws Exception;

    // Agregar un nuevo cliente
    void anadirCliente(Cliente cliente) throws Exception;
}
