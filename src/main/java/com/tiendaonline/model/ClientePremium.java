package com.tiendaonline.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

    public static final double DESCUENTO_ENVIO_PREMIUM = 20.0;
    public static final int CUOTA_ANUAL_PREMIUM = 30;

    @Column(name = "descuento_envio")
    private double descuentoEnvio;

    @Column(name = "cuota_anual")
    private int cuotaAnual;

    public ClientePremium() {
        super();
    }

    // 💡 Nota: Usas 'NIF' en mayúsculas aquí, lo cual está bien
    // porque el constructor del padre tiene el parámetro 'nif' en minúsculas.
    public ClientePremium(String email, String nombre, String domicilio, String NIF, double descuentoEnvio, int cuotaAnual) {
        super(email, nombre, domicilio, NIF);
        this.descuentoEnvio = descuentoEnvio;
        this.cuotaAnual = cuotaAnual;
    }

    public double getDescuentoEnvio() { return descuentoEnvio; }
    public void setDescuentoEnvio(double descuentoEnvio) { this.descuentoEnvio = descuentoEnvio; }

    public int getCuotaAnual() { return cuotaAnual; }
    public void setCuotaAnual(int cuotaAnual) { this.cuotaAnual = cuotaAnual; }

    // El método getNIF() ha sido ELIMINADO.
    // Ahora, los tests llamarán a Cliente.getNIF() (el método puente)

    @Override
    public String toString() {
        return super.toString() +
                " ClientePremium{" +
                "descuentoEnvio=" + descuentoEnvio +
                ", cuotaAnual=" + cuotaAnual +
                '}';
    }
}