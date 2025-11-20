import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/apiClient";

function ProductPage() {
  const { sku } = useParams();
  const [producto, setProducto] = useState(null);

  useEffect(() => {
    api.get(`/products/${sku}`)
      .then(res => setProducto(res.data))
      .catch(err => console.error(err));
  }, [sku]);

  if (!producto) return <p>Cargando...</p>;

  return (
    <div className="product-detail">
      <h2>{producto.nombre}</h2>
      <p>{producto.descripcion}</p>
      <p><strong>Precio:</strong> ${producto.precio}</p>
      <p><strong>Stock:</strong> {producto.stock}</p>
      <p><strong>Rating:</strong> ⭐ {producto.rating}</p>
    </div>
  );
}

export default ProductPage;
