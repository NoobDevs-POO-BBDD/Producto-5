package com.tiendaonline.test.onlinestore.tests;

import com.tiendaonline.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

//Solo comprueba la inicialización y que las listas se cargan correctamente.
class TiendaOnlineTest {

    TiendaOnline tienda;

    @BeforeEach
    void setUp() throws Exception {
        tienda = new TiendaOnline();
        try {
            tienda.cargarDatosDePrueba();
        } catch (IllegalArgumentException | java.sql.SQLException e) {
            System.out.println("Aviso: Datos de prueba ya cargados. Continuando...");
        }
    }

    @Test
    void testArraysVacios() throws Exception {
        // comprobaciones básicas
        assertNotNull(tienda.mostrarArticulos());
        assertNotNull(tienda.mostrarClientes());
        assertEquals(5, tienda.mostrarArticulos().size());
        assertEquals(5, tienda.mostrarClientes().size());
    }
    @Test
    void testToString() throws Exception {
        String resultado = tienda.toString();

        // Comprobamos que el resultado contiene los números correctos de artículos, clientes y pedidos
        assertTrue(resultado.contains("articulos=" + tienda.getTotalArticulos()));
        assertTrue(resultado.contains("clientes=" + tienda.getTotalClientes()));
        assertTrue(resultado.contains("Estandar: " + tienda.getTotalClientesEstandar()));
        assertTrue(resultado.contains("Premium: " + tienda.getTotalClientesPremium()));
        assertTrue(resultado.contains("pedidos=" + tienda.getTotalPedidos()));
        assertTrue(resultado.contains("Pendientes: " + tienda.getTotalPedidosPendientes()));
        assertTrue(resultado.contains("Enviados: " + tienda.getTotalPedidosEnviados()));

        System.out.println(resultado);
    }
}