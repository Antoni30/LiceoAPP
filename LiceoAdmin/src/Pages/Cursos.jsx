/* eslint-disable react-hooks/exhaustive-deps */
import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Navbar from "../components/Nabvar";

function CursosPorAnio() {
  const { idanio } = useParams();
  const navigate = useNavigate();
  const [cursos, setCursos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [newCurso, setNewCurso] = useState({
    nombreCurso: "",
    idAnioLectivo: idanio,
  });
  const [editCurso, setEditCurso] = useState(null);
  const [nombreCursoEdit, setNombreCursoEdit] = useState("");

  useEffect(() => {
    fetchCursos();
  }, [idanio]);

  const fetchCursos = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/cursos/por-anio/${idanio}`,
        {
          credentials: "include",
        }
      );

      if (!response.ok) throw new Error("Error al cargar los cursos");

      const data = await response.json();
      setCursos(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreateCurso = async () => {
    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/cursos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(newCurso),
      });

      if (!response.ok) throw new Error("Error al crear el curso");

      fetchCursos();
      setShowCreateModal(false);
      setNewCurso({ ...newCurso, nombreCurso: "" });
    } catch (err) {
      if (err.message === "Error al crear el curso") {
        setErrorMessage("Ya existe el curso"); // Si el error es por duplicado, se muestra este mensaje
      } else {
        setErrorMessage(err.message);
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteCurso = async (id) => {
    if (!window.confirm("¿Estás seguro de que deseas eliminar este curso?"))
      return;

    setIsLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/cursos/${id}`, {
        method: "DELETE",
        credentials: "include",
      });

      if (!response.ok) throw new Error("Error al eliminar el curso");

      fetchCursos();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEditCurso = (curso) => {
    setEditCurso(curso);
    setNombreCursoEdit(curso.nombreCurso);
  };

  const handleSaveEdit = async () => {
    setIsLoading(true);
    try {
      const updatedCurso = {
        idAnioLectivo: editCurso.id,
        nombreCurso: nombreCursoEdit,
      };

      const response = await fetch(
        `http://localhost:8080/api/cursos/${editCurso.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify(updatedCurso),
        }
      );

      if (!response.ok) throw new Error("Error al guardar los cambios");

      fetchCursos();
      setEditCurso(null);
    } catch (err) {
      if (err.message === "Error al guardar los cambios") {
        setErrorMessage("Ya existe el curso"); // Si el error es por duplicado, se muestra este mensaje
      } else {
        setErrorMessage(err.message);
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setEditCurso(null);
  };

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
                Cursos del Año Lectivo
              </h1>
            </div>
            <button
              onClick={() => setShowCreateModal(true)}
              className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 flex items-center mt-4 md:mt-0"
            >
              <svg
                className="w-5 h-5 mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M12 6v6m0 0v6m0-6h6m-6 0H6"
                />
              </svg>
              Nuevo Curso
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
          ) : cursos.length === 0 ? (
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
                No hay cursos registrados
              </h3>
              <p className="mt-1 text-sm text-gray-500">
                Comienza agregando un nuevo curso
              </p>
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
                        ID
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Nombre del Curso
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Acciones
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {cursos.map((curso) => (
                      <tr key={curso.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {curso.id}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {editCurso?.id === curso.id ? (
                            <input
                              type="text"
                              value={nombreCursoEdit}
                              onChange={(e) =>
                                setNombreCursoEdit(e.target.value)
                              }
                              className="border px-2 py-1 rounded-md"
                            />
                          ) : (
                            curso.nombreCurso
                          )}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                          <div className="space-x-2">
                            {editCurso?.id === curso.id ? (
                              <>
                                <button
                                  onClick={handleSaveEdit}
                                  className="text-indigo-600 hover:text-indigo-900"
                                >
                                  Guardar
                                </button>
                                <button
                                  onClick={handleCancelEdit}
                                  className="text-gray-600 hover:text-gray-800"
                                >
                                  Cancelar
                                </button>
                              </>
                            ) : (
                              <>
                                <button
                                  onClick={() => handleEditCurso(curso)}
                                  className="text-indigo-600 hover:text-indigo-900"
                                >
                                  Editar
                                </button>
                                <button
                                  onClick={() => handleDeleteCurso(curso.id)}
                                  className="text-red-600 hover:text-red-900"
                                >
                                  Eliminar
                                </button>
                                <button
                                  onClick={() =>
                                    navigate(`/cursos/materia/${curso.id}`)
                                  }
                                  className="text-green-600 hover:text-green-900"
                                >
                                  Materias
                                </button>

                                <button
                                  onClick={() =>
                                    navigate(`/participantes/${curso.id}`)
                                  }
                                  className="text-amber-600 hover:text-amber-900"
                                >
                                  Participantes
                                </button>
                              </>
                            )}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Modal para crear nuevo curso */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-amber-100 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">Nuevo Curso</h2>
              <button
                onClick={() => setShowCreateModal(false)}
                className="text-gray-500 hover:text-gray-700"
              >
                <svg
                  className="w-6 h-6"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
            <div className="mb-4">
              <label
                htmlFor="nombreCurso"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Nombre del Curso
              </label>
              <input
                id="nombreCurso"
                type="text"
                value={newCurso.nombreCurso}
                onChange={(e) =>
                  setNewCurso({ ...newCurso, nombreCurso: e.target.value })
                }
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 w-full"
                placeholder="Ej: 8vo A"
                autoFocus
              />
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
                onClick={handleCreateCurso}
                className="px-4 py-2 text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                disabled={isLoading || !newCurso.nombreCurso.trim()}
              >
                {isLoading ? "Creando..." : "Crear Curso"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal de Error */}
      {errorMessage && (
        <div className="fixed inset-0 bg-amber-100 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">Error</h2>
              <button
                onClick={() => setErrorMessage(null)}
                className="text-gray-500 hover:text-gray-700"
              >
                <svg
                  className="w-6 h-6"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
            <p className="text-sm text-gray-700">{errorMessage}</p>
            <div className="flex justify-end mt-4">
              <button
                onClick={() => setErrorMessage(null)}
                className="px-4 py-2 text-white bg-indigo-600 rounded-md hover:bg-indigo-700"
              >
                Aceptar
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default CursosPorAnio;
