package atualizacao.crud.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import atualizacao.crud.modelo.Usuario;
import atualizacao.crud.servico.UsuarioServico;

@Controller
public class IndexControle {

	@Autowired
	private UsuarioServico usuarioService; // Injetando o serviço de usuários

	@GetMapping("/")
	public String home() {
		return "index"; // Página inicial
	}

	@GetMapping("/novo-usuario")
	public String novoUsuario(Model model) {
		model.addAttribute("usuario", new Usuario()); // Cria um novo objeto Usuario para o formulário
		return "novo-usuario"; // Retorna a view para criar um novo usuário
	}

	@PostMapping("/usuarios")
	public String salvarUsuario(@ModelAttribute("usuario") Usuario usuario) {
		usuarioService.criarUsuario(usuario); // Chama o método do serviço para salvar o usuário
		return "redirect:/listar-usuarios"; // Redireciona para a lista de usuários
	}

	@GetMapping("/listar-usuarios")
	public String listarUsuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.buscarTodosUsuarios()); // Passa a lista de usuários para a view
		return "listar-usuarios"; // Retorna a view com a lista de usuários
	}

	@GetMapping("/novo-status")
	public String novoStatus(Model model) {
		// Lógica para incluir status, se necessário
		return "novo-status"; // Retorna à view para criar um novo status
	}

	@GetMapping("/listar-status")
	public String listarStatus(Model model) {
		// Lógica para listar status, se disponível
		return "listar-status"; // Retorna à view com a lista de status
	}

	@GetMapping("/nova-atualizacao")
	public String novaAtualizacao(Model model) {
		// Lógica para incluir atualização, se necessário
		return "nova-atualizacao"; // Retorna à view para criar uma nova atualização
	}

	@GetMapping("/listar-atualizacoes")
	public String listarAtualizacoes(Model model) {
		// Lógica para listar atualizações, se disponível
		return "listar-atualizacoes"; // Retorna à view com a lista de atualizações
	}
}
