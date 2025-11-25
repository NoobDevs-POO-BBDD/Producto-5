package com.tiendaonline.dao.jpa;

import com.tiendaonline.dao.interfaces.ClienteDAO;
import jakarta.persistence.EntityManager;

public class ClienteDAOJpaImpl implements ClienteDAO {
    public ClienteDAOJpaImpl(EntityManager em) {
    }
}
