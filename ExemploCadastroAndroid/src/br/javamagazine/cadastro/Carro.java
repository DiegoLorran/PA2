package br.javamagazine.cadastro;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Entidade para armazenar os valores de Carro
 * 
 * @author ricardo
 * 
 */
public class Carro {

  private Long id;
  private String nome;
  private String placa;
  private int ano;
  private byte[] imagem;

  public Carro() {}

  public Carro(String nome, String placa, int ano) {
    super();
    this.nome = nome;
    this.placa = placa;
    this.ano = ano;
  }

  public void deserialize(DataInputStream in) throws IOException {
    id = in.readLong();

    nome = in.readUTF();
    placa = in.readUTF();
    ano = in.readInt();

    // Le os bytes da imagem
    int tamanho = in.readInt();
    imagem = new byte[tamanho];
    in.readFully(imagem);
  }

  /**
   * Serializa o carro, escrevendo na OutputStream
   * 
   * @param out
   * @throws IOException
   */
  public void serialize(DataOutputStream out) throws IOException {
    out.writeLong(id == null ? 0 : id);
    out.writeUTF(nome);
    out.writeUTF(placa);
    out.writeInt(ano);
    // Escreve o tamanho e a imagem
    if (imagem != null) {
      out.writeInt(imagem.length);
      out.write(imagem);
    }
    else {
      out.writeInt(0);
    }
  }

  public Bitmap getBitmap() {
    if(imagem == null){
      return null;
    }
    // Cria o Bitmap necessário para exibir no ImageView
    Bitmap bitmap = BitmapFactory.decodeByteArray(imagem, 0, imagem.length);
    return bitmap;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getPlaca() {
    return placa;
  }

  public void setPlaca(String placa) {
    this.placa = placa;
  }

  public int getAno() {
    return ano;
  }

  public void setAno(int ano) {
    this.ano = ano;
  }

  public byte[] getImagem() {
    return imagem;
  }

  public void setImagem(byte[] imagem) {
    this.imagem = imagem;
  }

  @Override
  public String toString() {
    return "Nome: " + nome + ", Placa: " + placa + ", Ano: " + ano;
  }
}
