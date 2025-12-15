package com.tiendaonline.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESTANDAR")
public class ClienteStandar extends Cliente {

    public static final double DESCUENTO_ENVIO_STANDAR = 0.0;
    public static final int CUOTA_ANUAL_STANDAR = 0;

    public ClienteStandar() {
        super();
    }

    // Constructor con parámetros
    public ClienteStandar(String email, String nombre, String domicilio, String NIF) {
        super(email, nombre, domicilio, NIF);
    }

    // Implementación de métodos abstractos (devuelven constantes)
    @Override
    public int cuotaAnual() {
        return CUOTA_ANUAL_STANDAR;
    }

    @Override
    public double descuentoEnvio() {
        return DESCUENTO_ENVIO_STANDAR;
    }

    @Override
    public String toString() {
        return super.toString() + " (Tipo: Estándar)";
    }
}