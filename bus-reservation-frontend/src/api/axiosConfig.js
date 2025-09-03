import axios from "axios";

const BASE_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080/api/v1";

const API = axios.create({ baseURL: BASE_URL });

// attach JWT automatically if present
API.interceptors.request.use((cfg) => {
  const token = localStorage.getItem("token");
  if (token) cfg.headers.Authorization = `Bearer ${token}`;
  return cfg;
});

API.interceptors.response.use(
  (res) => res,
  (err) => {
    // optional: if 401 unauthorized, clear storage
    if (err?.response?.status === 401) {
      localStorage.clear();
      // window.location = "/login";
    }
    return Promise.reject(err);
  }
);

export default API;
