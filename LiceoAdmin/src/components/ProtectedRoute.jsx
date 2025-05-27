import { Navigate } from "react-router-dom";
import { useAuth } from "../hooks/AuthProvider";

function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/" replace />; // redirige al login si no está autenticado
  }
  return children; // si está autenticado, muestra el componente hijo
}

export default ProtectedRoute;
