import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../hooks/AuthProvider";
import logo from "../assets/logo.png";
import apiService from "../services/apiService";

export default function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const { logout } = useAuth(); // usamos la función logout del contexto

  const isActive = (path) => location.pathname === path;

  const handleLogout = () => {
    apiService.logout()
      .catch((error) => {
        console.error("Error en logout:", error);
      })
      .finally(() => {
        logout(); // llama a logout del AuthProvider (ya hace navigate('/login'))
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
         Bienvenido, Administrador
        </button>

        {/* Enlaces */}
        <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
          <button
            onClick={() => navigate("/dashboard")}
            className={`${
              isActive("/dashboard")
                ? "border-yellow-400 text-gray-900"
                : "border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700"
            } inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium`}
          >
            Años Lectivos
          </button>
          <button
            onClick={() => navigate("/usuarios")}
            className={`${
              isActive("/usuarios")
                ? "border-yellow-400 text-gray-900"
                : "border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700"
            } inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium`}
          >
            Usuarios
          </button>
          <button
            onClick={() => navigate("/materias")}
            className={`${
              isActive("/materias")
                ? "border-yellow-400 text-gray-900"
                : "border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700"
            } inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium`}
          >
            Materias
          </button>
        </div>
      </div>

      {/* Botón Cerrar sesión */}
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
          <span className="sr-only">Open main menu</span>
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
