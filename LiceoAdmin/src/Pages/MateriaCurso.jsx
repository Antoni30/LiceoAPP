import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Nabvar";

function MateriasDelCurso() {
  const { idCurso } = useParams();
  const navigate = useNavigate();
  const [materias, setMaterias] = useState([]); // Materias asignadas al curso
  const [allMaterias, setAllMaterias] = useState([]); // Todas las materias disponibles
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [materiaSeleccionada, setMateriaSeleccionada] = useState(""); // Materia seleccionada para agregar
  const [materiasExistentes, setMateriasExistentes] = useState([]); // Materias que ya están asignadas al curso
  const [showCreateModal, setShowCreateModal] = useState(false); // Control del modal de agregar materia

  useEffect(() => {
    fetchMateriasCurso();
    fetchMateriasDisponibles();
  }, [idCurso]);

  // Obtener las materias ya asignadas al curso
  const fetchMateriasCurso = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/cursos-materias/curso/${idCurso}`,
        {
          credentials: "include",
        }
      );

      if (!response.ok) throw new Error("Error al cargar las materias del curso");

      const data = await response.json();
      setMateriasExistentes(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  // Obtener todas las materias disponibles
  const fetchMateriasDisponibles = async () => {
    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/materias", {
        credentials: "include",
      });

      if (!response.ok) throw new Error("Error al cargar las materias disponibles");

      const data = await response.json();
      setAllMaterias(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  // Función para agregar una materia al curso
  const handleAddMateria = async () => {
    // Verifica si la materia ya está asignada al curso
    if (materiasExistentes.find((materia) => materia.idMateria === materiaSeleccionada)) {
      setError("Esta materia ya está asignada al curso.");
      return;
    }

    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/cursos-materias", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          idCurso,
          idMateria: materiaSeleccionada,
        }),
      });

      if (!response.ok) throw new Error("Error al agregar la materia");

      // Recargar las materias del curso después de agregar
      fetchMateriasCurso();
      setMateriaSeleccionada(""); // Limpiar la selección
      setShowCreateModal(false); // Cerrar el modal
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  // Función para eliminar una materia del curso
  const handleDeleteMateria = async (idMateria) => {
    if (!window.confirm("¿Estás seguro de que deseas eliminar esta materia del curso?")) return;

    setIsLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/cursos-materias/curso/${idCurso}/materia/${idMateria}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );

      if (!response.ok) throw new Error("Error al eliminar la materia");

      // Recargar las materias del curso después de eliminar
      fetchMateriasCurso();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  // Filtrar las materias disponibles para que no se muestren las ya asignadas
  const materiasDisponiblesFiltradas = allMaterias.filter(
    (materia) => !materiasExistentes.some((materiaExistente) => materiaExistente.idMateria === materia.id)
  );

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50 p-6">
        <div className="max-w-7xl mx-auto">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <div>
              <button
                onClick={() => navigate(-1)}
                className="flex items-center text-gray-600 hover:text-gray-800 mb-4 md:mb-0"
              >
                <svg
                  className="w-5 h-5 mr-1"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M10 19l-7-7m0 0l7-7m-7 7h18"
                  />
                </svg>
                Volver
              </button>
              <h1 className="text-2xl md:text-3xl font-bold text-gray-800">
                Materias del Curso
              </h1>
            </div>
            <button
              onClick={() => setShowCreateModal(true)}
              className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 mt-4 md:mt-0"
            >
              Agregar Materia
            </button>
          </div>

          {isLoading ? (
            <div className="flex justify-center items-center h-64">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
            </div>
          ) : error ? (
            <div className="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <svg
                    className="h-5 w-5 text-red-500"
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
          ) : materiasExistentes.length === 0 ? (
            <div className="text-center py-12 bg-white rounded-lg shadow">
              <svg
                className="mx-auto h-12 w-12 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
              <h3 className="mt-2 text-lg font-medium text-gray-900">
                No hay materias asignadas a este curso
              </h3>
            </div>
          ) : (
            <div className="bg-white shadow rounded-lg overflow-hidden">
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        ID Materia
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Nombre de la Materia
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Acciones
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {materiasExistentes.map((materia) => {
                      // Encuentra el nombre de la materia con el idMateria
                      const materiaNombre = allMaterias.find(
                        (m) => m.id === materia.idMateria
                      )?.nombreMateria;
                      return (
                        <tr key={materia.idMateria} className="hover:bg-gray-50">
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {materia.idMateria}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            {materiaNombre || "Materia no encontrada"}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                            <button
                              onClick={() => handleDeleteMateria(materia.idMateria)}
                              className="text-red-600 hover:text-red-900"
                            >
                              Eliminar
                            </button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Modal para agregar materia */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-amber-100 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">Agregar Materia</h2>
              <button
                onClick={() => setShowCreateModal(false)}
                className="text-gray-500 hover:text-gray-700"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <div className="mb-4">
              <label htmlFor="materiaSeleccionada" className="block text-sm font-medium text-gray-700 mb-1">
                Seleccionar Materia
              </label>
              <select
                id="materiaSeleccionada"
                value={materiaSeleccionada}
                onChange={(e) => setMateriaSeleccionada(e.target.value)}
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 w-full"
              >
                <option value="">Seleccione una materia</option>
                {materiasDisponiblesFiltradas.map((materia) => (
                  <option key={materia.id} value={materia.id}>
                    {materia.nombreMateria}
                  </option>
                ))}
              </select>
            </div>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => setShowCreateModal(false)}
                className="px-4 py-2 text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                disabled={isLoading}
              >
                Cancelar
              </button>
              <button
                onClick={handleAddMateria}
                className="px-4 py-2 text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                disabled={isLoading || !materiaSeleccionada}
              >
                {isLoading ? "Agregando..." : "Agregar Materia"}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default MateriasDelCurso;
