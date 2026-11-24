Projeto SEnC - Gestao da SEnC (POO em Java) - PARTE 2

Como compilar e executar (a partir da pasta do projeto):
  javac -d bin src/senc/*.java src/senc/*/*.java
  java -cp bin senc.Main
No Eclipse: basta rodar a classe senc.Main. O arquivo senc-dados.ser
eh criado na pasta do projeto.

Pacotes:
  senc              -> Main (menu interativo) e Simulacao (demonstracao automatica)
  senc.pessoas      -> Pessoa (abstrata), Estudante, MembroOrganizacao, Palestrante
  senc.atividades   -> Atividade (abstrata), Palestra, Minicurso, VisitaTecnica
  senc.excecoes     -> InscricaoInvalidaException (base) + 3 filhas, CertificadoNaoEmitidoException
  --- PARTE 2 ---
  senc.observer     -> Observador (interface), Sujeito (abstrata)      [padrao Observer]
  senc.sistema      -> SistemaSEnC (sujeito), DadosSEnC, Repositorio<T> [genericos]
  senc.persistencia -> Persistencia<T extends Serializable>, AutoSave   [serializacao + thread]

Requisitos da parte 2:
  Persistencia : serializacao (ObjectOutputStream/ObjectInputStream) no arquivo senc-dados.ser.
                 Carrega ao abrir; salva ao sair (opcao 0) e na opcao 11.
  Thread       : AutoSave (Runnable) salva a cada 20 s, so se houve alteracao.
                 Parada com interrupt() + join() ao sair. Metodos synchronized.
  Padrao       : Observer. SistemaSEnC (Sujeito) avisa o AutoSave (Observador) a cada alteracao.
  Genericos    : Repositorio<T>, Persistencia<T extends Serializable>,
                 metodo <T extends Pessoa> escolherPessoa, Comparable<Atividade>.
  Collections  : List/ArrayList, Collections.sort (programacao por data), Map/TreeMap (opcao 12).

Uso (menu da Main):
  PESSOAS      1 Cadastrar | 2 Listar
  PROGRAMACAO  3 Ver | 4 Adicionar atividade | 5 Alterar atividade | 6 Remover atividade
  EVENTO       7 Inscrever | 8 Registrar presenca/projeto/embarque | 9 Gerar certificados
               10 Demonstracao automatica
  DADOS        11 Salvar agora | 12 Relatorio de inscricoes | 0 Sair (salva os dados)
Datas no formato dd/MM/yyyy HH:mm (ex.: 22/10/2026 10:00).

Para voltar a programacao inicial: apague o arquivo senc-dados.ser.
Obs.: no Eclipse, digite as respostas na aba Console.
