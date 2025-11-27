package com.tiendaonline.test.onlinestore.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.tiendaonline.model.Articulo;

public class ArticuloTest {

    @Test
    public void testConstructorYGetters() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        assertEquals("A001", articulo.getCodigo());
        assertEquals("Libro", articulo.getDescripcion());

        // En JUnit 5, assertEquals para doubles admite un "delta" (margen de error)
        assertEquals(25.0, articulo.getPrecioVenta(), 0.001);
        assertEquals(5.0, articulo.getGastosEnvio(), 0.001);

        assertEquals(2, articulo.getTiempoPreparacion());
    }

    @Test
    public void testSetters() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        articulo.setCodigo("B002");
        articulo.setDescripcion("Cuaderno");
        articulo.setPrecioVenta(30.0);
        articulo.setGastosEnvio(7.5);
        articulo.setTiempoPreparacion(3);

        assertEquals("B002", articulo.getCodigo());
        assertEquals("Cuaderno", articulo.getDescripcion());

        assertEquals(30.0, articulo.getPrecioVenta(), 0.001);
        assertEquals(7.5, articulo.getGastosEnvio(), 0.001);

        assertEquals(3, articulo.getTiempoPreparacion());
    }

    @Test
    public void testToString() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        String esperado = "Articulo{codigo='A001', descripcion='Libro', precioVenta=25.0, gastosEnvio=5.0, tiempoPreparacion=2}";
        assertEquals(esperado, articulo.toString());
    }
}