package atualizacao.crud.excecao;

public class StatusNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// Construtor com mensagem
	public StatusNotFoundException(String mensagem) {
		super(mensagem);
	}

	// Construtor com mensagem e causa
	public StatusNotFoundException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

	// Construtor apenas com a causa (opcional, mas útil em alguns cenários)
	public StatusNotFoundException(Throwable causa) {
		super(causa);
	}
}