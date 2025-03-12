package proyectoFinalDWCAPI.controladores;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import proyectoFinalDWCAPI.dtos.RegistroDto;
import proyectoFinalDWCAPI.servicios.CorreoServicio;
import proyectoFinalDWCAPI.servicios.UsuarioServicio;

@RestController
@RequestMapping("/api/registro")
public class RegistroControlador {

    @Autowired
    private CorreoServicio correoServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    /**
     * Endpoint para registrar un usuario.
     * Ahora acepta datos en formato multipart/form-data, lo que permite enviar
     * tanto los datos del usuario como un archivo opcional (foto).
     *
     * @param usuarioDto Objeto con los datos del registro, vinculado desde los campos del form.
     * @param fotoUsuario Archivo opcional enviado en el form (nombre del parámetro: "fotoUsuario").
     * @return ResponseEntity con el mensaje de confirmación o error.
     */
    @PostMapping(value = "/usuario", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> registroUsuario(
            @ModelAttribute RegistroDto usuarioDto,
            @RequestParam(value = "fotoUsuario", required = false) MultipartFile fotoUsuario) {
        
        Map<String, String> response = new HashMap<>();

        try {
            // Validar que el correo no sea vacío o nulo
            if (usuarioDto.getCorreoUsuario() == null || usuarioDto.getCorreoUsuario().isEmpty()) {
                response.put("message", "El correo es obligatorio.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Verificar si el correo ya está registrado
            if (usuarioServicio.correoExisteUsuario(usuarioDto.getCorreoUsuario())) {
                response.put("message", "El email ya está registrado.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Generar token de registro y establecer tiempo de expiración
            String token = UUID.randomUUID().toString();
            LocalDateTime fechaFin = LocalDateTime.now().plusDays(7);

            // Verificar si el archivo de foto fue proporcionado
            if (fotoUsuario != null && !fotoUsuario.isEmpty()) {
                // Aquí puedes procesar el archivo y guardarlo en algún lugar, como en disco o en una base de datos.
                byte[] fotoBytes = fotoUsuario.getBytes();
                // Procesar la foto si es necesario.
            }

            // Guardar el registro de usuario
            usuarioServicio.guardarRegistro(usuarioDto, token, fechaFin);

            // Generar enlace de confirmación y enviar correo
            String enlaceConfirmacion = "http://localhost:8081/api/registro/confirmar?token=" + token;
            correoServicio.enviarCorreo(usuarioDto.getCorreoUsuario(), "Confirma tu cuenta",
                    "Haz clic en el siguiente enlace para activar tu cuenta: " + enlaceConfirmacion);

            response.put("message", "Correo de confirmación enviado.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error interno del servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
