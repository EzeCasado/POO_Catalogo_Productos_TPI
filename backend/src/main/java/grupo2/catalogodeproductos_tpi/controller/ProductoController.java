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

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    /**
     * [Endpoint 2.1] POST /products (Crear Producto)
     * Protegido por Interceptor (requiere X-Api-Key)
     */
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProduct(@Valid @RequestBody CreateProductoDTO dto) {
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(dto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    /**
     * [Endpoint 2.3] GET /products/{sku} (Detalle Producto)
     * Público (según Interceptor)
     */
    @GetMapping("/{sku}")
    public ResponseEntity<ProductoResponseDTO> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productoService.obtenerPorSku(sku));
    }

    /**
     * [Endpoint 2.4] PATCH /products/{sku} (Actualizar Producto)
     * Protegido por Interceptor (requiere X-Api-Key)
     */
    @PatchMapping("/{sku}")
    public ResponseEntity<ProductoResponseDTO> updateProduct(@PathVariable String sku, @Valid @RequestBody UpdateProductoDTO dto) {
        ProductoResponseDTO productoActualizado = productoService.actualizarProducto(sku, dto);
        return ResponseEntity.ok(productoActualizado);
    }
}