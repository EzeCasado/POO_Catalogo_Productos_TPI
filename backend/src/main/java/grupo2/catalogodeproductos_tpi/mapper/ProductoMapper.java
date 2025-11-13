package grupo2.catalogodeproductos_tpi.mapper;

import grupo2.catalogodeproductos_tpi.dto.CreateProductoDTO;
import grupo2.catalogodeproductos_tpi.dto.ProductoResponseDTO;
import grupo2.catalogodeproductos_tpi.dto.UpdateProductoDTO;
import grupo2.catalogodeproductos_tpi.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper para la entidad Producto.
 *
 * @Mapper(componentModel = "spring") - Genera un Bean de Spring.
 * * uses = CategoriaMapper.class - Le dice a MapStruct que puede usar
 * CategoriaMapper para convertir campos de tipo Categoria a CategoriaDTO.
 *
 * nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE -
 * Configuración CLAVE para el PATCH: si un campo en el DTO (fuente) es nulo,
 * NO se seteará en la entidad (destino).
 */
@Mapper(componentModel = "spring", 
        uses = CategoriaMapper.class, 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductoMapper {

    /**
     * Convierte la entidad Producto al DTO de respuesta detallada.
     * @param producto La entidad JPA.
     * @return El DTO de respuesta.
     */
    // "categoria" se mapea automáticamente a "categoria" (Categoria -> CategoriaDTO)
    // usando el CategoriaMapper que declaramos en 'uses'.
    @Mapping(target = "stock", ignore = true) // El stock se poblará manualmente desde el servicio.
    @Mapping(target = "ratingPromedio", ignore = true) // Se poblará manualmente.
    @Mapping(target = "ratingCantidad", ignore = true) // Se poblará manualmente.
    ProductoResponseDTO toProductoResponseDTO(Producto producto);

    /**
     * Convierte el DTO de creación a la entidad Producto.
     * @param dto El DTO con los datos para crear.
     * @return La entidad JPA (aún no guardada).
     */
    @Mapping(target = "id", ignore = true) // Ignoramos ID (es nuevo)
    @Mapping(target = "categoria", ignore = true) // La categoría se manejará manualmente en el servicio.
    Producto toProducto(CreateProductoDTO dto);

    /**
     * Actualiza una entidad Producto existente con los datos de un UpdateProductoDTO.
     * Este es el método usado para el PATCH.
     * Gracias a 'NullValuePropertyMappingStrategy.IGNORE', solo los campos
     * NO nulos en 'dto' se copiarán a 'producto'.
     *
     * @param dto El DTO con los campos a actualizar (puede tener nulos).
     * @param producto La entidad JPA existente (cargada de la BD) que será modificada.
     */
    @Mapping(target = "id", ignore = true) // Nunca actualizamos el ID.
    @Mapping(target = "sku", ignore = true) // El SKU es inmutable, no se actualiza.
    @Mapping(target = "categoria", ignore = true) // La categoría se actualiza a mano en el servicio.
    void updateProductoFromDTO(UpdateProductoDTO dto, @MappingTarget Producto producto);
}