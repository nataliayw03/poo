/*
 * TRABALHO DE POO
 * Ítallo Kauê Barbosa Santos -- NºUSP 16839029
 * Natália Yumi Watanabe - NºUSP 13725566
 */

package senc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

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
 * Classe principal interativa do sistema de gestao da SEnC.
 *
 * - O sistema ja comeca com uma programacao inicial (palestra, minicurso e visita),
 *   que pode ser ampliada, alterada ou reduzida pelo menu.
 * - As pessoas podem ser cadastradas pelo usuario (Scanner).
 * - Disponivel uma demonstracao automatica (classe Simulacao) para mostrar ao prof.
 */
public class Main {

	// Membros estaticos: compartilhados por todos os metodos static desta classe
	private static Scanner sc = new Scanner(System.in);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static List<Pessoa> pessoas = new ArrayList<>();
	private static List<Atividade> programacao = new ArrayList<>();

	public static void main(String[] args) throws ParseException {

		Locale.setDefault(Locale.US);
		criarProgramacaoInicial();

		System.out.println("================================================================");
		System.out.println("  X SEnC - Semana da Engenharia de Computacao");
		System.out.println("================================================================");

		int opcao = -1;
		do {
			mostrarMenu();
			try {
				opcao = sc.nextInt();
				sc.nextLine(); 

				switch (opcao) {
				case 1:
					cadastrarPessoa();
					break;
				case 2:
					listarPessoas();
					break;
				case 3:
					listarProgramacao();
					break;
				case 4:
					adicionarAtividade();
					break;
				case 5:
					alterarAtividade();
					break;
				case 6:
					removerAtividade();
					break;
				case 7:
					inscreverPessoa();
					break;
				case 8:
					registrarParticipacao();
					break;
				case 9:
					gerarCertificados();
					break;
				case 10:
					Simulacao.executar();
					break;
				case 0:
					System.out.println("Sistema encerrado. Ate a proxima SEnC!");
					break;
				default:
					System.out.println("Opcao invalida!");
				}
			}
			catch (InputMismatchException e) {
				// Lancada pelo Scanner quando se digita texto onde era esperado um numero
				System.out.println("Erro: digite um numero.");
				sc.nextLine(); // descarta o que foi digitado
				opcao = -1;
			}
		} while (opcao != 0);

		sc.close();
	}

	private static void mostrarMenu() {
		System.out.println();
		System.out.println("------------------------- MENU -------------------------");
		System.out.println(" PESSOAS");
		System.out.println("  1 - Cadastrar pessoa");
		System.out.println("  2 - Listar pessoas cadastradas");
		System.out.println(" PROGRAMACAO");
		System.out.println("  3 - Ver programacao");
		System.out.println("  4 - Adicionar atividade");
		System.out.println("  5 - Alterar atividade");
		System.out.println("  6 - Remover atividade");
		System.out.println(" EVENTO");
		System.out.println("  7 - Inscrever pessoa em uma atividade");
		System.out.println("  8 - Registrar presenca / entrega de projeto / embarque");
		System.out.println("  9 - Gerar certificados");
		System.out.println(" 10 - Executar demonstracao automatica");
		System.out.println("  0 - Sair");
		System.out.print("Opcao: ");
	}

	// ======================= PROGRAMACAO INICIAL =======================

	private static void criarProgramacaoInicial() throws ParseException {
		Palestrante fernanda = new Palestrante("Fernanda Rocha", "fernanda.rocha@eldorado.org.br", "Instituto Eldorado",
				"Pesquisadora em sistemas embarcados e IoT.");
		Palestrante rafael = new Palestrante("Rafael Costa", "rafael.costa@robotica.com.br", "ICMC",
				"Engenheiro de Computacao.");
		pessoas.add(fernanda);
		pessoas.add(rafael);

		// POLIMORFISMO: lista do tipo da SUPERCLASSE guardando objetos das subclasses
		programacao.add(new Palestra("IoT e Sistemas Embarcados na Industria",
				sdf.parse("20/10/2026 14:00"), 90, "Auditorio Principal", 100, fernanda));

		programacao.add(new Minicurso("Introducao ao Arduino",
				sdf.parse("20/10/2026 15:00"), 180, "Sala 4-000", 20, "Matlab", rafael));

		programacao.add(new VisitaTecnica("Visita ao Instituto Eldorado",
				sdf.parse("21/10/2026 09:00"), 240, "Entrada da Mat", 2,
				"Instituto Eldorado", sdf.parse("21/10/2026 07:00")));
	}

