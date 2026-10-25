package com.utp.salonbarberiavargas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity
@Table(name = "items_venta")
public class ItemVenta {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer id;
    @NotBlank (message = "El tipo de item es obligatorio (SERVICIO O PRODUCTO)")
    private String tipoItem;
    @NotBlank (message = "La descripción no puede estar en blanco")
    @Size (min = 3, max = 150, message = "La descripción debe tener entre 3 y 150 caracteres")
    private String descripcion;
    @NotNull (message = "La cantidad no puede ser nula")
    @Positive (message = "La cantidad debe ser un valor positivo")
    @DecimalMin (value = "1", message = "La cantidad debe ser mayor que cero")
    private int cantidad;
    @NotNull (message = "El precio unitario no puede ser nulo")
    @Positive (message = "El precio unitario debe ser un valor positivo")
    @DecimalMin (value = "0.01", message = "El precio unitario debe ser mayor que cero")
    private double precioUnitario;
    @NotNull (message = "El subtotal no puede ser nulo")
    @Positive (message = "El subtotal debe ser un valor positivo")
    @DecimalMin (value = "0.01", message = "El subtotal debe ser mayor que cero")
    private double subtotal;
    
    // RELACIÓN OPCIONAL 1: Si el ítem es un PRODUCTO, se asigna este objeto. Si es SERVICIO, queda en null.
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = true)
    private Producto producto;

    // RELACIÓN OPCIONAL 2: Si el ítem es un SERVICIO, se asigna este objeto. Si es PRODUCTO, queda en null.
    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = true)
    private Servicio servicio;

    // Relación hacia la cabecera Venta (@JsonIgnore evita bucles infinitos en la API REST)
    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    @JsonIgnore
    private Venta venta;
    
    
    public ItemVenta() {
    }
    
}
