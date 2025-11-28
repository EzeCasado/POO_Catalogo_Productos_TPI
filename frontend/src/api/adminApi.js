// src/api/adminApi.js
import api from './apiClient';


/**
 * Normaliza la respuesta de error para evitar try/catch en los componentes visuales.
 * Transforma errores de red (Axios) en un objeto estándar { ok: false, error: string }.
 */
function handleError(err) {
    return { ok: false, error: err.response?.data?.message || err.message };
}


/**
 * Helper para inyectar la seguridad requerida por el backend administrativo.
 * @param {string} apiKey - La llave del vendedor.
 */

function withApiKey(apiKey) {
    return { headers: { 'X-API-Key': apiKey } };
}


/**
 * Obtiene el listado plano de categorías para poblar selectores.
 * No requiere paginación por el momento (volumen de datos bajo).
 */

export async function getCategorias() {
    try {
        const res = await api.get('/categories');
        return { ok: true, data: res.data };
    } catch (err) {
        return handleError(err);
    }
}

export async function crearCategoria(categoria, apiKey) {
    try {
        const payload = {
            id: Number(categoria.id),
            nombre: categoria.nombre?.trim()
        };
        const res = await api.post('/categories', payload, withApiKey(apiKey));
        return { ok: true, data: res.data };
    } catch (err) {
        return handleError(err);
    }
}

export async function getProductos() {
    try {
        const res = await api.get('/products');
        return { ok: true, data: res.data };
    } catch (err) {
        return handleError(err);
    }
}

export async function getProductoPorSku(sku) {
    try {
        const res = await api.get(`/products/${encodeURIComponent(sku)}`);
        return { ok: true, data: res.data };
    } catch (err) {
        return handleError(err);
    }
}


/**
 * Orquesta la creación de un producto asegurando tipos de datos correctos.
 * @param {Object} producto - DTO del formulario (strings crudos).
 * @param {string} apiKey - Credencial de autorización.
 * @returns {Promise<{ok: boolean, data?: Object, error?: string}>}
 */

export async function crearProducto(producto, apiKey) {
    try {
        const payload = {
            sku: producto.sku,
            nombre: producto.nombre?.trim(),
            descripcion: producto.descripcion?.trim(),
            precio: Number(producto.precio),

            // --- NUEVOS CAMPOS AGREGADOS ---
            peso: Number(producto.peso),
            dimensiones: producto.dimensiones?.trim(),
            // -------------------------------

            categoriaId: Number(producto.categoriaId)
        };
        const res = await api.post('/products', payload, withApiKey(apiKey));
        return { ok: true, data: res.data };
    } catch (err) {
        return handleError(err);
    }
}

export async function actualizarProducto(sku, patchData, apiKey) {
    try {
        const payload = {
            nombre: patchData.nombre?.trim(),
            descripcion: patchData.descripcion?.trim(),
            precio: Number(patchData.precio)
        };
        const res = await api.patch(`/products/${encodeURIComponent(sku)}`, payload, withApiKey(apiKey));
        return { ok: true, data: res.data };
    } catch (err) {
        return handleError(err);
    }
}

export async function eliminarProducto(sku, apiKey) {
    try {
        const res = await api.delete(`/products/${encodeURIComponent(sku)}`, {
            headers: { 'X-API-Key': apiKey }
        });
        return { ok: true, data: res.data };
    } catch (err) {
        return handleError(err);
    }
}