package proyectoFinalDWCAPI.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoServicio {
	
	@Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destinatarioCorreo, String asuntoCorreo, String mensajeCorreo) {
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setTo(destinatarioCorreo);
        correo.setSubject(asuntoCorreo);
        correo.setText(mensajeCorreo);
        mailSender.send(correo);
    }
}
