package senc.excecoes;

/*
 * EXCECAO ESPECIFICA -> herda de InscricaoInvalidaException.
 * Lancada quando a pessoa tenta se inscrever duas vezes na mesma atividade.
 */
public class InscricaoDuplicadaException extends InscricaoInvalidaException {

	private static final long serialVersionUID = 1L;

	public InscricaoDuplicadaException(String msg) {
		super(msg);
	}
}
