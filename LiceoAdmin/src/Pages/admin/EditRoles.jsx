import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

export default function AsignarRolUsuario() {
  const { idUsuario } = useParams();
  const navigate = useNavigate();
  const [roles, setRoles] = useState([]);
  const [rolSeleccionado, setRolSeleccionado] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [loading, setLoading] = useState(true);
  const [usuarioInfo, setUsuarioInfo] = useState({});
  const [rolesAsignados, setRolesAsignados] = useState([]);

  // Obtener lista de roles disponibles y roles actuales asignados al usuario
  useEffect(() => {
    const fetchData = async () => {
      try {
        // Obtener roles disponibles
        const rolesResponse = await fetch('http://localhost:8080/api/roles', {
          credentials: 'include'
        });

        if (!rolesResponse.ok) throw new Error('Error al obtener los roles');
        
        const rolesData = await rolesResponse.json();
        setRoles(rolesData);

        // Obtener información básica del usuario (opcional)
        const usuarioResponse = await fetch(`http://localhost:8080/api/usuarios/${idUsuario}`, {
          credentials: 'include'
        });

        if (usuarioResponse.ok) {
          const usuarioData = await usuarioResponse.json();
          setUsuarioInfo({
            nombres: usuarioData.nombres,
            apellidos: usuarioData.apellidos
          });
        }

        // Obtener roles actuales asignados al usuario
        const rolesAsignadosResponse = await fetch(`http://localhost:8080/api/usuarios-roles/usuario/${idUsuario}`, {
          credentials: 'include'
        });

        if (rolesAsignadosResponse.ok) {
          const rolesData = await rolesAsignadosResponse.json();
          setRolesAsignados(rolesData);
        }

        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchData();
  }, [idUsuario]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    if (!rolSeleccionado) {
      setError('Debes seleccionar un rol');
      return;
    }

    const rolActual = rolesAsignados.length > 0 ? rolesAsignados[0].idRol : null;

    if (rolSeleccionado === rolActual) {
      setError('Este rol ya está asignado al usuario');
      return;
    }

    try {
      // Eliminar el primer rol actual
      if (rolesAsignados.length > 0) {
        const rolAEliminar = rolesAsignados[0].idRol;
        const eliminarRolResponse = await fetch(`http://localhost:8080/api/usuarios-roles/usuario/${idUsuario}/rol/${rolAEliminar}`, {
          method: 'DELETE',
          credentials: 'include'
        });

        if (!eliminarRolResponse.ok) {
          throw new Error('Error al eliminar el rol anterior');
        }
      }

      // Asignar el nuevo rol
      const asignarRolResponse = await fetch('http://localhost:8080/api/usuarios-roles', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          idUsuario: idUsuario,
          idRol: parseInt(rolSeleccionado)
        })
      });

      if (!asignarRolResponse.ok) {
        const errData = await asignarRolResponse.json();
        throw new Error(errData.message || 'Error al asignar el rol');
      }

      setSuccess('Rol asignado correctamente');
      setTimeout(() => navigate('/usuarios'), 1500);
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <div className="p-4 text-center">Cargando...</div>;
  if (error) return <div className="p-4 text-red-600 text-center">Error: {error}</div>;

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md mx-auto">
        <div className="bg-white shadow rounded-lg p-6 sm:p-8">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">
              Asignar Rol a Usuario
            </h2>
            <button
              onClick={() => navigate('/usuarios')}
              className="text-gray-500 hover:text-gray-700"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          {usuarioInfo.nombres && (
            <div className="mb-6 p-4 bg-blue-50 rounded-lg">
              <p className="font-medium">
                Usuario: {usuarioInfo.nombres} {usuarioInfo.apellidos}
              </p>
              <p className="text-sm text-gray-600">ID: {idUsuario}</p>
            </div>
          )}

          {error && (
            <div className="mb-4 p-3 bg-red-50 border-l-4 border-red-500 text-red-700">
              {error}
            </div>
          )}

          {success && (
            <div className="mb-4 p-3 bg-green-50 border-l-4 border-green-500 text-green-700">
              {success}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="rol" className="block text-sm font-medium text-gray-700 mb-2">
                Seleccionar Rol *
              </label>
              <select
                id="rol"
                value={rolSeleccionado}
                onChange={(e) => setRolSeleccionado(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                required
              >
                <option value="">Seleccione un rol</option>
                {roles.map((rol) => (
                  <option key={rol.id} value={rol.id}>
                    {rol.nombre}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                onClick={() => navigate('/usuarios')}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Asignar Rol
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
