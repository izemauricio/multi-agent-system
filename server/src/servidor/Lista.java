package servidor;

import java.io.Serializable;

public class Lista implements Serializable {

	private static final long serialVersionUID = 1L;

	private Nodo noAtual;
	private Nodo noPrimeiro;
	private Nodo noUltimo;
	private int ultimo = 0;
	
	public int inserirUsuario(Usuario novoUsuario)
	{
		Nodo meuNodo = new Nodo();
	    
		meuNodo.usuario = novoUsuario;
		noAtual = meuNodo;
		
		if(noPrimeiro == null)
		{
			noPrimeiro = noAtual;
			noUltimo = noAtual;
			noAtual.proximo = noAtual;
			noAtual.anterior = noAtual;
			return 1;
		}
		else
		{
			noUltimo.proximo = noAtual;
			noAtual.anterior = noUltimo;
			noAtual.proximo = noAtual;
		    noUltimo = noAtual;
		    return 2;
		}
	}
	
	public void removerUsuario(String usuario)
	{
	}
	
	public Nodo buscarUsuario(String bUsuario)
	{
        noAtual = noPrimeiro;
        
        if(noAtual == null)
        {
        	return null;
        }
        
        while(noAtual != noUltimo)
        {
            if(noAtual.usuario.usuario.equals(bUsuario))
                return noAtual;
            else
            	noAtual = noAtual.proximo;
        }
        
        noAtual = noAtual.proximo;
        if(noAtual.usuario.usuario.equals(bUsuario))
            return noAtual;
        
        return null;
	}
	
	public void exibirBanco()
	{
		noAtual = noPrimeiro;
		
		if(noAtual == null) {
			return;
		}

		while(noAtual.proximo != noUltimo)
        {
			noAtual = noAtual.proximo;
        }
	}
	
	public void setInicial()
	{
		noAtual = noPrimeiro;
		ultimo = 0;
	}
	
	public void setProximo()
	{
		noAtual = noAtual.proximo;
	}
	
	public Nodo getAtual()
	{
		if(ultimo == 1)
			return null;
		
		if(noAtual == noUltimo)
			ultimo = 1;
		
		return noAtual;
	}
	
}
