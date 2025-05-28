import React, { useEffect, useState } from "react";
import Navbar from "../components/Nabvar";
import { useNavigate } from "react-router-dom";

export default function Usuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [filteredUsuarios, setFilteredUsuarios] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [searchId, setSearchId] = useState("");
  const [userRol, setUserRol] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/api/usuarios", {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw new Error("Error al obtener usuarios");
        return res.json();
      })
      .then((data) => {
        setUsuarios(data);
        setFilteredUsuarios(data);
        fetchRolesForUsers(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  const fetchRolesForUsers = async (users) => {
    const rolesMap = {};

    for (const user of users) {
      try {
        const response = await fetch(
          `http://localhost:8080/api/usuarios-roles/usuario/${user.idUsuario}`,
          { credentials: "include" }
        );

        if (response.ok) {
          const rolesData = await response.json();
          if (rolesData.length > 0) {
            // Tomamos solo el primer rol (puedes ajustar esto según tus necesidades)
            rolesMap[user.idUsuario] = rolesData[0].idRol;
          }
        }
      } catch (error) {
        console.error(
          `Error obteniendo roles para usuario ${user.idUsuario}:`,
          error
        );
      }
    }

    setUserRol(rolesMap);
  };

  const getRolName = (idRol) => {
    switch (idRol) {
      case 1:
        return "Administrador";
      case 2:
        return "Profesor";
      case 3:
        return "Estudiante";
      default:
        return "S/R";
    }
  };

  const handleEdit = (idUsuario) => {
    navigate(`/usuario/editar/${idUsuario}`);
  };

  const handleRoleUpdate = (idUsuario) => {
    navigate(`/usuario/asignarRol/${idUsuario}`);
  };

  const handleSearch = (e) => {
    const searchTerm = e.target.value.toLowerCase();
    setSearchId(searchTerm);

    if (searchTerm === "") {
      setFilteredUsuarios(usuarios);
    } else {
      const filtered = usuarios.filter((user) =>
        user.idUsuario.toLowerCase().includes(searchTerm)
      );
      setFilteredUsuarios(filtered);
    }
  };

  if (error)
    return (
      <>
        <Navbar />
        <div className="p-4 text-red-600"> Error: {error}</div>;
      </>
    );
  if (loading)
    return (
      <>
        <Navbar />
        <div className="p-4">Cargando usuarios...</div>;
      </>
    );

  return (
    <>
      <Navbar />
      <div className="container mx-auto px-4 py-8">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
          <h2 className="text-2xl font-bold text-gray-800">
            Gestión de Usuarios
          </h2>

          <div className="flex flex-col sm:flex-row gap-4 w-full sm:w-auto">
            <div className="relative w-full sm:w-64">
              <input
                type="text"
                placeholder="Buscar por ID"
                value={searchId}
                onChange={handleSearch}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <svg
                  className="h-5 w-5 text-gray-400"
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                    clipRule="evenodd"
                  />
                </svg>
              </div>
            </div>

            <button
              onClick={() => navigate("/usuario/nuevo")}
              className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md transition duration-200 whitespace-nowrap"
            >
              Nuevo Usuario
            </button>
          </div>
        </div>

        {filteredUsuarios.length === 0 ? (
          <div className="bg-white rounded-lg shadow p-6 text-center">
            {searchId ? (
              <p className="text-gray-500">
                No se encontraron usuarios con el ID "{searchId}"
              </p>
            ) : (
              <p className="text-gray-500">No hay usuarios registrados</p>
            )}
          </div>
        ) : (
          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      ID
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      <div className=" flex-col justify-center items-center whitespace-nowrap  text-gray-500">
                        <div className="font-medium">Nombre</div>
                        <div className="text-gray-400">NickName</div>
                      </div>
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Apellidos
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Email
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Estado
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Rol
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      MFA
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Acciones
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filteredUsuarios.map((user) => (
                    <tr key={user.idUsuario} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {user.idUsuario}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <div className="font-medium">{user.nombres}</div>
                        <div className="text-gray-400">{user.nickname}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {user.apellidos}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        <div>{user.email}</div>
                        <div
                          className={`text-xs ${
                            user.emailVerificado
                              ? "text-green-500"
                              : "text-red-500"
                          }`}
                        >
                          {user.emailVerificado
                            ? "Verificado"
                            : "No verificado"}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span
                          className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                            user.estado === "ACTIVO"
                              ? "bg-green-100 text-green-800"
                              : "bg-red-100 text-red-800"
                          }`}
                        >
                          {user.estado}
                        </span>
                      </td>
                     <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        !userRol[user.idUsuario] ? 'bg-gray-100 text-gray-800' :
                        userRol[user.idUsuario] === 1 ? 'bg-purple-100 text-purple-800' :
                        userRol[user.idUsuario] === 2 ? 'bg-blue-100 text-blue-800' :
                        'bg-green-100 text-green-800'
                      }`}>
                        {getRolName(userRol[user.idUsuario])}
                      </span>
                    </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {user.mfaHabilitado ? (
                          <span className="text-green-600">Activado</span>
                        ) : (
                          <span className="text-gray-400">No</span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center space-x-2">
                          <button
                            onClick={() => handleEdit(user.idUsuario)}
                            className="inline-flex items-center px-3 py-1 border border-indigo-600 rounded-md shadow-sm text-sm font-medium text-indigo-600 bg-white hover:bg-indigo-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
                          >
                            <svg
                              className="h-4 w-4 mr-1"
                              fill="none"
                              viewBox="0 0 24 24"
                              stroke="currentColor"
                            >
                              <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                              />
                            </svg>
                            Editar
                          </button>

                          <button
                            onClick={() => handleRoleUpdate(user.idUsuario)}
                            className="inline-flex items-center px-3 py-1 border border-amber-600 rounded-md shadow-sm text-sm font-medium text-amber-700 bg-amber-50 hover:bg-amber-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-amber-500 transition-colors duration-200"
                          >
                            <svg
                              className="h-4 w-4 mr-1"
                              fill="none"
                              viewBox="0 0 24 24"
                              stroke="currentColor"
                            >
                              <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"
                              />
                            </svg>
                            Roles
                          </button>
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
    </>
  );
}