	// ======================= 1) CADASTRO DE PESSOAS =======================

	private static void cadastrarPessoa() {
		System.out.println("Tipo de pessoa: 1 - Estudante | 2 - Membro da organizacao | 3 - Palestrante");
		System.out.print("Tipo: ");
		int tipo = sc.nextInt();
		sc.nextLine();
		if (tipo < 1 || tipo > 3) {
			System.out.println("Tipo invalido.");
			return;
		}

		System.out.print("Nome: ");
		String nome = sc.nextLine();
		System.out.print("Email: ");
		String email = sc.nextLine();


		// upcasting: a variavel e do tipo Pessoa, mas recebe um objeto de uma subclasse
		Pessoa nova;
		try {
			if (tipo == 1) {
				System.out.print("Numero USP: ");
				int nroUSP = sc.nextInt();
				sc.nextLine();
				System.out.print("Curso: ");
				String curso = sc.nextLine();
				nova = new Estudante(nome, email, nroUSP, curso);
			}
			else if (tipo == 2) {
				System.out.print("Numero USP: ");
				int nroUSP = sc.nextInt();
				sc.nextLine();
				System.out.print("Cargo: ");
				String cargo = sc.nextLine();
				System.out.print("Diretoria: ");
				String diretoria = sc.nextLine();
				nova = new MembroOrganizacao(nome, email, nroUSP, cargo, diretoria);
			}
			else {
				System.out.print("Empresa: ");
				String empresa = sc.nextLine();
				System.out.print("Minibio: ");
				String minibio = sc.nextLine();
				nova = new Palestrante(nome, email, empresa, minibio);
			}
			pessoas.add(nova);
			System.out.println("Cadastro realizado: " + nova.toString());
		}
		catch (IllegalArgumentException e) {
			// Lancada pelo construtor de Pessoa quando o nome esta vazio ou o email nao tem "@"
			System.out.println("Erro no cadastro: " + e.getMessage());
		}
	}


	// ======================= 2) e 3) LISTAGENS =======================

	private static void listarPessoas() {
		if (pessoas.size() == 0) {
			System.out.println("Nenhuma pessoa cadastrada.");
			return;
		}
		System.out.println("Pessoas cadastradas:");
		for (int i = 0; i < pessoas.size(); i++) {
			// polimorfismo: cada objeto executa o seu toString()
			System.out.println((i + 1) + " - " + pessoas.get(i).toString());
		}
	}

	private static void listarProgramacao() {
		if (programacao.size() == 0) {
			System.out.println("A programacao esta vazia.");
			return;
		}
		System.out.println("Programacao da SEnC:");
		for (int i = 0; i < programacao.size(); i++) {
			System.out.println((i + 1) + " - " + programacao.get(i).toString());
		}
	}

	// Mostra a lista e devolve a pessoa escolhida (ou null se a escolha for invalida)
	private static Pessoa escolherPessoa(List<Pessoa> lista) {
		if (lista.size() == 0) {
			System.out.println("Nenhuma pessoa disponivel.");
			return null;
		}
		for (int i = 0; i < lista.size(); i++) {
			System.out.println((i + 1) + " - " + lista.get(i).getNome());
		}
		System.out.print("Numero da pessoa: ");
		int n = sc.nextInt();
		sc.nextLine();
		if (n < 1 || n > lista.size()) {
			System.out.println("Numero invalido.");
			return null;
		}
		return lista.get(n - 1);
	}

	private static Atividade escolherAtividade() {
		if (programacao.size() == 0) {
			System.out.println("A programacao esta vazia.");
			return null;
		}
		listarProgramacao();
		System.out.print("Numero da atividade: ");
		int n = sc.nextInt();
		sc.nextLine();
		if (n < 1 || n > programacao.size()) {
			System.out.println("Numero invalido.");
			return null;
		}
		return programacao.get(n - 1);
	}

	// Le uma data no formato dd/MM/yyyy HH:mm
	// "throws ParseException": quem chama decide como tratar uma data mal digitada
	private static Date lerData(String pergunta) throws ParseException {
		System.out.print(pergunta + " (dd/MM/yyyy HH:mm): ");
		return sdf.parse(sc.nextLine());
	}

