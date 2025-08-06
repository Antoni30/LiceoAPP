const API_BASE_URL = "http://localhost:8080/api";

const DEFAULT_OPTIONS = {
  credentials: "include",
  headers: {
    "Content-Type": "application/json",
  },
};

class ApiService {
  async request(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const config = {
      ...DEFAULT_OPTIONS,
      ...options,
      headers: {
        ...DEFAULT_OPTIONS.headers,
        ...options.headers,
      },
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
      }

      const contentType = response.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        return await response.json();
      }
      
      return response;
    } catch (error) {
      if (error.name === 'TypeError' && error.message.includes('fetch')) {
        throw new Error('Error de conexión. Verifica que el servidor esté funcionando.');
      }
      throw error;
    }
  }

  // Auth endpoints
  async login(idUsuario, contrasena) {
    return this.request("/auth/login", {
      method: "POST",
      body: JSON.stringify({ idUsuario, contrasena }),
    });
  }

  async verifyMfa(idUsuario, code) {
    return this.request("/auth/verify-mfa", {
      method: "POST",
      body: JSON.stringify({ idUsuario, code }),
    });
  }

  async resendVerificationEmail(nickname, email) {
    return this.request("/auth/resend-verification-email", {
      method: "POST",
      body: JSON.stringify({ nickname, email }),
    });
  }

  // User endpoints
  async getUsers() {
    return this.request("/usuarios");
  }

  async getUser(id) {
    return this.request(`/usuarios/${id}`);
  }

  async updateUser(id, userData) {
    return this.request(`/usuarios/${id}`, {
      method: "PUT",
      body: JSON.stringify(userData),
    });
  }

  async createUser(userData) {
    return this.request("/usuarios", {
      method: "POST",
      body: JSON.stringify(userData),
    });
  }

  // User roles endpoints
  async getUserRoles(userId) {
    return this.request(`/usuarios-roles/usuario/${userId}`);
  }

  // Role endpoints
  async getRole(roleId) {
    return this.request(`/roles/${roleId}`);
  }

  // Materias endpoints
  async getMaterias() {
    return this.request("/materias");
  }

  async createMateria(materiaData) {
    return this.request("/materias", {
      method: "POST",
      body: JSON.stringify(materiaData),
    });
  }

  async updateMateria(id, materiaData) {
    return this.request(`/materias/${id}`, {
      method: "PUT",
      body: JSON.stringify(materiaData),
    });
  }

  async deleteMateria(id) {
    return this.request(`/materias/${id}`, {
      method: "DELETE",
    });
  }
}

export default new ApiService();