package senc.excecoes;

/*
 * EXCECAO ESPECIFICA -> herda de InscricaoInvalidaException.
 * Lancada quando a atividade ja atingiu o numero maximo de vagas.
 */
public class VagasEsgotadasException extends InscricaoInvalidaException {

	private static final long serialVersionUID = 1L;

	public VagasEsgotadasException(String msg) {
		super(msg);
	}
}
