package atualizacao.crud.modelo;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@OneToMany(mappedBy = "status", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Atualizacao> atualizacoes; // Referência a Atualizacoes

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Atualizacao> getAtualizacoes() {
		return atualizacoes;
	}

	public void setAtualizacoes(List<Atualizacao> atualizacoes) {
		this.atualizacoes = atualizacoes;
	}
}
