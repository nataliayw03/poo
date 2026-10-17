package senc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import senc.atividades.Atividade;
import senc.atividades.Minicurso;
import senc.atividades.Palestra;
import senc.atividades.VisitaTecnica;
import senc.excecoes.CertificadoNaoEmitidoException;
import senc.excecoes.ConflitoHorarioException;
import senc.excecoes.InscricaoInvalidaException;
import senc.excecoes.VagasEsgotadasException;
import senc.pessoas.Estudante;
import senc.pessoas.MembroOrganizacao;
import senc.pessoas.Palestrante;
import senc.pessoas.Pessoa;

/*
 * Demonstracao de todos os conceitos do projeto.
 */
public class Simulacao {

	public static void executar() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		System.out.println("================================================================");
		System.out.println("  SEnC - Semana da Engenharia de Computacao");
		System.out.println("================================================================");

		// =====================================================================
		// 1) CLASSES E OBJETOS: criando objetos (instancias) das classes
		// =====================================================================
		Estudante ana = new Estudante("Ana Souza", "ana.souza@usp.br", 12345678, "Engenharia de Computacao");
		Estudante bruno = new Estudante("Bruno Lima", "bruno.lima@usp.br", 23456789, "Engenharia de Computacao");
		Estudante carla = new Estudante("Carla Mendes", "carla.mendes@usp.br", 34567890, "Engenharia Eletrica");
		MembroOrganizacao diego = new MembroOrganizacao("Diego Alves", "diego.alves@usp.br",
				45678901, "Diretor", "Diretoria de Conteúdos");

		Palestrante fernanda = new Palestrante("Fernanda Rocha", "fernanda.rocha@eldorado.org.br", "Instituto Eldorado",
				"Pesquisa");
		Palestrante rafael = new Palestrante("Rafael Costa", "rafael.costa@robotica.com.br", "ICMC",
				"Engenheiro de Computacao");

		// guarda objetos de subclasses diferentes
		Pessoa[] pessoas = new Pessoa[6];
		pessoas[0] = ana;
		pessoas[1] = bruno;
		pessoas[2] = carla;
		pessoas[3] = diego;
		pessoas[4] = fernanda;
		pessoas[5] = rafael;

		System.out.println("\n--- Pessoas cadastradas ---");
		for (int i = 0; i < pessoas.length; i++) {
			System.out.println(pessoas[i].toString());
		}

		// Atividades (datas sao convertidas de String para Date com o SimpleDateFormat)
		Palestra palestraIoT = new Palestra("IoT e Sistemas Embarcados na Industria",
				sdf.parse("20/10/2026 14:00"), 90, "Auditorio Fernao", 100, fernanda);

		Minicurso minicursoSIAT = new Minicurso("Misseis",
				sdf.parse("20/10/2026 15:00"), 180, "Sala 4-000", 20, "Matlab", rafael);

		// Visita tecnica com apenas 2 vagas (para forcarmos a VagasEsgotadasException)
		VisitaTecnica visitaEldorado = new VisitaTecnica("Visita ao Instituto Eldorado",
				sdf.parse("21/10/2026 09:00"), 240, "Entrada da Mat", 2,
				"Instituto Eldorado", sdf.parse("21/10/2026 07:00"));

		// lista do tipo da SUPERCLASSE (Atividade)
		List<Atividade> programacao = new ArrayList<>();
		programacao.add(palestraIoT);
		programacao.add(minicursoSIAT);
		programacao.add(visitaEldorado);

		System.out.println("\n--- Programacao da SEnC ---");
		for (Atividade a : programacao) {
			System.out.println(a.toString());
		}

		// =====================================================================
		// 2) INSCRICOES VALIDAS
		// =====================================================================
		System.out.println("\n--- Inscricoes validas ---");
		try {
			palestraIoT.inscrever(ana);
			palestraIoT.inscrever(diego);
			minicursoSIAT.inscrever(bruno);
			minicursoSIAT.inscrever(carla);
		} catch (InscricaoInvalidaException e) {
			System.out.println("  Erro inesperado: " + e.getMessage());
		}

