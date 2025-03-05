package atualizacao.crud.excecao;

public class UsuarioNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioNotFoundException(String mensagem) {
		super(mensagem);
	}

	public UsuarioNotFoundException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
