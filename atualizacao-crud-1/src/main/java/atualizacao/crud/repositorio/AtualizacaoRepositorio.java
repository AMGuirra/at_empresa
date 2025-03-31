package atualizacao.crud.repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import atualizacao.crud.modelo.Atualizacao;

@Repository
public interface AtualizacaoRepositorio extends JpaRepository<Atualizacao, Long> {

	@Query("SELECT a FROM Atualizacao a JOIN FETCH a.status")
	List<Atualizacao> findAllWithStatus();

	// Método para buscar atualizações por cliente (usando o ID do cliente)
	List<Atualizacao> findByClienteId(Long clienteId);

	// Método para buscar atualizações por usuário
	List<Atualizacao> findByUsuarioId(Long usuarioId);

	// Método para buscar atualizações por status
	List<Atualizacao> findByStatusId(Long statusId);

	// Método para buscar atualizações por data do pedido
	List<Atualizacao> findByDtPedido(LocalDate dtPedido);

	// Método para buscar atualizações com data de início dentro de um intervalo
	List<Atualizacao> findByDtInicioBetween(LocalDate inicio, LocalDate fim);

	// Método para buscar atualizações com data de fim dentro de um intervalo
	List<Atualizacao> findByDtFimBetween(LocalDate inicio, LocalDate fim);

	// Verifica se existe alguma atualização associada a um status
	@Query("SELECT COUNT(a) > 0 FROM Atualizacao a WHERE a.status.id = :statusId")
	boolean existsByStatusId(@Param("statusId") Long statusId);
	
	List<Atualizacao> findByStatusNome(String statusNome);
}