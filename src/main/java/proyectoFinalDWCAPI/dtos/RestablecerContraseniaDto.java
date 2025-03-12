package proyectoFinalDWCAPI.dtos;

public class RestablecerContraseniaDto {
	
	//Atributos
	
	private String token;
    private String nuevaContrasenia;
    
    //Getters & Setters
	
    public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNuevaContrasenia() {
		return nuevaContrasenia;
	}
	public void setNuevaContrasenia(String nuevaContrasenia) {
		this.nuevaContrasenia = nuevaContrasenia;
	}
    
}
