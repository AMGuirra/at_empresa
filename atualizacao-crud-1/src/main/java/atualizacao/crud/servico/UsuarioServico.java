package atualizacao.crud.servico;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atualizacao.crud.modelo.Usuario;
import atualizacao.crud.repositorio.UsuarioRepositorio;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServico {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	// Método para criar um novo usuário
	public Usuario criarUsuario(@Valid Usuario usuario) {
		return usuarioRepositorio.save(usuario); // Salva o novo usuário
	}

	// Método para atualizar um usuário existente
	public Usuario atualizarUsuario(Long id, @Valid Usuario usuario) {
		if (!usuarioRepositorio.existsById(id)) {
			throw new EntityNotFoundException("Usuário não encontrado");
		}
		usuario.setId(id); // Define o ID do usuário que será atualizado
		return usuarioRepositorio.save(usuario); // Salva as mudanças
	}

	// Método para buscar todos os usuários
	public List<Usuario> buscarTodosUsuarios() {
		return usuarioRepositorio.findAll(); // Retorna todos os usuários
	}

	// Método para buscar um usuário por ID
	public Optional<Usuario> buscarUsuarioPorId(Long id) {
		return usuarioRepositorio.findById(id); // Retorna o usuário com o ID especificado
	}

	// Método para excluir um usuário
	public void excluirUsuario(Long id) {
		if (!usuarioRepositorio.existsById(id)) {
			throw new EntityNotFoundException("Usuário não encontrado");
		}
		usuarioRepositorio.deleteById(id); // Exclui o usuário com o ID especificado
	}
}