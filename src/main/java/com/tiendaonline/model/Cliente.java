package com.tiendaonline.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "clientes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cliente", discriminatorType = DiscriminatorType.STRING)
public abstract class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    protected int id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "domicilio")
    private String domicilio;

    @Column(name = "nif", nullable = false)
    private String nif;

    // Constructores y Getters/Setters existentes...

    public Cliente() {}

    public Cliente(String email, String nombre, String domicilio, String nif) {
        this.email = email;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
    }

    // Getters y Setters Estándar (nif en minúsculas)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    // Métodos Puente para JUnit Test (NIF en mayúsculas)
    public String getNIF() { return this.getNif(); }
    public void setNIF(String NIF) { this.setNif(NIF); }

    // --- MÉTODOS DE NEGOCIO ABSTRACTOS (Polimorfismo) ---
    public abstract int cuotaAnual();
    public abstract double descuentoEnvio();

    public String getTipoCliente() {
        if (this instanceof ClientePremium) {
            return "Premium";
        }
        if (this instanceof ClienteStandar) {
            return "Estándar";
        }
        return "Desconocido";
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id + ", email='" + email + '\'' + ", nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' + ", NIF='" + nif + '\'' +
                '}';
    }
}