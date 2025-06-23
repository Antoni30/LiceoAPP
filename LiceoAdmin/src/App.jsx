import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./components/Login";
import Dashboard from "./Pages/admin/Dashboard";
import Usuarios from "./Pages/admin/Usuarios";
import UsuarioCrear from "./Pages/admin/UsuarioNuevo";
import { AuthProvider } from "./hooks/AuthProvider";
import EditarUsuario from "./Pages/admin/EditUser";
import AsignarRolUsuario from "./Pages/admin/EditRoles";
import AgregarAnio from "./Pages/admin/NuevoAnio";
import Materias from "./Pages/admin/Materias";
import CursosPorAnio from "./Pages/admin/Cursos";
import MateriasDelCurso from "./Pages/admin/MateriaCurso";
import Participantes from "./Pages/admin/Participantes";
import ProfesorPerfil from "./Pages/profesor/ProfesorPerfil";
import NoAutorizado from "./Pages/NoAutorizado";
import Home from "./Pages/Home";
import NotFound from "./Pages/NoFound";
import Perfil from "./Pages/Perfil";

function App() {
  return (
     <BrowserRouter>
    <AuthProvider>
  
        <Routes>
          {/* Rutas públicas */}
          <Route path="/" element={<Login />} />
          <Route path="/no-autorizado" element={<NoAutorizado />} />
          
          {/* Rutas para cualquier usuario autenticado */}
          <Route element={<ProtectedRoute />}>
            <Route path="/home" element={<Home />} />
            <Route path="/perfil" element={<Perfil/>} />
             <Route path="/usuario/editar/:id" element={<EditarUsuario />} />
          </Route>

          {/* Rutas solo para administradores */}
          <Route element={<ProtectedRoute allowedRoles={['administrador']} />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/usuarios" element={<Usuarios />} />
            <Route path="/usuario/nuevo" element={<UsuarioCrear />} />
            <Route path="/usuario/asignarRol/:idUsuario" element={<AsignarRolUsuario />} />
            <Route path="/agregarAnio" element={<AgregarAnio />} />
            <Route path="/materias" element={<Materias />} />
            <Route path="/cursos/:idanio" element={<CursosPorAnio />} />
            <Route path="/cursos/materia/:idCurso" element={<MateriasDelCurso />} />
            <Route path="/curso/participantes/:idCurso" element={<Participantes />} />
          </Route>

          {/* Rutas solo para profesores */}
          <Route element={<ProtectedRoute allowedRoles={['profesor']} />}>
            <Route path="/profesor/:idProfesor" element={<ProfesorPerfil />} />
            {/* Agrega aquí otras rutas específicas para profesores */}
          </Route>

          {/* Manejo de rutas no encontradas */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      
    </AuthProvider>
    </BrowserRouter>
  );
}

export default App;