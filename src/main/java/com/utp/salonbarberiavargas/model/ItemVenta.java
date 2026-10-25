package com.utp.salonbarberiavargas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @NotBlank (message = "El tipo de item no puede estar en blanco")
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
    

    
    public ItemVenta(int id, String descripcion, int cantidad, double precioUnitario, double subtotal) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
    public ItemVenta() {
    }
    
}
