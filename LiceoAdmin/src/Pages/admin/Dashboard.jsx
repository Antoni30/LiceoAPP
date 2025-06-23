import Navbar from "../../components/Nabvar";
import { useState, useEffect } from "react";
import { useAuth } from "../../hooks/AuthProvider";
import { useNavigate } from "react-router-dom";

function Dashboard() {
  const { isAuthenticated } = useAuth();
  const [aniosLectivos, setAniosLectivos] = useState([]);
  const [filter, setFilter] = useState("todos"); // 'activos', 'inactivos', 'todos'
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [editAnio, setEditAnio] = useState({
    id: null,
    fechaInicio: "",
    fechaFinal: "",
    error: ""
  });
  const [fechaInicioEdit, setFechaInicioEdit] = useState("");
  const [fechaFinalEdit, setFechaFinalEdit] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      fetchAniosLectivos();
    }
  }, [isAuthenticated, filter]);

  const fetchAniosLectivos = async () => {
    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/anios-lectivos", {
        credentials: "include",
      });

      if (!response.ok) throw new Error("Error al cargar datos");

      const data = await response.json();
      setAniosLectivos(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleToggleState = async (id, estado,fechaInicio,fechaFinal) => {
    setIsLoading(true);
    try {
      const updatedAnio = {
        fechaInicio: fechaInicio,
        fechaFinal: fechaFinal,
        estado: estado === "Activo" ? "Inactivo" : "Activo",
      };

      const response = await fetch(
        `http://localhost:8080/api/anios-lectivos/estado/${id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify(updatedAnio),
        }
      );

      if (!response.ok)
        throw new Error("Error al actualizar el estado del año lectivo");

      fetchAniosLectivos();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEdit = (anio) => {
    setEditAnio({
      id: anio.id,
      fechaInicio: anio.fechaInicio,
      fechaFinal: anio.fechaFinal,
      error: ""
    });
    setFechaInicioEdit(anio.fechaInicio);
    setFechaFinalEdit(anio.fechaFinal);
  };

  const handleSaveEdit = async () => {
    if (new Date(fechaFinalEdit) <= new Date(fechaInicioEdit)) {
      setEditAnio(prev => ({
        ...prev,
        error: "La fecha final debe ser mayor que la fecha de inicio."
      }));
      return;
    }

    setIsLoading(true);
    try {
      const updatedAnio = {
        fechaInicio: fechaInicioEdit,
        fechaFinal: fechaFinalEdit,
        estado: aniosLectivos.find(a => a.id === editAnio.id)?.estado || "Activo",
      };

      const response = await fetch(
        `http://localhost:8080/api/anios-lectivos/${editAnio.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify(updatedAnio),
        }
      );

      if (!response.ok) throw new Error("Las fechas se cruzan con otro año lectivo existente");

      fetchAniosLectivos();
      setEditAnio({ id: null, fechaInicio: "", fechaFinal: "", error: "" });
    } catch (err) {
      setEditAnio(prev => ({
        ...prev,
        error: err.message
      }));
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setEditAnio({ id: null, fechaInicio: "", fechaFinal: "", error: "" });
  };

  const filteredAnios = aniosLectivos.filter((anio) => {
    if (filter === "todos") return true;
    return filter === "activos"
      ? anio.estado === "Activo"
      : anio.estado === "Inactivo";
  });

  const formatDate = (dateString) => {
  // Extraer directamente las partes de la fecha del string (asumiendo formato YYYY-MM-DD)
  const [year, month, day] = dateString.split('-');
  
  // Usar estas partes directamente para evitar problemas de zona horaria
  const options = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(year, month - 1, day).toLocaleDateString('es-ES', options);
};

  const handleAddYears = () => {
    navigate(`/agregarAnio`);
  };

    if (error)
      return (
        <>
          <Navbar />
          <div className="p-4 text-red-600"> Error: {error}</div>;
        </>
      );
    if (isLoading)
      return (
        <>
          <Navbar />
          <div className="p-4">Cargando Años Lectivos...</div>;
        </>
      );

  return (
    <>
      <Navbar />
      <div className="min-h-screen bg-gray-50 p-6">
        <div className="max-w-7xl mx-auto">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
            <h1 className="text-3xl font-bold text-gray-800 mb-4 md:mb-0">
              Gestión de Años Lectivos
            </h1>

            <div className="flex space-x-4 w-full md:w-auto">
              <select
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option value="todos">Todos</option>
                <option value="activos">Activos</option>
                <option value="inactivos">Inactivos</option>
              </select>

              <button
                onClick={handleAddYears}
                className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                + Nuevo Año
              </button>
            </div>
          </div>

          {isLoading ? (
            <div className="flex justify-center items-center h-64">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-indigo-500"></div>
            </div>
          ) : filteredAnios.length === 0 ? (
            <div className="text-center py-12">
              <svg
                className="mx-auto h-12 w-12 text-gray-400"
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
              <h3 className="mt-2 text-lg font-medium text-gray-900">
                No se encontraron años lectivos
              </h3>
              <p className="mt-1 text-sm text-gray-500">
                {filter !== "todos"
                  ? `No hay años lectivos ${filter}`
                  : "No hay años lectivos registrados"}
              </p>
            </div>
          ) : (
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredAnios.map((anio) => (
                <div
                  key={anio.id}
                  className={`border rounded-lg overflow-hidden shadow-sm transition-all hover:shadow-md ${
                    anio.estado === "Activo"
                      ? "border-green-300"
                      : "border-gray-200"
                  }`}
                >
                  <div
                    className={`px-4 py-3 ${
                      anio.estado === "Activo"
                        ? "bg-green-50 border-b border-green-200"
                        : "bg-gray-50 border-b border-gray-200"
                    }`}
                  >
                    <div className="flex items-center">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          anio.estado === "Activo"
                            ? "bg-green-100 text-green-800"
                            : "bg-gray-100 text-gray-800"
                        }`}
                      >
                        {anio.estado}
                      </span>
                    </div>
                  </div>
                        
                  {editAnio.id === anio.id ? (
                    <div className="p-4">
                      <div className="space-y-2">
                        <div className="flex items-center text-sm text-gray-500">
                          <label
                            htmlFor="fechaInicioEdit"
                            className="block text-sm font-medium text-gray-700"
                          >
                            Fecha de Inicio
                          </label>
                          <input
                            type="date"
                            id="fechaInicioEdit"
                            value={fechaInicioEdit}
                            onChange={(e) => setFechaInicioEdit(e.target.value)}
                            className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                          />
                        </div>
                        <div className="flex items-center text-sm text-gray-500">
                          <label
                            htmlFor="fechaFinalEdit"
                            className="block text-sm font-medium text-gray-700"
                          >
                            Fecha Final
                          </label>
                          <input
                            type="date"
                            id="fechaFinalEdit"
                            value={fechaFinalEdit}
                            onChange={(e) => setFechaFinalEdit(e.target.value)}
                            className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                          />
                        </div>
                      </div>
                      <div className="flex justify-end space-x-2 mt-4">
                        <button
                          onClick={handleSaveEdit}
                          className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                          Guardar Cambios
                        </button>
                        <button
                          onClick={handleCancelEdit}
                          className="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
                        >
                          Cancelar
                        </button>
                      </div>
                      {editAnio.error && (
                        <div className="mt-3 bg-red-50 border-l-4 border-red-500 p-3 rounded-md">
                          <p className="text-sm text-red-700">{editAnio.error}</p>
                        </div>
                      )}
                    </div>
                  ) : (
                    <div className="p-4">
                      <h3 className="text-lg font-medium text-gray-900 mb-2">
                        Año Lectivo #{anio.id}
                      </h3>
                      <div className="space-y-2">
                        <div className="flex items-center text-sm text-gray-500">
                          <svg
                            className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400"
                            viewBox="0 0 20 20"
                            fill="currentColor"
                          >
                            <path
                              fillRule="evenodd"
                              d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z"
                              clipRule="evenodd"
                            />
                          </svg>
                          <span>Inicio: {formatDate(anio.fechaInicio)}</span>
                        </div>
                        <div className="flex items-center text-sm text-gray-500">
                          <svg
                            className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400"
                            viewBox="0 0 20 20"
                            fill="currentColor"
                          >
                            <path
                              fillRule="evenodd"
                              d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z"
                              clipRule="evenodd"
                            />
                          </svg>
                          <span>Fin: {formatDate(anio.fechaFinal)}</span>
                        </div>
                      </div>
                    </div>
                  )}
                  <div className="px-4 py-3 bg-gray-50 border-t border-gray-200 flex justify-end space-x-2">
                    {!editAnio.id || editAnio.id !== anio.id ? (
                      <button
                        onClick={() => handleEdit(anio)}
                        className="px-3 py-1 text-sm text-indigo-600 hover:text-indigo-900"
                      >
                        Editar
                      </button>
                      
                    ) : null}

                    {!editAnio.id || editAnio.id !== anio.id ? (
                       <button
                      onClick={() => handleToggleState(anio.id, anio.estado,anio.fechaInicio,anio.fechaFinal)}
                      className="px-3 py-1 text-sm text-red-600 hover:text-red-900"
                    >
                      {anio.estado === "Activo" ? "Desactivar" : "Activar"}
                    </button>
                    ):null}
                   {!editAnio.id || editAnio.id !== anio.id ? ( 

                      <button
                      onClick={() => navigate(`/cursos/${anio.id}`)}
                      className="px-3 py-1 text-sm text-red-600 hover:text-red-900"
                    >
                      Cursos
                    </button>
                   ):null}

                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  );
}

export default Dashboard;