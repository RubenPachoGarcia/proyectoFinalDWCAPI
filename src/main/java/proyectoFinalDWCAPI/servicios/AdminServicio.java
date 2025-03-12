package proyectoFinalDWCAPI.servicios;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyectoFinalDWCAPI.daos.UsuarioDao;
import proyectoFinalDWCAPI.repositorios.AdminRepositorio;

/**
 * Servicio para la gestión de administradores y usuarios.
 * 
 * @author irodhan - 06/03/2025
 */
@Service
public class AdminServicio {

    @Autowired
    private AdminRepositorio adminRepositorio;

    private static final Logger logger = LoggerFactory.getLogger(AdminServicio.class);

    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     *
     * @return Lista de todos los usuarios.
     */
    public List<UsuarioDao> obtenerTodosLosUsuarios() {
        logger.info("Obteniendo todos los usuarios de la base de datos");
        List<UsuarioDao> usuarios = adminRepositorio.findAll();
        logger.info("Usuarios obtenidos: {}", usuarios.size());
        return usuarios;
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    public Optional<UsuarioDao> obtenerUsuarioPorCorreo(String correoUsuario) {
        logger.info("Buscando usuario con correo: {}", correoUsuario);
        Optional<UsuarioDao> usuario = adminRepositorio.findByCorreoUsuario(correoUsuario);
        if (usuario.isPresent()) {
            logger.info("Usuario encontrado con correo: {}", correoUsuario);
        } else {
            logger.warn("No se encontró usuario con correo: {}", correoUsuario);
        }
        return usuario;
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id Identificador del usuario a eliminar.
     * @return true si el usuario fue eliminado, false si no se encontró.
     */
    public boolean eliminarUsuario(Long idUsuario) {
        logger.info("Solicitud para eliminar usuario con ID: {}", idUsuario);
        if (adminRepositorio.existsById(idUsuario)) {
            adminRepositorio.deleteById(idUsuario);
            logger.info("Usuario con ID {} eliminado correctamente", idUsuario);
            System.out.println("Usuario con ID " + idUsuario + " eliminado correctamente.");
            return true;
        } else {
            logger.warn("Usuario con ID {} no encontrado", idUsuario);
            System.err.println("Usuario con ID " + idUsuario + " no encontrado.");
            return false;
        }
    }
}
