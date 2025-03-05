package atualizacao.crud.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import atualizacao.crud.modelo.Cliente;

import java.util.List;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

	// Método para buscar clientes pelo nome (ignorando maiúsculas/minúsculas)
	List<Cliente> findByNomeContainingIgnoreCase(String nome);

	// Método para verificar se um cliente com um determinado nome já existe
	boolean existsByNome(String nome);

	// Método para buscar clientes ordenados por nome (ordem alfabética)
	List<Cliente> findAllByOrderByNomeAsc();
}