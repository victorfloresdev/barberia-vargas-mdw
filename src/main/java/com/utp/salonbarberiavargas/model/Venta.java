package com.utp.salonbarberiavargas.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idVenta;
    private LocalDateTime fecha;
    @NotNull (message = "El total no puede ser nulo")
    @Positive (message = "El total debe ser un valor positivo")
    @DecimalMin (value = "0.01", message = "El total debe ser mayor que cero")
    private Double total;
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;
    @OneToMany (mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenta> items;

    public Venta(){

    }
}
