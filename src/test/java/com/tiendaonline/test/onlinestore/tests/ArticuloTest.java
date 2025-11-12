package test.java.com.tiendaonline.test.onlinestore.tests;

import org.junit.Test;
//import org.junit.jupiter.api.Test;
import com.tiendaonline.model.Articulo;

import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.*;

public class ArticuloTest {

    @Test
    public void testConstructorYGetters() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        assertEquals("A001", articulo.getCodigo());
        assertEquals("Libro", articulo.getDescripcion());
        assertEquals(25.0, articulo.getPrecioVenta());
        assertEquals(5.0, articulo.getGastosEnvio());
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
        assertEquals(30.0, articulo.getPrecioVenta());
        assertEquals(7.5, articulo.getGastosEnvio());
        assertEquals(3, articulo.getTiempoPreparacion());
    }


    @Test
    public void testToString() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        String esperado = "Articulo{codigo='A001', descripcion='Libro', precioVenta=25.0, gastosEnvio=5.0, tiempoPreparacion=2}";
        assertEquals(esperado, articulo.toString());
    }
}