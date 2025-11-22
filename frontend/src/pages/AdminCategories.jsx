// AdminCategories.jsx improved version here

import React, { useState, useEffect } from 'react';
import { getCategorias, crearCategoria } from '../api/adminApi';

export default function AdminCategories() {
    const [categorias, setCategorias] = useState([]);
    const [nombre, setNombre] = useState('');
    const [apiKey, setApiKey] = useState(''); // Necesitamos la Key para crear
    const [mensaje, setMensaje] = useState('');

    // Cargar categorías al iniciar
    useEffect(() => {
        cargarCategorias();
    }, []);

    const cargarCategorias = async () => {
        const respuesta = await getCategorias();
        if (respuesta.ok) {
            setCategorias(respuesta.data);
        } else {
            setMensaje('Error cargando categorías: ' + respuesta.error);
        }
    };

    const handleCrear = async (e) => {
        e.preventDefault();
        if (!apiKey) {
            alert('Por favor ingresa la API Key de vendedor');
            return;
        }

        // Llamamos a la API usando la función que ya armó en adminApi.js
        // Nota: adminApi pide un objeto con id y nombre, pero para crear solemos mandar solo nombre.
        // Asumimos que el backend genera el ID.
        const nuevaCat = { id: 0, nombre: nombre };

        const respuesta = await crearCategoria(nuevaCat, apiKey);

        if (respuesta.ok) {
            setMensaje('Categoría creada con éxito');
            setNombre(''); // Limpiar input
            cargarCategorias(); // Recargar la lista
        } else {
            setMensaje('Error al crear: ' + respuesta.error);
        }
    };

    return (
        <div className="admin-container" style={{ padding: '20px' }}>
            <h2>Administrar Categorías</h2>

            {/* Input de API Key (Temporal, idealmente sería un Login) */}
            <div style={{ marginBottom: '20px', padding: '10px', background: '#f0f0f0' }}>
                <label>Tu API Key de Vendedor: </label>
                <input
                    type="text"
                    value={apiKey}
                    onChange={(e) => setApiKey(e.target.value)}
                    placeholder="Pegá tu X-API-Key acá"
                    style={{ width: '300px' }}
                />
            </div>

            {/* Formulario de Creación */}
            <form onSubmit={handleCrear} style={{ marginBottom: '30px' }}>
                <div style={{ display: 'flex', gap: '10px' }}>
                    <input
                        type="text"
                        placeholder="Nombre de la nueva categoría"
                        value={nombre}
                        onChange={(e) => setNombre(e.target.value)}
                        required
                    />
                    <button type="submit">Crear Categoría</button>
                </div>
            </form>

            {mensaje && <p style={{ color: 'blue' }}>{mensaje}</p>}

            {/* Lista de Categorías */}
            <h3>Listado Actual</h3>
            <ul>
                {categorias.map((cat) => (
                    <li key={cat.id}>
                        <strong>{cat.nombre}</strong> (ID: {cat.id})
                        {cat.descripcion && <p style={{ fontSize: '0.8em' }}>{cat.descripcion}</p>}
                    </li>
                ))}
            </ul>
        </div>
    );
}