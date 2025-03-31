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
import atualizacao.crud.repositorio.AtualizacaoRepositorio;
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

	@Autowired
	private AtualizacaoRepositorio atualizacaoRepositorio;

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

	@GetMapping("/alterar-status/{id}")
	public String mostrarFormularioAlterarStatus(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Atualizacao objetoAtualizacao = atualizacaoServico.buscarAtualizacaoPorId(id);
			List<Status> todosStatus = statusServico.buscarTodosStatus(); // Supondo que você tenha um serviço para
																			// buscar todos os status

			model.addAttribute("objetoAtualizacao", objetoAtualizacao);
			model.addAttribute("todosStatus", todosStatus);
			return "alterar-status-atualizacao"; // Substitua pelo nome do seu template Thymeleaf
		} catch (AtualizacaoNotFoundException e) {
			redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
			return "redirect:/atualizacoes/listar-atualizacoes"; // Redireciona para a lista de atualizações se não
																	// encontrar
		}
	}

	@PostMapping("/alterar-status/{id}")
	public String alterarStatus(@PathVariable Long id, @RequestParam Long statusId,
			RedirectAttributes redirectAttributes) {
		try {
			atualizacaoServico.alterarStatus(id, statusId);
			redirectAttributes.addFlashAttribute("mensagem", "Status alterado com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao alterar o status.");
		}
		return "redirect:/atualizacoes/listar-atualizacoes";
	}

	@GetMapping("/filtrar-por-status")
	public String filtrarAtualizacoesPorStatus(@RequestParam String status, Model model) {
		List<Atualizacao> atualizacoesFiltradas = atualizacaoServico.buscarAtualizacoesPorStatus(status);
		List<Status> listaStatus = statusServico.buscarTodosStatus();
		model.addAttribute("listaAtualizacoes", atualizacoesFiltradas);
		model.addAttribute("listaStatus", listaStatus);
		return "listar-atualizacoes";
	}

	// Carrega a tela de edição
	@GetMapping("/editar-observacao/{id}")
	public String editarObservacao(@PathVariable Long id, Model model) {
		Atualizacao atualizacao = atualizacaoRepositorio.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

		model.addAttribute("atualizacao", atualizacao);
		return "alterar-observacao"; // Nome do template Thymeleaf
	}

	// Salva a observação
	@PostMapping("/salvar-observacao/{id}")
	public String salvarObservacao(
	    @PathVariable Long id,
	    @RequestParam String obs,
	    @RequestParam Integer qtPc) {

	    Atualizacao atualizacao = atualizacaoRepositorio.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
	    
	    atualizacao.setObs(obs); // Atualiza a observação
	    atualizacao.setQtPc(qtPc); // Atualiza a quantidade de peças
	    atualizacaoRepositorio.save(atualizacao);

		return "redirect:/atualizacoes/listar-atualizacoes"; // Redireciona para a lista de atualizações
	}
}