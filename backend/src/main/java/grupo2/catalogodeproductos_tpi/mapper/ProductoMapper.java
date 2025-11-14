package grupo2.catalogodeproductos_tpi.mapper;

import grupo2.catalogodeproductos_tpi.dto.CreateProductoDTO;
import grupo2.catalogodeproductos_tpi.dto.ProductoResponseDTO;
import grupo2.catalogodeproductos_tpi.dto.RatingDTO;
import grupo2.catalogodeproductos_tpi.dto.StockDTO;
import grupo2.catalogodeproductos_tpi.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Interfaz de MapStruct para las conversiones de Producto.
 *
 * @Mapper(componentModel = "spring", uses = CategoriaMapper.class):
 * 1. componentModel = "spring": Igual que antes, la genera como un Spring Bean.
 * 2. uses = CategoriaMapper.class: ¡MUY IMPORTANTE!
 * Le decimos a este mapper que, si en algún momento necesita
 * convertir un objeto 'Categoria' a un 'CategoriaDTO' (o viceversa),
 * DEBE USAR el 'CategoriaMapper' que ya definimos.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CategoriaMapper.class)
public interface ProductoMapper {

    /**
     * Convierte un DTO de creación (CreateProductoDTO) a una entidad Producto.
     *
     * Ignoramos 'id' (lo genera la BD), 'fechaCreacion' (lo genera @PrePersist)
     * y 'categoria' (porque la buscamos y asignamos manualmente en el Servicio).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    Producto toProducto(CreateProductoDTO createDTO);


    /**
     * Este es el mapper más complejo e importante.
     * Combina TRES fuentes de datos (Producto, StockDTO, RatingDTO)
     * en UN SOLO DTO de respuesta (ProductoResponseDTO).
     *
     * MapStruct se encarga de manejar si 'stockDTO' o 'ratingDTO' son nulos
     * (en ese caso, los campos 'stock' o 'ratingPromedio' serán nulos en el JSON).
     *
     * @param producto La entidad Producto de nuestra BD.
     * @param stockDTO El DTO del servicio de Inventario (puede ser null).
     * @param ratingDTO El DTO del servicio de Reseñas (puede ser null).
     * @return El DTO de respuesta combinado.
     */
    @Mapping(target = "sku", source = "producto.sku")
    @Mapping(target = "nombre", source = "producto.nombre")
    @Mapping(target = "descripcion", source = "producto.descripcion")
    @Mapping(target = "precio", source = "producto.precio")
    @Mapping(target = "categoria", source = "producto.categoria") // ¡Usa CategoriaMapper!
    @Mapping(target = "fechaCreacion", source = "producto.fechaCreacion")
    @Mapping(target = "stock", source = "stockDTO.disponibilidad") // Mapeo de Stock
    @Mapping(target = "ratingPromedio", source = "ratingDTO.promedio") // Mapeo de Rating
    @Mapping(target = "cantidadResenas", source = "ratingDTO.cantidad") // Mapeo de Rating
    ProductoResponseDTO toProductoResponseDTO(Producto producto, StockDTO stockDTO, RatingDTO ratingDTO);
}