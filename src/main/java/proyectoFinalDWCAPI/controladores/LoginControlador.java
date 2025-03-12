package proyectoFinalDWCAPI.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import proyectoFinalDWCAPI.daos.UsuarioDao;
import proyectoFinalDWCAPI.dtos.LoginDto;
import proyectoFinalDWCAPI.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/api/login")
public class LoginControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private PasswordEncoder encriptacion;

    @PostMapping("/usuario")
    public ResponseEntity<String> autenticarUsuario(@RequestBody LoginDto usuario) {
        UsuarioDao usuarioDao = usuarioRepositorio.findByCorreoUsuario(usuario.getCorreoUsuario());

        if (usuarioDao == null || !encriptacion.matches(usuario.getContraseniaUsuario(), usuarioDao.getContraseniaUsuario())) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos.");
        }

        return ResponseEntity.ok(usuarioDao.getEsAdmin());
    }

}
