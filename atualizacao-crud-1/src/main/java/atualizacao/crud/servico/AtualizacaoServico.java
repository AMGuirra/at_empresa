package atualizacao.crud.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import atualizacao.crud.excecao.AtualizacaoNotFoundException;
import atualizacao.crud.modelo.Atualizacao;
import atualizacao.crud.modelo.Status;
import atualizacao.crud.repositorio.AtualizacaoRepositorio;

import java.time.LocalDate;
import java.util.List;

@Service
public class AtualizacaoServico {

    @Autowired
    private AtualizacaoRepositorio atualizacaoRepositorio;

    // Método para salvar ou atualizar uma atualização
    public Atualizacao salvarAtualizacao(Atualizacao atualizacao) {
        try {
            return atualizacaoRepositorio.save(atualizacao);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao salvar a atualização no banco de dados", e);
        }
    }

    // Método para buscar uma atualização pelo ID
    public Atualizacao buscarAtualizacaoPorId(Long id) throws AtualizacaoNotFoundException {
        return atualizacaoRepositorio.findById(id)
                .orElseThrow(() -> new AtualizacaoNotFoundException("Atualização com ID " + id + " não encontrada"));
    }

    // Método para buscar todas as atualizações com status
    public List<Atualizacao> buscarTodasAtualizacoes() {
        try {
            return atualizacaoRepositorio.findAllWithStatus();
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao buscar todas as atualizações", e);
        }
    }

    // Método para buscar atualizações por cliente
    public List<Atualizacao> buscarAtualizacoesPorCliente(Long clienteId) {
        try {
            return atualizacaoRepositorio.findByClienteId(clienteId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao buscar atualizações por cliente", e);
        }
    }

    // Método para buscar atualizações por usuário
    public List<Atualizacao> buscarAtualizacoesPorUsuario(Long usuarioId) {
        try {
            return atualizacaoRepositorio.findByUsuarioId(usuarioId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao buscar atualizações por usuário", e);
        }
    }

    // Método para buscar atualizações por status
    public List<Atualizacao> buscarAtualizacoesPorStatus(Long statusId) {
        try {
            return atualizacaoRepositorio.findByStatusId(statusId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao buscar atualizações por status", e);
        }
    }

    // Método para excluir uma atualização pelo ID
    public void excluirAtualizacaoPorId(Long id) throws AtualizacaoNotFoundException {
        if (!atualizacaoRepositorio.existsById(id)) {
            throw new AtualizacaoNotFoundException("Atualização com ID " + id + " não encontrada");
        }
        try {
            atualizacaoRepositorio.deleteById(id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Erro ao excluir a atualização", e);
        }
    }

    // Método para alterar o status de uma atualização
    public void alterarStatus(Long id, Long statusId) throws AtualizacaoNotFoundException {
        // Busca a atualização pelo ID
        Atualizacao atualizacao = buscarAtualizacaoPorId(id);
        
        // Atualiza o status da atualização
        Status novoStatus = new Status(); // Cria um novo objeto Status
        novoStatus.setId(statusId); // Define o ID do status que você deseja associar
        atualizacao.setStatus(novoStatus); // Atualiza o status da atualização
        
        // Salva a atualização com o novo status
        salvarAtualizacao(atualizacao);
    }
    
    public List<Atualizacao> buscarAtualizacoesPorStatus(String statusNome) {
        return atualizacaoRepositorio.findByStatusNome(statusNome);
    }
}
