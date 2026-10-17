package senc.atividades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import senc.excecoes.CertificadoNaoEmitidoException;
import senc.excecoes.ConflitoHorarioException;
import senc.excecoes.InscricaoDuplicadaException;
import senc.excecoes.InscricaoInvalidaException;
import senc.excecoes.VagasEsgotadasException;
import senc.pessoas.Pessoa;

/*
 * Superclasse abstrata de todas as atividades da SEnC.
 *
 * Contem o que eh comum entre Palestra, Minicurso e VisitaTecnica
 * (titulo, data/horario, local, vagas, inscritos, presenca e a regra de inscricao).
 *
 * O que eh diferente fica no metodo abstrato gerarCertificado(),
 * que cada subclasse implementa do seu jeito.
 */
public abstract class Atividade {

	// Membros estaticos: um unico formatador compartilhado por todos os objetos.
	// "protected" para que as subclasses tambem possam usa-los.
	protected static SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
	protected static SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

	private String titulo;
	private Date dataHorario;      
	private int duracaoMinutos;
	private String local;
	private int vagasMaximas;

	// composicao: listas de pessoas
	private List<Pessoa> inscritos = new ArrayList<>();
	private List<Pessoa> presentes = new ArrayList<>();

	public Atividade(String titulo, Date dataHorario, int duracaoMinutos, String local, int vagasMaximas) {
		if (vagasMaximas <= 0)
			throw new IllegalArgumentException("Numero de vagas invalido: " + vagasMaximas);
		if (duracaoMinutos <= 0)
			throw new IllegalArgumentException("Duracao invalida: " + duracaoMinutos);

		this.titulo = titulo;
		this.dataHorario = dataHorario;
		this.duracaoMinutos = duracaoMinutos;
		this.local = local;
		this.vagasMaximas = vagasMaximas;
	}

	// ======================= GETTERS =======================

	public String getTitulo() {
		return titulo;
	}

	public Date getDataHorario() {
		return dataHorario;
	}

