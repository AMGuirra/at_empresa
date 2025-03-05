package atualizacao.crud.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import atualizacao.crud.excecao.StatusNotFoundException;
import atualizacao.crud.modelo.Status;
import atualizacao.crud.servico.StatusServico;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/status")
public class StatusControle {

	@Autowired
	private StatusServico statusServico;

	@GetMapping("/listar-status")
	public String listarStatus(Model model) {
		List<Status> listaStatus = statusServico.buscarTodosStatus();
		model.addAttribute("listaStatus", listaStatus);
		return "listar-status";
	}

	@GetMapping("/novo-status")
	public String exibirFormularioNovoStatus(Model model) {
		model.addAttribute("status", new Status());
		return "novo-status";
	}

	@PostMapping("/gravar-status")
	public String gravarStatus(@ModelAttribute("status") @Valid Status status, BindingResult erros,
			RedirectAttributes attributes) {
		if (erros.hasErrors()) {
			return "novo-status";
		}
		statusServico.criarStatus(status);
		attributes.addFlashAttribute("mensagem", "Status salvo com sucesso!");
		return "redirect:/status/listar-status";
	}

	@GetMapping("/editar-status/{id}")
	public String exibirFormularioEdicaoStatus(@PathVariable("id") Long id, RedirectAttributes attributes,
			Model model) {
		try {
			Status status = statusServico.buscarStatusPorId(id);
			model.addAttribute("status", status);
			return "alterar-status";
		} catch (StatusNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
			return "redirect:/status/listar-status";
		}
	}

	@PostMapping("/editar-status/{id}")
	public String salvarStatus(@PathVariable("id") Long id, @ModelAttribute("status") @Valid Status status,
			BindingResult erros, RedirectAttributes attributes, Model model) {
		if (erros.hasErrors()) {
			model.addAttribute("status", status);
			return "alterar-status";
		}
		try {
			statusServico.atualizarStatus(id, status);
			attributes.addFlashAttribute("mensagem", "Status atualizado com sucesso!");
		} catch (StatusNotFoundException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
			return "redirect:/status/editar-status/" + id;
		}
		return "redirect:/status/listar-status";
	}

	@GetMapping("/excluir-status/{id}")
	public String excluirStatus(@PathVariable("id") Long id, RedirectAttributes attributes) {
		try {
			statusServico.excluirStatus(id);
			attributes.addFlashAttribute("mensagem", "Status excluído com sucesso!");
		} catch (Exception e) {
			attributes.addFlashAttribute("mensagemErro", "Erro ao excluir status: " + e.getMessage());
		}
		return "redirect:/status/listar-status";
	}
}
