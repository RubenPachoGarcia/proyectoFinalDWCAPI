package proyectoFinalDWCAPI.servicios;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyectoFinalDWCAPI.daos.RegistroDao;
import proyectoFinalDWCAPI.daos.UsuarioDao;
import proyectoFinalDWCAPI.dtos.RegistroDto;
import proyectoFinalDWCAPI.repositorios.RegistroRepositorio;
import proyectoFinalDWCAPI.repositorios.TokenRepositorio;
import proyectoFinalDWCAPI.repositorios.UsuarioRepositorio;

/**
 * Servicio encargado de la lógica de usuario: autenticación, registro y confirmación.
 */
@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private PasswordEncoder encriptacion;
    
    @Autowired
    private TokenRepositorio tokenRepositorio;
    
    @Autowired
    private RegistroRepositorio registroRepositorio;

    /**
     * Valida las credenciales del usuario.
     *
     * @param correoUsuario     Email del usuario.
     * @param contraseniaUsuario Contraseña sin encriptar.
     * @return ResponseEntity con el valor de esAdmin si las credenciales son correctas;
     *         de lo contrario, retorna 401.
     */
    public ResponseEntity<String> validarCredenciales(String correoUsuario, String contraseniaUsuario) {
        UsuarioDao usuario = usuarioRepositorio.findByCorreoUsuario(correoUsuario);
        if (usuario == null || !encriptacion.matches(contraseniaUsuario, usuario.getContraseniaUsuario())) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
        return ResponseEntity.ok(usuario.getEsAdmin());
    }
    
    /**
     * Verifica si ya existe un usuario con el correo indicado.
     *
     * @param correoUsuario Email a verificar.
     * @return true si el usuario ya existe, false en caso contrario.
     */
    public boolean correoExisteUsuario(String correoUsuario) {
        return usuarioRepositorio.existsByCorreoUsuario(correoUsuario);
    }
    
    /**
     * Registra un usuario de forma directa (sin proceso de confirmación).
     *
     * @param usuarioDto Datos del usuario a registrar.
     */
    public void registroUsuario(RegistroDto usuarioDto) {
        if (usuarioDto.getCorreoUsuario() == null || usuarioDto.getCorreoUsuario().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }

        UsuarioDao usuario = new UsuarioDao();
        usuario.setNombreCompletoUsuario(usuarioDto.getNombreCompletoUsuario());
        usuario.setCorreoUsuario(usuarioDto.getCorreoUsuario());
        usuario.setTelefonoUsuario(usuarioDto.getTelefonoUsuario());
        usuario.setContraseniaUsuario(encriptacion.encode(usuarioDto.getContraseniaUsuario()));
        usuario.setEsAdmin("false");
        usuario.setEsPremium("false");

        // Asignar la foto si existe en el DTO, convirtiéndola a byte[]
        if (usuarioDto.getFotoUsuario() != null && !usuarioDto.getFotoUsuario().isEmpty()) {
            try {
                byte[] fotoBytes = usuarioDto.getFotoUsuario().getBytes(); // Convertir MultipartFile a byte[]
                usuario.setFotoUsuario(fotoBytes);  // Asignar la foto como byte[]
            } catch (IOException e) {
                System.err.println("Error al convertir la foto a bytes: " + e.getMessage());
            }
        }

        usuarioRepositorio.save(usuario);
    }
    
    /**
     * Actualiza la contraseña de un usuario identificado por un token.
     *
     * @param token            Token asociado al usuario.
     * @param nuevaContrasenia Nueva contraseña sin encriptar.
     * @return true si la actualización fue exitosa; false en caso contrario.
     */
    @Transactional
    public boolean actualizarContrasenia(String token, String nuevaContrasenia) {
        UsuarioDao usuario = usuarioRepositorio.findByToken(token);
        if (usuario == null) {
            System.err.println("ERROR: Token inválido o usuario no encontrado.");
            return false;
        }
        usuario.setContraseniaUsuario(encriptacion.encode(nuevaContrasenia));
        usuarioRepositorio.save(usuario);
        tokenRepositorio.deleteByToken(token);
        System.out.println("Contraseña actualizada con éxito para el usuario: " + usuario.getCorreoUsuario());
        return true;
    }
    
    /**
     * Guarda el registro de un usuario generando un token de confirmación.
     * Se incluye el procesamiento de la foto (ya convertida a byte[] en el DTO).
     *
     * @param usuarioDto Datos del usuario a registrar.
     * @param token      Token generado para la confirmación.
     * @param fechaFin   Fecha límite para confirmar el registro.
     */
    @Transactional
    public void guardarRegistro(RegistroDto usuarioDto, String token, LocalDateTime fechaFin) {
        System.out.println("Recibiendo datos del usuario...");
        UsuarioDao usuario = new UsuarioDao();
        usuario.setNombreCompletoUsuario(usuarioDto.getNombreCompletoUsuario());
        usuario.setCorreoUsuario(usuarioDto.getCorreoUsuario());
        usuario.setTelefonoUsuario(usuarioDto.getTelefonoUsuario());
        usuario.setContraseniaUsuario(encriptacion.encode(usuarioDto.getContraseniaUsuario()));
        usuario.setEsAdmin("false");
        usuario.setEsPremium("false");

        // Si la foto está presente, convertir MultipartFile a byte[]
        if (usuarioDto.getFotoUsuario() != null && !usuarioDto.getFotoUsuario().isEmpty()) {
            try {
                byte[] fotoBytes = usuarioDto.getFotoUsuario().getBytes(); // Convertir MultipartFile a byte[]
                usuario.setFotoUsuario(fotoBytes);  // Asignar la foto como byte[]
            } catch (IOException e) {
                System.err.println("Error al convertir la foto a bytes: " + e.getMessage());
            }
        }

        usuarioRepositorio.save(usuario);
        System.out.println("Usuario guardado con éxito.");

        RegistroDao registro = new RegistroDao();
        registro.setUsuario(usuario);
        registro.setToken(token);
        registro.setFechaFin(fechaFin);
        registroRepositorio.save(registro);
        System.out.println("Registro guardado con token: " + token);
    }
    
    /**
     * Confirma el registro de un usuario utilizando el token enviado.
     *
     * @param token Token enviado al usuario para confirmar su registro.
     * @return true si la confirmación fue exitosa; false en caso contrario.
     */
    @Transactional
    public boolean confirmarRegistro(String token) {
        Optional<RegistroDao> registroOpcional = registroRepositorio.findByToken(token);
        if (registroOpcional.isEmpty()) {
            System.out.println("Token no encontrado en la base de datos.");
            return false;
        }
        RegistroDao registro = registroOpcional.get();
        if (registro.getFechaFin().isBefore(LocalDateTime.now())) {
            System.out.println("Token expirado.");
            return false;
        }
        UsuarioDao usuario = registro.getUsuario();
        if (usuario == null) {
            System.out.println("Usuario no encontrado en la base de datos.");
            return false;
        }
        // Aquí se podría actualizar alguna propiedad para marcar al usuario como confirmado.
        usuarioRepositorio.save(usuario);
        System.out.println("Usuario confirmado correctamente: " + usuario.getCorreoUsuario());
        registroRepositorio.delete(registro);
        System.out.println("Registro eliminado.");
        return true;
    }
}
