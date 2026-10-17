package senc.excecoes;

/*
 * Segunda familia de excecoes 
 * Lancada por gerarCertificado() quando a pessoa nao cumpriu os requisitos
 * da atividade (faltou, nao entregou o projeto, nao embarcou no onibus...).
 */
public class CertificadoNaoEmitidoException extends Exception {

	private static final long serialVersionUID = 1L;

	public CertificadoNaoEmitidoException(String msg) {
		super(msg);
	}
}
