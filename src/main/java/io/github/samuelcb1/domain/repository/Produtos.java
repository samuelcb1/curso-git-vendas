package io.github.samuelcb1.domain.repository;

import io.github.samuelcb1.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {


}
