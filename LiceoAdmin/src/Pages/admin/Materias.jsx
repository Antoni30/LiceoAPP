import { useState, useEffect } from "react";
import Navbar from "../../components/Nabvar";
import { useApi } from "../../hooks/useApi";
import apiService from "../../services/apiService";
import { ErrorMessage, Button, Modal, Table } from "../../components/UI";

function Materias() {
  const [materias, setMaterias] = useState([]);
  const [message, setMessage] = useState("");
  const [editMateria, setEditMateria] = useState(null);
  const [nombreMateriaEdit, setNombreMateriaEdit] = useState("");
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [materiaToDelete, setMateriaToDelete] = useState(null);
  const [newMateria, setNewMateria] = useState("");
  const { loading: isLoading, error, executeRequest, clearError } = useApi();

  useEffect(() => {
    setMessage("");
    fetchMaterias();
  }, []);

  const fetchMaterias = async () => {
    setMessage("");
    try {
      await executeRequest(async () => {
        const data = await apiService.getMaterias();
        setMaterias(data);
      });
    } catch (err) {
      console.error('Error fetching materias:', err);
    }
  };

  const handleEditMateria = (materia) => {
    setMessage("");
    setEditMateria(materia);
    setNombreMateriaEdit(materia.nombreMateria);
  };

  const handleSaveEdit = async () => {
    setMessage("");
    setIsLoading(true);
    try {
      const updatedMateria = { nombreMateria: nombreMateriaEdit };

      const response = await fetch(
        `http://localhost:8080/api/materias/${editMateria.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify(updatedMateria),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Error al guardar los cambios");
      }

      fetchMaterias();
      setEditMateria(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setMessage("");
    setEditMateria(null);
  };

  const confirmDelete = (materia) => {
    setMessage("");
    setMateriaToDelete(materia);
    setShowDeleteModal(true);
  };

  const handleDeleteMateria = async () => {
    setMessage("");
    if (!materiaToDelete) return;

    setIsLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/materias/${materiaToDelete.id}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );

      if (!response.ok) {
        throw new Error("Materia asignada a uno o varios cursos");
      }

      fetchMaterias();
      setShowDeleteModal(false);
      setMateriaToDelete(null);
    } catch (err) {
      setMessage(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreateMateria = async () => {
    setMessage("");
    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/materias", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ nombreMateria: newMateria }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Error al crear la materia");
      }

      fetchMaterias();
      setShowCreateModal(false);
      setNewMateria("");
    } catch (err) {
      setMessage(err.message);
      setMessage("");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50 p-6">
        <div className="max-w-7xl mx-auto">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <h1 className="text-2xl md:text-3xl font-bold text-gray-800 mb-4 md:mb-0">
              Gestión de Materias
            </h1>
            <Button 
              onClick={() => setShowCreateModal(true)}
              icon={
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                </svg>
              }
            >
              Nueva Materia
            </Button>
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
                        Nombre de Materia
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
                    {materias.map((materia) => (
                      <tr key={materia.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {materia.id}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {editMateria?.id === materia.id ? (
                            <input
                              type="text"
                              value={nombreMateriaEdit}
                              onChange={(e) =>
                                setNombreMateriaEdit(e.target.value)
                              }
                              className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 w-full max-w-md"
                              autoFocus
                            />
                          ) : (
                            materia.nombreMateria
                          )}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                          {editMateria?.id === materia.id ? (
                            <div className="space-x-2">
                              <button
                                onClick={handleSaveEdit}
                                className="text-indigo-600 hover:text-indigo-900 bg-indigo-50 px-3 py-1 rounded-md"
                              >
                                Guardar
                              </button>
                              <button
                                onClick={handleCancelEdit}
                                className="text-gray-600 hover:text-gray-900 bg-gray-50 px-3 py-1 rounded-md"
                              >
                                Cancelar
                              </button>
                            </div>
                          ) : (
                            <div className="space-x-2">
                              <button
                                onClick={() => handleEditMateria(materia)}
                                className="text-indigo-600 hover:text-indigo-900"
                              >
                                Editar
                              </button>
                              <button
                                onClick={() => confirmDelete(materia)}
                                className="text-red-600 hover:text-red-900"
                              >
                                Eliminar
                              </button>
                            </div>
                          )}
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

      {/* Modal para crear nueva materia */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-amber-50 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">Nueva Materia</h2>
              <button
                onClick={() => {
                  setShowCreateModal(false);
                  setMessage("");
                }}
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
                htmlFor="newMateria"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Nombre de la materia
              </label>
              <input
                id="newMateria"
                type="text"
                value={newMateria}
                onChange={(e) => {
                  setNewMateria(e.target.value);
                  setMessage("");
                }}
                className="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 w-full"
                autoFocus
              />
            </div>
            {message && (
              <div className="mb-4 bg-red-50 border-l-4 border-red-500 p-3 rounded">
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
                    <p className="text-sm text-red-700">{message}</p>
                  </div>
                </div>
              </div>
            )}
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => {
                  setShowCreateModal(false);
                  setMessage("");
                }}
                className="px-4 py-2 text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                disabled={isLoading}
              >
                Cancelar
              </button>
              <button
                onClick={handleCreateMateria}
                className="px-4 py-2 text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                disabled={isLoading || !newMateria.trim()}
              >
                {isLoading ? "Guardando..." : "Guardar"}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal para confirmar eliminación */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-amber-50 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">
                Confirmar Eliminación
              </h2>
              <button
                onClick={() => {
                  setShowDeleteModal(false);
                  setMessage("");
                }}
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
            <div className="mb-6">
              <p className="text-gray-700">
                ¿Estás seguro que deseas eliminar la materia{" "}
                <span className="font-semibold">
                  "{materiaToDelete?.nombreMateria}"
                </span>
                ?
              </p>
              <p className="text-sm text-red-600 mt-2">
                Esta acción no se puede deshacer.
              </p>
            </div>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => {
                  setShowDeleteModal(false);
                  setMessage("");
                }}
                className="px-4 py-2 text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                disabled={isLoading}
              >
                Cancelar
              </button>
              <button
                onClick={handleDeleteMateria}
                className="px-4 py-2 text-white bg-red-600 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                disabled={isLoading}
              >
                {isLoading ? "Eliminando..." : "Eliminar"}
              </button>
            </div>
              {message && (
              <div className="mb-4 bg-red-50 border-l-4 border-red-500 p-3 rounded my-4">
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
                    <p className="text-sm text-red-700">{message}</p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </>
  );
}

export default Materias;
