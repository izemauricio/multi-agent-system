package servidor;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String usuario;
	String senha;
	String nome;
	
	public Usuario(String nome,String usuario,String senha)
	{
		this.nome = nome;
		this.usuario = usuario;
		this.senha = senha;
	}
	
}
