package com.tiendaonline.dao.jpa;

import com.tiendaonline.dao.interfaces.ArticuloDAO;
import com.tiendaonline.model.Articulo;
import jakarta.persistence.EntityManager; // Importamos JAKARTA
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ArticuloDAOJpaImpl implements ArticuloDAO {

    // 1. Almacenamos el EntityManager que nos pasa la Factory
    private final EntityManager em;

    // 2. Constructor que recibe el EntityManager (Inyección de Dependencias)
    public ArticuloDAOJpaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void anadirArticulo(Articulo articulo) throws Exception {
        // ¡SIN GESTIÓN DE TRANSACCIÓN NI EM.CLOSE()!
        try {
            em.getTransaction().begin();
            em.persist(articulo);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public Articulo getArticuloPorCodigo(String codigo) throws Exception {
        // ¡SIN GESTIÓN DE EM.CLOSE()!
        try{
            String query = "SELECT a FROM Articulo a WHERE a.codigo = :codigo";
            return em.createQuery(query, Articulo.class).setParameter("codigo", codigo).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Articulo> getTodosLosArticulos() throws Exception {
        // ¡SIN GESTIÓN DE EM.CLOSE()!
        String jpql = "SELECT a FROM Articulo a";
        TypedQuery<Articulo> query = em.createQuery(jpql, Articulo.class);
        return query.getResultList();
    }
}