import { useAuth } from "../hooks/AuthProvider";
import { Link } from "react-router-dom";

function Home() {
  const { userRole, logout } = useAuth();

  // Obtener el nombre del rol formateado
  const getRoleName = () => {
    switch(userRole?.toLowerCase()) {
      case 'administrador': return 'Administrador';
      case 'profesor': return 'Profesor';
      default: return 'Usuario';
    }
  };

  // Tarjetas de acciones disponibles según rol
  const getActionCards = () => {
    const commonActions = [
      {
        title: "Mi Perfil",
        description: "Ver y editar tu información personal",
        icon: "👤",
        path: "/perfil",
        color: "bg-blue-100 text-blue-800"
      }
    ];

    const adminActions = [
      {
        title: "Gestión de Usuarios",
        description: "Administrar todos los usuarios del sistema",
        icon: "👥",
        path: "/usuarios",
        color: "bg-purple-100 text-purple-800"
      },
      {
        title: "Panel de Control",
        description: "Acceder al panel de administración",
        icon: "📊",
        path: "/dashboard",
        color: "bg-green-100 text-green-800"
      }
    ];

    const teacherActions = [
      {
        title: "Mis Cursos",
        description: "Gestionar tus cursos asignados",
        icon: "📚",
        path: "/profesor/cursos",
        color: "bg-indigo-100 text-indigo-800"
      },
      {
        title: "Calificaciones",
        description: "Registrar y consultar calificaciones",
        icon: "📝",
        path: "/profesor/calificaciones",
        color: "bg-yellow-100 text-yellow-800"
      }
    ];

    let actions = [...commonActions];
    
    if (userRole?.toLowerCase() === 'administrador') {
      actions = [...actions, ...adminActions];
    } else if (userRole?.toLowerCase() === 'profesor') {
      actions = [...actions, ...teacherActions];
    }

    return actions;
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-4 sm:px-6 lg:px-8 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-gray-900">Bienvenido, {getRoleName()}</h1>
          <button
            onClick={logout}
            className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Cerrar sesión
          </button>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
        {/* Panel de bienvenida */}
        <div className="bg-white overflow-hidden shadow rounded-lg mb-8">
          <div className="px-4 py-5 sm:p-6">
            <div className="flex items-center">
              <div className="flex-shrink-0 bg-indigo-500 rounded-md p-3">
                <svg className="h-8 w-8 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
              </div>
              <div className="ml-4">
                <h3 className="text-lg leading-6 font-medium text-gray-900">Panel de Inicio</h3>
                <p className="mt-1 text-sm text-gray-500">
                  Selecciona una de las opciones disponibles para comenzar
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Tarjetas de acciones */}
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {getActionCards().map((action, index) => (
            <Link
              key={index}
              to={action.path}
              className="hover:transform hover:scale-105 transition-all duration-200"
            >
              <div className={`${action.color} overflow-hidden shadow rounded-lg h-full`}>
                <div className="px-4 py-5 sm:p-6">
                  <div className="flex items-center">
                    <div className="text-2xl mr-4">{action.icon}</div>
                    <div>
                      <h3 className="text-lg leading-6 font-medium">{action.title}</h3>
                      <p className="mt-1 text-sm opacity-80">
                        {action.description}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </Link>
          ))}
        </div>

        {/* Mensaje para usuarios sin acciones específicas */}
        {getActionCards().length <= 1 && (
          <div className="mt-8 bg-yellow-50 border-l-4 border-yellow-400 p-4">
            <div className="flex">
              <div className="flex-shrink-0">
                <svg className="h-5 w-5 text-yellow-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                </svg>
              </div>
              <div className="ml-3">
                <p className="text-sm text-yellow-700">
                  Tu rol actual no tiene acciones específicas asignadas. Contacta al administrador si necesitas acceso a más funciones.
                </p>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="bg-white border-t border-gray-200 mt-8">
        <div className="max-w-7xl mx-auto px-4 py-4 sm:px-6 lg:px-8">
          <p className="text-center text-sm text-gray-500">
            Sistema Académico © {new Date().getFullYear()}
          </p>
        </div>
      </footer>
    </div>
  );
}

export default Home;