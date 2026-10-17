package senc.atividades;

import java.util.Date;

import senc.excecoes.CertificadoNaoEmitidoException;
import senc.pessoas.Palestrante;
import senc.pessoas.Pessoa;

/*
 * HERANCA: Palestra "e uma" Atividade -> extends Atividade.
 * Regra do certificado: basta confirmar a presenca.
 */
public class Palestra extends Atividade {

	private Palestrante palestrante;

	public Palestra(String titulo, Date dataHorario, int duracaoMinutos, String local,
			int vagasMaximas, Palestrante palestrante) {
		super(titulo, dataHorario, duracaoMinutos, local, vagasMaximas); // HERANCA
		this.palestrante = palestrante;
	}

	public Palestrante getPalestrante() {
		return palestrante;
	}

	@Override
	public String getTipo() {
		return "Palestra";
	}

	/*
	 * polimorfismo: implementacao propria da Palestra para gerarCertificado().
	 * Unica exigencia: presenca.
	 */
	@Override
	public String gerarCertificado(Pessoa p) throws CertificadoNaoEmitidoException {
		verificarInscricao(p); // metodo herdado de Atividade

		if (!estevePresente(p))
			throw new CertificadoNaoEmitidoException(
					"Palestra \"" + getTitulo() + "\": " + p.getNome() + " nao teve a presenca registrada.");

		return "------------ CERTIFICADO DE PARTICIPACAO (PALESTRA) ------------\n"
				+ "Certificamos que " + p.getIdentificacao() + "\n"
				+ "ESTEVE PRESENTE na palestra \"" + getTitulo() + "\",\n"
				+ "ministrada por " + palestrante.getNome() + " (" + palestrante.getEmpresa() + "),\n"
				+ "em " + sdfData.format(getDataHorario()) + ", no(a) " + getLocal() + ".\n"
				+ "----------------------------------------------------------------";
	}

	@Override
	public String toString() {
		return super.toString() + " ** palestrante: " + palestrante.getNome();
	}
}
