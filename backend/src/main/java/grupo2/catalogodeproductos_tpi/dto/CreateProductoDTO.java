package grupo2.catalogodeproductos_tpi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO específico para la creación de un nuevo Producto (POST /products).
 * Usamos un 'record' de Java (disponible desde Java 16+) para crear
 * una clase de datos inmutable y concisa.
 * Contiene las validaciones de entrada (@NotBlank, @Positive, etc.)
 */
public record CreateProductoDTO(
    
    @NotBlank(message = "El SKU no puede estar vacío")
    @Size(min = 3, max = 50, message = "El SKU debe tener entre 3 y 50 caracteres")
    String sku,

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    String nombre,

    String descripcion,

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser un valor positivo")
    BigDecimal precio,

    @NotNull(message = "El ID de la categoría no puede ser nulo")
    Long categoriaId // Solo recibimos el ID de la categoría
) {
}