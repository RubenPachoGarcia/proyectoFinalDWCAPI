package proyectoFinalDWCAPI.daos;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro", schema = "proyecto")
public class RegistroDao {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegistro;

    @OneToOne(fetch = FetchType.EAGER) // 🚀 IMPORTANTE: Asegura que siempre cargue el usuario
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private UsuarioDao usuario;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    public boolean estaExpirado() {
        return fechaFin.isBefore(LocalDateTime.now());
    }

    // Getters y Setters
    
	public Long getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(Long idRegistro) {
		this.idRegistro = idRegistro;
	}

	public UsuarioDao getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDao usuario) {
		this.usuario = usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}
}