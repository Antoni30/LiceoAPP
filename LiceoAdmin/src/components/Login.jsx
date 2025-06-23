import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/AuthProvider";
import VerificationModal from "./VerificationModal";

function Login() {
  const { login } = useAuth();
  const [idUsuario, setIdUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [message, setMessage] = useState("");
  const [showVerificationModal, setShowVerificationModal] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setIsLoading(true);

    try {
      // 1. Intento de autenticación
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ idUsuario, contrasena }),
        credentials: "include",
      });

      const data = await response.json();


      if (!response.ok) {
        throw new Error(data.message || "Error en la autenticación");
      }

      // 2. Si requiere verificación
      if (data["success"] === false) {
        setShowVerificationModal(true);
        return;
      }

      // 3. Obtener información del usuario (roles)
      const rolResponse = await fetch(
        `http://localhost:8080/api/usuarios-roles/usuario/${idUsuario}`,
        { credentials: "include" }
      );

      if (!rolResponse.ok) {
        throw new Error("El rol de usuario aún no ha sido asignado por el administrador del sistema.");
      }

      const roles = await rolResponse.json();

      if (roles.length === 0) {
        throw new Error("Estado: Pendiente de asignación de rol por parte del administrador.");
      }

      // 4. Obtener nombre del rol
      const nombreRolResponse = await fetch(
        `http://localhost:8080/api/roles/${roles[0].idRol}`,
        { credentials: "include" }
      );

      if (!nombreRolResponse.ok) {
        throw new Error("No se pudo obtener el nombre del rol");
      }

      const rolData = await nombreRolResponse.json();
      const userRole = rolData.nombre.toLowerCase();

      // 5. Manejar redirección según rol
      login(userRole,idUsuario); // Actualiza el estado de autenticación

      switch (userRole) {
        case "administrador":
          navigate("/home");
          break;
        case "profesor":
          navigate(`/home`);
          break;
        default:
          setMessage("Tu rol no tiene acceso al sistema");
          break;
      }
    } catch (error) {
      setMessage(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-lg shadow-md">
        <div className="text-center">
          <h2 className="mt-6 text-3xl font-extrabold text-gray-900">
            Iniciar Sesión
          </h2>
        </div>

        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="rounded-md shadow-sm space-y-4">
            <div>
              <label htmlFor="idUsuario" className="sr-only">
                ID Usuario
              </label>
              <input
                id="idUsuario"
                name="idUsuario"
                type="text"
                required
                className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                placeholder="ID Usuario"
                value={idUsuario}
                onChange={(e) => setIdUsuario(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="contrasena" className="sr-only">
                Contraseña
              </label>
              <input
                id="contrasena"
                name="contrasena"
                type="password"
                required
                className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                placeholder="Contraseña"
                value={contrasena}
                onChange={(e) => setContrasena(e.target.value)}
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              disabled={isLoading}
              className={`group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${
                isLoading ? "opacity-75 cursor-not-allowed" : ""
              }`}
            >
              {isLoading ? (
                <>
                  <svg
                    className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                  >
                    <circle
                      className="opacity-25"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      strokeWidth="4"
                    ></circle>
                    <path
                      className="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                    ></path>
                  </svg>
                  Procesando...
                </>
              ) : (
                "Ingresar"
              )}
            </button>
          </div>
        </form>

        {message && (
          <div
            className={`mt-4 p-3 rounded-md ${
              message.includes("Error") || message.includes("no tiene acceso")
                ? "bg-red-50 text-red-700"
                : "bg-green-50 text-green-700"
            }`}
          >
            <p className="text-sm">{message}</p>
            {message.includes("no tiene acceso") && (
              <p className="mt-2 text-sm">
                Solo usuarios con rol de Administrador o Profesor pueden acceder
                al sistema.
              </p>
            )}
          </div>
        )}

        {showVerificationModal && (
          <VerificationModal
            idUsuario={idUsuario}
            onClose={() => {
              setShowVerificationModal(false);
              setIsLoading(false);
            }}
            onSuccess={(userRole) => {
              login(userRole,idUsuario);
              navigate(
                userRole === "administrador"
                  ? "/home"
                  : `/home`
              );
            }}
          />
        )}
      </div>
    </div>
  );
}

export default Login;
