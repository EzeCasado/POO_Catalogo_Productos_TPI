package grupo2.catalogodeproductos_tpi.mapper;

import grupo2.catalogodeproductos_tpi.dto.CategoriaDTO;
import grupo2.catalogodeproductos_tpi.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Interfaz de MapStruct para convertir entre Categoria (Entidad) y CategoriaDTO (DTO).
 *
 * @Mapper(componentModel = "spring"):
 * 1. @Mapper: Le dice a MapStruct que esta es una interfaz de mapeo.
 * 2. componentModel = "spring": Le dice a MapStruct que genere una
 * implementación de esta interfaz que sea un "Spring Bean" (@Component).
 * Esto nos permite inyectarla (@Autowired o @RequiredArgsConstructor)
 * en nuestros Servicios.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper {

    /**
     * Convierte una entidad Categoria (de la BD) a un CategoriaDTO (para el JSON).
     */
    CategoriaDTO toCategoriaDTO(Categoria categoria);

    /**
     * Convierte un CategoriaDTO (del JSON) a una entidad Categoria (para la BD).
     *
     * @Mapping(target = "id", ignore = true):
     * Le decimos a MapStruct que al convertir de DTO a Entidad,
     * ignore el campo "id". Esto es CRUCIAL para evitar errores
     * cuando creamos una categoría nueva (ya que el DTO no trae ID
     * o trae uno nulo, y la BD se encarga de generarlo).
     */
    @Mapping(target = "id", ignore = true)
    Categoria toCategoria(CategoriaDTO categoriaDTO);
    
    /**
     * MapStruct es lo suficientemente inteligente para saber que si puede
     * convertir una 'Categoria' a un 'CategoriaDTO', también puede
     * convertir una 'List<Categoria>' a una 'List<CategoriaDTO>'.
     */
    List<CategoriaDTO> toCategoriaDTOList(List<Categoria> categorias);
}