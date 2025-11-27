package com.tiendaonline.dao.jpa;
import com.tiendaonline.dao.interfaces.PedidoDAO;
import com.tiendaonline.model.Pedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class PedidoDAOJpaImpl implements PedidoDAO {
    private EntityManager em;

    public PedidoDAOJpaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Pedido getPedidoPorNumero(String numeroPedido){
        try{
            return em.createQuery("SELECT p FROM Pedido p WHERE p.numeroPedido = :numero", Pedido.class)
                    .setParameter("numero", numeroPedido)
                    .getSingleResult();
        } catch (NoResultException e){
            return null;
        } catch(Exception e){
            throw new RuntimeException("Error al obtener pedido por número: " + e.getMessage(), e);
        }
    }
    @Override
    public List<Pedido> getTodosLosPedidos() {
        try {
            return em.createQuery("SELECT p FROM Pedido p", Pedido.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los pedidos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pedido> getPedidosPendientesPorCliente(String emailCliente){
        try{
            return em.createQuery("SELECT p FROM Pedido p WHERE p.cliente.email = :email AND p.estado = false", Pedido.class)
                    .setParameter("email", emailCliente)
                    .getResultList();
        }catch (Exception e){
            throw new RuntimeException("Error al obtener pedidos pendientes por cliente: " + e.getMessage(), e);
        }
    }
    @Override
    public List<Pedido> getPedidosEnviadosPorCliente(String emailCliente){
        try {
            return em.createQuery(
                            "SELECT p FROM Pedido p WHERE p.cliente.email = :email AND p.estado = true", Pedido.class)
                    .setParameter("email", emailCliente)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pedidos enviados por cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pedido> getPedidosPendientes(){
        try {
            return em.createQuery("SELECT p FROM Pedido p WHERE p.estado = false", Pedido.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pedidos pendientes: " + e.getMessage(), e);
        }

    }

    @Override
    public List<Pedido> getPedidosEnviados() {
        try {
            return em.createQuery("SELECT p FROM Pedido p WHERE p.estado = true", Pedido.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pedidos enviados: " + e.getMessage(), e);
        }
    }

    @Override
    public void anadirPedido(Pedido pedido){
        try{
            em.getTransaction().begin();
            em.persist(pedido);
            em.getTransaction().commit();
        }catch (Exception e){
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al añadir pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarPedido(String numeroPedido) {
    try {
        em.getTransaction().begin();

        Pedido pedido = em.createQuery("SELECT p FROM Pedido p WHERE p.numeroPedido = :numero", Pedido.class)
                .setParameter("numero", numeroPedido)
                .getSingleResult();

        if (pedido.isEstado()) {
            throw new IllegalStateException("No se puede eliminar el pedido " + numeroPedido + " porque ya ha sido ENVIADO.");
        }

        long minutosPasados = java.time.Duration.between(pedido.getFechaHora(), java.time.LocalDateTime.now()).toMinutes();
        int tiempoPreparacion = pedido.getArticulo().getTiempoPreparacion();
        if (minutosPasados > tiempoPreparacion) {
            throw new IllegalStateException("No se puede eliminar: Ha superado el tiempo de preparación (" + tiempoPreparacion + " min).");
        }

        em.remove(pedido);
        em.getTransaction().commit(); // Confirmamos cambios
        return true;
    }catch (jakarta.persistence.NoResultException e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        return false;
    }catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);
    }
}
    @Override
    public void marcarPedidoEnviado(String numeroPedido) {
        try {
            em.getTransaction().begin();
            Pedido pedido = em.createQuery("SELECT p FROM Pedido p WHERE p.numeroPedido = :numero", Pedido.class)
                    .setParameter("numero", numeroPedido)
                    .getSingleResult();

            if (pedido != null) {
                pedido.setEstado(true);
                em.merge(pedido);
            }
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al marcar pedido como enviado: " + e.getMessage(), e);
        }
    }

}





