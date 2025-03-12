package proyectoFinalDWCAPI.dtos;

public class LoginDto {

	//Atributos
	
	private Long idUsuario;
	private String correoUsuario;
	private String contraseniaUsuario;
	private String esAdmin;
	
	//Getters & Setters

	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getCorreoUsuario() {
		return correoUsuario;
	}
	public void setCorreoUsuario(String correoUsuario) {
		this.correoUsuario = correoUsuario;
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
}
