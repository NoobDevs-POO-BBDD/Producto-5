package com.tiendaonline.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

    // Constantes (Para inicialización o referencia si el formulario no los pide)
    public static final double DESCUENTO_ENVIO_PREMIUM = 0.20; // 20% en formato 0.xx
    public static final int CUOTA_ANUAL_PREMIUM = 30;

    // **NOTA JPA:** Estos campos se persisten en la tabla 'clientes' y serán NULL para Estándar.
    @Column(name = "descuento_envio")
    private double descuentoEnvio;

    @Column(name = "cuota_anual")
    private int cuotaAnual;

    public ClientePremium() {
        super();
        this.descuentoEnvio = DESCUENTO_ENVIO_PREMIUM;
        this.cuotaAnual = CUOTA_ANUAL_PREMIUM;
    }

    // Constructor completo
    public ClientePremium(String email, String nombre, String domicilio, String NIF, double descuentoEnvio, int cuotaAnual) {
        super(email, nombre, domicilio, NIF);
        this.descuentoEnvio = descuentoEnvio;
        this.cuotaAnual = cuotaAnual;
    }

    // Implementación de métodos abstractos
    @Override
    public int cuotaAnual() {
        return this.cuotaAnual;
    }

    @Override
    public double descuentoEnvio() {
        return this.descuentoEnvio;
    }

    // Getters y Setters (Necesarios para JPA)
    public double getDescuentoEnvio() { return descuentoEnvio; }
    public void setDescuentoEnvio(double descuentoEnvio) { this.descuentoEnvio = descuentoEnvio; }
    public int getCuotaAnual() { return cuotaAnual; }
    public void setCuotaAnual(int cuotaAnual) { this.cuotaAnual = cuotaAnual; }

    @Override
    public String toString() {
        return super.toString() +
                " (Tipo: Premium | Cuota: " + cuotaAnual() + " | Descuento: " + (descuentoEnvio() * 100) + "%)";
    }
}