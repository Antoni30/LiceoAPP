import Navbar from "../components/Nabvar";
import { useState, useEffect } from 'react';
import { useAuth } from '../hooks/AuthProvider';
import {  useNavigate } from "react-router-dom";

function Dashboard() {
  const { isAuthenticated } = useAuth();
  const [aniosLectivos, setAniosLectivos] = useState([]);
  const [filter, setFilter] = useState('todos'); // 'activos', 'inactivos', 'todos'
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editAnio, setEditAnio] = useState(null);  // Estado para almacenar el año lectivo en edición
  const [fechaInicioEdit, setFechaInicioEdit] = useState('');
  const [fechaFinalEdit, setFechaFinalEdit] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      fetchAniosLectivos();
    }
  }, [isAuthenticated, filter]);

  const fetchAniosLectivos = async () => {
    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/anios-lectivos', {
        credentials: 'include'
      });
      
      if (!response.ok) throw new Error('Error al cargar datos');
      
      const data = await response.json();
      setAniosLectivos(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleToggleState = async (id, estado) => {
    setIsLoading(true);
    try {
      const updatedAnio = {
        fechaInicio: '2025-05-05',
        fechaFinal: '2026-03-11',
        estado: estado === 'Activo' ? 'Inactivo' : 'Activo'
      };

      const response = await fetch(`http://localhost:8080/api/anios-lectivos/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(updatedAnio),
      });

      if (!response.ok) throw new Error('Error al actualizar el estado del año lectivo');
      
      fetchAniosLectivos();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleEdit = (anio) => {
    setEditAnio(anio);
    setFechaInicioEdit(anio.fechaInicio);
    setFechaFinalEdit(anio.fechaFinal);
  };

  const handleSaveEdit = async () => {
    setIsLoading(true);
    try {
      const updatedAnio = {
        fechaInicio: fechaInicioEdit,
        fechaFinal: fechaFinalEdit,
        estado:editAnio.estado
      };

      const response = await fetch(`http://localhost:8080/api/anios-lectivos/${editAnio.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(updatedAnio),
      });

      if (!response.ok) throw new Error('Error al guardar los cambios');

      fetchAniosLectivos();
      setEditAnio(null);  // Cerrar el formulario de edición
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelEdit = () => {
    setEditAnio(null);  // Cerrar el formulario de edición sin guardar cambios
  };

  const filteredAnios = aniosLectivos.filter(anio => {
    if (filter === 'todos') return true;
    return filter === 'activos' 
      ? anio.estado === 'Activo' 
      : anio.estado === 'Inactivo';
  });

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('es-ES', options);
  };

  const handleAddYears = () => {
    navigate(`/agregarAnio`);
  };

  return (
    <>
    <Navbar />
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-7xl mx-auto">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800 mb-4 md:mb-0">Gestión de Años Lectivos</h1>
          
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
        ) : error ? (
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
        ) : filteredAnios.length === 0 ? (
          <div className="text-center py-12">
            <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <h3 className="mt-2 text-lg font-medium text-gray-900">No se encontraron años lectivos</h3>
            <p className="mt-1 text-sm text-gray-500">
              {filter !== 'todos' ? `No hay años lectivos ${filter}` : 'No hay años lectivos registrados'}
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredAnios.map((anio) => (
              <div 
                key={anio.id} 
                className={`border rounded-lg overflow-hidden shadow-sm transition-all hover:shadow-md ${
                  anio.estado === 'Activo' ? 'border-green-300' : 'border-gray-200'
                }`}
              >
                <div className={`px-4 py-3 ${
                  anio.estado === 'Activo' 
                    ? 'bg-green-50 border-b border-green-200' 
                    : 'bg-gray-50 border-b border-gray-200'
                }`}>
                  <div className="flex items-center">
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      anio.estado === 'Activo' 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-gray-100 text-gray-800'
                    }`}>
                      {anio.estado}
                    </span>
                  </div>
                </div>

                {/* Editar año lectivo */}
                {editAnio?.id === anio.id ? (
                  <div className="p-4">
                    <div className="space-y-2">
                      <div className="flex items-center text-sm text-gray-500">
                        <label htmlFor="fechaInicioEdit" className="block text-sm font-medium text-gray-700">
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
                        <label htmlFor="fechaFinalEdit" className="block text-sm font-medium text-gray-700">
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
                  </div>
                ) : (
                  <div className="p-4">
                    <h3 className="text-lg font-medium text-gray-900 mb-2">Año Lectivo #{anio.id}</h3>
                    <div className="space-y-2">
                      <div className="flex items-center text-sm text-gray-500">
                        <svg className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                          <path fillRule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clipRule="evenodd" />
                        </svg>
                        <span>Inicio: {formatDate(anio.fechaInicio)}</span>
                      </div>
                      <div className="flex items-center text-sm text-gray-500">
                        <svg className="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                          <path fillRule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clipRule="evenodd" />
                        </svg>
                        <span>Fin: {formatDate(anio.fechaFinal)}</span>
                      </div>
                    </div>
                  </div>
                )}
                <div className="px-4 py-3 bg-gray-50 border-t border-gray-200 flex justify-end space-x-2">
                  {!editAnio || editAnio.id !== anio.id ? (
                    <button 
                      onClick={() => handleEdit(anio)}
                      className="px-3 py-1 text-sm text-indigo-600 hover:text-indigo-900"
                    >
                      Editar
                    </button>
                  ) : null}
                  <button 
                    onClick={() => handleToggleState(anio.id, anio.estado)} 
                    className="px-3 py-1 text-sm text-red-600 hover:text-red-900"
                  >
                    {anio.estado === 'Activo' ? 'Desactivar' : 'Activar'}
                  </button>

                  <button 
                    onClick={() => navigate(`/cursos/${anio.id}`)} 
                    className="px-3 py-1 text-sm text-red-600 hover:text-red-900"
                  >
                    Cursos
                  </button>


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
