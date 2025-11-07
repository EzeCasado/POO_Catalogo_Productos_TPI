package grupo2.catalogodeproductos_tpi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    private String sku;
    private Integer disponibilidad;

}
