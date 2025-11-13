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

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService; // Reutilizamos el servicio de Categoría
    private final ProductoMapper productoMapper;
    
    // Clientes para otros módulos
    private final InventarioClient inventarioClient;
    private final ResenasClient resenasClient;

    /**
     * [Endpoint 2.1] POST /products (Crear Producto)
     */
    @Transactional
    public ProductoResponseDTO crearProducto(CreateProductoDTO dto) {
        // 1. Validar que el SKU no exista
        if (productoRepository.existsBySku(dto.sku())) {
            throw new SkuAlreadyExistsException("El SKU '" + dto.sku() + "' ya está en uso.");
        }

        // 2. Buscar la Categoría (lanza excepción si no existe)
        Categoria categoria = categoriaService.findByIdOrThrow(dto.categoriaId());

        // 3. Mapear DTO a Entidad
        Producto producto = productoMapper.toProducto(dto);
        producto.setCategoria(categoria);

        // 4. Guardar
        Producto productoGuardado = productoRepository.save(producto);

        // 5. Mapear a DTO de respuesta
        ProductoResponseDTO responseDTO = productoMapper.toProductoResponseDTO(productoGuardado);

        // 6. Poblar datos iniciales (un producto nuevo no tiene stock ni reseñas)
        responseDTO.setStock(0); // Asumimos 0 stock inicial
        responseDTO.setRatingPromedio(null);
        responseDTO.setRatingCantidad(0);

        return responseDTO;
    }

    /**
     * [Endpoint 2.3] GET /products/{sku} (Detalle Producto)
     */
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorSku(String sku) {
        // 1. Buscar producto base
        Producto producto = findBySkuOrThrow(sku);

        // 2. Mapear a DTO de respuesta
        ProductoResponseDTO responseDTO = productoMapper.toProductoResponseDTO(producto);

        // 3. Enriquecer con datos del Módulo 3 (Inventario)
        Optional<StockDTO> stockOpt = inventarioClient.getStockPorSku(sku);
        responseDTO.setStock(stockOpt.map(StockDTO::getDisponibilidad).orElse(0));

        // 4. Enriquecer con datos del Módulo 7 (Reseñas)
        Optional<RatingDTO> ratingOpt = resenasClient.getRatingDTO(sku);
        responseDTO.setRatingPromedio(ratingOpt.map(RatingDTO::getPromedio).orElse(null));
        responseDTO.setRatingCantidad(ratingOpt.map(RatingDTO::getCantidad).orElse(0));

        return responseDTO;
    }

    /**
     * [Endpoint 2.4] PATCH /products/{sku} (Actualizar Producto)
     */
    @Transactional
    public ProductoResponseDTO actualizarProducto(String sku, UpdateProductoDTO dto) {
        // 1. Buscar producto existente
        Producto producto = findBySkuOrThrow(sku);

        // 2. Actualizar categoría si viene en el DTO
        if (dto.categoriaId() != null) {
            Categoria nuevaCategoria = categoriaService.findByIdOrThrow(dto.categoriaId());
            producto.setCategoria(nuevaCategoria);
        }

        // 3. Usar MapStruct para actualizar campos (ignora nulos)
        productoMapper.updateProductoFromDTO(dto, producto);

        // 4. Guardar
        Producto productoActualizado = productoRepository.save(producto);

        // 5. Devolver la vista detallada (GET)
        // Esto asegura que la respuesta incluya stock y rating actualizados
        return obtenerPorSku(productoActualizado.getSku());
    }

    /**
     * Método auxiliar para buscar por SKU o lanzar 404
     */
    private Producto findBySkuOrThrow(String sku) {
        return productoRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con SKU: " + sku));
    }
}