		// =====================================================================
		// 3) EXCECAO: VAGAS ESGOTADAS
		//    A Visita ao Instituto Eldorado tem 2 vagas; tentamos inscrever 3.
		// =====================================================================
		System.out.println("\n--- EXCECAO: VAGAS ESGOTADAS ---");
		try {
			visitaEldorado.inscrever(ana);
			visitaEldorado.inscrever(diego);
			visitaEldorado.inscrever(bruno); // <- aqui a excecao eh lancada
			System.out.println("  (esta linha NAO sera executada)");
		} catch (VagasEsgotadasException e) {
			// Captura a excecao especifica
			System.out.println("  [VagasEsgotadasException] " + e.getMessage());
		} catch (InscricaoInvalidaException e) {
			// Captura qualquer outra excecao da hierarquia de inscricao
			System.out.println("  [InscricaoInvalidaException] " + e.getMessage());
		}

		// =====================================================================
		// 4) EXCECAO: CONFLITO DE HORARIO
		//    Ana esta na palestra das 14:00-15:30 e tenta o minicurso das 15:00.
		// =====================================================================
		System.out.println("\n--- EXCECAO: CONFLITO DE HORARIO ---");
		try {
			minicursoSIAT.inscrever(ana);
		} catch (ConflitoHorarioException e) {
			System.out.println("  [ConflitoHorarioException] " + e.getMessage());
		} catch (InscricaoInvalidaException e) {
			System.out.println("  [InscricaoInvalidaException] " + e.getMessage());
		}

		// =====================================================================
		// 5) EXCECAO: INSCRICAO DUPLICADA
		//    Nao ha catch especifico para InscricaoDuplicadaException:
		//    como ela herda de InscricaoInvalidaException, cai no catch da excecao base.
		// =====================================================================
		System.out.println("\n--- EXCECAO: INSCRICAO DUPLICADA ---");
		try {
			palestraIoT.inscrever(diego);
		} catch (InscricaoInvalidaException e) {
			System.out.println("  [InscricaoInvalidaException] " + e.getMessage());
		}

		// =====================================================================
		// 6) DIA DO EVENTO: presencas, entrega de projetos e embarque
		// =====================================================================
		palestraIoT.registrarPresenca(ana);
		palestraIoT.registrarPresenca(diego);

		minicursoSIAT.registrarPresenca(bruno);
		minicursoSIAT.registrarPresenca(carla);
		minicursoSIAT.registrarEntregaProjeto(carla); // Bruno NAO entregou o projeto

		visitaEldorado.registrarEmbarque(ana);            // Diego NAO embarcou

		// =====================================================================
		// 7) POLIMORFISMO: gerarCertificado() em um loop de Atividade
		//    A variavel "a" e do tipo Atividade, mas o Java executa a versao
		//    de Palestra, Minicurso ou VisitaTecnica conforme o objeto real.
		// =====================================================================
		System.out.println("\n================================================================");
		System.out.println("  Emissao de certificados ");
		System.out.println("================================================================");
		for (Atividade a : programacao) {
			System.out.println("\n--- " + a.getTipo() + ": " + a.getTitulo() + " ---");
			for (Pessoa p : a.getInscritos()) {
				try {
					System.out.println(a.gerarCertificado(p)); // <- chamada polimorfica
				} catch (CertificadoNaoEmitidoException e) {
					System.out.println("  [Certificado NAO emitido] " + e.getMessage());
				}
				System.out.println();
			}
		}

		// Erros de dados invalidos usam excecoes padrao do Java
		System.out.println("--- TESTE 4: dados invalidos no construtor ---");
		try {
			Estudante invalido = new Estudante("", "invalido@usp.br", 1, "EngComp");
			System.out.println(invalido.toString()); // nao sera executada
		} catch (IllegalArgumentException e) {
			System.out.println("  [IllegalArgumentException] " + e.getMessage());
		}
	}
}
