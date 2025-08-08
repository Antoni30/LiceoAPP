/* eslint-disable react-hooks/exhaustive-deps */
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import NavbarProfesor from "../../components/NabvarProfesor";
import LoadingSpinner from "../../components/LoadingSpinner";
import { useAuth } from "../../hooks/AuthProvider";
import apiService from "../../services/apiService";

export default function GenerarReporte() {
  const { idProfesor } = useParams();
  const [profesor, setProfesor] = useState(null);
  const [estudiantes, setEstudiantes] = useState([]);
  const [idCurso, setIdCurso] = useState("");
  const [nombreCurso, setNombreCurso] = useState("");
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");
  const [generandoPDF, setGenerandoPDF] = useState(false);
  const [promediosParciales, setPromediosParciales] = useState({ 1: "S/N", 2: "S/N", 3: "S/N" });
  const [promedioGeneral, setPromedioGeneral] = useState("S/N");
  const [materiasNombres, setMateriasNombres] = useState({});
  const { logout } = useAuth();

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
      
      // Obtener nombres de materias
      const nombresMaterias = {};
      for (const materia of materiasDelCurso) {
        try {
          const materiaDetalle = await apiService.getMateriaID(materia.idMateria);
          nombresMaterias[materia.idMateria] = materiaDetalle.nombreMateria;
        } catch (err) {
          nombresMaterias[materia.idMateria] = `Materia ID: ${materia.idMateria}`;
        }
      }
      setMateriasNombres(nombresMaterias);
      
      for (const { idUsuario } of usuCursos) {
        try {
          const roles = await apiService.getUserRoles(idUsuario);
        const tieneRolEstudiante = roles.some((r) => r.idRol === 3);
        if (!tieneRolEstudiante) continue;

          const usuario = await apiService.getUser(idUsuario);

          // Obtener todas las notas del estudiante por materia
          const notasEstudiante = {};
          for (const materia of materiasDelCurso) {
            try {
              const notasMateria = await apiService.getNotasByUsuarioMateria(idUsuario, materia.idMateria);
              const parciales = { 1: "S/N", 2: "S/N", 3: "S/N" };
              
              if (notasMateria && Array.isArray(notasMateria)) {
                notasMateria.forEach(nota => {
                  if (nota.parcial >= 1 && nota.parcial <= 3) {
                    parciales[nota.parcial] = nota.nota;
                  }
                });
              }
              
              notasEstudiante[materia.idMateria] = parciales;
            } catch (err) {
              notasEstudiante[materia.idMateria] = { 1: "S/N", 2: "S/N", 3: "S/N" };
            }
          }

          // Calcular promedios por parcial del estudiante
          const notasParciales = { 1: "S/N", 2: "S/N", 3: "S/N" };
          
          for (let parcial = 1; parcial <= 3; parcial++) {
            const notasDelParcial = [];
            
            for (const materia of materiasDelCurso) {
              const nota = notasEstudiante[materia.idMateria][parcial];
              notasDelParcial.push(nota === "S/N" ? 0 : parseFloat(nota));
            }
            
            if (notasDelParcial.length > 0) {
              const suma = notasDelParcial.reduce((total, nota) => total + nota, 0);
              const promedio = suma / notasDelParcial.length;
              notasParciales[parcial] = promedio.toFixed(2);
              notasPorParcial[parcial].push(promedio);
            } else {
              notasPorParcial[parcial].push(0);
            }
          }

          // Calcular promedio general del estudiante
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
            notasPorMateria: notasEstudiante
          });
        } catch (userErr) {
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
    fetchData();
  }, [idProfesor]);

  const generarPDF = async () => {
    setGenerandoPDF(true);
    
    try {
      // Importar jsPDF dinámicamente
      const { jsPDF } = await import('jspdf');
      const doc = new jsPDF('p', 'mm', 'a4'); // Formato A4 explícito
      
      // Dimensiones de página A4: 210mm x 297mm
      const pageWidth = 210;
      const pageHeight = 297;
      const margin = 15;
      const contentWidth = pageWidth - (margin * 2);
      
      // Fecha actual
      const fechaActual = new Date().toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
      
      // Encabezado
      doc.setFontSize(18);
      doc.text('REPORTE DE NOTAS', pageWidth / 2, 20, { align: 'center' });
      
      doc.setFontSize(12);
      doc.text(`Curso: ${nombreCurso}`, margin, 35);
      doc.text(`Profesor: ${profesor.nombres} ${profesor.apellidos}`, margin, 45);
      doc.text(`Fecha: ${fechaActual}`, margin, 55);
      
      // Línea separadora
      doc.line(margin, 60, pageWidth - margin, 60);
      
      // Promedios del curso
      doc.setFontSize(14);
      doc.text('PROMEDIOS DEL CURSO', margin, 75);
      
      doc.setFontSize(11);
      doc.text(`Parcial 1: ${promediosParciales[1]}`, margin, 85);
      doc.text(`Parcial 2: ${promediosParciales[2]}`, margin + 45, 85);
      doc.text(`Parcial 3: ${promediosParciales[3]}`, margin + 90, 85);
      doc.text(`Promedio General: ${promedioGeneral}`, margin + 135, 85);
      
      // Línea separadora
      doc.line(margin, 95, pageWidth - margin, 95);
      
      // Tabla de estudiantes
      let yPosition = 110;
      doc.setFontSize(14);
      doc.text('DETALLE POR ESTUDIANTE', margin, yPosition);
      
      yPosition += 10;
      
      // Encabezados de tabla
      doc.setFontSize(10);
      doc.setFont("helvetica", "bold");
      doc.text('ESTUDIANTE', margin, yPosition);
      doc.text('P1', margin + 80, yPosition);
      doc.text('P2', margin + 95, yPosition);
      doc.text('P3', margin + 110, yPosition);
      doc.text('PROMEDIO', margin + 125, yPosition);
      
      // Línea bajo encabezados
      yPosition += 3;
      doc.line(margin, yPosition, pageWidth - margin, yPosition);
      
      yPosition += 8;
      doc.setFont("helvetica", "normal");
      
      // Datos de estudiantes
      estudiantes.forEach((estudiante, index) => {
        // Verificar si necesita nueva página (dejar espacio para pie de página)
        if (yPosition > pageHeight - 30) {
          doc.addPage('p', 'a4');
          yPosition = 20;
          
          // Repetir encabezados en nueva página
          doc.setFont("helvetica", "bold");
          doc.text('ESTUDIANTE', margin, yPosition);
          doc.text('P1', margin + 80, yPosition);
          doc.text('P2', margin + 95, yPosition);
          doc.text('P3', margin + 110, yPosition);
          doc.text('PROMEDIO', margin + 125, yPosition);
          
          yPosition += 3;
          doc.line(margin, yPosition, pageWidth - margin, yPosition);
          yPosition += 8;
          doc.setFont("helvetica", "normal");
        }
        
        // Truncar nombre si es muy largo
        const nombreCompleto = `${estudiante.nombres} ${estudiante.apellidos}`;
        const nombreTruncado = nombreCompleto.length > 25 ? nombreCompleto.substring(0, 25) + '...' : nombreCompleto;
        
        doc.text(nombreTruncado, margin, yPosition);
        doc.text(estudiante.nota1.toString(), margin + 80, yPosition);
        doc.text(estudiante.nota2.toString(), margin + 95, yPosition);
        doc.text(estudiante.nota3.toString(), margin + 110, yPosition);
        doc.text(estudiante.promedio.toString(), margin + 125, yPosition);
        
        yPosition += 7;
      });
      
      // Pie de página en la última página
      const totalPages = doc.internal.getNumberOfPages();
      for (let i = 1; i <= totalPages; i++) {
        doc.setPage(i);
        doc.setFontSize(8);
        doc.text(`Página ${i} de ${totalPages}`, pageWidth - margin, pageHeight - 10, { align: 'right' });
        doc.text('Generado por Sistema LiceoAdmin', margin, pageHeight - 10);
      }
      
      // Guardar el PDF
      doc.save(`reporte-notas-${nombreCurso.replace(/\s+/g, '-')}-${new Date().toISOString().split('T')[0]}.pdf`);
      
    } catch (err) {
      console.error('Error generando PDF:', err);
      setError('Error al generar el reporte PDF');
    } finally {
      setGenerandoPDF(false);
    }
  };

  if (cargando) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <LoadingSpinner text="Cargando datos para el reporte..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="bg-white p-6 rounded-lg shadow-md max-w-md w-full">
          <div className="text-red-500 font-medium text-lg mb-2">ERROR</div>
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
                  Generar Reporte - {nombreCurso}
                </h1>
                <p className="text-indigo-200">Profesor: {profesor.nombres} {profesor.apellidos}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Contenido principal */}
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          
          {/* Sección de promedios del curso */}
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Resumen del Curso</h3>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
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

            <div className="text-center">
              <p className="text-gray-600 mb-4">
                Total de estudiantes: {estudiantes.length}
              </p>
              <button
                onClick={generarPDF}
                disabled={generandoPDF}
                className={`inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white ${
                  generandoPDF
                    ? "bg-gray-400 cursor-not-allowed"
                    : "bg-green-600 hover:bg-green-700"
                } focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 transition-colors duration-200`}
              >
                {generandoPDF ? (
                  <>
                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    Generando PDF...
                  </>
                ) : (
                  <>
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
                        d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                      />
                    </svg>
                    Descargar Reporte PDF
                  </>
                )}
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}