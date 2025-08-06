import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/AuthProvider";
import { useApi } from "../hooks/useApi";
import apiService from "../services/apiService";
import VerificationModal from "./VerificationModal";
import { ErrorMessage, Button } from "./UI";
import logo from "../assets/logo.png";

function Login() {
  const { login } = useAuth();
  const { loading, error, executeRequest, clearError } = useApi();
  const [idUsuario, setIdUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [message, setMessage] = useState("");
  const [showVerificationModal, setShowVerificationModal] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    clearError();

    try {
      await executeRequest(async () => {
        // 1. Intento de autenticación
        const data = await apiService.login(idUsuario, contrasena);

        // 2. Si requiere verificación
        if (data["success"] === false) {
          setShowVerificationModal(true);
          return;
        }

        // 3. Obtener información del usuario (roles)
        const roles = await apiService.getUserRoles(idUsuario);

        if (roles.length === 0) {
          throw new Error("Estado: Pendiente de asignación de rol por parte del administrador.");
        }

        // 4. Obtener nombre del rol
        const rolData = await apiService.getRole(roles[0].idRol);
        const userRole = rolData.nombre.toLowerCase();

        // 5. Manejar redirección según rol
        login(userRole, idUsuario);

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
      });
    } catch (error) {
      setMessage(error.message);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#e5f0ff] py-12 px-4 sm:px-6 lg:px-8">
  <div className="max-w-md w-full space-y-8 bg-white p-10 rounded-xl shadow-lg border border-blue-200">
    <div className="text-center">
      <img src={logo} alt="Logo Institucional" className="mx-auto h-24 mb-6" />
      <h2 className="text-3xl font-semibold text-blue-900 tracking-wide">
        Bienvenido al Portal Académico
      </h2>
      <p className="mt-1 text-sm text-gray-600">
        Por favor, inicia sesión con tu ID institucional
      </p>
    </div>

    <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
      <div className="space-y-4">
        <div>
          <label htmlFor="idUsuario" className="sr-only">ID Usuario</label>
          <input
            id="idUsuario"
            name="idUsuario"
            type="text"
            required
            className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-500 focus:outline-none focus:ring-yellow-400 focus:border-yellow-500 sm:text-sm"
            placeholder="ID Usuario"
            value={idUsuario}
            onChange={(e) => setIdUsuario(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="contrasena" className="sr-only">Contraseña</label>
          <input
            id="contrasena"
            name="contrasena"
            type="password"
            required
            className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-500 focus:outline-none focus:ring-yellow-400 focus:border-yellow-500 sm:text-sm"
            placeholder="Contraseña"
            value={contrasena}
            onChange={(e) => setContrasena(e.target.value)}
          />
        </div>
      </div>

      <div>
        <Button
          type="submit"
          loading={loading}
          disabled={loading}
          className="w-full bg-yellow-400 hover:bg-yellow-500 text-white"
        >
          Ingresar
        </Button>
      </div>
    </form>

    <ErrorMessage 
      message={error || message} 
      type={error ? "error" : message.includes("Error") || message.includes("no tiene acceso") ? "error" : "success"}
      onClose={() => {
        clearError();
        setMessage("");
      }}
    />
    
    {message && message.includes("no tiene acceso") && (
      <div className="mt-2 p-2 rounded-md bg-gray-100 text-gray-700">
        <p className="text-xs">
          Solo usuarios con rol de Administrador o Profesor pueden acceder al sistema.
        </p>
      </div>
    )}

    {showVerificationModal && (
      <VerificationModal
        idUsuario={idUsuario}
        onClose={() => {
          setShowVerificationModal(false);
        }}
        onSuccess={(userRole) => {
          login(userRole, idUsuario);
          navigate(userRole === "administrador" ? "/home" : "/home");
        }}
      />
    )}
  </div>
</div>

  );
}

export default Login;
