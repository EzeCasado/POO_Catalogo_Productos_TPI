package grupo2.catalogodeproductos_tpi.client;


import grupo2.catalogodeproductos_tpi.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * Cliente HTTP para comunicarse con el Módulo 3 (Inventario).
 * Se encarga de obtener el stock de los productos.
 */
@Component
public class InventarioClient {

    @Autowired
    private RestTemplate restTemplate;

    // Lee la URL desde application.properties
    @Value("${api.inventario.url}")
    private String INVENTARIO_API_URL;

    /**
     * Llama al Módulo 3 para obtener el stock disponible de un SKU específico.
     *
     * @param sku El SKU (String) del producto a consultar.
     * @return un StockDTO con la información del stock. Devuelve null si la API falla o el producto no se encuentra.
     */
    public StockDTO getStockPorSku(String sku){


        String url = INVENTARIO_API_URL + sku;

        try{

            StockDTO respuesta = restTemplate.getForObject(url, StockDTO.class);

            return respuesta;

        }catch(Exception e){

            return null;

        }

    }
}
