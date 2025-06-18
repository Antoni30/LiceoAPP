/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [authState, setAuthState] = useState({
    isAuthenticated: false,
    userRole: null,
    isLoading: true, // Para manejar el estado de carga inicial
  });
  const navigate = useNavigate();

  // Verificar autenticación al cargar (por ejemplo, si hay datos en localStorage)
  useEffect(() => {
    const checkAuth = () => {
      const storedAuth = localStorage.getItem('auth');
      if (storedAuth) {
        const { isAuthenticated, userRole } = JSON.parse(storedAuth);
        setAuthState({ isAuthenticated, userRole, isLoading: false });
      } else {
        setAuthState(prev => ({ ...prev, isLoading: false }));
      }
    };
    
    checkAuth();
  }, []);

  const login = (role) => {
    const newAuthState = {
      isAuthenticated: true,
      userRole: role,
      isLoading: false,
    };
    setAuthState(newAuthState);
    localStorage.setItem('auth', JSON.stringify(newAuthState));
  };

  const logout = () => {
    setAuthState({
      isAuthenticated: false,
      userRole: null,
      isLoading: false,
    });
    localStorage.removeItem('auth');
    navigate('/login');
  };

  // Función para verificar roles
  const hasRole = (requiredRoles) => {
    if (!requiredRoles) return true;
    if (!authState.userRole) return false;
    return requiredRoles.some(role => 
      authState.userRole.toLowerCase().includes(role.toLowerCase())
    );
  };

  return (
    <AuthContext.Provider 
      value={{
        ...authState,
        login,
        logout,
        hasRole,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe usarse dentro de un AuthProvider');
  }
  return context;
}