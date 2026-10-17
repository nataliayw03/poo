package senc.atividades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import senc.excecoes.CertificadoNaoEmitidoException;
import senc.pessoas.Pessoa;

/*
 * HERANCA: VisitaTecnica "e uma" Atividade -> extends Atividade.
 * Adiciona a empresa de destino, o horario de saida do onibus e o controle de embarque.
 * Regra do certificado: o participante precisa ter embarcado no onibus.
 */
public class VisitaTecnica extends Atividade {

	private String empresaDestino;
	private Date horarioSaidaOnibus;
	private List<Pessoa> embarcados = new ArrayList<>();

	public VisitaTecnica(String titulo, Date dataHorario, int duracaoMinutos, String local,
			int vagasMaximas, String empresaDestino, Date horarioSaidaOnibus) {
		super(titulo, dataHorario, duracaoMinutos, local, vagasMaximas); // HERANCA
		this.empresaDestino = empresaDestino;
		this.horarioSaidaOnibus = horarioSaidaOnibus;
	}

	public String getEmpresaDestino() {
		return empresaDestino;
	}

	public Date getHorarioSaidaOnibus() {
		return horarioSaidaOnibus;
	}

	public void registrarEmbarque(Pessoa p) {
		if (!estaInscrito(p))
			throw new IllegalStateException(p.getNome() + " nao esta inscrito(a) em \"" + getTitulo() + "\"");
		if (!embarcados.contains(p))
			embarcados.add(p);
	}

	@Override
	public String getTipo() {
		return "Visita Tecnica";
	}

	/*
	 * POLIMORFISMO: implementacao PROPRIA da VisitaTecnica para gerarCertificado().
	 * Aqui nao importa a "lista de presenca", e sim o EMBARQUE no onibus.
	 */
	@Override
	public String gerarCertificado(Pessoa p) throws CertificadoNaoEmitidoException {
		verificarInscricao(p); // metodo herdado de Atividade

		if (!embarcados.contains(p))
			throw new CertificadoNaoEmitidoException(
					"Visita ao " + empresaDestino + ": " + p.getNome()
					+ " nao embarcou no onibus das " + sdfHora.format(horarioSaidaOnibus) + ".");

		return "------------ CERTIFICADO DE VISITA TECNICA -----------\n"
				+ "Certificamos que " + p.getIdentificacao() + "\n"
				+ "PARTICIPOU da visita tecnica ao " + empresaDestino + "\n"
				+ "em " + sdfData.format(getDataHorario()) + ", com embarque VALIDADO no onibus\n"
				+ "que partiu do(a) " + getLocal() + " as " + sdfHora.format(horarioSaidaOnibus) + ".\n"
				+ "---------------------------------------------------";
	}

	@Override
	public String toString() {
		return super.toString() + " ** destino: " + empresaDestino
				+ " ** onibus: " + sdfHora.format(horarioSaidaOnibus);
	}
}
