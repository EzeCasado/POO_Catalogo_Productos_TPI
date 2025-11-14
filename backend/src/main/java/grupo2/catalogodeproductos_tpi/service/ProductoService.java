package grupo2.catalogodeproductos_tpi.service;

import grupo2.catalogodeproductos_tpi.client.InventarioClient;
import grupo2.catalogodeproductos_tpi.client.ResenasClient;
import grupo2.catalogodeproductos_tpi.dto.*;
import grupo2.catalogodeproductos_tpi.exception.ResourceNotFoundException;
import grupo2.catalogodeproductos_tpi.exception.SkuAlreadyExistsException;
import grupo2.catalogodeproductos_tpi.mapper.ProductoMapper;
import grupo2.catalogodeproductos_tpi.model.Categoria;
import grupo2.catalogodeproductos_tpi.model.Producto;
import grupo2.catalogodeproductos_tpi.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio para la lógica de negocio de los Productos.
 * Este servicio es el ORQUESTADOR:
 * 1. Lee/escribe en su propia BD (Catálogo).
 * 2. Llama a otros microservicios (Inventario, Reseñas).
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    // Repositorios y Mappers
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaService categoriaService; // Para buscar la categoría

    // Clientes HTTP (para otras APIs)
    private final InventarioClient inventarioClient;
    private final ResenasClient resenasClient;

    /**
     * [Endpoint 2.1] Crea un nuevo producto.
     */
    @Transactional
    public ProductoResponseDTO crearProducto(CreateProductoDTO createDTO) {
        // 1. Validar regla de negocio: que el SKU no exista
        if (productoRepository.existsBySku(createDTO.sku())) {
            throw new SkuAlreadyExistsException("El SKU '" + createDTO.sku() + "' ya existe.");
        }

        // 2. Buscar la entidad Categoria (el método de CategoriaService
        // lanzará ResourceNotFoundException si no existe)
        Categoria categoria = categoriaService.findByIdOrThrow(createDTO.categoriaId());

        // 3. Mapear DTO a Entidad (el mapper se encarga de los campos simples)
        Producto producto = productoMapper.toProducto(createDTO);
        producto.setCategoria(categoria); // Asignar la categoría que buscamos

        // 4. Guardar el nuevo producto en la BD
        // (La fecha de creación se setea automáticamente gracias a @PrePersist en la entidad)
        Producto productoGuardado = productoRepository.save(producto);

        // 5. Devolver la respuesta combinada (sin stock ni rating, porque es nuevo)
        // El mapper sabe manejar DTOs nulos (stockDTO y ratingDTO)
        // Esta es la llamada correcta al mapper.
        return productoMapper.toProductoResponseDTO(productoGuardado, null, null);
    }

    /**
     * [Endpoint 2.3] Obtiene el detalle de un producto por SKU.
     * Orquesta llamadas a 3 servicios.
     */
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorSku(String sku) {
        // 1. Buscar el producto en nuestra BD (Catálogo)
        // (Lanzará ResourceNotFoundException si no lo encuentra)
        Producto producto = findBySkuOrThrow(sku);

        // 2. Llamar a la API de Inventario (no bloqueante, usa Optional)
        Optional<StockDTO> stockOpt = inventarioClient.getStockPorSku(sku);
        
        // 3. Llamar a la API de Reseñas (no bloqueante, usa Optional)
        Optional<RatingDTO> ratingOpt = resenasClient.getRatingDTO(sku);

        // 4. Combinar toda la información usando el Mapper
        // .orElse(null) es seguro; el mapper está programado para manejar nulos
        // y simplemente dejará los campos (stock, ratingPromedio) como null en el JSON final.
        return productoMapper.toProductoResponseDTO(
                producto,
                stockOpt.orElse(null),
                ratingOpt.orElse(null)
        );
    }

    /**
     * [Endpoint 2.4] Actualiza parcialmente un producto (PATCH).
     */
    @Transactional
    public ProductoResponseDTO actualizarProducto(String sku, UpdateProductoDTO updateDTO) {
        // 1. Buscar el producto existente (o fallar)
        Producto producto = findBySkuOrThrow(sku);

        // 2. Lógica de actualización parcial (solo actualiza campos no nulos del DTO)
        if (updateDTO.nombre() != null) {
            producto.setNombre(updateDTO.nombre());
        }
        if (updateDTO.descripcion() != null) {
            producto.setDescripcion(updateDTO.descripcion());
        }
        if (updateDTO.precio() != null) {
            producto.setPrecio(updateDTO.precio());
        }

        // 3. Guardar la entidad actualizada en la BD
        Producto productoActualizado = productoRepository.save(producto);
        
        // 4. Manejar la actualización de stock (si vino en el DTO)
        if (updateDTO.stock() != null) {
            // ¡¡IMPORTANTE!!
            // En un escenario real, este servicio NO debería actualizar el stock de otro.
            // Debería *solicitar* una actualización de stock al Módulo de Inventario.
            // Ej: inventarioClient.actualizarStock(sku, updateDTO.stock());
            // Por ahora, solo lo logueamos como un TODO:
            System.out.println("--- SIMULACIÓN ---");
            System.out.println("TODO: Llamar a API Inventario para actualizar stock de " + sku + " a " + updateDTO.stock());
            System.out.println("------------------");
        }

        // 5. Devolver la vista actualizada del producto (volvemos a llamar a los clientes)
        // (Esto asegura que vemos el stock actualizado si el paso 4 lo modificó)
        Optional<StockDTO> stockOpt = inventarioClient.getStockPorSku(sku);
        Optional<RatingDTO> ratingOpt = resenasClient.getRatingDTO(sku);

        return productoMapper.toProductoResponseDTO(
                productoActualizado,
                stockOpt.orElse(null),
                ratingOpt.orElse(null)
        );
    }
    
    // --- Métodos de Ayuda ---

    /**
     * Busca un Producto por SKU o lanza ResourceNotFoundException.
     * Es un helper privado para no repetir código.
     */
    private Producto findBySkuOrThrow(String sku) {
        return productoRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con SKU: " + sku));
    }
}