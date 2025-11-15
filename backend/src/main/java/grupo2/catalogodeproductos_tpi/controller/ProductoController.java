package grupo2.catalogodeproductos_tpi.controller;

import grupo2.catalogodeproductos_tpi.dto.CreateProductoDTO;
import grupo2.catalogodeproductos_tpi.dto.ProductoResponseDTO;
import grupo2.catalogodeproductos_tpi.dto.UpdateProductoDTO;
import grupo2.catalogodeproductos_tpi.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para los endpoints de Productos.
 *
 * @RestController
 * @RequestMapping("/products") Define la URL base (http://localhost:8080/products)
 * @RequiredArgsConstructor Inyecta ProductoService.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * [Endpoint 2.1] POST /products (Crear Producto)
     * Crea un nuevo producto en el catálogo.
     *
     * @param createDto El JSON del body, validado por @Valid.
     * @return ResponseEntity con el ProductoResponseDTO creado (201 Created).
     * Si el SKU ya existe, ProductoService lanzará SkuAlreadyExistsException,
     * que GlobalExceptionHandler convertirá en un error 409 (Conflict).
     */
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProduct(@Valid @RequestBody CreateProductoDTO createDto) {
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(createDto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    /**
     * [Endpoint 2.3] GET /products/{sku} (Detalle Producto)
     * Obtiene la información detallada y combinada de un producto por su SKU.
     *
     * @param sku El SKU que viene en la URL.
     * @return ResponseEntity con el ProductoResponseDTO (200 OK).
     * Si no se encuentra, ProductoService lanzará ResourceNotFoundException
     * (manejado por GlobalExceptionHandler, devuelve 404).
     */
    @GetMapping("/{sku}")
    public ResponseEntity<ProductoResponseDTO> getProductBySku(@PathVariable String sku) {
        ProductoResponseDTO producto = productoService.obtenerPorSku(sku);
        return ResponseEntity.ok(producto);
    }

    /**
     * [Endpoint 2.4] PATCH /products/{sku} (Actualizar Producto)
     * Actualiza parcialmente la información de un producto.
     *
     * @PatchMapping Define que maneja peticiones PATCH.
     *
     * @param sku El SKU del producto a actualizar.
     * @param updateDto El JSON del body, con los campos opcionales a cambiar.
     * @return ResponseEntity con la vista actualizada del producto (200 OK).
     */
    @PatchMapping("/{sku}")
    public ResponseEntity<ProductoResponseDTO> updateProduct(
            @PathVariable String sku,
            @Valid @RequestBody UpdateProductoDTO updateDto) {
        
        ProductoResponseDTO productoActualizado = productoService.actualizarProducto(sku, updateDto);
        return ResponseEntity.ok(productoActualizado);
    }

    // Dentro de la clase ProductoController.java

    /**
     * [Endpoint 2.2] GET /products (Búsqueda y Filtrado)
     * Obtiene una lista de todos los productos, con filtros opcionales.
     *
     * @param nombre Filtro opcional por nombre (búsqueda parcial).
     * @param categoriaId Filtro opcional por ID de categoría (búsqueda exacta).
     * @return ResponseEntity con la lista de ProductoResponseDTO (200 OK).
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> searchProducts(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId) {

        List<ProductoResponseDTO> productos = productoService.buscarProductos(nombre, categoriaId);
        return ResponseEntity.ok(productos);
    }
}