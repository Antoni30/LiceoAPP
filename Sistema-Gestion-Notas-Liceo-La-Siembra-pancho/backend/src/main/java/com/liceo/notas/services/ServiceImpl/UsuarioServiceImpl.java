package com.liceo.notas.services.ServiceImpl;

import com.liceo.notas.dtos.UsuarioDTO;
import com.liceo.notas.dtos.mappers.UsuarioMapper;
import com.liceo.notas.entities.Usuario;
import com.liceo.notas.exceptions.EmailInvalidoException;
import com.liceo.notas.repositories.UsuarioRepository;
import com.liceo.notas.services.EmailService;
import com.liceo.notas.services.UsuarioService;
import com.liceo.notas.services.ValidacionCedulaEcuatoriana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO dto) {
        // 1. Validaciones básicas
        if(repository.findByNickname(dto.getNickname()).isPresent()) {
            throw new RuntimeException("El nickname ya está en uso");
        }
        // Validación de cédula
        if (!ValidacionCedulaEcuatoriana.validar(dto.getIdUsuario())) {
            throw new IllegalArgumentException("La cédula ingresada no es válida para Ecuador");
        }

        // En UsuarioServiceImpl.java
        if(!dto.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new EmailInvalidoException("El formato del email es inválido. Debe ser ejemplo@dominio.com");
        }

        // 2. Preparar entidad
        String contrasenaHasheada = passwordEncoder.encode(dto.getContrasena());
        dto.setContrasena(contrasenaHasheada);
        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacion(UUID.randomUUID().toString());

        // 3. Guardar en BD
        usuario = repository.save(usuario);

        // 4. Enviar email de verificación
        enviarEmailVerificacion(usuario);

        return UsuarioMapper.toDTO(usuario);
    }




    @Override
    @Transactional
    public void enviarEmailVerificacion(Usuario usuario) {
        String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + usuario.getTokenVerificacion();

        String htmlContent = String.format(
                "<html>" +
                        "<body>" +
                        "<h2>Verificación de Email</h2>" +
                        "<p>Por favor haz clic en el siguiente enlace para verificar tu email:</p>" +
                        "<a href='%s' style='display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;'>" +
                        "Verificar Email</a>" +
                        "</body>" +
                        "</html>",

                verificationUrl
        );

        emailService.sendHtmlEmail(
                usuario.getEmail(),
                "Verifica tu email - Sistema de Notas del usuario:"+usuario.getNombres()+" "+usuario.getApellidos(),
                htmlContent
        );
    }

    @Override
    @Transactional
    public boolean verificarEmail(String token) {
        Optional<Usuario> usuarioOpt = repository.findByTokenVerificacion(token);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEmailVerificado(true);
            usuario.setTokenVerificacion(null); // Eliminar token después de usar
            repository.save(usuario);
            return true;
        }
        return false;
    }

    // Métodos existentes (sin cambios excepto donde se indique)
    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> obtenerPorId(String id) {
        return repository.findById(id)
                .map(UsuarioMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return repository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<UsuarioDTO> actualizarUsuario(String id, UsuarioDTO dto) {
        return repository.findById(id)
                .map(usuario -> {
                    // Validar email si está siendo modificado
                    if(!usuario.getEmail().equals(dto.getEmail())) {
                        usuario.setEmail(dto.getEmail());
                        usuario.setEmailVerificado(false); // Requiere nueva verificación
                        usuario.setTokenVerificacion(UUID.randomUUID().toString());
                        enviarEmailVerificacion(usuario);
                    }
                    // Actualizar campos básicos
                    usuario.setNombres(dto.getNombres());
                    usuario.setApellidos(dto.getApellidos());

                    // Verificar si el nickname cambió y es único
                    if(!usuario.getNickname().equals(dto.getNickname())) {
                        if(repository.findByNickname(dto.getNickname()).isPresent()) {
                            throw new RuntimeException("El nuevo nickname ya está en uso");
                        }
                        usuario.setNickname(dto.getNickname());
                    }

                    // Hashear la nueva contraseña si se está actualizando
                    if(dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
                        String contrasenaHasheada = passwordEncoder.encode(dto.getContrasena());
                        usuario.setContrasena(contrasenaHasheada);
                    }

                    usuario.setEstado(dto.getEstado());
                    usuario.setMfaHabilitado(dto.isMfaHabilitado());
                    usuario = repository.save(usuario);
                    return UsuarioMapper.toDTO(usuario);
                });
    }

    @Override
    @Transactional
    public boolean eliminarUsuario(String id) {
        if(repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarPorEstado(String estado) {
        return repository.findByEstado(estado).stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }


}