	// calcula o horario de termino somando a duracao com Calendar
	public Date getHorarioTermino() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataHorario);
		cal.add(Calendar.MINUTE, duracaoMinutos);
		return cal.getTime();
	}

	public int getDuracaoMinutos() {
		return duracaoMinutos;
	}

	public String getLocal() {
		return local;
	}

	public int getVagasMaximas() {
		return vagasMaximas;
	}

	public int getVagasDisponiveis() {
		return vagasMaximas - inscritos.size();
	}

	public List<Pessoa> getInscritos() {
		return inscritos;
	}

	public boolean estaInscrito(Pessoa p) {
		return inscritos.contains(p);
	}

	public boolean estevePresente(Pessoa p) {
		return presentes.contains(p);
	}

	// texto do tipo "20/10/2026 14:00-15:30"
	public String getPeriodo() {
		return sdfData.format(dataHorario) + " " + sdfHora.format(dataHorario)
				+ "-" + sdfHora.format(getHorarioTermino());
	}

	// ======================= ALTERACAO DA PROGRAMACAO =======================
	// Setters com validacao (como setLimite na classe Conta da aula):
	// o objeto nunca fica em um estado invalido.

	public void setTitulo(String titulo) {
		if (titulo == null || titulo.isBlank())
			throw new IllegalArgumentException("Titulo invalido");
		this.titulo = titulo;
	}

	public void setLocal(String local) {
		if (local == null || local.isBlank())
			throw new IllegalArgumentException("Local invalido");
		this.local = local;
	}

	public void setVagasMaximas(int vagasMaximas) {
		if (vagasMaximas <= 0)
			throw new IllegalArgumentException("Numero de vagas invalido: " + vagasMaximas);
		if (vagasMaximas < inscritos.size())
			throw new IllegalStateException("Ja existem " + inscritos.size()
					+ " inscritos; o numero de vagas nao pode ser menor que isso.");
		this.vagasMaximas = vagasMaximas;
	}

	/*
	 * Muda data/horario e duracao. Se o novo horario criar conflito para
	 * algum inscrito, a alteracao eh desfeita e uma excecao eh lancada.
	 */
	public void alterarHorario(Date novaDataHorario, int novaDuracaoMinutos) {
		if (novaDuracaoMinutos <= 0)
			throw new IllegalArgumentException("Duracao invalida: " + novaDuracaoMinutos);

		Date dataAntiga = dataHorario;
		int duracaoAntiga = duracaoMinutos;
		dataHorario = novaDataHorario;
		duracaoMinutos = novaDuracaoMinutos;

		for (Pessoa p : inscritos) {
			for (Atividade outra : p.getAtividadesInscritas()) {
				if (conflitaCom(outra)) {
					dataHorario = dataAntiga;       
					duracaoMinutos = duracaoAntiga;
					throw new IllegalStateException("O novo horario conflita com \"" + outra.getTitulo()
							+ "\", em que " + p.getNome() + " tambem esta inscrito(a).");
				}
			}
		}
	}

	// ======================= INSCRICAO (EXCECOES) =======================

	/*
	 * TRATAMENTO DE EXCECOES:
	 * A assinatura declara "throws InscricaoInvalidaException" (a excecao BASE).
	 * Por heranca, isso cobre todas as excecoes filhas que podem ser lancadas aqui:
	 *   - InscricaoDuplicadaException
	 *   - VagasEsgotadasException
	 *   - ConflitoHorarioException
	 * Quem chamar este metodo eh obrigado a tratar (try-catch) ou repassar (throws).
	 */
	public void inscrever(Pessoa p) throws InscricaoInvalidaException {

		// 1) A pessoa ja esta inscrita nesta atividade?
		if (estaInscrito(p))
			throw new InscricaoDuplicadaException(
					p.getNome() + " ja esta inscrito(a) em \"" + titulo + "\".");

		// 2) Ainda ha vagas?
		if (inscritos.size() >= vagasMaximas)
			throw new VagasEsgotadasException(
					"Nao foi possivel inscrever " + p.getNome() + " em \"" + titulo
					+ "\": todas as " + vagasMaximas + " vagas ja foram preenchidas.");

		// 3) A pessoa ja tem outra atividade no mesmo horario?
		for (Atividade outra : p.getAtividadesInscritas()) {
			if (conflitaCom(outra))
				throw new ConflitoHorarioException(
						p.getNome() + " nao pode se inscrever em \"" + titulo + "\" (" + getPeriodo()
						+ "), pois ja esta inscrito(a) em \"" + outra.getTitulo() + "\" ("
						+ outra.getPeriodo() + ").");
		}

		// Passou por todas as validacoes: confirma a inscricao
		inscritos.add(p);
		p.adicionarAtividade(this);
		System.out.println("  [OK] " + p.getNome() + " inscrito(a) em \"" + titulo
				+ "\" (vagas restantes: " + getVagasDisponiveis() + ")");
	}

	/*
	 * Duas atividades conflitam se os intervalos [inicio, termino] se sobrepoem.
	 * Um objeto Date armazena internamente o numero de milissegundos desde
	 * 01/01/1970; getTime() devolve esse numero, que podemos comparar com < e >.
	 */
	public boolean conflitaCom(Atividade outra) {
		if (this == outra) // uma atividade nao conflita com ela mesma
			return false;

		long inicio1 = this.getDataHorario().getTime();
		long termino1 = this.getHorarioTermino().getTime();
		long inicio2 = outra.getDataHorario().getTime();
		long termino2 = outra.getHorarioTermino().getTime();

		return inicio1 < termino2 && inicio2 < termino1;
	}

	// ======================= PRESENCA =======================

	public void registrarPresenca(Pessoa p) {
		if (!estaInscrito(p))
			throw new IllegalStateException(p.getNome() + " nao esta inscrito(a) em \"" + titulo + "\"");
		if (!presentes.contains(p)) // evita registrar duas vezes
			presentes.add(p);
	}

	// ======================= CERTIFICADO  =======================

	/*
	 * METODO ABSTRATO -> POLIMORFISMO.
	 * Cada subclasse (Palestra, Minicurso, VisitaTecnica) tem uma regra diferente
	 * para emitir o certificado e um texto diferente.
	 * Se a pessoa nao cumprir a regra, eh lancada CertificadoNaoEmitidoException.
	 */
	public abstract String gerarCertificado(Pessoa p) throws CertificadoNaoEmitidoException;

	// Cada subclasse informa o seu "tipo"
	public abstract String getTipo();

	/*
	 * Metodo auxiliar reaproveita por todas as subclasses (heranca).
	 * "protected": acessivel nas subclasses.
	 * So faz sentido gerar certificado para quem estava inscrito.
	 */
	protected void verificarInscricao(Pessoa p) throws CertificadoNaoEmitidoException {
		if (!estaInscrito(p))
			throw new CertificadoNaoEmitidoException(
					p.getNome() + " nao esta inscrito(a) em \"" + titulo + "\".");
	}

	@Override
	public String toString() {
		return "[" + getTipo() + "] " + titulo + " ** " + getPeriodo() + " ** " + local
				+ " ** inscritos: " + inscritos.size() + "/" + vagasMaximas;
	}
}
