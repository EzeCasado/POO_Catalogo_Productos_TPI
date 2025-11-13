package grupo2.catalogodeproductos_tpi.controller;

import grupo2.catalogodeproductos_tpi.dto.CategoriaDTO;
import grupo2.catalogodeproductos_tpi.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * [Endpoint 2.5] POST /categories
     */
    @PostMapping
    public ResponseEntity<CategoriaDTO> createCategory(@Valid @RequestBody CategoriaDTO dto) {
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(dto);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    /**
     * [Endpoint 2.6] GET /categories
     */
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAllCategories() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    /**
     * [Endpoint 2.7] GET /categories/{categoryId}
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoriaDTO> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(categoryId));
    }
}
