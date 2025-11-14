package main.java.grupo2.catalogodeproductos_tpi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta del detalle de un Producto (GET /products/{sku}).
 * Este DTO es el "contrato de salida" de tu API.
 * Combina información de 3 microservicios:
 * 1. Catálogo (este servicio): info base (nombre, precio, categoria, etc.)
 * 2. Inventario: el stock disponible.
 * 3. Reseñas: el rating promedio y cantidad de reseñas.
 * Usamos un 'record' para una respuesta inmutable.
 */
public record ProductoResponseDTO(
    
    // --- Información de Catálogo (Este servicio) ---
    String sku,
    String nombre,
    String descripcion,
    BigDecimal precio,
    CategoriaDTO categoria, // Devolvemos el DTO de categoría anidado
    LocalDateTime fechaCreacion,

    // --- Información de Inventario (Módulo 3) ---
    Integer stock, // Viene de InventarioClient

    // --- Información de Reseñas (Módulo 7) ---
    Double ratingPromedio, // Viene de ResenasClient
    Integer cantidadResenas // Viene de ResenasClient

) {
}