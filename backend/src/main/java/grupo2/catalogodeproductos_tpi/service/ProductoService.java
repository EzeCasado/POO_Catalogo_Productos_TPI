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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaService categoriaService;
    private final InventarioClient inventarioClient;
    private final ResenasClient resenasClient;

    @Transactional
    public ProductoResponseDTO crearProducto(CreateProductoDTO createDTO) {
        if (productoRepository.existsBySku(createDTO.sku())) {
            throw new SkuAlreadyExistsException("El SKU '" + createDTO.sku() + "' ya existe.");
        }
        Categoria categoria = categoriaService.findByIdOrThrow(createDTO.categoriaId());

        Producto producto = productoMapper.toProducto(createDTO);
        producto.setCategoria(categoria);
        
        // El peso y el activo ya los maneja el Mapper y la Entidad
        
        Producto productoGuardado = productoRepository.save(producto);

        return productoMapper.toProductoResponseDTO(productoGuardado, null, null);
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorSku(String sku) {
        Producto producto = findBySkuOrThrow(sku);
        
        Optional<StockDTO> stockOpt = inventarioClient.getStockPorSku(sku);
        Optional<RatingDTO> ratingOpt = resenasClient.getRatingDTO(sku);

        return productoMapper.toProductoResponseDTO(
                producto,
                stockOpt.orElse(null),
                ratingOpt.orElse(null)
        );
    }

    @Transactional
    public ProductoResponseDTO actualizarProducto(String sku, UpdateProductoDTO updateDTO) {
         Producto producto = findBySkuOrThrow(sku);

         if (updateDTO.nombre() != null) producto.setNombre(updateDTO.nombre());
         if (updateDTO.descripcion() != null) producto.setDescripcion(updateDTO.descripcion());
         if (updateDTO.precio() != null) producto.setPrecio(updateDTO.precio());
         
         // Si en el futuro agregas peso al UpdateDTO, descomenta esto:
         // if (updateDTO.peso() != null) producto.setPeso(updateDTO.peso());

         Producto guardado = productoRepository.save(producto);
         
         if (updateDTO.stock() != null) {
             System.out.println("TODO: Llamar a API Inventario para actualizar stock de " + sku + " a " + updateDTO.stock());
         }

         Optional<StockDTO> stockOpt = inventarioClient.getStockPorSku(sku);
         Optional<RatingDTO> ratingOpt = resenasClient.getRatingDTO(sku);

         return productoMapper.toProductoResponseDTO(guardado, stockOpt.orElse(null), ratingOpt.orElse(null));
    }

    /**
     * --- NUEVO MÉTODO ---
     * Da de baja un producto (Baja Lógica).
     * No borra el registro de la base de datos, solo pone activo = false.
     */
    @Transactional
    public void darDeBajaProducto(String sku) {
        Producto producto = findBySkuOrThrow(sku);
        producto.setActivo(false); // Cambiamos el estado a inactivo
        productoRepository.save(producto);
    }

    private Producto findBySkuOrThrow(String sku) {
        return productoRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con SKU: " + sku));
    }
}