package senc.excecoes;

/*
 * EXCECAO ESPECIFICA -> harda de InscricaoInvalidaException.
 * Lancada quando a pessoa ja esta inscrita em outra atividade
 * que acontece no mesmo dia e em horario sobreposto.
 */
public class ConflitoHorarioException extends InscricaoInvalidaException {

	private static final long serialVersionUID = 1L;

	public ConflitoHorarioException(String msg) {
		super(msg);
	}
}
