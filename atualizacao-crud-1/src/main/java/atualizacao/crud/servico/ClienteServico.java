package atualizacao.crud.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import atualizacao.crud.excecao.ClienteNotFoundException;
import atualizacao.crud.modelo.Cliente;
import atualizacao.crud.repositorio.ClienteRepositorio;

import java.util.List;

@Service
public class ClienteServico {

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	// Método para salvar ou atualizar um cliente
	public Cliente salvarCliente(Cliente cliente) {
		try {
			return clienteRepositorio.save(cliente);
		} catch (DataAccessException e) {
			throw new RuntimeException("Erro ao salvar o cliente no banco de dados", e);
		}
	}

	// Método para buscar um cliente pelo ID
	public Cliente buscarClientePorId(Long id) throws ClienteNotFoundException {
		return clienteRepositorio.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException("Cliente com ID " + id + " não encontrado"));
	}

	// Método para buscar todos os clientes
	public List<Cliente> buscarTodosClientes() {
		try {
			return clienteRepositorio.findAll();
		} catch (DataAccessException e) {
			throw new RuntimeException("Erro ao buscar todos os clientes", e);
		}
	}

	// Método para buscar clientes pelo nome (ignorando maiúsculas/minúsculas)
	public List<Cliente> buscarClientesPorNome(String nome) {
		try {
			return clienteRepositorio.findByNomeContainingIgnoreCase(nome);
		} catch (DataAccessException e) {
			throw new RuntimeException("Erro ao buscar clientes por nome", e);
		}
	}

	// Método para excluir um cliente pelo ID
	public void excluirClientePorId(Long id) throws ClienteNotFoundException {
		if (!clienteRepositorio.existsById(id)) {
			throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado");
		}
		try {
			clienteRepositorio.deleteById(id);
		} catch (DataAccessException e) {
			throw new RuntimeException("Erro ao excluir o cliente", e);
		}
	}

	// Método para verificar se um cliente com um determinado nome já existe
	public boolean verificarNomeExistente(String nome) {
		try {
			return clienteRepositorio.existsByNome(nome);
		} catch (DataAccessException e) {
			throw new RuntimeException("Erro ao verificar nome existente", e);
		}
	}

	// Método para buscar clientes ordenados por nome (ordem alfabética)
	public List<Cliente> buscarClientesOrdenadosPorNome() {
		try {
			return clienteRepositorio.findAllByOrderByNomeAsc();
		} catch (DataAccessException e) {
			throw new RuntimeException("Erro ao buscar clientes ordenados por nome", e);
		}
	}

	// Método para atualizar um cliente existente
	public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) throws ClienteNotFoundException {
		Cliente clienteExistente = buscarClientePorId(id); // Busca o cliente existente

		// Atualiza os campos do cliente existente
		clienteExistente.setNome(clienteAtualizado.getNome());

		return salvarCliente(clienteExistente); // Salva as alterações
	}
}