package atualizacao.crud.excecao;

@SuppressWarnings("serial")
public class AtualizacaoNotFoundException extends Exception {
	public AtualizacaoNotFoundException(String mensagem) {
		super(mensagem);
	}
}