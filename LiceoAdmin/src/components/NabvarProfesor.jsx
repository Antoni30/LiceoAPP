import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../hooks/AuthProvider";
import logo from "../assets/logo.png";
import apiService from "../services/apiService";
import { useState, useEffect } from "react";

export default function NavbarProfesor() {
  const navigate = useNavigate();
  const location = useLocation();
  const { logout, user, useCedula } = useAuth();
  const [idProfesor, setIdProfesor] = useState(null);

  useEffect(() => {
    if (user?.idUsuario) {
      setIdProfesor(user.idUsuario);
    }
  }, [user]);

  // Función para determinar si una ruta está activa
  const isActive = (path) => location.pathname === path || location.pathname.startsWith(path);

  const handleLogout = () => {
    apiService.logout()
      .finally(() => {
        logout();
      });
  };

  return (
  <nav className="bg-white shadow-sm border-b border-blue-200">
  <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <div className="flex justify-between h-16">
      {/* Logo + Título */}
      <div className="flex space-x-4 items-center">
        <img
          src={logo}
          alt="Logo Institucional"
          className="h-10 w-auto cursor-pointer"
          onClick={() => navigate("/home")}
        />
        <button
          onClick={() => navigate("/home")}
          className="text-xl font-bold text-blue-800 hover:text-blue-900"
        >
          Lice Profesor
        </button>

        {/* Navegación */}
        <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
      
          <button
            onClick={() => navigate(`/profesor/generar-reporte/${useCedula}`)}
            className={`${
              isActive("/profesor/generar-reporte")
                ? "border-yellow-400 text-gray-900"
                : "border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700"
            } inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium`}
          >
            Generar Reporte
          </button>
          <button
            onClick={() => navigate("/horario")}
            className={`${
              isActive("/horario")
                ? "border-yellow-400 text-gray-900"
                : "border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700"
            } inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium`}
          >
            Horario
          </button>
        </div>
      </div>

      {/* Botón logout */}
      <div className="hidden sm:ml-6 sm:flex sm:items-center">
        <button
          onClick={handleLogout}
          className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-yellow-400 hover:bg-yellow-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500"
        >
          Cerrar Sesión
        </button>
      </div>

      {/* Menú móvil */}
      <div className="-mr-2 flex items-center sm:hidden">
        <button
          type="button"
          className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-yellow-500"
        >
          <span className="sr-only">Abrir menú principal</span>
          <svg
            className="block h-6 w-6"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</nav>

  );
}
