package senc.excecoes;

/*
 * EXCECAO BASE (checked) da hierarquia de erros de inscricao.
 *Herda de Exception e repassa a mensagem para o construtor da superclasse.
 *
 * Como as excecoes especificas (VagasEsgotadasException, ConflitoHorarioException,
 * InscricaoDuplicadaException) herdam desta classe, um unico
 *     catch (InscricaoInvalidaException e)
 * consegue capturar qualquer uma delas (polimorfismo aplicado a excecoes).
 */
public class InscricaoInvalidaException extends Exception {

	private static final long serialVersionUID = 1L;

	public InscricaoInvalidaException(String msg) {
		super(msg);
	}
}
