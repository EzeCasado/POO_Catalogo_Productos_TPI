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

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProduct(@Valid @RequestBody CreateProductoDTO createDto) {
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(createDto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductoResponseDTO> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productoService.obtenerPorSku(sku));
    }

    @PatchMapping("/{sku}")
    public ResponseEntity<ProductoResponseDTO> updateProduct(
            @PathVariable String sku,
            @Valid @RequestBody UpdateProductoDTO updateDto) {
        
        ProductoResponseDTO productoActualizado = productoService.actualizarProducto(sku, updateDto);
        return ResponseEntity.ok(productoActualizado);
    }

    /**
     * --- NUEVO ENDPOINT ---
     * DELETE /products/{sku}
     * Realiza una baja lógica del producto.
     */
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String sku) {
        productoService.darDeBajaProducto(sku);
        // Devolvemos 204 No Content (es el estándar para un borrado exitoso)
        return ResponseEntity.noContent().build();
    }
}