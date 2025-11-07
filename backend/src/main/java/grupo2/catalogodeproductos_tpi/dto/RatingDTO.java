package grupo2.catalogodeproductos_tpi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    private String sku;
    private Double promedio;
    private Integer cantidad;

}
