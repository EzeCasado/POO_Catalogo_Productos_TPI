package grupo2.catalogodeproductos_tpi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos", indexes = {
    // Creamos un índice en 'sku' para que las búsquedas por SKU sean rapidísimas
    @Index(name = "idx_sku", columnList = "sku", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;


    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // NOTA: El stock real no se guarda aquí, sino en el Módulo de Inventario.
    // Este campo podría usarse como un 'stock reservado' o caché,
    // pero para este TPI lo omitimos para no duplicar datos.
    // @Column(nullable = false)
    // private Integer stock;

    // Relación: Muchos productos pueden pertenecer a UNA categoría.
    // 'FetchType.LAZY' es crucial para no cargar la Categoría a menos que se necesite.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}