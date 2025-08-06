import { useState, useEffect } from "react";
import { useAuth } from "../hooks/AuthProvider";
import { useApi } from "../hooks/useApi";
import apiService from "../services/apiService";
import Navbar from "../components/Nabvar";
import NavbarProfesor from "../components/NabvarProfesor";
import { ErrorMessage } from "../components/UI";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faEnvelope, faIdCard, faPen, faShieldAlt, faCheckCircle } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";

export default function Perfil() {
  const { useCedula, userRole } = useAuth();
  const navigate = useNavigate();
  const { loading, error, executeRequest, clearError } = useApi();
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        await executeRequest(async () => {
          const data = await apiService.getUser(useCedula);
          setUserData(data);
        });
      } catch (err) {
        console.error('Error fetching user data:', err);
      }
    };

    fetchUserData();
  }, [useCedula, executeRequest]);

  if (loading) {
    return (
      <>
        {userRole === 'administrador' ? <Navbar /> : <NavbarProfesor />}
        <div className="flex justify-center items-center h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
        </div>
      </>
    );
  }

  if (error) {
    return (
      <>
        {userRole === 'administrador' ? <Navbar /> : <NavbarProfesor />}
        <div className="mx-auto max-w-md mt-8">
          <ErrorMessage message={error} onClose={clearError} />
        </div>
      </>
    );
  }

  if (!userData) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-600">No se encontraron datos del usuario</p>
      </div>
    );
  }

  return (
    <>
      {userRole === 'administrador' ? <Navbar /> : <NavbarProfesor />}
      
      <div className="min-h-screen bg-gray-100 py-10 px-4">
        <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-lg overflow-hidden">
          <div className="bg-indigo-600 px-6 py-4 flex justify-between items-center">
            <h1 className="text-2xl font-bold text-white flex items-center gap-2">
              <FontAwesomeIcon icon={faUser} /> Perfil de Usuario-{userRole.toUpperCase()}
            </h1>
            <button
              onClick={() => navigate(`/usuario/editar/${useCedula}`)}
              className="bg-white text-indigo-600 hover:bg-indigo-100 px-4 py-2 rounded shadow-sm text-sm font-medium flex items-center gap-2"
            >
              <FontAwesomeIcon icon={faPen} />
              Editar Perfil
            </button>
          </div>

          <div className="px-6 py-6 grid md:grid-cols-2 gap-6">
            {/* Información Personal */}
            <div>
              <h2 className="text-lg font-semibold text-gray-800 mb-4">
                <FontAwesomeIcon icon={faIdCard} className="mr-2 text-indigo-600" />
                Información Personal
              </h2>
              <div className="space-y-2 text-sm text-gray-700">
                <p><strong>ID Usuario:</strong> {userData.idUsuario}</p>
                <p><strong>Nombres:</strong> {userData.nombres}</p>
                <p><strong>Apellidos:</strong> {userData.apellidos}</p>
                <p><strong>Nickname:</strong> {userData.nickname}</p>
              </div>
            </div>

            {/* Información de Contacto */}
            <div>
              <h2 className="text-lg font-semibold text-gray-800 mb-4">
                <FontAwesomeIcon icon={faEnvelope} className="mr-2 text-indigo-600" />
                Información de Contacto
              </h2>
              <div className="space-y-2 text-sm text-gray-700">
                <p>
                  <strong>Email:</strong> {userData.email}
                  {userData.emailVerificado && (
                    <FontAwesomeIcon icon={faCheckCircle} className="ml-2 text-green-500" title="Verificado" />
                  )}
                </p>
                <p>
                  <strong>Estado:</strong>{" "}
                  <span className={`font-medium ${userData.estado === "ACTIVO" ? "text-green-600" : "text-red-600"}`}>
                    {userData.estado}
                  </span>
                </p>
              </div>
            </div>
          </div>

          {/* Configuración de cuenta */}
          <div className="px-6 pb-6 pt-2 border-t border-gray-200">
            <h2 className="text-lg font-semibold text-gray-800 mb-2">
              <FontAwesomeIcon icon={faShieldAlt} className="mr-2 text-indigo-600" />
              Configuración de Cuenta
            </h2>
            <div className="text-sm text-gray-700">
              <p>
                Autenticación en dos pasos:{" "}
                <span className={`font-medium ${userData.mfaHabilitado ? "text-green-600" : "text-gray-500"}`}>
                  {userData.mfaHabilitado ? "Activada" : "Desactivada"}
                </span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
