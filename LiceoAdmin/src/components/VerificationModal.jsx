import { useState } from "react";
import { useApi } from "../hooks/useApi";
import apiService from "../services/apiService";
import { ErrorMessage, Button, Modal } from "./UI";

function VerificationModal({ idUsuario, onClose, onSuccess }) {
  const [code, setCode] = useState("");
  const [message, setMessage] = useState("");
  const { loading, error, executeRequest, clearError } = useApi();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    clearError();

    try {
      await executeRequest(async () => {
        const data = await apiService.verifyMfa(idUsuario, code);

        if (!data.success) {
          throw new Error(data.message || "Verificación fallida");
        }

        const roles = await apiService.getUserRoles(idUsuario);
        if (!roles || roles.length === 0) {
          throw new Error("El usuario no tiene roles asignados");
        }

        const rolData = await apiService.getRole(roles[0].idRol);
        const userRole = rolData.nombre?.toLowerCase();

        if (!userRole) {
          throw new Error("Rol no definido");
        }

        setMessage("Verificación exitosa");
        onSuccess(userRole);
      });
    } catch (error) {
      console.error("❌ Error en VerificationModal:", error);
      setMessage("Error: " + error.message);
    }
  };

  return (
    <Modal
      isOpen={true}
      onClose={onClose}
      title="Verificación de Código"
      footer={
        <>
          <Button variant="secondary" onClick={onClose}>
            Cancelar
          </Button>
          <Button
            type="submit"
            loading={loading}
            disabled={loading}
            className="bg-yellow-400 hover:bg-yellow-500 text-white"
            onClick={handleSubmit}
          >
            Verificar
          </Button>
        </>
      }
    >
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
      </form>

      <ErrorMessage 
        message={error || message} 
        type={error ? "error" : message.includes("Error") ? "error" : "success"}
        onClose={() => {
          clearError();
          setMessage("");
        }}
      />
    </Modal>
  );
}

export default VerificationModal;
