package senc.atividades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import senc.excecoes.CertificadoNaoEmitidoException;
import senc.pessoas.Palestrante;
import senc.pessoas.Pessoa;

/*
 * HERANCA: Minicurso "e uma" Atividade -> extends Atividade.
 * Adiciona o softaware requerido e o controle de entrega de projetos.
 * Regra do certificado: PRESENCA + ENTREGA DO PROJETO.
 */
public class Minicurso extends Atividade {

	private String softwareRequerido;
	private Palestrante instrutor;
	private List<Pessoa> projetosEntregues = new ArrayList<>();

	public Minicurso(String titulo, Date dataHorario, int duracaoMinutos, String local,
			int vagasMaximas, String softwareRequerido, Palestrante instrutor) {
		super(titulo, dataHorario, duracaoMinutos, local, vagasMaximas); 
		this.softwareRequerido = softwareRequerido;
		this.instrutor = instrutor;
	}

	public String getSoftwareRequerido() {
		return softwareRequerido;
	}

	public Palestrante getInstrutor() {
		return instrutor;
	}

	public void registrarEntregaProjeto(Pessoa p) {
		if (!estaInscrito(p))
			throw new IllegalStateException(p.getNome() + " nao esta inscrito(a) em \"" + getTitulo() + "\"");
		if (!projetosEntregues.contains(p))
			projetosEntregues.add(p);
	}

	@Override
	public String getTipo() {
		return "Minicurso";
	}

	/*
	 * POLIMORFISMO: implementacao PROPRIA do Minicurso para gerarCertificado().
	 * Exige duas condicoes: presenca e entrega do projeto final.
	 */
	@Override
	public String gerarCertificado(Pessoa p) throws CertificadoNaoEmitidoException {
		verificarInscricao(p); // metodo herdado de Atividade

		if (!estevePresente(p))
			throw new CertificadoNaoEmitidoException(
					"Minicurso \"" + getTitulo() + "\": " + p.getNome() + " nao teve a presenca registrada.");

		if (!projetosEntregues.contains(p))
			throw new CertificadoNaoEmitidoException(
					"Minicurso \"" + getTitulo() + "\": " + p.getNome() + " nao entregou o projeto final.");

		double cargaHoraria = getDuracaoMinutos() / 60.0;

		return "------------- CERTIFICADO DE CONCLUSAO (MINICURSO) -------------\n"
				+ "Certificamos que " + p.getIdentificacao() + "\n"
				+ "CONCLUIU o minicurso \"" + getTitulo() + "\" (software: " + softwareRequerido + "),\n"
				+ "com carga horaria de " + cargaHoraria + "h, ministrado por " + instrutor.getNome() + ",\n"
				+ "tendo cumprido os requisitos: [X] presenca  [X] entrega do projeto final.\n"
				+ "-------------------------------------------------------------";
	}

	@Override
	public String toString() {
		return super.toString() + " ** software: " + softwareRequerido;
	}
}
