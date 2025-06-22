import { useAuth } from "../hooks/AuthProvider";
import { Link } from "react-router-dom";

function NoAutorizado() {
  const { userRole, logout } = useAuth();

  const getRoleName = () => {
    switch(userRole?.toLowerCase()) {
      case 'administrador': return 'Administrador';
      case 'profesor': return 'Profesor';
      default: return 'Usuario';
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 flex flex-col items-center justify-center p-4">
      <div className="w-full max-w-md bg-white rounded-xl shadow-md overflow-hidden p-6 text-center">
        <div className="flex justify-center mb-6">
          <div className="bg-red-100 p-4 rounded-full">
            <svg 
              xmlns="http://www.w3.org/2000/svg" 
              className="h-12 w-12 text-red-600" 
              fill="none" 
              viewBox="0 0 24 24" 
              stroke="currentColor"
            >
              <path 
                strokeLinecap="round" 
                strokeLinejoin="round" 
                strokeWidth={2} 
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" 
              />
            </svg>
          </div>
        </div>

        <h1 className="text-2xl font-bold text-gray-800 mb-2">Acceso no autorizado</h1>
        
        <p className="text-gray-600 mb-6">
          Tu rol actual ({getRoleName()}) no tiene permisos para acceder a esta página.
        </p>

        <div className="space-y-3">
          {userRole ? (
            <>
              <Link
                to={userRole.toLowerCase() === 'administrador' ? '/dashboard' : '/profesor'}
                className="block w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded-lg transition duration-200"
              >
                Ir a mi área de trabajo
              </Link>
              <button
                onClick={logout}
                className="block w-full text-gray-700 hover:bg-gray-100 font-medium py-2 px-4 rounded-lg border border-gray-300 transition duration-200"
              >
                Cerrar sesión
              </button>
            </>
          ) : (
            <Link
              to="/"
              className="block w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded-lg transition duration-200"
            >
              Ir a la página de inicio
            </Link>
          )}
        </div>

        <div className="mt-6 pt-6 border-t border-gray-200">
          <p className="text-sm text-gray-500">
            Si crees que esto es un error, por favor contacta al administrador del sistema.
          </p>
        </div>
      </div>
    </div>
  );
}

export default NoAutorizado;