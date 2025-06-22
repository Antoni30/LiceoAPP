import Navbar from "../../components/Nabvar";
import { useState } from 'react';
import { useNavigate } from "react-router-dom";

function AgregarAnio() {
  const [fechaInicio, setFechaInicio] = useState('');
  const [fechaFinal, setFechaFinal] = useState('');
  const [estado, setEstado] = useState('Activo');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate=useNavigate()

const handleSubmit = async (e) => {
  e.preventDefault();
  setIsLoading(true);
  setError(null); // Limpiar error anterior

  // Validación: fechaFinal debe ser mayor que fechaInicio
  if (new Date(fechaFinal) <= new Date(fechaInicio)) {
    setError("La fecha final debe ser mayor que la fecha de inicio.");
    setIsLoading(false);
    return;
  }

  const nuevoAnio = {
    fechaInicio,
    fechaFinal,
    estado,
  };

  try {
    const response = await fetch('http://localhost:8080/api/anios-lectivos', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(nuevoAnio),
    });

    if (!response.ok) throw new Error('Error al agregar el nuevo año lectivo');

    navigate(-1);
  } catch (err) {
    setError(err.message);
  } finally {
    setIsLoading(false);
  }
};

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50 flex justify-center items-center p-6">
        <div className="max-w-7xl w-full bg-white p-8 rounded-lg shadow-lg">
          <h1 className="text-3xl font-bold text-gray-800 mb-4 text-center">Agregar Nuevo Año Lectivo</h1>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-4">
              <div className="w-full md:w-1/2">
                <label htmlFor="fechaInicio" className="block text-sm font-medium text-gray-700">
                  Fecha de Inicio
                </label>
                <input
                  type="date"
                  id="fechaInicio"
                  value={fechaInicio}
                  onChange={(e) => setFechaInicio(e.target.value)}
                  required
                  className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-4">
              <div className="w-full md:w-1/2">
                <label htmlFor="fechaFinal" className="block text-sm font-medium text-gray-700">
                  Fecha Final
                </label>
                <input
                  type="date"
                  id="fechaFinal"
                  value={fechaFinal}
                  onChange={(e) => setFechaFinal(e.target.value)}
                  required
                  className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-4">
              <div className="w-full md:w-1/2">
                <label htmlFor="estado" className="block text-sm font-medium text-gray-700">
                  Estado
                </label>
                <select
                  id="estado"
                  value={estado}
                  onChange={(e) => setEstado(e.target.value)}
                  required
                  className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option value="Activo">Activo</option>
                  <option value="Inactivo">Inactivo</option>
                </select>
              </div>
            </div>

            {error && (
              <div className="bg-red-50 border-l-4 border-red-500 p-4">
                <div className="flex">
                  <div className="flex-shrink-0">
                    <svg className="h-5 w-5 text-red-500" viewBox="0 0 20 20" fill="currentColor">
                      <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                    </svg>
                  </div>
                  <div className="ml-3">
                    <p className="text-sm text-red-700">{error}</p>
                  </div>
                </div>
              </div>
            )}

            <div className="flex justify-end">
              <button
                type="submit"
                disabled={isLoading}
                className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                {isLoading ? 'Cargando...' : 'Agregar Año Lectivo'}
              </button>

               <button
                type="submit"
                onClick={()=>navigate(-1)}
                className="px-4 py-2 mx-1 bg-red-600 text-white rounded-md hover:bg-red-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
}

export default AgregarAnio;
