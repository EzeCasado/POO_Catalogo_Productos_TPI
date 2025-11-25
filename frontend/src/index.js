import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';

// Buscamos el div con id="root" en tu HTML
const container = document.getElementById('root');

// Creamos la raíz de React y renderizamos la App
const root = createRoot(container);
root.render(<App />);