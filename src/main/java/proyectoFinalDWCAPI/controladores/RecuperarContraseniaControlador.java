package proyectoFinalDWCAPI.controladores;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import proyectoFinalDWCAPI.dtos.RecuperarContraseniaDto;
import proyectoFinalDWCAPI.servicios.TokenServicio;
import proyectoFinalDWCAPI.servicios.UsuarioServicio;

@RestController
@RequestMapping("/api/recuperar")
public class RecuperarContraseniaControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private TokenServicio tokenServicio;

    @PostMapping("/solicitar")
    public ResponseEntity<?> recuperarClave(@RequestBody RecuperarContraseniaDto solicitud) {
        try {
            tokenServicio.enviarCorreoRecuperacion(solicitud.getCorreoUsuario());
            return ResponseEntity.ok(Map.of("mensaje", "Se ha enviado un correo con instrucciones para recuperar la clave."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al enviar el correo: " + e.getMessage()));
        }
    }

    @PostMapping("/cambiar")
    public ResponseEntity<?> restablecerPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        String nuevaContrasenia = requestBody.get("contraseniaUsuario");

        System.out.println("Token: " + token);
        System.out.println("Nueva contraseña: " + nuevaContrasenia);

        if (nuevaContrasenia == null || nuevaContrasenia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "La contraseña no puede estar vacía."));
        }

        boolean actualizado = usuarioServicio.actualizarContrasenia(token, nuevaContrasenia);
        
        if (actualizado) {
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña restablecida con éxito."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Token inválido o usuario no encontrado."));
        }
    }
}
