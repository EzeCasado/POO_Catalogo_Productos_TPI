// AdminProducts.jsx improved version here

import React, { useState, useEffect } from 'react';
import { getCategorias, crearProducto } from '../api/adminApi';

export default function AdminProducts() {
    // Estado del formulario
    const [form, setForm] = useState({
        sku: '',
        nombre: '',
        descripcion: '',
        precio: '',
        categoriaId: ''
    });

    const [categorias, setCategorias] = useState([]);
    const [apiKey, setApiKey] = useState('');
    const [mensaje, setMensaje] = useState('');

    // Cargar categorías para el desplegable <select>
    useEffect(() => {
        async function fetchCats() {
            const res = await getCategorias();
            if (res.ok) setCategorias(res.data);
        }
        fetchCats();
    }, []);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!apiKey) {
            alert('Falta la API Key');
            return;
        }

        // Llamamos a la función crearProducto de adminApi.js
        const respuesta = await crearProducto(form, apiKey);

        if (respuesta.ok) {
            setMensaje(`¡Producto ${respuesta.data.nombre} creado con éxito!`);
            // Limpiar formulario opcionalmente
            setForm({ sku: '', nombre: '', descripcion: '', precio: '', categoriaId: '' });
        } else {
            setMensaje('Error: ' + respuesta.error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Alta de Productos</h2>

            {/* API Key Section */}
            <div style={{ marginBottom: '20px', padding: '10px', background: '#f0f0f0' }}>
                <label>API Key: </label>
                <input
                    type="text"
                    value={apiKey}
                    onChange={(e) => setApiKey(e.target.value)}
                    style={{ width: '300px' }}
                />
            </div>

            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxWidth: '400px' }}>

                <label>SKU (Código Único):</label>
                <input name="sku" value={form.sku} onChange={handleChange} required />

                <label>Nombre:</label>
                <input name="nombre" value={form.nombre} onChange={handleChange} required />

                <label>Descripción:</label>
                <textarea name="descripcion" value={form.descripcion} onChange={handleChange} />

                <label>Precio:</label>
                <input type="number" name="precio" value={form.precio} onChange={handleChange} required step="0.01" />

                <label>Categoría:</label>
                <select name="categoriaId" value={form.categoriaId} onChange={handleChange} required>
                    <option value="">Seleccione una categoría...</option>
                    {categorias.map(cat => (
                        <option key={cat.id} value={cat.id}>
                            {cat.nombre}
                        </option>
                    ))}
                </select>

                <button type="submit" style={{ marginTop: '10px', padding: '10px', cursor: 'pointer' }}>
                    Guardar Producto
                </button>
            </form>

            {mensaje && <p style={{ marginTop: '20px', fontWeight: 'bold' }}>{mensaje}</p>}
        </div>
    );
}