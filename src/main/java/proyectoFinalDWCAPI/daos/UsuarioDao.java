package proyectoFinalDWCAPI.daos;

import java.util.Arrays;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Clase entidad que representa un usuario */
@Entity
@Table(name = "usuarios", schema = "proyecto")
public class UsuarioDao {
	
	/** Atributos del usuario*/
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", updatable = false)
    private long idUsuario;

    @Column(name = "nombre_completo_usuario", length = 50)
    private String nombreCompletoUsuario;
    
    @Column(name = "correo_usuario", length = 50, unique = true)
    private String correoUsuario;

    @Column(name = "telefono_usuario")
    private String telefonoUsuario;
    
    @Column(name = "foto_usuario", columnDefinition = "bytea")
    private byte[] fotoUsuario;
    
    @Column(name = "contrasenia_usuario", length = 100)
    private String contraseniaUsuario;

    @Column(name = "es_admin")
    private String esAdmin;

    @Column(name = "es_premium", length = 5)
    private String esPremium;
    
    /** Constructor con los campos del usuario*/ 
    public UsuarioDao(String nombreCompletoUsuario, String correoUsuario, String telefonoUsuario,
			byte[] fotoUsuario, String contraseniaUsuario, String esAdmin, String esPremium) {
		super();
		this.nombreCompletoUsuario = nombreCompletoUsuario;
		this.correoUsuario = correoUsuario;
		this.telefonoUsuario = telefonoUsuario;
		this.fotoUsuario = fotoUsuario;
		this.contraseniaUsuario = contraseniaUsuario;
		this.esAdmin = esAdmin;
		this.esPremium = esPremium;
	}

    /** Constructor vacio */
	public UsuarioDao() {
    	
    }
    
	/** Getters y setters
	 * Get-lectura
	 * Set-escritura */
	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreCompletoUsuario() {
		return nombreCompletoUsuario;
	}

	public void setNombreCompletoUsuario(String nombreCompletoUsuario) {
		this.nombreCompletoUsuario = nombreCompletoUsuario;
	}

	public String getCorreoUsuario() {
		return correoUsuario;
	}

	public void setCorreoUsuario(String correoUsuario) {
		this.correoUsuario = correoUsuario;
	}

	public String getTelefonoUsuario() {
		return telefonoUsuario;
	}

	public void setTelefonoUsuario(String telefonoUsuario) {
		this.telefonoUsuario = telefonoUsuario;
	}

	public byte[] getFotoUsuario() {
		return fotoUsuario;
	}

	public void setFotoUsuario(byte[] fotoUsuario) {
		this.fotoUsuario = fotoUsuario;
	}

	public String getContraseniaUsuario() {
		return contraseniaUsuario;
	}

	public void setContraseniaUsuario(String contraseniaUsuario) {
		this.contraseniaUsuario = contraseniaUsuario;
	}

	public String getEsAdmin() {
		return esAdmin;
	}

	public void setEsAdmin(String esAdmin) {
		this.esAdmin = esAdmin;
	}

	public String getEsPremium() {
		return esPremium;
	}

	public void setEsPremium(String esPremium) {
		this.esPremium = esPremium;
	}
	
	/** Metodo toString */
	@Override
	public String toString() {
		return "UsuarioDao [idUsuario=" + idUsuario + ", nombreCompletoUsuario=" + nombreCompletoUsuario
				+ ", correoUsuario=" + correoUsuario + ", telefonoUsuario=" + telefonoUsuario + ", fotoUsuario="
				+ Arrays.toString(fotoUsuario) + ", contraseniaUsuario=" + contraseniaUsuario + ", esAdmin=" + esAdmin
				+ ", esPremium=" + esPremium + "]";	
	}
}