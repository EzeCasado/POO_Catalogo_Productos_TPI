package grupo2.catalogodeproductos_tpi.client;


import grupo2.catalogodeproductos_tpi.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventarioClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String INVENTARIO_API_URL = "https://poo2025.unsada.edu.ar:{puerto}";


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
