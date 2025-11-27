package com.tiendaonline.dao.interfaces;

import com.tiendaonline.model.Articulo;
import java.util.List;

public interface ArticuloDAO {

    // Obtener un artículo por su código
    Articulo getArticuloPorCodigo(String codigo) throws Exception;

    // Obtener todos los artículos
    List<Articulo> getTodosLosArticulos() throws Exception;

    // Agregar un nuevo artículo
    void anadirArticulo(Articulo articulo) throws Exception;
}
