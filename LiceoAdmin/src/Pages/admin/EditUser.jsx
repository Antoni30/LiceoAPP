import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useApi } from "../../hooks/useApi";
import apiService from "../../services/apiService";
import { ErrorMessage, Button } from "../../components/UI";

export default function EditarUsuario() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [message, setMessage] = useState("");
  const [form, setForm] = useState({
    nombres: "",
    apellidos: "",
    nickname: "",
    contrasena: "",
    estado: "ACTIVO",
    mfaHabilitado: false,
  });
  const [existente, setExistente] = useState("");
  const { loading, error, executeRequest, clearError } = useApi();
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    setMessage("");
    const fetchUsuario = async () => {
      try {
        await executeRequest(async () => {
          const data = await apiService.getUser(id);
          setForm({
            nombres: data.nombres,
            apellidos: data.apellidos,
            nickname: data.nickname,
            estado: data.estado,
            mfaHabilitado: data.mfaHabilitado ?? !!data.mfaSecret ?? false,
            email: data.email,
            emailVerificado: data.emailVerificado,
          });
        });
      } catch (err) {
        console.error('Error fetching user:', err);
      }
    };

    fetchUsuario();
  }, [id, executeRequest]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setExistente("");
    setMessage("");
    clearError();

    try {
      const payload = {
        nombres: form.nombres,
        apellidos: form.apellidos,
        nickname: form.nickname,
        estado: form.estado,
        mfaHabilitado: form.mfaHabilitado ?? false,
        roles: null,
        email: form.email,
        emailVerificado: form.emailVerificado,
      };

      await apiService.updateUser(id, payload);
      navigate(-1);
    } catch (err) {
      if (err.message.includes("ya existe")) {
        setExistente(err.message);
      } else {
        setMessage(err.message);
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
      </div>
    );
  }
  
  if (error) {
    return (
      <div className="p-4">
        <ErrorMessage message={error} onClose={clearError} />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-3xl mx-auto">
        <div className="bg-white shadow rounded-lg p-6 sm:p-8">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">
              Editar Usuario: {id}
            </h2>
            <button
              onClick={() => navigate(-1)}
              className="text-gray-500 hover:text-gray-700"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-6 w-6"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>

          {error && (
            <div className="mb-6 p-4 bg-red-50 border-l-4 border-red-500 text-red-700">
              <p>{error}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
              <div>
                <label
                  htmlFor="nombres"
                  className="block text-sm font-medium text-gray-700 mb-1"
                >
                  Nombres *
                </label>
                <input
                  type="text"
                  id="nombres"
                  name="nombres"
                  value={form.nombres}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>

              <div>
                <label
                  htmlFor="apellidos"
                  className="block text-sm font-medium text-gray-700 mb-1"
                >
                  Apellidos *
                </label>
                <input
                  type="text"
                  id="apellidos"
                  name="apellidos"
                  value={form.apellidos}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>

              <div>
                <label
                  htmlFor="nickname"
                  className="block text-sm font-medium text-gray-700 mb-1"
                >
                  Nickname *
                </label>
                <input
                  type="text"
                  id="nickname"
                  name="nickname"
                  value={form.nickname}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>

              <div>
                <label
                  htmlFor="estado"
                  className="block text-sm font-medium text-gray-700 mb-1"
                >
                  Estado *
                </label>
                <select
                  id="estado"
                  name="estado"
                  value={form.estado}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="ACTIVO">Activo</option>
                  <option value="INACTIVO">Inactivo</option>
                </select>
              </div>

              <div>
                <label
                  htmlFor="email"
                  className="block text-sm font-medium text-gray-700 mb-1"
                >
                  Email
                </label>
                <input
                  value={form.email}
                  id="email"
                  name="email"
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>

              <div className="flex items-center">
                <input
                  type="checkbox"
                  id="mfaHabilitado"
                  name="mfaHabilitado"
                  checked={form.mfaHabilitado ?? false} // Usamos nullish coalescing por si es null/undefined
                  onChange={handleChange}
                  className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                />
                <label
                  htmlFor="mfaHabilitado"
                  className="ml-2 block text-sm text-gray-700"
                >
                  MFA Habilitado
                </label>
              </div>
            </div>

            <div className="flex justify-end space-x-3 pt-4">
              <Button
                type="button"
                variant="secondary"
                onClick={() => navigate(-1)}
              >
                Cancelar
              </Button>
              <Button
                type="submit"
                loading={isSubmitting}
                disabled={isSubmitting}
              >
                Guardar Cambios
              </Button>
            </div>
          </form>
           {existente && (
            <ErrorMessage 
              message={existente} 
              type="warning" 
              onClose={() => setExistente("")} 
            />
          )}

          {message && (
            <ErrorMessage 
              message={message} 
              onClose={() => setMessage("")} 
            />
          )}
          
          {error && (
            <ErrorMessage 
              message={error} 
              onClose={clearError} 
            />
          )}
        </div>
      </div>
    </div>
  );
}
