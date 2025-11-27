package com.tiendaonline.test.onlinestore.tests;

import com.tiendaonline.model.Articulo;
import com.tiendaonline.model.TiendaOnline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class GestionArticulosTest {
    private TiendaOnline tienda;

    @BeforeEach
    void setUp()  throws Exception{
        tienda = new TiendaOnline();
        try{
        tienda.cargarDatosDePrueba(); // Carga los artículos iniciales
        }catch(Exception e){
            System.out.println("Aviso: Datos de prueba ya cargados. Continuando...");
        }
    }


    @Test
    void testAnadirArticuloValido()  throws Exception {
        String codigoTest = "A999";

        try {
            tienda.anadirArticulo(codigoTest, "Tablet Samsung", 300.0, 10.0, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Aviso: Codigo usado en un test anterior");
        }
        Articulo articulo = tienda.buscarArticulo(codigoTest);
        assertNotNull(articulo);
        assertEquals("Tablet Samsung", articulo.getDescripcion());
    }


    @Test
    void testAnadirArticuloDuplicadoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirArticulo("A001", "Laptop Pro 16", 1499.99, 15.00, 120);
        });
    }


    @Test
    void testBuscarArticuloExistente()  throws Exception {
        Articulo articulo = tienda.buscarArticulo("A001");
        assertNotNull(articulo);
        assertEquals("Laptop Pro 16", articulo.getDescripcion());
    }


    @Test
    void testBuscarArticuloInexistente()  throws Exception {
        Articulo articulo = tienda.buscarArticulo("AXXX");
        assertNull(articulo);
    }


    @Test
    void testMostrarArticulos()  throws Exception {
        List<Articulo> articulos = tienda.mostrarArticulos();
        assertFalse(articulos.isEmpty());
        assertTrue(articulos.size() >= 5); // según cargarDatosDePrueba()
    }
}