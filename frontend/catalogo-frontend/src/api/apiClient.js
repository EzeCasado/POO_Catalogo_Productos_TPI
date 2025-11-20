import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // backend del módulo 2
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
