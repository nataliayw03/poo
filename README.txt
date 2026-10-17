Projeto SEnC - Gestao da SEnC (POO em Java)

Como compilar e executar (a partir da pasta SEnC):
  javac -d bin src/senc/*.java src/senc/pessoas/*.java src/senc/atividades/*.java src/senc/excecoes/*.java
  java -cp bin senc.Main

Pacotes:
  senc             -> Main (menu interativo) e Simulacao (demonstracao automatica)
  senc.pessoas     -> Pessoa (abstrata), Estudante, MembroOrganizacao, Palestrante
  senc.atividades  -> Atividade (abstrata), Palestra, Minicurso, VisitaTecnica
  senc.excecoes    -> InscricaoInvalidaException (base)
                        |- VagasEsgotadasException
                        |- ConflitoHorarioException
                        |- InscricaoDuplicadaException
                      CertificadoNaoEmitidoException

Uso (menu da Main):
  PESSOAS      1 Cadastrar | 2 Listar
  PROGRAMACAO  3 Ver | 4 Adicionar atividade | 5 Alterar atividade | 6 Remover atividade
  EVENTO       7 Inscrever | 8 Registrar presenca/projeto/embarque | 9 Gerar certificados
               10 Demonstracao automatica | 0 Sair
Datas no formato dd/MM/yyyy HH:mm (ex.: 22/10/2026 10:00).
Obs.: no Eclipse, digite as respostas na aba Console.
