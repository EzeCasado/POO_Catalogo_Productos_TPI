import axios from 'axios';

const api = axios.create({
  baseURL: '', // backend del módulo 2
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
