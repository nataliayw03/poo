package senc.pessoas;

import java.util.ArrayList;
import java.util.List;

import senc.atividades.Atividade;

/*
 * SUPERCLASSE abstrata da hierarquia de pessoas.
 *
 * - "abstract": nao faz sentido criar uma "Pessoa generica" no evento;
 *   so criamos objetos das SUBCLASSES (Estudante, MembroOrganizacao, Palestrante).
 *
 * - Atributos "private" : as subclasses acessam
 *   esses dados apenas pelos servicos (getters) da superclasse.
 */
public abstract class Pessoa {

	private String nome;
	private String email;

	// composicao: uma pessoa tem varias atividades inscritas.
	// Usada para detectar conflito de horario na inscricao.
	private List<Atividade> atividadesInscritas = new ArrayList<>();

	public Pessoa(String nome, String email) {
		// Validacao no construtor
		if (nome == null || nome.isBlank())
			throw new IllegalArgumentException("Nome invalido");
		// indexOf devolve -1 quando o texto nao contem "@"
		if (email == null || email.indexOf("@") == -1)
			throw new IllegalArgumentException("Email invalido para a pessoa: " + nome);

		this.nome = nome;
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public List<Atividade> getAtividadesInscritas() {
		return atividadesInscritas;
	}

	// Chamado pela classe Atividade quando a inscricao eh confirmada.
	public void adicionarAtividade(Atividade atividade) {
		atividadesInscritas.add(atividade);
	}

	/*
	 * METODO ABSTRATO -> cada subclasse eh obrigada a implementar.
	 * Retorna como a pessoa e identificada no certificado.
	 * Chamar p.getIdentificacao() numa variavel do tipo Pessoa eh polimrfismo:
	 * o Java decide em tempo de execucao qual versao executar.
	 */
	public abstract String getIdentificacao();

	@Override
	public String toString() {
		return getNome() + " ** " + getEmail();
	}
}
