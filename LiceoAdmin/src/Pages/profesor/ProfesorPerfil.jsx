/* eslint-disable no-unused-vars */
/* eslint-disable react-hooks/exhaustive-deps */
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import NavbarProfesor from "../../components/NabvarProfesor";
import LoadingSpinner from "../../components/LoadingSpinner";
import { useAuth } from "../../hooks/AuthProvider";
import apiService from "../../services/apiService";

export default function ProfesorPerfil() {
  const { idProfesor } = useParams();
  const [profesor, setProfesor] = useState(null);
  const [estudiantes, setEstudiantes] = useState([]);
  const [idCurso, setIdCurso] = useState("");
  const [nombreCurso, setNombreCurso] = useState("");
  const [cargando, setCargando] = useState(true);
  const [message, setMessage] = useState("");
  const [existente, setExistente] = useState("");
  const [error, setError] = useState("");
  const [showFormEstudiante, setShowFormEstudiante] = useState(false);
  const { logout } = useAuth();
  const [guardando, setGuardando] = useState(false);
  const [promediosParciales, setPromediosParciales] = useState({ 1: "S/N", 2: "S/N", 3: "S/N" });
  const [promedioGeneral, setPromedioGeneral] = useState("S/N");
  const navigator = useNavigate()
  const [nuevoEstudiante, setNuevoEstudiante] = useState({
    idUsuario: "",
    nombres: "",
    apellidos: "",
    nickname: "",
    contrasena: "",
    estado: "ACTIVO",
    email: "",
  });

  const handleLogout = () => {
    apiService.logout()
      .finally(() => {
        logout();
      });
  };

  const fetchData = async () => {
    try {
      const profData = await apiService.getUser(idProfesor);
      setProfesor(profData);

      const cursos = await apiService.getUserCursos(idProfesor);
      if (!cursos.length)
        throw new Error(
          "Profesor disponible (sin cursos asignados) - Requiere acción administrativa"
        );
      const cursoId = cursos[0].idCurso;
      setIdCurso(cursoId);

      const nombreCursoRespond = await apiService.getCurso(cursoId);
      setNombreCurso(nombreCursoRespond.nombreCurso);

      const usuCursos = await apiService.getCursoUsuarios(cursoId);

      const estudiantesFiltrados = [];
      const notasPorParcial = { 1: [], 2: [], 3: [] };
      
      // Obtener materias del curso para calcular promedios correctamente
      const materiasDelCurso = await apiService.getCursoMaterias(cursoId);
      
      for (const { idUsuario } of usuCursos) {
        try {
          const roles = await apiService.getUserRoles(idUsuario);
        const tieneRolEstudiante = roles.some((r) => r.idRol === 3);
        if (!tieneRolEstudiante) continue;

          const usuario = await apiService.getUser(idUsuario);

          // Obtener todas las notas del estudiante por materia y calcular promedios por parcial
          const notasParciales = { 1: "S/N", 2: "S/N", 3: "S/N" };
          
          for (let parcial = 1; parcial <= 3; parcial++) {
            const notasDelParcial = [];
            
            for (const materia of materiasDelCurso) {
              try {
                const notasMateria = await apiService.getNotasByUsuarioMateria(idUsuario, materia.idMateria);
                if (notasMateria && Array.isArray(notasMateria)) {
                  const notaParcial = notasMateria.find(n => n.parcial === parcial);
                  if (notaParcial) {
                    notasDelParcial.push(parseFloat(notaParcial.nota));
                  } else {
                    notasDelParcial.push(0); // S/N como 0
                  }
                } else {
                  notasDelParcial.push(0); // S/N como 0
                }
              } catch (err) {
                notasDelParcial.push(0); // S/N como 0
              }
            }
            
            // Calcular promedio del parcial
            if (notasDelParcial.length > 0) {
              const suma = notasDelParcial.reduce((total, nota) => total + nota, 0);
              const promedio = suma / notasDelParcial.length;
              notasParciales[parcial] = promedio.toFixed(2);
              notasPorParcial[parcial].push(promedio);
            } else {
              notasPorParcial[parcial].push(0);
            }
          }

          // Calcular promedio general del estudiante (promedio de los 3 parciales)
          const notasConvertidas = [notasParciales[1], notasParciales[2], notasParciales[3]]
            .map(n => n === "S/N" ? 0 : parseFloat(n));
          const notasValidas = notasConvertidas.filter(n => !isNaN(n));
          
          let promedioEstudiante = "S/N";
          if (notasValidas.length > 0) {
            const suma = notasValidas.reduce((total, nota) => total + nota, 0);
            promedioEstudiante = (suma / notasValidas.length).toFixed(2);
          }

          estudiantesFiltrados.push({
            idUsuario,
            nombres: usuario.nombres,
            apellidos: usuario.apellidos,
            nota1: notasParciales[1],
            nota2: notasParciales[2],
            nota3: notasParciales[3],
            promedio: promedioEstudiante,
          });
        } catch (userErr) {
          // Error obteniendo datos del usuario, continuar
          continue;
        }
      }

      // Calcular promedios por parcial del curso
      const promediosParcialCalculados = {};
      for (let parcial = 1; parcial <= 3; parcial++) {
        const notasDelParcial = notasPorParcial[parcial].filter(n => !isNaN(n));
        if (notasDelParcial.length > 0) {
          const suma = notasDelParcial.reduce((total, nota) => total + nota, 0);
          promediosParcialCalculados[parcial] = (suma / notasDelParcial.length).toFixed(2);
        } else {
          promediosParcialCalculados[parcial] = "S/N";
        }
      }

      // Calcular promedio general del curso
      const promediosEstudiantes = estudiantesFiltrados
        .map(e => e.promedio === "S/N" ? 0 : parseFloat(e.promedio))
        .filter(p => !isNaN(p));
      
      let promedioGeneralCalculado = "S/N";
      if (promediosEstudiantes.length > 0) {
        const suma = promediosEstudiantes.reduce((total, promedio) => total + promedio, 0);
        promedioGeneralCalculado = (suma / promediosEstudiantes.length).toFixed(2);
      }

      setEstudiantes(estudiantesFiltrados);
      setPromediosParciales(promediosParcialCalculados);
      setPromedioGeneral(promedioGeneralCalculado);
    } catch (err) {
      console.error(err);
      setError(err.message);
    } finally {
      setCargando(false);
    }
  };
  useEffect(() => {
    setMessage("");
    setExistente("");
    fetchData();
  }, [idCurso]);

  const handleChange = (e) => {
    setMessage("");
    setExistente("");
    setNuevoEstudiante((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleCrearEstudiante = async () => {
    setMessage("");
    setExistente("");
    setGuardando(true);
    try {
      // 1. Crear usuario
      await apiService.createUser(nuevoEstudiante);

      // 2. Asignar rol estudiante (idRol = 3)
      await apiService.assignUserRole(nuevoEstudiante.idUsuario, 3);

      // 3. Asignar a curso
      await apiService.createUsuarioCurso(nuevoEstudiante.idUsuario, idCurso);

      setShowFormEstudiante(false); // cerrar modal
      setNuevoEstudiante({
        idUsuario: "",
        nombres: "",
        apellidos: "",
        nickname: "",
        contrasena: "",
        estado: "ACTIVO",
        email: "",
      });
      await fetchData();
    } catch (err) {
      if (err.response && err.response.data) {
        const errData = err.response.data;
        if (typeof errData === "object" && !Array.isArray(errData)) {
          if (errData.message) {
            // Error general tipo "ya existe"
            setExistente(errData.message);
          } else {
            const messages = Object.values(errData).join("\n -");
            setMessage(messages);
          }
        } else {
          setExistente("Error desconocido al crear usuario");
        }
      } else {
        setExistente(err.message);
      }
    } finally {
      setGuardando(false); // <-- terminando
    }
  };

  if (cargando) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <LoadingSpinner text="Cargando perfil del profesor..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="bg-white p-6 rounded-lg shadow-md max-w-md w-full">
          <div className="text-red-500 font-medium text-lg mb-2">ESTADO</div>
          <p className="text-gray-700">{error}</p>
          <button
            onClick={() => window.location.reload()}
            className="mt-4 bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
          >
            Reintentar
          </button>
          <button
            onClick={handleLogout}
            className="mt-4 bg-red-700 text-white px-4 py-2 w-1/4 mx-1.5 rounded-md hover:bg-red-500"
          >
            Salir
          </button>
        </div>
      </div>
    );
  }

  return (
    <>
      <NavbarProfesor />
      <div className="min-h-screen bg-gray-50">
        {/* Header del profesor */}
        <div className="bg-indigo-700 text-white py-8">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex items-center space-x-4">
              <div className="h-16 w-16 rounded-full bg-indigo-600 border-4 border-white flex items-center justify-center text-2xl font-bold">
                {profesor.nombres.charAt(0)}
                {profesor.apellidos.charAt(0)}
              </div>
              <div>
                <h1 className="text-2xl font-bold">
                  {profesor.nombres} {profesor.apellidos}
                </h1>
                <p className="text-indigo-200">Profesor</p>
              </div>
            </div>
          </div>
        </div>

        {/* Contenido principal */}
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-semibold text-gray-800">
              Estudiantes del Curso {nombreCurso}
            </h2>
            <button
              onClick={() => setShowFormEstudiante(true)}
              className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg flex items-center"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5 mr-2"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fillRule="evenodd"
                  d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z"
                  clipRule="evenodd"
                />
              </svg>
              Agregar Estudiante
            </button>
          </div>

          {/* Sección de promedios del curso */}
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Promedios del Curso</h3>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              {/* Promedios por parcial */}
              {[1, 2, 3].map((parcial) => (
                <div key={parcial} className="text-center">
                  <div className="text-sm text-gray-500 mb-1">Parcial {parcial}</div>
                  <div className={`text-xl font-bold px-3 py-2 rounded-full ${
                    promediosParciales[parcial] === "S/N"
                      ? "bg-gray-100 text-gray-800"
                      : parseFloat(promediosParciales[parcial]) >= 14
                      ? "bg-green-100 text-green-800"
                      : "bg-red-100 text-red-800"
                  }`}>
                    {promediosParciales[parcial]}
                  </div>
                </div>
              ))}
              
              {/* Promedio general */}
              <div className="text-center">
                <div className="text-sm text-gray-500 mb-1">Promedio General</div>
                <div className={`text-xl font-bold px-3 py-2 rounded-full ${
                  promedioGeneral === "S/N"
                    ? "bg-gray-100 text-gray-800"
                    : parseFloat(promedioGeneral) >= 14
                    ? "bg-green-100 text-green-800"
                    : "bg-red-100 text-red-800"
                }`}>
                  {promedioGeneral}
                </div>
              </div>
            </div>
          </div>

          {estudiantes.length === 0 ? (
            <div className="bg-white rounded-lg shadow-sm p-8 text-center">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-12 w-12 mx-auto text-gray-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"
                />
              </svg>
              <h3 className="mt-4 text-lg font-medium text-gray-900">
                No hay estudiantes
              </h3>
              <p className="mt-1 text-gray-500">
                Aún no hay estudiantes asignados a este curso.
              </p>
              <button
                onClick={() => setShowFormEstudiante(true)}
                className="mt-4 bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
              >
                Agregar primer estudiante
              </button>
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
                        Estudiante
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Parcial 1
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Parcial 2
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Parcial 3
                      </th>
                      <th
                        scope="col"
                        className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        Promedio
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
                    {estudiantes.map((e) => (
                      <tr key={e.idUsuario} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center">
                            <div className="flex-shrink-0 h-10 w-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-medium">
                              {e.nombres.charAt(0)}
                              {e.apellidos.charAt(0)}
                            </div>
                            <div className="ml-4">
                              <div className="text-sm font-medium text-gray-900">
                                {e.nombres} {e.apellidos}
                              </div>
                              <div className="text-sm text-gray-500">
                                ID: {e.idUsuario}
                              </div>
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-center">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                            e.nota1 === "S/N" 
                              ? "bg-gray-100 text-gray-800"
                              : parseFloat(e.nota1) >= 14
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}>
                            {e.nota1}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-center">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                            e.nota2 === "S/N" 
                              ? "bg-gray-100 text-gray-800"
                              : parseFloat(e.nota2) >= 14
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}>
                            {e.nota2}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-center">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                            e.nota3 === "S/N" 
                              ? "bg-gray-100 text-gray-800"
                              : parseFloat(e.nota3) >= 14
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}>
                            {e.nota3}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-center">
                          <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                            e.promedio === "S/N" 
                              ? "bg-gray-100 text-gray-800"
                              : parseFloat(e.promedio) >= 14
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}>
                            {e.promedio}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <button
                            onClick={() => navigator(`/profesor/asignar-notas/${e.idUsuario}/${idCurso}`)}
                            className="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
                          >
                            <svg 
                              className="w-4 h-4 mr-2" 
                              fill="none" 
                              stroke="currentColor" 
                              viewBox="0 0 24 24"
                            >
                              <path 
                                strokeLinecap="round" 
                                strokeLinejoin="round" 
                                strokeWidth="2" 
                                d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                              />
                            </svg>
                            Ver Detalles
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </div>

        {/* Modal para agregar nuevo estudiante */}
        {showFormEstudiante && (
          <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">
                  Agregar Nuevo Estudiante
                </h3>
                <button
                  onClick={() => setShowFormEstudiante(false)}
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

              <div className="space-y-4">
                {Object.keys(nuevoEstudiante)
                  .filter((key) => key !== "estado")
                  .map((key) => (
                    <div key={key}>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        {key.charAt(0).toUpperCase() + key.slice(1) ===
                        "IdUsuario"
                          ? "Cedula"
                          : key.charAt(0).toUpperCase() + key.slice(1)}
                      </label>
                      <input
                        type={key === "contrasena" ? "password" : "text"}
                        name={key}
                        value={nuevoEstudiante[key]}
                        onChange={handleChange}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                        required={key !== "email" && key !== "nickname"}
                      />
                    </div>
                  ))}
              </div>

              <div className="mt-6 flex justify-end space-x-3">
                <button
                  type="button"
                  onClick={() => setShowFormEstudiante(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                  Cancelar
                </button>
                <button
                  type="button"
                  onClick={handleCrearEstudiante}
                  disabled={guardando}
                  className={`px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white ${
                    guardando
                      ? "bg-indigo-400"
                      : "bg-indigo-600 hover:bg-indigo-700"
                  } focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500`}
                >
                  {guardando ? "Guardando..." : "Guardar Estudiante"}
                </button>
              </div>
              {existente && (
                <div className="mt-4 p-3 rounded-md bg-yellow-50 text-yellow-700">
                  <p className="text-sm font-medium">{existente}</p>
                </div>
              )}

              {message && (
                <div className="mt-4 p-3 rounded-md bg-red-50 text-red-700">
                  {message.split("\n").map((line, idx) => (
                    <p key={idx} className="text-sm">
                      {line}
                    </p>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </>
  );
}
