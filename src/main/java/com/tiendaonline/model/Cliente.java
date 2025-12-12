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

    // CAMBIO 1: Nombre del campo en Java en minúsculas para coincidir con la convención del getter
    @Column(name = "nif", nullable = false)
    private String nif;

    // Constructor vacío (Obligatorio para JPA)
    public Cliente() {
    }

    // Constructor con parámetros
    public Cliente(String email, String nombre, String domicilio, String nif) {
        this.email = email;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif; // Usamos el campo nif en minúscula
    }

    // --- Getters y Setters ---

    // Getters para JavaFX TableView (nif en minúsculas)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    // CAMBIO 2: Getter en minúsculas para compatibilidad con PropertyValueFactory("nif")
    // Se asume que en el controlador has usado PropertyValueFactory("nif")
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }


    // CAMBIO 3: Método VITAL para la columna 'Tipo' de la TableView
    // Se llama 'getTipoCliente' porque en tu FXML se usa PropertyValueFactory("tipoCliente")
    public String getTipoCliente() {
        // Usa 'instanceof' para determinar la clase real del objeto cargado por JPA
        if (this instanceof ClientePremium) {
            return "Premium";
        }
        if (this instanceof ClienteStandar) {
            return "Estándar";
        }
        return "Desconocido"; // Fallback
    }

    // --- Métodos de Negocio y Utilidad ---

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", nif='" + nif + '\'' +
                '}';
    }
}