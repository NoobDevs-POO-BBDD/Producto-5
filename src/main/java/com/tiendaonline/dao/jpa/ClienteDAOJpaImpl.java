package com.tiendaonline.dao.jpa;

import com.tiendaonline.dao.interfaces.ClienteDAO;
import com.tiendaonline.model.Cliente;
import com.tiendaonline.model.ClientePremium;
import com.tiendaonline.model.ClienteStandar;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClienteDAOJpaImpl implements ClienteDAO {

    // Variable para guardar el gestor de entidades
    private EntityManager em;

    // CONSTRUCTOR: Recibe el EntityManager de la factoría y lo guarda
    public ClienteDAOJpaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Cliente getClientePorEmail(String email) {
        try {
            // JPQL: Buscamos en la clase Cliente por el campo email
            String jpql = "SELECT c FROM Cliente c WHERE c.email = :email";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("email", email);

            return query.getSingleResult();
        } catch (Exception e) {
            // Si no se encuentra, devolvemos null
            return null;
        }
    }

    @Override
    public List<Cliente> getTodosLosClientes() {
        // JPQL: Traer todos los objetos Cliente
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }

    @Override
    public List<Cliente> getClientesEstandar() {
        // Hibernate filtra automáticamente los de tipo ESTANDAR
        return em.createQuery("SELECT cs FROM ClienteStandar cs", Cliente.class).getResultList();
    }

    @Override
    public List<Cliente> getClientesPremium() {
        // Hibernate filtra automáticamente los de tipo PREMIUM
        return em.createQuery("SELECT cp FROM ClientePremium cp", Cliente.class).getResultList();
    }

    @Override
    public void anadirCliente(Cliente cliente) {
        try {
            em.getTransaction().begin();

            // persist() es el método mágico de JPA para guardar
            em.persist(cliente);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Lanzamos una excepción genérica RuntimeException
            throw new RuntimeException("Error al guardar cliente JPA: " + e.getMessage(), e);
        }
    }
}