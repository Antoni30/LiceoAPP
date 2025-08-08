import Navbar from "../../components/Nabvar";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import apiService from "../../services/apiService";

export default function Participantes() {
  const { idCurso } = useParams();
  const navigate = useNavigate();
  const [participantes, setParticipantes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [usuariosDisponibles, setUsuariosDisponibles] = useState([]);
  const [showAddProfessor, setShowAddProfessor] = useState(false);
  const [selectedUsuario, setSelectedUsuario] = useState("");
  const [addingProfessor, setAddingProfessor] = useState(false);
  const [estudiantesDisponible, setEstudianteDisponibles] = useState([]);
  const [addingParticipant, setAddingParticipant] = useState(false);
  const [addError, setAddError] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [participantToDelete, setParticipantToDelete] = useState(null);
  const [showAddStudent, setShowAddStudent] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState("");

  const handleEdit = (idUsuario) => {
    navigate(`/usuario/editar/${idUsuario}`);
  };

  // Función para cargar usuarios disponibles
  const fetchUsuariosDisponibles = async () => {
    try {
      const usuarios = await apiService.getUsers();

      // Filtrar solo usuarios que son profesores
      const profesores = [];
      for (const usuario of usuarios) {
        const esProf = await esProfesor(usuario.idUsuario);
        if (esProf) {
          profesores.push(usuario);
        }
      }

      // Verificar disponibilidad de cada profesor
      const disponibles = [];
      for (const profesor of profesores) {
        const estaDisponible = await verificarDisponibilidad(
          profesor.idUsuario
        );
        if (estaDisponible) {
          disponibles.push(profesor);
        }
      }

      setUsuariosDisponibles(disponibles); // Solo los disponibles van al select
    } catch (err) {
      console.error("Error cargando usuarios:", err);
    }
  };

  const fetchEstudianteDisponibles = async () => {
    try {
      const usuarios = await apiService.getUsers();

      // Filtrar solo usuarios que son estudiantes
      const estudiantes = [];
      for (const usuario of usuarios) {
        const esEst = await esEstudiante(usuario.idUsuario);
        if (esEst) {
          estudiantes.push(usuario);
        }
      }

      // Verificar disponibilidad de cada estudiante
      const disponibles = [];
      for (const estudiante of estudiantes) {
        const estaDisponible = await verificarDisponibilidad(
          estudiante.idUsuario
        );
        if (estaDisponible) {
          disponibles.push(estudiante);
        }
      }

      setEstudianteDisponibles(disponibles); // Solo los disponibles van al select
    } catch (err) {
      console.error("Error cargando usuarios:", err);
    }
  };

  // Verifica si un usuario tiene el rol de profesor (idRol = 2)
  const esProfesor = async (idUsuario) => {
    try {
      const roles = await apiService.getUserRoles(idUsuario);
      return roles.some((r) => r.idRol === 2);
    } catch (err) {
      console.error(`Error verificando roles del usuario ${idUsuario}:`, err);
      return false;
    }
  };

  // Verifica si un usuario tiene el rol de estudiante (idRol = 3)
  const esEstudiante = async (idUsuario) => {
    try {
      const roles = await apiService.getUserRoles(idUsuario);
      return roles.some((r) => r.idRol === 3);
    } catch (err) {
      console.error(`Error verificando roles del usuario ${idUsuario}:`, err);
      return false;
    }
  };

  // Verifica si el usuario no está asignado a ningún curso
  const verificarDisponibilidad = async (idUsuario) => {
    try {
      const cursosAsignados = await apiService.getUserCursos(idUsuario);
      return cursosAsignados.length === 0; // true si no tiene cursos
    } catch (err) {
      console.error(
        `Error verificando disponibilidad de usuario ${idUsuario}:`,
        err
      );
      return false; // en caso de error, asumir que no está disponible
    }
  };

  // Función para agregar profesor al curso
  const agregarProfesorAlCurso = async () => {
    if (!selectedUsuario) return;

    setAddingProfessor(true);
    setAddError(null);

    try {
      // 1. Verificar si el usuario es profesor
      const isProfessor = await esProfesor(selectedUsuario);
      if (!isProfessor) {
        throw new Error("El usuario seleccionado no es un profesor");
      }

      // 2. Asignar al curso
      await apiService.createUsuarioCurso(selectedUsuario, idCurso);

      // Recargar la lista de participantes
      await fetchParticipantes();
      setShowAddProfessor(false);
      setSelectedUsuario("");
    } catch (err) {
      setAddError(err.message);
    } finally {
      setAddingProfessor(false);
    }
  };

  // Función para agregar estudiante al curso
  const agregarEstudianteAlCurso = async () => {
    if (!selectedStudent) return;

    setAddingParticipant(true);
    setAddError(null);

    try {
      // 1. Verificar si el usuario es estudiante
      const isStudent = await esEstudiante(selectedStudent);
      if (!isStudent) {
        throw new Error("El usuario seleccionado no es un estudiante");
      }

      // 2. Asignar al curso
      await apiService.createUsuarioCurso(selectedStudent, idCurso);

      // Recargar la lista de participantes
      await fetchParticipantes();
      setShowAddStudent(false);
      setSelectedStudent("");
    } catch (err) {
      setAddError(err.message);
    } finally {
      setAddingParticipant(false);
    }
  };

  const fetchParticipantes = async () => {
    try {
      setLoading(true);
      setError(null);

      // Paso 1: Obtener los IDs de usuarios del curso
      const usuariosCursos = await apiService.getCursoUsuarios(idCurso);

      // Paso 2: Para cada usuario, obtener sus datos y rol
      const participantesData = await Promise.all(
        usuariosCursos.map(async (usuarioCurso) => {
          // Obtener información del usuario
          const userData = await apiService.getUser(usuarioCurso.idUsuario);

          // Obtener rol del usuario
          const rolData = await apiService.getUserRoles(usuarioCurso.idUsuario);

          let nombreRol = "Sin rol asignado";

          if (rolData.length > 0) {
            try {
              const nombreRolData = await apiService.getRole(rolData[0].idRol);
              nombreRol = nombreRolData.nombre || `Rol ID: ${rolData[0].idRol}`;
            } catch (roleErr) {
              nombreRol = `Rol ID: ${rolData[0].idRol}`;
            }
          }

          return {
            idUsuario: usuarioCurso.idUsuario,
            nombres: userData.nombres,
            apellidos: userData.apellidos,
            rol: nombreRol,
          };
        })
      );

      setParticipantes(participantesData);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const eliminarParticipante = async () => {
    if (!participantToDelete) return;

    setAddingParticipant(true);
    try {
      await apiService.deleteUsuarioCurso(participantToDelete.idUsuario, idCurso);
      // Recargar la lista de participantes
      await fetchParticipantes();
      closeDeleteModal();
    } catch (err) {
      setError(err.message);
    } finally {
      setAddingParticipant(false);
    }
  };

  const closeDeleteModal = () => {
    setParticipantToDelete(null);
    setShowDeleteModal(false);
  };

  const openDeleteModal = (participante) => {
    setParticipantToDelete(participante);
    setShowDeleteModal(true);
  };

  useEffect(() => {
    fetchParticipantes();
    fetchUsuariosDisponibles();
    fetchEstudianteDisponibles();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [idCurso]);
  if (loading) {
    return (
      <>
        <Navbar />
        <div className="flex justify-center items-center h-screen bg-gray-50">
          <div className="flex flex-col items-center">
            <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-indigo-500 mb-4"></div>
            <p className="text-gray-600">Cargando participantes...</p>
          </div>
        </div>
      </>
    );
  }

  if (error) {
    return (
      <>
        <Navbar />
        <div className="bg-red-50 border-l-4 border-red-500 p-4 mx-auto max-w-4xl mt-8">
          <div className="flex">
            <div className="flex-shrink-0">
              <svg
                className="h-5 w-5 text-red-500"
                xmlns="http://www.w3.org/2000/svg"
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
              <h3 className="text-sm font-medium text-red-800">
                Error al cargar participantes
              </h3>
              <div className="mt-2 text-sm text-red-700">
                <p>{error}</p>
              </div>
            </div>
          </div>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="bg-white shadow-md rounded-lg overflow-hidden">
            <div className="px-6 py-4 border-b border-gray-200 bg-gradient-to-r from-indigo-50 to-blue-50">
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
              <div className="flex justify-between items-center">
                <div>
                  <h1 className="text-2xl font-semibold text-gray-800">
                    Participantes del Curso
                  </h1>
                  <p className="mt-1 text-sm text-gray-600">
                    Lista de todos los participantes inscritos en este curso
                  </p>
                </div>
                <div className="space-x-2">
                  {participantes.length <= 0 ? null: (
                    <button
                      onClick={() => setShowAddStudent(true)}
                      className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                    >
                      Asignar Estudiante
                    </button>
                  )}
                </div>
              </div>
            </div>

            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-100">
                  <tr>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider"
                    >
                      ID Usuario
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider"
                    >
                      Nombres
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider"
                    >
                      Apellidos
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider"
                    >
                      Rol
                    </th>
                    <th
                      scope="col"
                      className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider"
                    >
                      Acciones
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {participantes.length > 0 ? (
                    participantes.map((participante, index) => (
                      <tr
                        key={participante.idUsuario}
                        className={
                          index % 2 === 0
                            ? "bg-white"
                            : "bg-gray-50 hover:bg-gray-50"
                        }
                      >
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {participante.idUsuario}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                          {participante.nombres}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                          {participante.apellidos}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span
                            className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full 
                            ${
                              participante.rol
                                .toLowerCase()
                                .includes("estudiante")
                                ? "bg-green-100 text-green-800"
                                : participante.rol
                                    .toLowerCase()
                                    .includes("profesor")
                                ? "bg-blue-100 text-blue-800"
                                : participante.rol
                                    .toLowerCase()
                                    .includes("admin")
                                ? "bg-purple-100 text-purple-800"
                                : "bg-gray-100 text-gray-800"
                            }`}
                          >
                            {participante.rol}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                          <button
                            onClick={() => handleEdit(participante.idUsuario)}
                            className="text-indigo-600 hover:text-indigo-900"
                            title="Editar"
                          >
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              className="h-5 w-5"
                              viewBox="0 0 20 20"
                              fill="currentColor"
                            >
                              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                            </svg>
                          </button>
                          <button
                            onClick={() => openDeleteModal(participante)}
                            className="text-red-600 hover:text-red-900"
                            title="Eliminar del curso"
                          >
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              className="h-5 w-5"
                              viewBox="0 0 20 20"
                              fill="currentColor"
                            >
                              <path
                                fillRule="evenodd"
                                d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                clipRule="evenodd"
                              />
                            </svg>
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td
                        colSpan="5"
                        className="px-6 py-4 text-center text-sm text-gray-500"
                      >
                        <div className="flex flex-col items-center justify-center py-8">
                          <svg
                            className="h-12 w-12 text-gray-400"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth={2}
                              d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                            />
                          </svg>
                          <h3 className="mt-2 text-sm font-medium text-gray-700">
                            No hay participantes
                          </h3>
                          <p className="mt-1 text-sm text-gray-500">
                            No se encontraron participantes inscritos en este
                            curso.
                          </p>
                          <div className="space-x-2 mt-4">
                            <button
                              onClick={() => setShowAddProfessor(true)}
                              className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg"
                            >
                              Asignar Profesor
                            </button>
                          </div>
                        </div>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>

            {participantes.length > 0 && (
              <div className="px-6 py-3 bg-gray-50 border-t border-gray-200 flex items-center justify-between">
                <div className="text-sm text-gray-500">
                  Mostrando{" "}
                  <span className="font-medium">{participantes.length}</span>{" "}
                  participantes
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Modal para agregar profesor */}
      {showAddProfessor && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-medium text-gray-900">
                Agregar Profesor al Curso
              </h3>
              <button
                onClick={() => {
                  setShowAddProfessor(false);
                  setAddError(null);
                }}
                className="text-gray-400 hover:text-gray-500"
              >
                <svg
                  className="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
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

            {addError && (
              <div className="mb-4 p-2 bg-red-100 text-red-700 rounded text-sm">
                {addError}
              </div>
            )}

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Seleccionar Profesor
                </label>
                <select
                  value={selectedUsuario}
                  onChange={(e) => setSelectedUsuario(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  disabled={addingProfessor}
                >
                  <option value="">Seleccione un profesor</option>
                  {usuariosDisponibles.map((usuario) => (
                    <option key={usuario.idUsuario} value={usuario.idUsuario}>
                      {usuario.nombres} {usuario.apellidos} (ID:{" "}
                      {usuario.idUsuario})
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="mt-6 flex justify-end space-x-3">
              <button
                type="button"
                onClick={() => {
                  setShowAddProfessor(false);
                  setAddError(null);
                }}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                disabled={addingProfessor}
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={agregarProfesorAlCurso}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
                disabled={!selectedUsuario || addingProfessor}
              >
                {addingProfessor ? (
                  <>
                    <svg
                      className="animate-spin -ml-1 mr-2 h-4 w-4 text-white inline"
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
                  "Agregar Profesor"
                )}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal para agregar estudiante */}
      {showAddStudent && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-medium text-gray-900">
                Agregar Estudiante al Curso
              </h3>
              <button
                onClick={() => {
                  setShowAddStudent(false);
                  setAddError(null);
                }}
                className="text-gray-400 hover:text-gray-500"
              >
                <svg
                  className="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
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

            {addError && (
              <div className="mb-4 p-2 bg-red-100 text-red-700 rounded text-sm">
                {addError}
              </div>
            )}

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Seleccionar Estudiante
                </label>
                <select
                  value={selectedStudent}
                  onChange={(e) => setSelectedStudent(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500"
                  disabled={addingParticipant}
                >
                  <option value="">Seleccione un estudiante</option>
                  {estudiantesDisponible.map((usuario) => (
                    <option key={usuario.idUsuario} value={usuario.idUsuario}>
                      {usuario.nombres} {usuario.apellidos} (ID:{" "}
                      {usuario.idUsuario})
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="mt-6 flex justify-end space-x-3">
              <button
                type="button"
                onClick={() => {
                  setShowAddStudent(false);
                  setAddError(null);
                }}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                disabled={addingParticipant}
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={agregarEstudianteAlCurso}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50"
                disabled={!selectedStudent || addingParticipant}
              >
                {addingParticipant ? (
                  <>
                    <svg
                      className="animate-spin -ml-1 mr-2 h-4 w-4 text-white inline"
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
                  "Agregar Estudiante"
                )}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Modal para confirmar eliminación */}
      {showDeleteModal && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-medium text-gray-900">
                Confirmar Eliminación
              </h3>
              <button
                onClick={closeDeleteModal}
                className="text-gray-400 hover:text-gray-500"
                disabled={addingParticipant}
              >
                <svg
                  className="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
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
                ¿Estás seguro de que deseas eliminar a{" "}
                <span className="font-semibold">
                  {participantToDelete?.nombres}{" "}
                  {participantToDelete?.apellidos}
                </span>{" "}
                del curso?
              </p>
            </div>

            <div className="flex justify-end space-x-3">
              <button
                onClick={closeDeleteModal}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                disabled={addingParticipant}
              >
                Cancelar
              </button>
              <button
                onClick={eliminarParticipante}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 disabled:opacity-50"
                disabled={addingParticipant}
              >
                {addingParticipant ? (
                  <>
                    <svg
                      className="animate-spin -ml-1 mr-2 h-4 w-4 text-white inline"
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
                    Eliminando...
                  </>
                ) : (
                  "Eliminar del Curso"
                )}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
