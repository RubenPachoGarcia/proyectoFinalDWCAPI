package proyectoFinalDWCAPI.servicios;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import proyectoFinalDWCAPI.daos.TokenDao;
import proyectoFinalDWCAPI.daos.UsuarioDao;
import proyectoFinalDWCAPI.repositorios.TokenRepositorio;
import proyectoFinalDWCAPI.repositorios.UsuarioRepositorio;

@Service
public class TokenServicio {
	@Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TokenRepositorio tokenRepositorio;

    @Autowired
    private CorreoServicio correoServicio;

    public void enviarCorreoRecuperacion(String correoUsuario) {
        UsuarioDao usuario = usuarioRepositorio.findByCorreoUsuario(correoUsuario);
        if (usuario == null) {
            System.out.println("⚠️ Usuario no encontrado: " + correoUsuario);
            return; // No lanzar excepción, solo termina la ejecución
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime nuevaFechaFin = LocalDateTime.now().plusMinutes(30);

        // Buscar si ya existe un token para el usuario
        Optional<TokenDao> tokenExistente = tokenRepositorio.findByUsuario(usuario);

        if (tokenExistente.isPresent()) {
            TokenDao tokenDao = tokenExistente.get();
            tokenDao.setToken(token);
            tokenDao.setFechaFin(nuevaFechaFin);
            tokenRepositorio.save(tokenDao);
        } else {
            TokenDao tokenDao = new TokenDao();
            tokenDao.setToken(token);
            tokenDao.setUsuario(usuario);
            tokenDao.setFechaFin(nuevaFechaFin);
            tokenRepositorio.save(tokenDao);
        }

        String enlaceRecuperacion = "http://localhost:4200/cambiar-contrasenia?token=" + token;
        
        correoServicio.enviarCorreo(correoUsuario, "Recuperación de Contraseña",
        		"<p>Haga clic en el siguiente enlace para cambiar su contraseña:</p>"
                        + "<p><a href='" + enlaceRecuperacion);
        System.out.println("Correo de recuperación enviado a: " + correoUsuario);
    }

    public void restablecerClave(String token, String nuevaContrasenia) {
        TokenDao tokenDao = tokenRepositorio.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (tokenDao.esFin()) {
            throw new RuntimeException("El token ha expirado");
        }

        UsuarioDao usuario = tokenDao.getUsuario();
        usuario.setContraseniaUsuario(new BCryptPasswordEncoder().encode(nuevaContrasenia));
        usuarioRepositorio.save(usuario);

        tokenRepositorio.delete(tokenDao);
    }
}
