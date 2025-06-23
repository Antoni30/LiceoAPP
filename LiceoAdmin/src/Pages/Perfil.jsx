import { useState, useEffect } from "react";
import { useAuth } from "../hooks/AuthProvider";
import Navbar from "../components/Nabvar";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faEnvelope, faIdCard, faPen, faShieldAlt, faCheckCircle } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import NavbarProfesor from "../components/NabvarProfesor";

export default function Perfil() {
  const { useCedula,userRole } = useAuth();
  const navigate = useNavigate();
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/usuarios/${useCedula}`, {
          credentials: "include",
        });

        if (!response.ok) {
          throw new Error("No se pudo obtener la información del usuario");
        }

        const data = await response.json();
        setUserData(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, [useCedula]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 border-l-4 border-red-500 p-4 mx-auto max-w-md mt-8">
        <div className="flex">
          <div className="flex-shrink-0">
            <svg
              className="h-5 w-5 text-red-500"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fillRule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                clipRule="evenodd"
              />
            </svg>
          </div>
          <div className="ml-3">
            <p className="text-sm text-red-700">{error}</p>
          </div>
        </div>
      </div>
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
    {userRole ==='adminstrador'?<Navbar />: <NavbarProfesor/>}
      
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
