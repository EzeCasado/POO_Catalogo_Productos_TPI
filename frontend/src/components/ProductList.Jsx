import React, { useEffect, useState } from "react";
import api from "../api/apiClient";
import { Link } from "react-router-dom";
import Filters from "../components/Filters";

function ProductList() {
    const [productos, setProductos] = useState([]);
    const [cargando, setCargando] = useState(false);

    // Esta función busca los productos en la API
    // Acepta un objeto de filtros (opcional)
    const fetchProductos = async (filtros = {}) => {
        setCargando(true);
        try {
            // Axios convierte automáticamente el objeto 'params' en ?clave=valor
            const res = await api.get("/products", {
                params: {
                    nombre: filtros.nombre || null,       // Si está vacío, mandamos null
                    categoriaId: filtros.categoriaId || null
                }
            });
            setProductos(res.data);
        } catch (err) {
            console.error("Error buscando productos:", err);
        } finally {
            setCargando(false);
        }
    };

    // Carga inicial (sin filtros) cuando se monta el componente
    useEffect(() => {
        fetchProductos();
    }, []);

    return (
        <div className="product-list">
            {/* Le pasamos la función fetchProductos al componente Filters */}
            <Filters onFilter={fetchProductos} />

            {cargando ? (
                <p>Cargando catálogo...</p>
            ) : (
                <div className="grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '20px' }}>
                    {productos.length > 0 ? (
                        productos.map(p => (
                            <div key={p.sku} className="card" style={{ border: '1px solid #ddd', padding: '15px', borderRadius: '8px' }}>
                                <h3>{p.nombre}</h3>
                                <p style={{ color: '#555' }}>{p.descripcion?.substring(0, 50)}...</p>
                                <p style={{ fontSize: '1.2em', fontWeight: 'bold', color: 'green' }}>
                                    ${p.precio}
                                </p>

                                {/* Botón ver detalle */}
                                <Link
                                    to={`/producto/${p.sku}`}
                                    style={{ display: 'inline-block', marginTop: '10px', textDecoration: 'none', color: '#007bff' }}
                                >
                                    Ver Detalle →
                                </Link>
                            </div>
                        ))
                    ) : (
                        <p>No se encontraron productos con esos filtros.</p>
                    )}
                </div>
            )}
        </div>
    );
}

export default ProductList;
