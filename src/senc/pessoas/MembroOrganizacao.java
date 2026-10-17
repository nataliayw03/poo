package senc.pessoas;

/*
 * HERANCA: MembroOrganizacao "e uma" Pessoa -> extends Pessoa.
 * Equivale ao "Empregado" do material de aula.
 * Herda nome e email e adiciona numero USP, cargo e diretoria.
 */
public class MembroOrganizacao extends Pessoa {

	private int nroUSP;
	private String cargo;
	private String diretoria;

	public MembroOrganizacao(String nome, String email, int nroUSP, String cargo, String diretoria) {
		super(nome, email);
		this.nroUSP = nroUSP;
		this.cargo = cargo;
		this.diretoria = diretoria;
	}

	public int getNroUSP() {
		return nroUSP;
	}

	public String getCargo() {
		return cargo;
	}

	public String getDiretoria() {
		return diretoria;
	}

	// polimorfismo (sobrescrita do metodo abstrato de Pessoa)
	@Override
	public String getIdentificacao() {
		return getNome() + " (" + cargo + " - " + diretoria + ", No USP " + nroUSP
				+ ", Comissao Organizadora da SEnC)";
	}

	@Override
	public String toString() {
		return "[Organizacao] " + super.toString() + " ** No USP: " + nroUSP + " ** " + cargo + " ** " + diretoria;
	}
}
