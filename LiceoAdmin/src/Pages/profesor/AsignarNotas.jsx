/* eslint-disable react-hooks/exhaustive-deps */
import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import NavbarProfesor from "../../components/NabvarProfesor";
import LoadingSpinner from "../../components/LoadingSpinner";
import apiService from "../../services/apiService";

export default function AsignarNotas() {
  const { idUsuario, idCurso } = useParams();
  const navigate = useNavigate();
  const [materias, setMaterias] = useState([]);
  const [materiasNombres, setMateriasNombres] = useState({});
  const [estudiante, setEstudiante] = useState(null);
  const [notas, setNotas] = useState({});
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");
  const [curso, setCurso] = useState(null);
  const [editando, setEditando] = useState(null);
  const [valorEditado, setValorEditado] = useState("");
  const [guardando, setGuardando] = useState(false);

  // Función para calcular el promedio de una materia
  const calcularPromedio = (notasMateria) => {
    const valores = [notasMateria[1], notasMateria[2], notasMateria[3]];
    const notasConvertidas = valores.map(n => n === "S/N" ? 0 : parseFloat(n));
    const notasValidas = notasConvertidas.filter(n => !isNaN(n));
    
    if (notasValidas.length === 0) return "S/N";
    
    const suma = notasValidas.reduce((total, nota) => total + nota, 0);
    const promedio = suma / notasValidas.length;
    return promedio.toFixed(2); // Redondea a 2 decimales
  };

  // Función para calcular el promedio general del estudiante
  const calcularPromedioGeneral = () => {
    const promediosMaterias = materias.map(materia => {
      const promedio = calcularPromedio(notas[materia.idMateria]);
      return promedio === "S/N" ? 0 : parseFloat(promedio);
    }).filter(p => !isNaN(p));

    if (promediosMaterias.length === 0) return "S/N";
    
    const suma = promediosMaterias.reduce((total, promedio) => total + promedio, 0);
    const promedioGeneral = suma / promediosMaterias.length;
    return promedioGeneral.toFixed(2);
  };

  // Función para calcular el promedio de un parcial específico
  const calcularPromedioParcial = (numParcial) => {
    const notasParcial = materias.map(materia => {
      const nota = notas[materia.idMateria]?.[numParcial];
      return nota === "S/N" ? 0 : parseFloat(nota);
    }).filter(n => !isNaN(n));

    if (notasParcial.length === 0) return "S/N";

    const suma = notasParcial.reduce((total, nota) => total + nota, 0);
    const promedio = suma / notasParcial.length;
    return promedio.toFixed(2);
  };

  useEffect(() => {
    fetchData();
  }, [idUsuario, idCurso]);

  const fetchData = async () => {
    try {
      setCargando(true);
      setEditando(null);
      
      const cursoData = await apiService.getCurso(idCurso);
      setCurso(cursoData);
      
      const estudianteData = await apiService.getUser(idUsuario);
      setEstudiante(estudianteData);
      
      const materiasData = await apiService.getCursoMaterias(idCurso);
      setMaterias(materiasData);

      const nombresMaterias = {};
      for (const materia of materiasData) {
        try {
          const materiaDetalle = await apiService.getMateriaID(materia.idMateria);
          nombresMaterias[materia.idMateria] = materiaDetalle.nombreMateria;
        } catch (err) {
          nombresMaterias[materia.idMateria] = `Materia ID: ${materia.idMateria}`;
        }
      }
      setMateriasNombres(nombresMaterias);
      
      const notasData = {};
      for (const materia of materiasData) {
        try {
          const notasEstudiante = await apiService.getNotasByUsuarioMateria(
            idUsuario, 
            materia.idMateria
          );
          
          const parciales = { 1: "S/N", 2: "S/N", 3: "S/N" };
          
          if (notasEstudiante && Array.isArray(notasEstudiante)) {
            notasEstudiante.forEach(nota => {
              if (nota.parcial >= 1 && nota.parcial <= 3) {
                parciales[nota.parcial] = nota.nota;
              }
            });
          }
          
          notasData[materia.idMateria] = parciales;
        } catch (err) {
          notasData[materia.idMateria] = { 1: "S/N", 2: "S/N", 3: "S/N" };
        }
      }
      
      setNotas(notasData);
    } catch (err) {
      setError(err.message);
    } finally {
      setCargando(false);
    }
  };

  const iniciarEdicion = (idMateria, parcial, valorActual) => {
    setEditando({ idMateria, parcial });
    setValorEditado(valorActual === "S/N" ? "" : valorActual);
  };

  const cancelarEdicion = () => {
    setEditando(null);
    setValorEditado("");
  };

  const handleGuardarNota = async (idMateria, parcial) => {
    // Validación de la nota
    if (valorEditado === "") {
      setError("Por favor ingrese una nota");
      return;
    }

    const notaValue = parseFloat(valorEditado);
    if (isNaN(notaValue)) {
      setError("La nota debe ser un número válido");
      return;
    }

    if (notaValue < 0 || notaValue > 20) {
      setError("La nota debe estar entre 0 y 20");
      return;
    }

    try {
      setGuardando(true);
      setError("");
      
      // Buscar si ya existe una nota para este parcial
      let notaExistente = null;
      try {
        const notasEstudiante = await apiService.getNotasByUsuarioMateria(idUsuario, idMateria);
        if (Array.isArray(notasEstudiante)) {
          notaExistente = notasEstudiante.find(n => n.parcial === parcial);
        }
      } catch (err) {
        console.log("Error al buscar nota existente", err);
      }

      if (notaExistente && notaExistente.id) {
        // Actualizar nota existente
        await apiService.updateNota(
          notaExistente.id,
          idUsuario,
          idMateria,
          notaValue,
          parcial
        );
      } else {
        // Crear nueva nota
        await apiService.createNota(
          idUsuario,
          idMateria,
          notaValue,
          parcial
        );
      }
      
      // Actualizar el estado local
      setNotas(prev => ({
        ...prev,
        [idMateria]: {
          ...prev[idMateria],
          [parcial]: notaValue
        }
      }));
      
      cancelarEdicion();
    } catch (err) {
      setError("Error al guardar la nota: " + (err.response?.data?.message || err.message));
    } finally {
      setGuardando(false);
    }
  };

  if (cargando) {
    return (
      <>
        <NavbarProfesor />
        <LoadingSpinner />
      </>
    );
  }

  if (error) {
    return (
      <>
        <NavbarProfesor />
        <div className="min-h-screen bg-gray-50 p-6">
          <div className="max-w-7xl mx-auto">
            <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <svg className="h-5 w-5 text-red-500" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                  </svg>
                </div>
                <div className="ml-3">
                  <p className="text-sm text-red-700">{error}</p>
                </div>
              </div>
              <button
                onClick={() => setError("")}
                className="mt-2 text-sm text-red-600 hover:text-red-800"
              >
                Intentar de nuevo
              </button>
            </div>
          </div>
        </div>
      </>
    );
  }

  return (
    <>
      <NavbarProfesor />
      <div className="min-h-screen bg-gray-50 p-6">
        <div className="max-w-7xl mx-auto">
          <div className="flex justify-between items-center mb-6">
            <div>
              <button
                onClick={() => navigate(-1)}
                className="flex items-center text-gray-600 hover:text-gray-800 mb-4"
              >
                <svg className="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                </svg>
                Volver
              </button>
              <h1 className="text-2xl md:text-3xl font-bold text-gray-800">
                Asignar Notas - {curso?.nombreCurso}
              </h1>
            </div>
          </div>

          {/* Sección del estudiante */}
          {estudiante && (
            <div className="bg-white rounded-lg shadow p-6 mb-6">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-4">
                  <div className="flex-shrink-0">
                    <div className="h-16 w-16 rounded-full bg-indigo-500 flex items-center justify-center text-white text-xl font-bold">
                      {estudiante.nombres.charAt(0)}{estudiante.apellidos.charAt(0)}
                    </div>
                  </div>
                  <div>
                    <h2 className="text-xl font-semibold text-gray-900">
                      {estudiante.nombres} {estudiante.apellidos}
                    </h2>
                    <p className="text-gray-600">ID: {estudiante.idUsuario}</p>
                    <p className="text-gray-600">Email: {estudiante.email || 'No disponible'}</p>
                  </div>
                </div>
                
                {/* Promedio General */}
                <div className="text-right">
                  <p className="text-sm text-gray-500 mb-1">Promedio General</p>
                  <span className={`inline-flex items-center px-4 py-2 rounded-full text-lg font-bold ${
                    calcularPromedioGeneral() === "S/N"
                      ? "bg-gray-200 text-gray-800"
                      : parseFloat(calcularPromedioGeneral()) >= 14
                      ? "bg-green-200 text-green-900"
                      : "bg-red-200 text-red-900"
                  }`}>
                    {calcularPromedioGeneral()}
                  </span>
                </div>
              </div>
            </div>
          )}

          {/* Información para el usuario */}
          <div className="bg-blue-50 border-l-4 border-blue-500 p-4 rounded mb-6">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <svg className="h-5 w-5 text-blue-500" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
                </svg>
              </div>
              <div className="ml-3">
                <p className="text-sm text-blue-700">
                  <strong>Instrucciones:</strong> Las notas se ingresan seleccionando el parcial correspondiente. Haga clic sobre cualquier nota para editarla.
                </p>
              </div>
            </div>
          </div>

          {/* Tabla de materias y notas */}
          {materias.length === 0 ? (
            <div className="text-center py-12 bg-white rounded-lg shadow">
              <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
              <h3 className="mt-2 text-lg font-medium text-gray-900">No hay materias asignadas</h3>
              <p className="mt-1 text-sm text-gray-500">Este curso no tiene materias configuradas</p>
            </div>
          ) : (
            <div className="bg-white rounded-lg shadow overflow-hidden">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Materia
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Parcial 1
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Parcial 2
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Parcial 3
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Promedio Materia
                    </th>
                    
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {materias.map((materia) => {
                    const promedio = calcularPromedio(notas[materia.idMateria]);
                    return (
                      <tr key={materia.idMateria} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center">
                            <div className="ml-4">
                              <div className="text-sm font-medium text-gray-900">
                                {materiasNombres[materia.idMateria] || `Materia ID: ${materia.idMateria}`}
                              </div>
                            </div>
                          </div>
                        </td>
                        {[1, 2, 3].map((parcial) => (
                          <td key={parcial} className="px-6 py-4 whitespace-nowrap text-center">
                            {editando?.idMateria === materia.idMateria && editando?.parcial === parcial ? (
                              <div className="flex items-center justify-center space-x-2">
                                <input
                                  type="number"
                                  min="0"
                                  max="20"
                                  step="0.1"
                                  value={valorEditado}
                                  onChange={(e) => {
                                    const value = e.target.value;
                                    if (value === "" || (parseFloat(value) >= 0 && parseFloat(value) <= 20)) {
                                      setValorEditado(value);
                                    }
                                  }}
                                  className="w-20 px-3 py-1 border rounded focus:ring-indigo-500 focus:border-indigo-500"
                                  disabled={guardando}
                                />
                                <button
                                  onClick={() => handleGuardarNota(materia.idMateria, parcial)}
                                  className="text-green-600 hover:text-green-800 disabled:opacity-50"
                                  disabled={guardando}
                                >
                                  {guardando ? (
                                    <svg className="animate-spin h-5 w-5 text-green-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                    </svg>
                                  ) : (
                                    "✓"
                                  )}
                                </button>
                                <button
                                  onClick={cancelarEdicion}
                                  className="text-red-600 hover:text-red-800"
                                  disabled={guardando}
                                >
                                  ✗
                                </button>
                              </div>
                            ) : (
                              <span 
                                className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${
                                  notas[materia.idMateria]?.[parcial] === "S/N"
                                    ? "bg-gray-100 text-gray-800 cursor-pointer hover:bg-gray-200"
                                    : notas[materia.idMateria]?.[parcial] >= 14
                                    ? "bg-green-100 text-green-800 cursor-pointer hover:bg-green-200"
                                    : "bg-red-100 text-red-800 cursor-pointer hover:bg-red-200"
                                }`}
                                onClick={() => iniciarEdicion(
                                  materia.idMateria, 
                                  parcial, 
                                  notas[materia.idMateria]?.[parcial]
                                )}
                              >
                                {notas[materia.idMateria]?.[parcial] || "S/N"}
                              </span>
                            )}
                          </td>
                        ))}
                        <td className="px-6 py-4 whitespace-nowrap text-center">
                          <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${
                            promedio === "S/N"
                              ? "bg-gray-100 text-gray-800"
                              : parseFloat(promedio) >= 14
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}>
                            {promedio}
                          </span>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
                
                {/* Fila de promedios por parcial */}
                {materias.length > 0 && (
                  <tfoot className="bg-gray-100 border-t-2 border-gray-300">
                    <tr>
                      <td className="px-6 py-4 text-sm font-semibold text-gray-900">
                        Promedio por Parcial
                      </td>
                      {[1, 2, 3].map((parcial) => (
                        <td key={parcial} className="px-6 py-4 text-center">
                          <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-bold ${
                            calcularPromedioParcial(parcial) === "S/N"
                              ? "bg-gray-200 text-gray-800"
                              : parseFloat(calcularPromedioParcial(parcial)) >= 14
                              ? "bg-green-200 text-green-900"
                              : "bg-red-200 text-red-900"
                          }`}>
                            {calcularPromedioParcial(parcial)}
                          </span>
                        </td>
                      ))}
                      <td className="px-6 py-4 text-center">
                        <span className="text-sm font-semibold text-gray-600">
                          -
                        </span>
                      </td>
                    </tr>
                  </tfoot>
                )}
              </table>
            </div>
          )}
        </div>
      </div>
    </>
  );
}