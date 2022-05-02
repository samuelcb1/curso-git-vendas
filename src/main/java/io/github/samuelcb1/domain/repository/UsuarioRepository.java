package io.github.samuelcb1.domain.repository;

import io.github.samuelcb1.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario>findByLogin(String login);
}
