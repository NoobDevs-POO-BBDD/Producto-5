package com.tiendaonline.model;

import jakarta.persistence.*;
import java.io.Serializable;
import com.tiendaonline.model.ClientePremium; // Asegúrate de importar ClientePremium si está en este mismo paquete.
// Si ClienteStandar existe, también deberías importarlo aquí:
// import com.tiendaonline.model.ClienteStandar;

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

    // Campo real en la base de datos y en Java (en minúsculas)
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
        this.nif = nif;
    }

    // --- Getters y Setters Estándar (nif en minúsculas) ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    // GetNif/SetNif (Métodos que operan sobre el campo 'nif')
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }


    // --- Métodos "Puente" para JUnit Test (NIF en mayúsculas) ---

    /** * Getter puente: Usado por el JUnit Test que espera 'getNIF()'.
     * Llama al getter con convención Java 'getNif()'.
     */
    public String getNIF() {
        return this.getNif();
    }

    /**
     * Setter puente: Usado por el JUnit Test que espera 'setNIF()'.
     * Llama al setter con convención Java 'setNif()'.
     */
    public void setNIF(String NIF) {
        this.setNif(NIF);
    }

    // --- Métodos de Negocio y Utilidad ---

    /**
     * Determina el tipo de cliente para la columna "Tipo" en la TableView.
     */
    public String getTipoCliente() {
        if (this instanceof ClientePremium) {
            return "Premium";
        }
        // Asumiendo que existe ClienteStandar (o es el valor por defecto)
        // if (this instanceof ClienteStandar) {
        //     return "Estándar";
        // }
        return "Estándar"; // Valor por defecto
    }

    @Override
    public String toString() {
        // Formato final corregido para coincidir exactamente con el JUnit Test.
        return "Cliente{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                // Se usa la etiqueta 'NIF' mayúscula para coincidir con el test.
                ", NIF='" + nif + '\'' +
                '}';
    }
}