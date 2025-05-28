import { useState } from "react";

function VerificationModal({ idUsuario, onClose, onSuccess }) {
  const [code, setCode] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/auth/verify-mfa", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ idUsuario, code }),
      credentials: "include",
    });

    const data = await response.json();

    if (data.success) {
      setMessage("Verificación MFA exitosa");
      onSuccess(); // Cierra el modal y redirige al dashboard
    } else {
      setMessage("Error: " + data.message);
    }
  };

  return (
    <div className="fixed inset-0 bg-amber-50 bg-opacity-50 flex items-center justify-center z-50 ">
      <div className="bg-white p-6 rounded-lg shadow-lg max-w-md w-full">
        <h2 className="text-xl font-bold mb-4">Verificación de Código</h2>
        <p className="mb-4">
          Se ha enviado un código de verificación a tu email. Por favor, ingrésalo
          a continuación.
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
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              value={code}
              onChange={(e) => setCode(e.target.value)}
            />
          </div>
          <div className="flex justify-end space-x-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
            >
              Verificar
            </button>
          </div>
        </form>
        {message && (
          <div
            className={`mt-4 p-2 rounded-md ${
              message.includes("Error") ? "bg-red-50 text-red-700" : "bg-green-50 text-green-700"
            }`}
          >
            <p className="text-sm">{message}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default VerificationModal;