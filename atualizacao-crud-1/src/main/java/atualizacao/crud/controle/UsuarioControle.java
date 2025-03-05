package atualizacao.crud.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import atualizacao.crud.excecao.UsuarioNotFoundException;
import atualizacao.crud.modelo.Usuario;
import atualizacao.crud.servico.UsuarioServico;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControle {

	@Autowired
	private UsuarioServico usuarioServico;

	@GetMapping("/listar")
	public String listarUsuarios(Model model) {
		List<Usuario> listaUsuarios = usuarioServico.buscarTodosUsuarios();
		model.addAttribute("listaUsuarios", listaUsuarios);
		return "listar-usuarios";
	}

	@GetMapping("/novo")
	public String exibirFormularioNovoUsuario(Model model) {
		Usuario usuario = new Usuario();
		model.addAttribute("novoUsuario", usuario); // Nome do atributo
		return "novo-usuario"; // Nome do template
	}

	@PostMapping("/gravar")
	public String salvarUsuario(@ModelAttribute @Valid Usuario novoUsuario, BindingResult erros,
			RedirectAttributes attributes, Model model) {
		if (erros.hasErrors()) {
			// Adiciona o objeto novamente ao modelo para evitar o erro
			model.addAttribute("novoUsuario", novoUsuario);
			return "novo-usuario"; // Retorna para o formulário em caso de erro
		}

		usuarioServico.criarUsuario(novoUsuario); // Usa o método correto
		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
		return "redirect:/usuarios/listar";
	}

	@GetMapping("/excluir/{id}")
	public String excluirUsuario(@PathVariable("id") long id, RedirectAttributes attributes) {
		try {
			usuarioServico.excluirUsuario(id);
			attributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
		} catch (UsuarioNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
		}
		return "redirect:/usuarios/listar";
	}

	@GetMapping("/editar/{id}")
	public String editarForm(@PathVariable("id") long id, RedirectAttributes attributes, Model model) {
		try {
			Usuario usuario = usuarioServico.buscarUsuarioPorId(id)
					.orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));
			model.addAttribute("usuario", usuario); // Nome do atributo
			return "alterar-usuario"; // Nome do template
		} catch (UsuarioNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
			return "redirect:/usuarios/listar";
		}
	}

	@PostMapping("/editar/{id}")
	public String editarUsuario(@PathVariable("id") long id, @ModelAttribute("usuario") @Valid Usuario usuario,
			BindingResult erros, RedirectAttributes attributes) {
		if (erros.hasErrors()) {
			usuario.setId(id);
			return "alterar-usuario";
		}
		try {
			usuarioServico.atualizarUsuario(id, usuario);
			attributes.addFlashAttribute("mensagem", "Usuário alterado com sucesso!");
		} catch (UsuarioNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
			return "redirect:/usuarios/editar/" + id;
		}
		return "redirect:/usuarios/listar";
	}
}