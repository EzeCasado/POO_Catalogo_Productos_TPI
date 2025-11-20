import { useEffect, useState } from "react";
import api from "../api/apiClient";
import { Link } from "react-router-dom";

function ProductList() {
  const [productos, setProductos] = useState([]);

  useEffect(() => {
    api.get("/products")
      .then(res => setProductos(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="product-list">
      <h2>Catálogo de Productos</h2>
      <div className="grid">
        {productos.map(p => (
          <div key={p.sku} className="card">
            <h3>{p.nombre}</h3>
            <p>${p.precio}</p>
            <Link to={`/producto/${p.sku}`}>Ver Detalle</Link>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ProductList;
