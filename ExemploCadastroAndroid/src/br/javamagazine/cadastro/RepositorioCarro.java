package br.javamagazine.cadastro;

import java.util.List;

/**
 * <pre>
 * Repositório para carros que utiliza o SQLite internamente
 * 
 * Para visualizar o banco pelo adb shell:
 * 
 * &gt;&gt; sqlite3 /data/data/br.livro.android.exemplos.banco/databases/BancoCarro
 * 
 * &gt;&gt; Mais info dos comandos em: http://www.sqlite.org/sqlite.html
 * 
 * &gt;&gt; .exit para sair
 * 
 * </pre>
 * 
 * @author rlecheta
 * 
 */
public interface RepositorioCarro {
  // Insere ou atualiza o carro
  public boolean salvar(Carro carro);

  // Deleta o carro
  public boolean deletar(Carro carro);

  // Busca o carro pelo id
  public Carro getCarro(Long id);

  // Retorna uma lista com todos os carros
  public List<Carro> listarCarros();

  // Busca o carro pelo nome
  public Carro buscarCarroPorNome(String nome);
}
