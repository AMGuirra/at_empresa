package atualizacao.crud.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import atualizacao.crud.excecao.ClienteNotFoundException;
import atualizacao.crud.modelo.Cliente;
import atualizacao.crud.servico.ClienteServico;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteControle {

	@Autowired
	private ClienteServico clienteServico;

	// Método para exibir o formulário de cadastro de novo cliente
	@GetMapping("/novo")
	public String exibirFormularioNovoCliente(Model model) {
		model.addAttribute("novoCliente", new Cliente());
		return "novo-cliente"; // Nome da página Thymeleaf (sem extensão .html)
	}

	// Método para salvar um novo cliente
	@PostMapping("/gravar")
	public String salvarCliente(@ModelAttribute("novoCliente") Cliente cliente, Model model) {
		clienteServico.salvarCliente(cliente);
		model.addAttribute("mensagem", "Cliente salvo com sucesso!");
		return "redirect:/clientes/listar-clientes";
	}

	// Método para listar todos os clientes
	@GetMapping("/listar-clientes")
	public String listarClientes(Model model) {
		List<Cliente> clientes = clienteServico.buscarTodosClientes();
		model.addAttribute("listaClientes", clientes);
		return "listar-cliente";
	}

	// Método para exibir o formulário de edição de cliente
	@GetMapping("/editar/{id}")
	public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
		try {
			Cliente cliente = clienteServico.buscarClientePorId(id);
			model.addAttribute("cliente", cliente);
			return "alterar-cliente";
		} catch (ClienteNotFoundException e) {
			model.addAttribute("mensagemErro", e.getMessage());
			return "redirect:/clientes/listar-clientes";
		}
	}

	// Método para atualizar um cliente existente
	@PostMapping("/editar/{id}")
	public String atualizarCliente(@PathVariable Long id, @ModelAttribute("cliente") Cliente cliente, Model model) {
		try {
			clienteServico.atualizarCliente(id, cliente);
			model.addAttribute("mensagem", "Cliente atualizado com sucesso!");
		} catch (ClienteNotFoundException e) {
			model.addAttribute("mensagemErro", e.getMessage());
		}
		return "redirect:/clientes/listar-clientes";
	}

	// Método para excluir um cliente
	@GetMapping("/apagar/{id}")
	public String excluirCliente(@PathVariable Long id, Model model) {
		try {
			clienteServico.excluirClientePorId(id);
			model.addAttribute("mensagem", "Cliente excluído com sucesso!");
		} catch (ClienteNotFoundException e) {
			model.addAttribute("mensagemErro", e.getMessage());
		}
		return "redirect:/clientes/listar-clientes";
	}
}