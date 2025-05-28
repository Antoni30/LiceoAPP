import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./components/Login";
import Dashboard from "./Pages/Dashboard";
import Usuarios from "./Pages/Usuarios";
import UsuarioCrear from "./Pages/UsuarioNuevo";
import { AuthProvider } from "./hooks/AuthProvider";
import EditarUsuario from "./Pages/EditUser";
import AsignarRolUsuario from "./Pages/EditRoles";
import AgregarAnio from "./Pages/NuevoAnio";
import Materias from "./Pages/Materias";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/usuarios"
            element={
              <ProtectedRoute>
                <Usuarios />
              </ProtectedRoute>
            }
          />

           <Route
            path="/usuario/nuevo"
            element={
              <ProtectedRoute>
                <UsuarioCrear />
              </ProtectedRoute>
            }
          />

           <Route
            path="/usuario/editar/:id"
            element={
              <ProtectedRoute>
                <EditarUsuario />
              </ProtectedRoute>
            }
          />

            <Route
            path="/usuario/asignarRol/:idUsuario"
            element={
              <ProtectedRoute>
                <AsignarRolUsuario />
              </ProtectedRoute>
            }
          />
           <Route
            path="/agregarAnio"
            element={
              <ProtectedRoute>
                <AgregarAnio />
              </ProtectedRoute>
            }
          />
          <Route
            path="/materias"
            element={
              <ProtectedRoute>
                <Materias />
              </ProtectedRoute>
            }
          />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
