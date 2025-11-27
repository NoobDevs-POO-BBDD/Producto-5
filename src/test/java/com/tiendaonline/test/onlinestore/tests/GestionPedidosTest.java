package com.tiendaonline.test.onlinestore.tests;

import com.tiendaonline.model.Pedido;
import com.tiendaonline.model.TiendaOnline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionPedidosTest {

    TiendaOnline tienda;

    @BeforeEach
    void setUp() throws Exception {
        tienda = new TiendaOnline();
        try {
            tienda.cargarDatosDePrueba();
        } catch (Exception e) {
            System.out.println("Aviso: Datos de prueba ya cargados o error controlado.");
        }
    }

    @Test
    void testAnadirPedidoValido() throws Exception {
        // Nota: P006 no existe en la carga inicial, así que se crea nuevo
        tienda.anadirPedido("P006", "anna@mail.com", "A001", 2);
        Pedido pedido = tienda.buscarPedido("P006");

        assertNotNull(pedido);
        assertEquals(2, pedido.getCantidad());
        assertEquals("anna@mail.com", pedido.getCliente().getEmail());
        assertFalse(pedido.isEstado()); // Pendiente por defecto
    }

    @Test
    void testAnadirPedidoArticuloNoExiste() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirPedido("P007", "anna@mail.com", "A999", 1);
        });
        // Ajustamos el mensaje esperado para que coincida con tu TiendaOnline.java
        assertTrue(exception.getMessage().contains("No existe el artículo"));
    }

    @Test
    void testAnadirPedidoClienteNoExiste() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirPedido("P008", "no.existe@mail.com", "A001", 1);
        });
        assertTrue(exception.getMessage().contains("No existe el cliente"));
    }

    @Test
    void testAnadirPedidoCantidadInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirPedido("P009", "anna@mail.com", "A001", 0);
        });
        assertTrue(exception.getMessage().contains("mayor a 0"));
    }

    @Test
    void testEliminarPedidoPendienteCancelable() throws Exception {
        // P001 se acaba de crear en setUp(), así que debería ser cancelable
        boolean eliminado = tienda.eliminarPedido("P001");

        assertTrue(eliminado, "El pedido P001 debería poder eliminarse (es reciente y pendiente)");
        assertNull(tienda.buscarPedido("P001"), "El pedido P001 ya no debería existir");
    }

    @Test
    void testEliminarPedidoEnviado() {
        // El pedido P004 es enviado. Intentar borrarlo debe lanzar excepción.
        Exception exception = assertThrows(Exception.class, () -> {
            tienda.eliminarPedido("P004");
        });

        // Opcional: Verificar el mensaje de error
        assertTrue(exception.getMessage().contains("No se puede eliminar"));
    }

    @Test
    void testMarcarPedidoComoEnviado() throws Exception {
        // P002 nace como pendiente
        Pedido pAntes = tienda.buscarPedido("P002");
        assertFalse(pAntes.isEstado());

        tienda.marcarPedidoComoEnviado("P002");

        Pedido pDespues = tienda.buscarPedido("P002");
        assertTrue(pDespues.isEstado(), "El pedido P002 debería constar como enviado");
    }

    @Test
    void testMostrarPedidosPendientesYEnviados() throws Exception {
        List<Pedido> pendientes = tienda.mostrarPedidosPendientes();
        List<Pedido> enviados = tienda.mostrarPedidosEnviados();

        // Verificamos que las listas no estén vacías (basado en datos de prueba)
        assertFalse(pendientes.isEmpty());
        assertFalse(enviados.isEmpty());

        assertTrue(pendientes.stream().allMatch(p -> !p.isEstado()));
        assertTrue(enviados.stream().allMatch(Pedido::isEstado));
    }

    @Test
    void testMostrarPedidosFiltradosPorCliente() throws Exception {
        // CORRECCIÓN IMPORTANTE: "anna" con dos 'n', no "ana"
        List<Pedido> pendientesAnna = tienda.mostrarPedidosPendientes("anna@mail.com");
        List<Pedido> enviadosKevin = tienda.mostrarPedidosEnviados("kevin@mail.com");

        // Aseguramos que anna tiene pedidos (P004 enviado, P006 nuevo...)
        // Nota: P004 es enviado, así que pendientesAnna no lo tendrá.
        // Anna en datos de prueba NO tiene pendientes iniciales (P004 es enviado).
        // Para que este test pase, necesitamos verificar que la lógica de filtro funciona,
        // aunque la lista vuelva vacía, no debe dar error.

        // Vamos a crear un pendiente para Anna para asegurar el test
        tienda.anadirPedido("P-TEST-ANNA", "anna@mail.com", "A002", 1);
        pendientesAnna = tienda.mostrarPedidosPendientes("anna@mail.com");

        assertFalse(pendientesAnna.isEmpty(), "Anna debería tener pedidos pendientes ahora");
        assertTrue(pendientesAnna.stream().allMatch(p -> p.getCliente().getEmail().equals("anna@mail.com") && !p.isEstado()));

        // Kevin tiene el P005 enviado en los datos de prueba
        assertFalse(enviadosKevin.isEmpty(), "Kevin debería tener pedidos enviados");
        assertTrue(enviadosKevin.stream().allMatch(p -> p.getCliente().getEmail().equals("kevin@mail.com") && p.isEstado()));
    }
}