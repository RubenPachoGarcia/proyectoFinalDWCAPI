package proyectoFinalDWCAPI.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyectoFinalDWCAPI.daos.RegistroDao;

@Repository
public interface RegistroRepositorio extends JpaRepository<RegistroDao, Long> {
    Optional<RegistroDao> findByToken(String token);
}