	// ======================= 4) ADICIONAR ATIVIDADE =======================

	private static void adicionarAtividade() {
		System.out.println("Tipo de atividade: 1 - Palestra | 2 - Minicurso | 3 - Visita tecnica");
		System.out.print("Tipo: ");
		int tipo = sc.nextInt();
		sc.nextLine();
		if (tipo < 1 || tipo > 3) {
			System.out.println("Tipo invalido.");
			return;
		}

		// Palestra e minicurso precisam de um palestrante ja cadastrado
		Palestrante palestrante = null;
		if (tipo == 1 || tipo == 2) {
			palestrante = escolherPalestrante();
			if (palestrante == null)
				return;
		}

		try {
			System.out.print("Titulo: ");
			String titulo = sc.nextLine();
			Date dataHorario = lerData("Data e horario de inicio");
			System.out.print("Duracao (minutos): ");
			int duracao = sc.nextInt();
			sc.nextLine();
			System.out.print("Local: ");
			String local = sc.nextLine();
			System.out.print("Numero de vagas: ");
			int vagas = sc.nextInt();
			sc.nextLine();

			// upcasting: a variavel e do tipo Atividade, mas recebe uma subclasse
			Atividade nova;
			if (tipo == 1) {
				nova = new Palestra(titulo, dataHorario, duracao, local, vagas, palestrante);
			}
			else if (tipo == 2) {
				System.out.print("Software requerido: ");
				String software = sc.nextLine();
				nova = new Minicurso(titulo, dataHorario, duracao, local, vagas, software, palestrante);
			}
			else {
				System.out.print("Empresa de destino: ");
				String empresa = sc.nextLine();
				Date saidaOnibus = lerData("Saida do onibus");
				nova = new VisitaTecnica(titulo, dataHorario, duracao, local, vagas, empresa, saidaOnibus);
			}

			programacao.add(nova);
			System.out.println("Atividade adicionada: " + nova.toString());
		}
		catch (ParseException e) {
			System.out.println("Data invalida. Use o formato dd/MM/yyyy HH:mm, por exemplo 22/10/2026 10:00.");
		}
		catch (IllegalArgumentException e) {
			// Lancada pelo construtor de Atividade (vagas ou duracao invalidas)
			System.out.println("Erro ao adicionar: " + e.getMessage());
		}
	}

	// Lista apenas as pessoas que sao palestrante (instanceof + downcasting)
	private static Palestrante escolherPalestrante() {
		List<Pessoa> palestrantes = new ArrayList<>();
		for (Pessoa p : pessoas) {
			if (p instanceof Palestrante)
				palestrantes.add(p);
		}
		if (palestrantes.size() == 0) {
			System.out.println("Nenhum palestrante cadastrado. Cadastre um na opcao 1.");
			return null;
		}
		System.out.println("Escolha o palestrante:");
		Pessoa escolhido = escolherPessoa(palestrantes);
		if (escolhido == null)
			return null;
		return (Palestrante) escolhido; // downcasting seguro: so ha palestrantes na lista
	}

	// ======================= 5) ALTERAR ATIVIDADE =======================

	private static void alterarAtividade() {
		Atividade a = escolherAtividade();
		if (a == null)
			return;

		System.out.println("O que deseja alterar em \"" + a.getTitulo() + "\"?");
		System.out.println("1 - Titulo | 2 - Data e horario | 3 - Duracao | 4 - Local | 5 - Numero de vagas");
		System.out.print("Opcao: ");
		int campo = sc.nextInt();
		sc.nextLine();

		try {
			switch (campo) {
			case 1:
				System.out.print("Novo titulo: ");
				a.setTitulo(sc.nextLine());
				break;
			case 2:
				Date novaData = lerData("Nova data e horario");
				a.alterarHorario(novaData, a.getDuracaoMinutos());
				break;
			case 3:
				System.out.print("Nova duracao (minutos): ");
				int novaDuracao = sc.nextInt();
				sc.nextLine();
				a.alterarHorario(a.getDataHorario(), novaDuracao);
				break;
			case 4:
				System.out.print("Novo local: ");
				a.setLocal(sc.nextLine());
				break;
			case 5:
				System.out.print("Novo numero de vagas: ");
				int novasVagas = sc.nextInt();
				sc.nextLine();
				a.setVagasMaximas(novasVagas);
				break;
			default:
				System.out.println("Opcao invalida.");
				return;
			}
			System.out.println("Atividade atualizada: " + a.toString());
		}
		catch (ParseException e) {
			System.out.println("Data invalida. Use o formato dd/MM/yyyy HH:mm.");
		}
		catch (IllegalArgumentException e) {
			System.out.println("Valor invalido: " + e.getMessage());
		}
		catch (IllegalStateException e) {
			System.out.println("Alteracao nao permitida: " + e.getMessage());
		}
	}

