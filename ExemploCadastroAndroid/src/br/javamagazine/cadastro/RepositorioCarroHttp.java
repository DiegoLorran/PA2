package br.javamagazine.cadastro;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Implementacao "Fake"
 * 
 * @author ricardo
 *
 */
public class RepositorioCarroHttp implements RepositorioCarro {
  // O IP 127.0.0.1 precisa ser '10.0.2.2' porque o 127.0.0.1 est‡ reservado para o emulador
  private static final String URL_LISTAR = "http://10.0.2.2:8080/carros/listarCarros";
  private static final String URL_SALVAR = "http://10.0.2.2:8080/carros/salvarCarro";
  private static final String URL_DELETAR = "http://10.0.2.2:8080/carros/deletarCarro";
  // Categoria do Log para filtrar na janela LogCat
  private static final String CATEGORIA = "javamagazine";
  // Depois de buscar os carros, armazena a lista em memória
  private List<Carro> carros;

  @Override
  // Lista da web, e armazena a lista de carros em memória
  public List<Carro> listarCarros() {
    Log.i(CATEGORIA, "listarCarros: " + URL_LISTAR);
    HttpURLConnection conn = null;
    try {
      // Cria a URL
      URL u = new URL(URL_LISTAR);
      conn = (HttpURLConnection) u.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(false);

      conn.connect();

      int resposta = conn.getResponseCode();
      Log.i(CATEGORIA, "Resposta: " + resposta);

      this.carros = new ArrayList<Carro>();

      InputStream in = conn.getInputStream();
      DataInputStream dataIn = new DataInputStream(in);

      if (resposta == 200) {
        int quantidade = dataIn.readInt();
        Log.i(CATEGORIA, "Lendo " + quantidade + " carros...");

        for (int i = 0; i < quantidade; i++) {
          Carro c = new Carro();
          c.deserialize(dataIn);
          carros.add(c);
        }
      }

      return carros;
    }
    catch (IOException e) {
      Log.e(CATEGORIA, e.getMessage(), e);
    }
    finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return null;
  }

  @Override
  // Salva o carro fazendo uma requisição web, enviando os dados por POST
  public boolean salvar(Carro carro) {
    Log.i(CATEGORIA, "salvar: " + URL_SALVAR);
    try {
      // Cria a URL
      URL u = new URL(URL_SALVAR);
      HttpURLConnection conn = (HttpURLConnection) u.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);

      conn.connect();

      OutputStream out = conn.getOutputStream();
      DataOutputStream dataOut = new DataOutputStream(out);

      try {
        Log.i(CATEGORIA, "Salvando carro: " + carro.getId());
        carro.serialize(dataOut);

        dataOut.flush();
        out.flush();
        out.close();

        int resposta = conn.getResponseCode();
        Log.i(CATEGORIA, "Resposta: " + resposta);

        Log.i(CATEGORIA, "Carro salvo com sucesso");

        return resposta == 200;
      }
      finally {
        conn.disconnect();
      }
    }
    catch (IOException e) {
      Log.e(CATEGORIA, e.getMessage(), e);
    }
    return false;
  }

  @Override
  // Deleta o carro fazendo uma requisição web
  public boolean deletar(Carro carro) {
    Log.i(CATEGORIA, "deletar: " + URL_DELETAR);
    try {
      // Cria a URL
      URL u = new URL(URL_DELETAR);
      HttpURLConnection conn = (HttpURLConnection) u.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);

      conn.connect();

      OutputStream out = conn.getOutputStream();
      DataOutputStream dataOut = new DataOutputStream(out);

      try {
        Log.i(CATEGORIA, "Deletando carro: " + carro.getId());
        //Envia apenas o id
        dataOut.writeLong(carro.getId());

        dataOut.flush();
        out.flush();
        out.close();

        int resposta = conn.getResponseCode();
        Log.i(CATEGORIA, "Resposta: " + resposta);

        Log.i(CATEGORIA, "Carro excluido com sucesso");

        return resposta == 200;
      }
      finally {
        conn.disconnect();
      }

    }
    catch (MalformedURLException e) {
      Log.e(CATEGORIA, e.getMessage(), e);
    }
    catch (IOException e) {
      Log.e(CATEGORIA, e.getMessage(), e);
    }
    return false;
  }

  @Override
  // Busca em memória pelo id
  public Carro getCarro(Long id) {
    if (carros != null) {
      for (Carro carro : carros) {
        if (carro.getId().equals(id)) { return carro; }
      }
    }
    return null;
  }

  @Override
  // Busca em memória
  public Carro buscarCarroPorNome(String nome) {
    List<Carro> carros = listarCarros();
    for (Carro carro : carros) {
      if (carro.getNome().equals(nome)) { return carro; }
    }
    return null;
  }
}
