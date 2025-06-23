import { useState } from "react";

function VerificationModal({ idUsuario, onClose, onSuccess }) {
  const [code, setCode] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setIsLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/auth/verify-mfa", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ idUsuario, code }),
        credentials: "include",
      });

      const data = await response.json();

      if (!response.ok || !data.success) {
        throw new Error(data.message || "Verificación fallida");
      }

      const rolResponse = await fetch(
        `http://localhost:8080/api/usuarios-roles/usuario/${idUsuario}`,
        { credentials: "include" }
      );

      if (!rolResponse.ok) {
        throw new Error("No se pudo obtener los roles del usuario");
      }

      const roles = await rolResponse.json();
      if (!roles || roles.length === 0) {
        throw new Error("El usuario no tiene roles asignados");
      }

      const idRol = roles[0].idRol;

      const nombreRolResponse = await fetch(
        `http://localhost:8080/api/roles/${idRol}`,
        { credentials: "include" }
      );

      if (!nombreRolResponse.ok) {
        throw new Error("No se pudo obtener el nombre del rol");
      }

      const rolData = await nombreRolResponse.json();
      const userRole = rolData.nombre?.toLowerCase();

      if (!userRole) {
        throw new Error("Rol no definido");
      }

      setMessage("Verificación exitosa");
      onSuccess(userRole);
    } catch (error) {
      console.error("❌ Error en VerificationModal:", error);
      setMessage("Error: " + error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-white bg-opacity-60 backdrop-blur-sm flex items-center justify-center z-50">
      <div className="bg-white p-8 rounded-lg shadow-xl max-w-md w-full border border-blue-100">
        <h2 className="text-2xl font-semibold text-blue-800 tracking-wide mb-4 text-center">
          Verificación de Código
        </h2>
        <p className="mb-4 text-sm text-gray-600 text-center">
          Se ha enviado un código de verificación a tu correo institucional. Ingrésalo para continuar.
        </p>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label htmlFor="code" className="block text-sm font-medium text-gray-700">
              Código de Verificación
            </label>
            <input
              id="code"
              type="text"
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-yellow-400 focus:border-yellow-500"
              value={code}
              onChange={(e) => setCode(e.target.value)}
            />
          </div>

          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 transition"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="px-4 py-2 bg-yellow-400 text-white font-medium rounded-md hover:bg-yellow-500 transition disabled:opacity-70"
            >
              {isLoading ? "Verificando..." : "Verificar"}
            </button>
          </div>
        </form>

        {message && (
          <div
            className={`mt-4 p-2 rounded-md text-sm ${
              message.includes("Error")
                ? "bg-red-50 text-red-700"
                : "bg-green-50 text-green-700"
            }`}
          >
            {message}
          </div>
        )}
      </div>
    </div>
  );
}

export default VerificationModal;
