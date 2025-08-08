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

  async getMateriaID(idMateria) {
    return this.request(`/materias/${idMateria}`);
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

  // Cursos endpoints
  async getCursosPorAnio(idAnio) {
    return this.request(`/cursos/por-anio/${idAnio}`);
  }

  async createCurso(cursoData) {
    return this.request("/cursos", {
      method: "POST",
      body: JSON.stringify(cursoData),
    });
  }

  async updateCurso(id, cursoData) {
    return this.request(`/cursos/${id}`, {
      method: "PUT",
      body: JSON.stringify(cursoData),
    });
  }

  async deleteCurso(id) {
    return this.request(`/cursos/${id}`, {
      method: "DELETE",
    });
  }

  async getCurso(id) {
    return this.request(`/cursos/${id}`);
  }

  // Años lectivos endpoints
  async getAniosLectivos() {
    return this.request("/anios-lectivos");
  }

  async getAniosLectivosActivos() {
    return this.request("/anios-lectivos/activos");
  }

  async createAnioLectivo(anioData) {
    return this.request("/anios-lectivos", {
      method: "POST",
      body: JSON.stringify(anioData),
    });
  }

  async updateAnioLectivoEstado(id, activo) {
    return this.request(`/anios-lectivos/estado/${id}`, {
      method: "PUT",
      body: JSON.stringify({ activo }),
    });
  }

  async updateAnioLectivo(id, anioData) {
    return this.request(`/anios-lectivos/${id}`, {
      method: "PUT",
      body: JSON.stringify(anioData),
    });
  }

  // Roles endpoints
  async getRoles() {
    return this.request("/roles");
  }

  async deleteUserRole(userId, roleId) {
    return this.request(`/usuarios-roles/usuario/${userId}/rol/${roleId}`, {
      method: "DELETE",
    });
  }

  async assignUserRole(idUsuario, idRol) {
   
    return this.request("/usuarios-roles", {
      method: "POST",
      body: JSON.stringify({
      idUsuario,
      idRol
    })
    });
  }

  // Logout endpoint
  async logout() {
    return this.request("/auth/logout", {
      method: "POST",
    });
  }

  // Cursos-Materias endpoints
  async getCursoMaterias(idCurso) {
    return this.request(`/cursos-materias/curso/${idCurso}`);
  }

  async createCursoMateria(idCurso,idMateria) {
    return this.request("/cursos-materias", {
      method: "POST",
      body: JSON.stringify({ idCurso, idMateria }),
    });
  }

  async deleteCursoMateria(idCurso, idMateria) {
    return this.request(`/cursos-materias/curso/${idCurso}/materia/${idMateria}`, {
      method: "DELETE",
    });
  }

  // Usuarios-Cursos endpoints
  async getUserCursos(userId) {
    return this.request(`/usuarios-cursos/usuario/${userId}`);
  }

  async getCursoUsuarios(cursoId) {
    return this.request(`/usuarios-cursos/curso/${cursoId}`);
  }

  async createUsuarioCurso(idUsuario, idCurso) {
    return this.request("/usuarios-cursos", {
      method: "POST",
      body: JSON.stringify({
        idUsuario,
        idCurso
      }),
    });
  }

  async deleteUsuarioCurso(userId, cursoId) {
    return this.request(`/usuarios-cursos/usuario/${userId}/curso/${cursoId}`, {
      method: "DELETE",
    });
  }

  // Notas endpoints
  async getNotasByUsuarioMateria(idUsuario, idMateria) {
    return this.request(`/notas/usuario/${idUsuario}/materia/${idMateria}`);
  }

  async createNota(idUsuario, idMateria, nota, parcial) {
    return this.request("/notas", {
      method: "POST",
      body: JSON.stringify({ idUsuario, idMateria, nota, parcial }),
    });
  }


  async updateNota(idNota, idUsuario, idMateria, nota, parcial) {
    return this.request(`/notas/${idNota}`, {
      method: "PUT",
      body: JSON.stringify({ idUsuario, idMateria, nota, parcial }),
    });
  }

  async notasParcial(idUsuario,parcial) {
    return this.request(`/notas/usuario/${idUsuario}/parcial/${parcial}`);
  }

  // Profesor-Materias endpoints (si existe este endpoint)
  async getProfesorMaterias(idProfesor) {
    return this.request(`/profesores/${idProfesor}/materias`);
  }
}

export default new ApiService();