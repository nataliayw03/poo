package senc.pessoas;

/*
 * HERANCA: Estudante "e uma" Pessoa -> extends Pessoa.
 * Herda nome e email e adiciona numero USP e curso.
 */
public class Estudante extends Pessoa {

	private int nroUSP;
	private String curso;

	public Estudante(String nome, String email, int nroUSP, String curso) {
		super(nome, email);
		this.nroUSP = nroUSP;
		this.curso = curso;
	}

	public int getNroUSP() {
		return nroUSP;
	}

	public String getCurso() {
		return curso;
	}

	// polimorfismo (sobrescrita do metodo abstrato de Pessoa)
	@Override
	public String getIdentificacao() {
		return getNome() + " (Estudante de " + curso + ", No USP " + nroUSP + ")";
	}

	// Metodo override nao invoca o mesmo metodo da superclasse automaticamente:
	// fazemos uma chamada explicita a super.toString()
	@Override
	public String toString() {
		return "[Estudante] " + super.toString() + " ** No USP: " + nroUSP + " ** " + curso;
	}
}
