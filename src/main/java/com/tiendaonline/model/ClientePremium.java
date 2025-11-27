package com.tiendaonline.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

    // Las constantes se quedan igual (JPA las ignora al ser static final)
    public static final double DESCUENTO_ENVIO_PREMIUM = 20.0;
    public static final int CUOTA_ANUAL_PREMIUM = 30;

    @Column(name = "descuento_envio")
    private double descuentoEnvio;

    @Column(name = "cuota_anual")
    private int cuotaAnual;

    // Constructor vacío (Obligatorio para JPA)
    public ClientePremium() {
        super();
    }

    // Constructor con parámetros original
    public ClientePremium(String email, String nombre, String domicilio, String NIF, double descuentoEnvio, int cuotaAnual) {
        super(email, nombre, domicilio, NIF);
        this.descuentoEnvio = descuentoEnvio;
        this.cuotaAnual = cuotaAnual;
    }

    public double getDescuentoEnvio() { return descuentoEnvio; }
    public void setDescuentoEnvio(double descuentoEnvio) { this.descuentoEnvio = descuentoEnvio; }

    public int getCuotaAnual() { return cuotaAnual; }
    public void setCuotaAnual(int cuotaAnual) { this.cuotaAnual = cuotaAnual; }

    @Override
    public String toString() {
        return super.toString() +
                " ClientePremium{" +
                "descuentoEnvio=" + descuentoEnvio +
                ", cuotaAnual=" + cuotaAnual +
                '}';
    }
}