package atualizacao.crud.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import atualizacao.crud.excecao.AtualizacaoNotFoundException;
import atualizacao.crud.modelo.Atualizacao;
import atualizacao.crud.modelo.Status;
import atualizacao.crud.servico.AtualizacaoServico;
import atualizacao.crud.servico.ClienteServico;
import atualizacao.crud.servico.StatusServico;
import atualizacao.crud.servico.UsuarioServico;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/atualizacoes")
public class AtualizacaoControle {

	@Autowired
	private AtualizacaoServico atualizacaoServico;

	@Autowired
	private UsuarioServico usuarioServico;

	@Autowired
	private ClienteServico clienteServico;

	@Autowired
	private StatusServico statusServico;

	@GetMapping("/novo")
	public String exibirFormularioNovaAtualizacao(Model model) {
		model.addAttribute("novaAtualizacao", new Atualizacao());
		model.addAttribute("todosUsuarios", usuarioServico.buscarTodosUsuarios());
		model.addAttribute("todosClientes", clienteServico.buscarTodosClientes());
		model.addAttribute("todosStatus", statusServico.buscarTodosStatus());
		return "nova-atualizacao";
	}

	@PostMapping("/gravar")
	public String gravarAtualizacao(@ModelAttribute("novaAtualizacao") @Valid Atualizacao novaAtualizacao,
			BindingResult erros, RedirectAttributes attributes, Model model) {
		if (erros.hasErrors()) {
			model.addAttribute("todosUsuarios", usuarioServico.buscarTodosUsuarios());
			model.addAttribute("todosClientes", clienteServico.buscarTodosClientes());
			model.addAttribute("todosStatus", statusServico.buscarTodosStatus());
			return "nova-atualizacao";
		}

		Status statusSelecionado = statusServico.buscarStatusPorId(novaAtualizacao.getStatus().getId());
		novaAtualizacao.setStatus(statusSelecionado);
		atualizacaoServico.salvarAtualizacao(novaAtualizacao);
		attributes.addFlashAttribute("mensagem", "Atualização salva com sucesso!");
		return "redirect:/atualizacoes/listar-atualizacoes";
	}

	@GetMapping("/listar-atualizacoes")
	public String listarAtualizacoes(Model model) {
		List<Atualizacao> listaAtualizacoes = atualizacaoServico.buscarTodasAtualizacoes();
		model.addAttribute("listaAtualizacoes", listaAtualizacoes);
		return "listar-atualizacoes";
	}

	@PostMapping("/buscar")
	public String buscarAtualizacoes(Model model, @RequestParam("clienteId") Long clienteId) {
		if (clienteId == null) {
			return "redirect:/atualizacoes/listar-atualizacoes";
		}
		model.addAttribute("listaAtualizacoes", atualizacaoServico.buscarAtualizacoesPorCliente(clienteId));
		return "listar-atualizacoes";
	}

	@GetMapping("/apagar/{id}")
	public String apagarAtualizacao(@PathVariable("id") Long id, RedirectAttributes attributes) {
		try {
			atualizacaoServico.excluirAtualizacaoPorId(id);
			attributes.addFlashAttribute("mensagem", "Atualização apagada com sucesso!");
		} catch (AtualizacaoNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
		}
		return "redirect:/atualizacoes/listar-atualizacoes";
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEdicao(@PathVariable("id") Long id, RedirectAttributes attributes, Model model) {
		try {
			model.addAttribute("objetoAtualizacao", atualizacaoServico.buscarAtualizacaoPorId(id));
			model.addAttribute("todosUsuarios", usuarioServico.buscarTodosUsuarios());
			model.addAttribute("todosClientes", clienteServico.buscarTodosClientes());
			model.addAttribute("todosStatus", statusServico.buscarTodosStatus());
			return "alterar-atualizacao";
		} catch (AtualizacaoNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
			return "redirect:/atualizacoes/listar-atualizacoes";
		}
	}

	@PostMapping("/editar/{id}")
	public String editarAtualizacao(@PathVariable("id") Long id,
			@ModelAttribute("objetoAtualizacao") @Valid Atualizacao atualizacao, BindingResult erros,
			RedirectAttributes attributes, Model model) {
		if (erros.hasErrors()) {
			model.addAttribute("todosUsuarios", usuarioServico.buscarTodosUsuarios());
			model.addAttribute("todosClientes", clienteServico.buscarTodosClientes());
			model.addAttribute("todosStatus", statusServico.buscarTodosStatus());
			return "alterar-atualizacao";
		}
		Status statusSelecionado = statusServico.buscarStatusPorId(atualizacao.getStatus().getId());
		atualizacao.setStatus(statusSelecionado);
		atualizacaoServico.salvarAtualizacao(atualizacao); // Altere para usar o método correto
		attributes.addFlashAttribute("mensagem", "Atualização alterada com sucesso!");
		return "redirect:/atualizacoes/listar-atualizacoes";
	}
}