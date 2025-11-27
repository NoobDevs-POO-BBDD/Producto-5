package com.tiendaonline.model;

import jakarta.persistence.*; // Usamos jakarta (JPA 3.0)

@Entity
@DiscriminatorValue("ESTANDAR") // Valor para la columna 'tipo_cliente'
public class ClienteStandar extends Cliente {

    // Lógica de negocio (se mantiene igual)
    public static final double DESCUENTO_ENVIO_STANDAR = 0.0;

    @Column(name = "descuento_envio")
    private double descuentoEnvio = 0.0;

    // Constructor vacío (Obligatorio para JPA)
    public ClienteStandar() {
        super();
    }

    // Constructor con parámetros original
    public ClienteStandar(String email, String nombre, String domicilio, String NIF, double descuentoEnvio) {
        super(email, nombre, domicilio, NIF);
        this.descuentoEnvio = descuentoEnvio;
    }

    public double getDescuentoEnvio() {
        return descuentoEnvio;
    }

    public void setDescuentoEnvio(double descuentoEnvio) {
        this.descuentoEnvio = descuentoEnvio;
    }

    @Override
    public String toString() {
        return super.toString() + " (Tipo: Estandar)";
    }
}