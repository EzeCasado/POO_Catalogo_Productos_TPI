import React, { useState, useEffect } from 'react';
import { getCategorias } from '../api/adminApi'; // O desde apiClient si preferís

export default function Filters({ onFilter }) {
    const [categorias, setCategorias] = useState([]);

    // Estado local para los filtros
    const [filtros, setFiltros] = useState({
        nombre: '',
        categoriaId: ''
    });

    // Cargar categorías para el <select>
    useEffect(() => {
        async function fetchCats() {
            const res = await getCategorias();
            if (res.ok) setCategorias(res.data);
        }
        fetchCats();
    }, []);

    // Manejar cambios en los inputs
    const handleChange = (e) => {
        setFiltros({
            ...filtros,
            [e.target.name]: e.target.value
        });
    };

    // Al enviar el formulario, avisamos al padre (ProductList)
    const handleSubmit = (e) => {
        e.preventDefault();
        // Llamamos a la función que nos pasaron por props
        onFilter(filtros);
    };

    return (
        <form onSubmit={handleSubmit} style={{
            padding: '15px',
            background: '#f8f9fa',
            borderRadius: '8px',
            marginBottom: '20px',
            display: 'flex',
            gap: '10px',
            alignItems: 'center'
        }}>
            {/* Buscador por Nombre */}
            <input
                type="text"
                name="nombre"
                placeholder="Buscar por nombre..."
                value={filtros.nombre}
                onChange={handleChange}
                style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
            />

            {/* Filtro por Categoría */}
            <select
                name="categoriaId"
                value={filtros.categoriaId}
                onChange={handleChange}
                style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
            >
                <option value="">Todas las categorías</option>
                {categorias.map(cat => (
                    <option key={cat.id} value={cat.id}>
                        {cat.nombre}
                    </option>
                ))}
            </select>

            {/* Botón Filtrar */}
            <button
                type="submit"
                style={{
                    padding: '8px 15px',
                    background: '#007bff',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: 'pointer'
                }}
            >
                Buscar
            </button>
        </form>
    );
}
