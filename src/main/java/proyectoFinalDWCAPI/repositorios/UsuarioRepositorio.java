package proyectoFinalDWCAPI.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import proyectoFinalDWCAPI.daos.UsuarioDao;

@Repository
public interface UsuarioRepositorio extends JpaRepository<UsuarioDao, Long> {
	UsuarioDao findByCorreoUsuario(String correoUsuario);

	boolean existsByCorreoUsuario(String correoUsuario);

	@Query("SELECT t.usuario FROM TokenDao t WHERE t.token = :token")
    UsuarioDao findByToken(@Param("token") String token);
}
