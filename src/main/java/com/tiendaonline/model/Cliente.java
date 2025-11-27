package com.tiendaonline.model;

import jakarta.persistence.*; // Usamos jakarta (JPA 3.0)
import java.io.Serializable;

@Entity
@Table(name = "clientes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Tabla única para todos los clientes
@DiscriminatorColumn(name = "tipo_cliente", discriminatorType = DiscriminatorType.STRING) // Columna que diferencia tipos
public abstract class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    protected int id; // ID interno necesario para Hibernate

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "domicilio")
    private String domicilio;

    @Column(name = "nif", nullable = false)
    private String NIF;

    // Constructor vacío (Obligatorio para JPA)
    public Cliente() {
    }

    // Constructor con parámetros
    public Cliente(String email, String nombre, String domicilio, String NIF) {
        this.email = email;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.NIF = NIF;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getNIF() { return NIF; }
    public void setNIF(String NIF) { this.NIF = NIF; }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", NIF='" + NIF + '\'' +
                '}';
    }
}