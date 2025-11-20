import { useEffect, useState } from "react";
import api from "../api/apiClient";

function Filters({ onFilter }) {
  const [categorias, setCategorias] = useState([]);
  const [filtros, setFiltros] = useState({ q: "", categoryId: "", precioMin: "", precioMax: "" });

  useEffect(() => {
    api.get("/categories")
      .then(res => setCategorias(res.data))
      .catch(err => console.error(err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFiltros({ ...filtros, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilter(filtros);
  };

  return (
    <form onSubmit={handleSubmit} className="filters">
      <input type="text" name="q" placeholder="Buscar..." onChange={handleChange} />
      <select name="categoryId" onChange={handleChange}>
        <option value="">Todas las categorías</option>
        {categorias.map(cat => (
          <option key={cat.id} value={cat.id}>{cat.nombre}</option>
        ))}
      </select>
      <input type="number" name="precioMin" placeholder="Precio mínimo" onChange={handleChange} />
      <input type="number" name="precioMax" placeholder="Precio máximo" onChange={handleChange} />
      <button type="submit">Filtrar</button>
    </form>
  );
}

export default Filters;
