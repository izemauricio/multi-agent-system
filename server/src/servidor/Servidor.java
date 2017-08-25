package servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class Servidor implements Runnable
{
	private static final int numeroMaximoDeAgentes = 8;
	private AgenteMinerador agenteMinerador[] = new AgenteMinerador[numeroMaximoDeAgentes];
	private boolean idEstaLivre[] = new boolean[numeroMaximoDeAgentes];
	
	private ServerSocket socketDoServidor;
	
	private Janela minhaJanela;
	private Mapa meuMapa;
	
	public Servidor(Janela minhaJanela,Mapa meuMapa)
	{
		this.minhaJanela = minhaJanela;
		this.meuMapa = meuMapa;
		setAllIdLivre();
	}
	
	@Override
	public void run() {
		try {
			socketDoServidor = new ServerSocket(21);
			minhaJanela.addMsg("Servidor criado");
		} catch(Exception e)
		{
			minhaJanela.addMsg("Erro ao criar o servidor");
		}
		
		try {
			while(true)
			{
				int id = getIdLivre();
				if(id != -1)
				{
					minhaJanela.addMsg("Esperando agente"+id+" se conectar no slot["+id+"]...");
					agenteMinerador[id] = new AgenteMinerador(this,minhaJanela,socketDoServidor.accept(),id);
					new Thread(agenteMinerador[id]).start();
				} else {
					minhaJanela.addMsg("Número máximo de conexões atingidas");
					minhaJanela.addMsg("Aguarde algum agente se desconectar...");
					Thread.sleep(10000);
				}
			}
		} catch(Exception e)
		{
			minhaJanela.addMsg("Erro na thread:"+e.getMessage());
		}
	}
	
	private void setAllIdLivre()
	{
		int id=0;
		
		while(id<numeroMaximoDeAgentes)
		{
			minhaJanela.addMsg("slot["+id+"] = livre");
			idEstaLivre[id] = true;
			id++;
		}
	}
	
	private int getIdLivre()
	{
		int id=0;
		
		while(id<numeroMaximoDeAgentes)
		{
			if(idEstaLivre[id]==true)
			{
				minhaJanela.addMsg("slot["+id+"] = ocupado");
				idEstaLivre[id] = false;
				return id;
			}
			id++;
		}
		
		return -1;
	}
	
	public void setIdLivre(int id)
	{
		minhaJanela.addMsg("slot["+id+"] = livre");
		idEstaLivre[id] = true;
	}
	
	public void enviarMsgParaTodos(String msg)
	{
		int id=0;
		
		while(id<numeroMaximoDeAgentes)
		{
			if(agenteMinerador[id] != null)
			{
				if(agenteMinerador[id].socketDoCliente.isConnected() && !(agenteMinerador[id].isLivre))
				{
					agenteMinerador[id].enviarMsg(msg);
					minhaJanela.addMsg("Comando \""+msg+"\" enviado para agente"+id);
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			id++;
		}
	}
	
	public void enviarMsgParaId(int id,String msg)
	{
		if(agenteMinerador[id] != null)
		{
			if(agenteMinerador[id].socketDoCliente.isConnected() && !(agenteMinerador[id].isLivre))
			{
				agenteMinerador[id].enviarMsg(msg);
				minhaJanela.addMsg("Comando \""+msg+"\" enviado para agente"+id);
			}
		}
	}
	
	public void desconectar(int id)
	{
		agenteMinerador[id].enviarMsg("DESCONECTE#");
	}

	public String getPosicaoInicialDoAgente(int id)
	{
		int i,j;
		
		minhaJanela.addMsg("Procurando um lugar na superfície para o agente"+id+"...");
		
		for(i=0;i<20;i++)
		{
			for(j=0;j<20;j++)
			{
				minhaJanela.addMsg("i="+i+" j="+j+" >> "+meuMapa.getTerreno(i,j)+" e "+meuMapa.getTerreno(i+1,j));
				if(meuMapa.getTerreno(i,j)==4 && meuMapa.getTerreno(i+1,j)==0) // é céu && embaixo é terra
				{
					minhaJanela.addMsg("Agete"+id+" inicializara em X="+i+" e Y="+j);
					return(i+"#"+j+"#"+meuMapa.getTerreno(i,j));
				}
			}
		}
		
		return "";
	}
	
	public void setPosicaoDoAgente(int id,int i,int j)
	{
		meuMapa.setPosicaoDoAgente(id,i,j);
		minhaJanela.addMsg("Agente"+id+" foi para posição X="+i+" Y="+j);
	}
	
	public void setTerreno(int id,int i,int j,int terreno)
	{
		if(terreno==2)
			minhaJanela.addMsg("Agente"+id+" encontrou MINÉRIO em X="+i+" Y="+j);
		
		meuMapa.setTerreno(i,j,terreno);
	}	
	
	public void avancarAgentes()
	{
		enviarMsgParaTodos("AVANCAR#");
	}
}