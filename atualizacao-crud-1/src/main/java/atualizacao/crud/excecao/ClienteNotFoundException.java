package atualizacao.crud.excecao;

@SuppressWarnings("serial")
public class ClienteNotFoundException extends Exception {
	public ClienteNotFoundException(String mensagem) {
		super(mensagem);
	}
}