	// ======================= 6) REMOVER ATIVIDADE =======================

	private static void removerAtividade() {
		Atividade a = escolherAtividade();
		if (a == null)
			return;

		if (a.getInscritos().size() > 0) {
			System.out.println("Nao e possivel remover \"" + a.getTitulo() + "\": ha "
					+ a.getInscritos().size() + " pessoa(s) inscrita(s).");
			return;
		}

		System.out.print("Confirma a remocao de \"" + a.getTitulo() + "\" (s/n)? ");
		char resposta = sc.next().charAt(0);
		sc.nextLine();
		if (resposta == 's' || resposta == 'S') {
			programacao.remove(a);
			System.out.println("Atividade removida.");
		}
		else {
			System.out.println("Remocao cancelada.");
		}
	}

	// ======================= 7) INSCRICAO =======================

	private static void inscreverPessoa() {
		Pessoa p = escolherPessoa(pessoas);
		if (p == null)
			return;
		Atividade a = escolherAtividade();
		if (a == null)
			return;

		// Tratamento de excessoes: do catch mais especifico para o mais generico
		try {
			a.inscrever(p);
		}
		catch (VagasEsgotadasException e) {
			System.out.println("[VagasEsgotadasException] " + e.getMessage());
		}
		catch (ConflitoHorarioException e) {
			System.out.println("[ConflitoHorarioException] " + e.getMessage());
		}
		catch (InscricaoInvalidaException e) {
			// captura as outras filhas, como InscricaoDuplicadaException
			System.out.println("[InscricaoInvalidaException] " + e.getMessage());
		}
	}

	// ======================= 8) PARTICIPACAO =======================

	private static void registrarParticipacao() {
		Atividade a = escolherAtividade();
		if (a == null)
			return;

		System.out.println("Inscritos em \"" + a.getTitulo() + "\":");
		Pessoa p = escolherPessoa(a.getInscritos());
		if (p == null)
			return;

		// downcasting com instanceof: so a visitaTecnica tem registrarEmbarque()
		// e so o minicurso tem registrarEntregaProjeto()
		if (a instanceof VisitaTecnica) {
			VisitaTecnica visita = (VisitaTecnica) a;
			visita.registrarEmbarque(p);
			System.out.println("Embarque de " + p.getNome() + " registrado.");
		}
		else if (a instanceof Minicurso) {
			Minicurso minicurso = (Minicurso) a;
			minicurso.registrarPresenca(p);
			System.out.println("Presenca de " + p.getNome() + " registrada.");
			System.out.print("Entregou o projeto final (s/n)? ");
			char resposta = sc.next().charAt(0);
			sc.nextLine();
			if (resposta == 's' || resposta == 'S') {
				minicurso.registrarEntregaProjeto(p);
				System.out.println("Entrega do projeto registrada.");
			}
		}
		else {
			a.registrarPresenca(p);
			System.out.println("Presenca de " + p.getNome() + " registrada.");
		}
	}

	// ======================= 9) CERTIFICADOS  =======================

	private static void gerarCertificados() {
		if (programacao.size() == 0) {
			System.out.println("A programacao esta vazia.");
			return;
		}
		for (Atividade a : programacao) {
			System.out.println("\n--- " + a.getTipo() + ": " + a.getTitulo() + " ---");
			if (a.getInscritos().size() == 0) {
				System.out.println("(nenhum inscrito)");
			}
			for (Pessoa p : a.getInscritos()) {
				try {
					// polimorfismo: executa a versao de Palestra, Minicurso ou VisitaTecnica
					System.out.println(a.gerarCertificado(p));
				}
				catch (CertificadoNaoEmitidoException e) {
					System.out.println("[Certificado NAO emitido] " + e.getMessage());
				}
			}
		}
	}
}
