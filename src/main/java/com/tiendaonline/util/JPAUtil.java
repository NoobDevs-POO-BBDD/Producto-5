package com.tiendaonline.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * esta clase reemplaza a ConexionBD.
 * Gestionará el EntityManagerFactory de JPA, que se creará solo una vez (patrón Singleton)
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "TiendaOnlinePU";  // El nombre de unidad de persistencia del persistence.xml

    private static EntityManagerFactory factory; //guarda la fábrica

    /**
     * metodo para construir la fábrica, lee persistence.xml
     */
    private static void buildFactory(){
        try{
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // comando de JPA para leer el persistence.xml
        }catch (Throwable ex){
            System.err.println("La creación del EntityManagerFactory falló: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * metodo para obtener un EntityManager
     * @return un nuevo EntityManager
     */
    public static synchronized EntityManager getEntityManager(){
        if (factory == null){ // si la fabrica no está creada la creamos
            buildFactory();
        }
        return factory.createEntityManager();
    }

    /**
     * metodo para cerrar la fábrica al cerrar la aplicación
     */
    public static void shutdown(){
        if (factory != null){
            factory.close();
        }
    }
}
