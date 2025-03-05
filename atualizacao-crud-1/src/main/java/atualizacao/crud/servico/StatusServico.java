package atualizacao.crud.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atualizacao.crud.excecao.StatusNotFoundException;
import atualizacao.crud.modelo.Status;
import atualizacao.crud.repositorio.AtualizacaoRepositorio;
import atualizacao.crud.repositorio.StatusRepositorio;

@Service
public class StatusServico {

	@Autowired
	private StatusRepositorio statusRepositorio;

	@Autowired
	private AtualizacaoRepositorio atualizacaoRepositorio; // Injete o repositório de Atualizacao

	// Método para criar um novo status
	public Status criarStatus(Status status) {
		return statusRepositorio.save(status);
	}

	// Método para buscar todos os status
	public List<Status> buscarTodosStatus() {
		return statusRepositorio.findAll();
	}

	// Método para buscar um status por ID
	public Status buscarStatusPorId(Long id) throws StatusNotFoundException {
		return statusRepositorio.findById(id).orElseThrow(() -> new StatusNotFoundException("Status não encontrado"));
	}

	// Método para atualizar um status existente
	public Status atualizarStatus(Long id, Status status) throws StatusNotFoundException {
		Status statusExistente = buscarStatusPorId(id);
		statusExistente.setNome(status.getNome()); // Atualiza os campos necessários
		return statusRepositorio.save(statusExistente);
	}

	// Método para excluir um status
	public void excluirStatus(Long id) {
		statusRepositorio.deleteById(id);
	}

	// Método para verificar se há atualizações associadas a um status
	public boolean temAtualizacoes(Long id) {
		return atualizacaoRepositorio.existsByStatusId(id);
	}
}
