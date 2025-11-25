import React, { useState, useEffect } from 'react';
// 1. Importamos las funciones nuevas para listar y eliminar
import { getCategorias, crearProducto, getProductos, eliminarProducto } from '../api/adminApi';

export default function AdminProducts() {
    // Estado del formulario de Alta
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

    // 2. Nuevo Estado: Lista de productos para la tabla de Baja
    const [productos, setProductos] = useState([]);

    // Carga inicial de datos (Categorías y Productos)
    useEffect(() => {
        cargarDatosIniciales();
    }, []);

    const cargarDatosIniciales = () => {
        cargarCategorias();
        cargarProductos();
    };

    const cargarCategorias = async () => {
        const res = await getCategorias();
        if (res.ok) setCategorias(res.data);
    };

    const cargarProductos = async () => {
        const res = await getProductos();
        if (res.ok) setProductos(res.data);
    };

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    // --- Lógica de ALTA (Crear) ---
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!apiKey) {
            alert('Falta la API Key');
            return;
        }

        const respuesta = await crearProducto(form, apiKey);

        if (respuesta.ok) {
            setMensaje(`¡Producto ${respuesta.data.nombre} creado con éxito!`);
            // Limpiar formulario
            setForm({ sku: '', nombre: '', descripcion: '', precio: '', categoriaId: '' });
            // Recargar la lista de abajo para ver el nuevo producto
            cargarProductos();
        } else {
            setMensaje('Error al crear: ' + respuesta.error);
        }
    };

    // --- Lógica de BAJA (Eliminar) ---
    const handleEliminar = async (sku) => {
        // Confirmación para evitar accidentes
        if (!window.confirm(`¿Seguro que querés eliminar el producto ${sku}?`)) return;

        if (!apiKey) {
            alert('Por favor, ingresá tu API Key arriba para poder borrar.');
            return;
        }

        const res = await eliminarProducto(sku, apiKey);

        if (res.ok) {
            setMensaje(`Producto ${sku} eliminado correctamente.`);
            // Recargar la lista para que desaparezca
            cargarProductos();
        } else {
            setMensaje('Error al eliminar: ' + res.error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Administración de Productos (ABM)</h2>

            {/* Input de API Key */}
            <div style={{ marginBottom: '20px', padding: '10px', background: '#f0f0f0', borderRadius: '5px' }}>
                <label style={{ fontWeight: 'bold' }}>API Key de Vendedor: </label>
                <input
                    type="text"
                    value={apiKey}
                    onChange={(e) => setApiKey(e.target.value)}
                    placeholder="Pegá tu llave acá..."
                    style={{ width: '300px', padding: '5px' }}
                />
            </div>

            <div style={{ display: 'flex', gap: '40px', flexWrap: 'wrap' }}>

                {/* SECCIÓN 1: FORMULARIO DE ALTA */}
                <div style={{ flex: 1, minWidth: '300px' }}>
                    <h3>Alta de Producto</h3>
                    <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>

                        <label>SKU (Código Único):</label>
                        <input name="sku" value={form.sku} onChange={handleChange} required style={{ padding: '5px' }} />

                        <label>Nombre:</label>
                        <input name="nombre" value={form.nombre} onChange={handleChange} required style={{ padding: '5px' }} />

                        <label>Descripción:</label>
                        <textarea name="descripcion" value={form.descripcion} onChange={handleChange} style={{ padding: '5px' }} />

                        <label>Precio:</label>
                        <input type="number" name="precio" value={form.precio} onChange={handleChange} required step="0.01" style={{ padding: '5px' }} />

                        <label>Categoría:</label>
                        <select name="categoriaId" value={form.categoriaId} onChange={handleChange} required style={{ padding: '5px' }}>
                            <option value="">Seleccione una categoría...</option>
                            {categorias.map(cat => (
                                <option key={cat.id} value={cat.id}>
                                    {cat.nombre}
                                </option>
                            ))}
                        </select>

                        <button type="submit" style={{ marginTop: '10px', padding: '10px', cursor: 'pointer', background: '#28a745', color: 'white', border: 'none', borderRadius: '5px' }}>
                            Guardar Producto
                        </button>
                    </form>
                    {mensaje && <p style={{ marginTop: '20px', fontWeight: 'bold', color: mensaje.includes('Error') ? 'red' : 'green' }}>{mensaje}</p>}
                </div>

                {/* SECCIÓN 2: LISTADO DE BAJA */}
                <div style={{ flex: 1, minWidth: '400px' }}>
                    <h3>Listado de Productos (Baja)</h3>
                    <div style={{ maxHeight: '500px', overflowY: 'auto', border: '1px solid #ccc' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead style={{ position: 'sticky', top: 0, background: '#f8f9fa' }}>
                            <tr>
                                <th style={{ padding: '10px', textAlign: 'left', borderBottom: '2px solid #ddd' }}>SKU</th>
                                <th style={{ padding: '10px', textAlign: 'left', borderBottom: '2px solid #ddd' }}>Nombre</th>
                                <th style={{ padding: '10px', textAlign: 'left', borderBottom: '2px solid #ddd' }}>Precio</th>
                                <th style={{ padding: '10px', textAlign: 'center', borderBottom: '2px solid #ddd' }}>Acción</th>
                            </tr>
                            </thead>
                            <tbody>
                            {productos.map(p => (
                                <tr key={p.sku} style={{ borderBottom: '1px solid #eee' }}>
                                    <td style={{ padding: '10px' }}>{p.sku}</td>
                                    <td style={{ padding: '10px' }}>{p.nombre}</td>
                                    <td style={{ padding: '10px' }}>${p.precio}</td>
                                    <td style={{ padding: '10px', textAlign: 'center' }}>
                                        <button
                                            onClick={() => handleEliminar(p.sku)}
                                            style={{
                                                backgroundColor: '#dc3545',
                                                color: 'white',
                                                border: 'none',
                                                padding: '5px 10px',
                                                cursor: 'pointer',
                                                borderRadius: '4px'
                                            }}
                                        >
                                            Eliminar
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            {productos.length === 0 && (
                                <tr>
                                    <td colSpan="4" style={{ padding: '20px', textAlign: 'center', color: '#888' }}>
                                        No hay productos cargados.
                                    </td>
                                </tr>
                            )}
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </div>
    );
}