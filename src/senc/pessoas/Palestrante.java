package senc.pessoas;

/*
 * HERANCA: Palestrante "e uma" Pessoa -> extends Pessoa.
 * Herda nome e email e adiciona empresa e minibio.
 */
public class Palestrante extends Pessoa {

	private String empresa;
	private String minibio;

	public Palestrante(String nome, String email, String empresa, String minibio) {
		super(nome, email);
		this.empresa = empresa;
		this.minibio = minibio;
	}

	public String getEmpresa() {
		return empresa;
	}

	public String getMinibio() {
		return minibio;
	}

	// polimorfismo (sobrescrita do metodo abstrato de Pessoa)
	@Override
	public String getIdentificacao() {
		return getNome() + " (Palestrante convidado(a) - " + empresa + ")";
	}

	@Override
	public String toString() {
		return "[Palestrante] " + super.toString() + " ** " + empresa + " ** " + minibio;
	}
}
