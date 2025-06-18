import { useAuth } from "../hooks/AuthProvider";
import { Navigate, Outlet } from "react-router-dom";
import LoadingSpinner from "./LoadingSpinner";

const ProtectedRoute = ({ allowedRoles, redirectPath = '/no-autorizado' }) => {
  const { isAuthenticated, userRole, isLoading } = useAuth();

  if (isLoading) {
    return <LoadingSpinner />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }


  if (allowedRoles && !allowedRoles.includes(userRole?.toLowerCase())) {
    return <Navigate to={redirectPath} replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;