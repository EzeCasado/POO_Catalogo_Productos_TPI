

import React from "react";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import Home from "./pages/Home";
import ProductPage from "./pages/ProductPage";
// 1. Importamos las pantallas de Admin
import AdminProducts from "./pages/AdminProducts";
import AdminCategories from "./pages/AdminCategories";

function App() {
    return (
        <BrowserRouter>
            {/* Un menú de navegación simple para ir y venir */}
            <nav style={{ padding: '10px', borderBottom: '1px solid #ccc', marginBottom: '20px' }}>
                <Link to="/" style={{ marginRight: '10px' }}>Inicio</Link> |
                <Link to="/admin/products" style={{ margin: '0 10px' }}>Alta Productos</Link> |
                <Link to="/admin/categories" style={{ marginLeft: '10px' }}>Categorías</Link>
            </nav>

            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/producto/:sku" element={<ProductPage />} />

                {/* 2. Definimos las Rutas para los formularios */}
                <Route path="/admin/products" element={<AdminProducts />} />
                <Route path="/admin/categories" element={<AdminCategories />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
