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
        <div className="product-detail" style={{ padding: '20px' }}>
            <h2>{producto.nombre}</h2>
            <p>{producto.descripcion}</p>
            <p><strong>Precio:</strong> ${producto.precio}</p>

            {/* CAMBIO 1: Manejo seguro si stock es null */}
            <p><strong>Stock:</strong> {producto.stock !== null ? producto.stock : 'No disponible'}</p>

            {/* CAMBIO 2: Usar el nombre correcto 'ratingPromedio' que viene del DTO */}
            <p><strong>Rating:</strong> ⭐ {producto.ratingPromedio ? producto.ratingPromedio : '-'}</p>
            <p><strong>Reseñas:</strong> {producto.cantidadResenas}</p>
        </div>
    );
}

export default ProductPage;
