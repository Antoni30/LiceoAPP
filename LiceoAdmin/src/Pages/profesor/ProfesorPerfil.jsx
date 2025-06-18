import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import NavbarProfesor from "../../components/NabvarProfesor";
import LoadingSpinner from "../../components/LoadingSpinner";

export default function ProfesorPerfil() {
  const { idProfesor } = useParams();
  const [profesor, setProfesor] = useState(null);
  const [estudiantes, setEstudiantes] = useState([]);
  const [idCurso, setIdCurso] = useState("");
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [nuevoEstudiante, setNuevoEstudiante] = useState({
    idUsuario: "",
    nombres: "",
    apellidos: "",
    nickname: "",
    contrasena: "",
    estado: "ACTIVO",
    email: "",
  });

  useEffect(() => {
    async function fetchData() {
      try {
        const resProf = await fetch(
          `http://localhost:8080/api/usuarios/${idProfesor}`,
          { credentials: "include" }
        );
        if (!resProf.ok) throw new Error("No se pudo cargar el profesor");
        const profData = await resProf.json();
        setProfesor(profData);

        const resCursos = await fetch(
          `http://localhost:8080/api/usuarios-cursos/usuario/${idProfesor}`,
          { credentials: "include" }
        );
        if (!resCursos.ok) throw new Error("No se pudo cargar el curso");
        const cursos = await resCursos.json();
        if (!cursos.length)
          throw new Error("El profesor no está asignado a un curso");
        const cursoId = cursos[0].idCurso;
        setIdCurso(cursoId);

        const resUsuCursos = await fetch(
          `http://localhost:8080/api/usuarios-cursos/curso/${cursoId}`,
          { credentials: "include" }
        );
        if (!resUsuCursos.ok)
          throw new Error("No se pudieron cargar los usuarios del curso");
        const usuCursos = await resUsuCursos.json();

        const estudiantesFiltrados = [];
        for (const { idUsuario } of usuCursos) {
          const resRol = await fetch(
            `http://localhost:8080/api/usuarios-roles/usuario/${idUsuario}`,
            { credentials: "include" }
          );
          if (!resRol.ok) continue;
          const roles = await resRol.json();
          const tieneRolEstudiante = roles.some((r) => r.idRol === 3);
          if (!tieneRolEstudiante) continue;

          const resUsu = await fetch(
            `http://localhost:8080/api/usuarios/${idUsuario}`,
            { credentials: "include" }
          );
          if (!resUsu.ok) continue;
          const usuario = await resUsu.json();

          estudiantesFiltrados.push({
            idUsuario,
            nombres: usuario.nombres,
            apellidos: usuario.apellidos,
            nota1: "",
            nota2: "",
            nota3: "",
            promedio: "",
          });
        }

        setEstudiantes(estudiantesFiltrados);
      } catch (err) {
        console.error(err);
        setError(err.message);
      } finally {
        setCargando(false);
      }
    }

    fetchData();
  }, [idProfesor]);

  const handleChange = (e) => {
    setNuevoEstudiante((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleCrearEstudiante = async () => {
    try {
      // 1. Crear usuario
      const resUsuario = await fetch("http://localhost:8080/api/usuarios", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(nuevoEstudiante),
      });
      if (!resUsuario.ok) throw new Error("Error al crear usuario");

      // 2. Asignar rol estudiante (idRol = 3)
      await fetch("http://localhost:8080/api/usuarios-roles", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          idUsuario: nuevoEstudiante.idUsuario,
          idRol: 3,
        }),
      });

      // 3. Asignar a curso
      await fetch("http://localhost:8080/api/usuarios-cursos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ idUsuario: nuevoEstudiante.idUsuario, idCurso }),
      });

      setShowForm(false);
      window.location.reload(); // O volver a llamar a fetchData()
    } catch (err) {
      console.error(err);
      alert("Error al crear estudiante");
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
          <div className="text-red-500 font-medium text-lg mb-2">Error</div>
          <p className="text-gray-700">{error}</p>
          <button 
            onClick={() => window.location.reload()}
            className="mt-4 bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700"
          >
            Reintentar
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
                {profesor.nombres.charAt(0)}{profesor.apellidos.charAt(0)}
              </div>
              <div>
                <h1 className="text-2xl font-bold">{profesor.nombres} {profesor.apellidos}</h1>
                <p className="text-indigo-200">Profesor</p>
              </div>
            </div>
          </div>
        </div>

        {/* Contenido principal */}
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-semibold text-gray-800">Estudiantes del Curso</h2>
            <button
              onClick={() => setShowForm(true)}
              className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg flex items-center"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clipRule="evenodd" />
              </svg>
              Agregar Estudiante
            </button>
          </div>

          {estudiantes.length === 0 ? (
            <div className="bg-white rounded-lg shadow-sm p-8 text-center">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-12 w-12 mx-auto text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
              </svg>
              <h3 className="mt-4 text-lg font-medium text-gray-900">No hay estudiantes</h3>
              <p className="mt-1 text-gray-500">Aún no hay estudiantes asignados a este curso.</p>
              <button
                onClick={() => setShowForm(true)}
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
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estudiante</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nota 1</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nota 2</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nota 3</th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Promedio</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {estudiantes.map((e) => (
                      <tr key={e.idUsuario} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center">
                            <div className="flex-shrink-0 h-10 w-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-medium">
                              {e.nombres.charAt(0)}{e.apellidos.charAt(0)}
                            </div>
                            <div className="ml-4">
                              <div className="text-sm font-medium text-gray-900">{e.nombres} {e.apellidos}</div>
                              <div className="text-sm text-gray-500">ID: {e.idUsuario}</div>
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                            {e.nota1 || 'N/A'}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                            {e.nota2 || 'N/A'}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                            {e.nota3 || 'N/A'}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                            {e.promedio || 'N/A'}
                          </span>
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
        {showForm && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">Agregar Nuevo Estudiante</h3>
                <button
                  onClick={() => setShowForm(false)}
                  className="text-gray-400 hover:text-gray-500"
                >
                  <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>

              <div className="space-y-4">
                {Object.keys(nuevoEstudiante)
                  .filter(key => key !== "estado")
                  .map((key) => (
                    <div key={key}>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        {key.charAt(0).toUpperCase() + key.slice(1)}
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
                  onClick={() => setShowForm(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                  Cancelar
                </button>
                <button
                  type="button"
                  onClick={handleCrearEstudiante}
                  className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                  Guardar Estudiante
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
