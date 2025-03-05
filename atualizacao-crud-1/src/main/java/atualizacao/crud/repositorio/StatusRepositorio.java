package atualizacao.crud.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import atualizacao.crud.modelo.Status;

public interface StatusRepositorio extends JpaRepository<Status, Long> {
}
