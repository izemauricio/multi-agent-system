package servidor;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AgenteMinerador implements Runnable
{
	public Socket socketDoCliente;
	
	private Servidor meuServidor;
	private Janela minhaJanela;
	
	private int id;
	public boolean isLivre = true;
	
	int i;
	int j;
	int terreno;

	int novoi;
	int novoj;
	
	private BufferedOutputStream saida;
	private PrintWriter dado;
    private BufferedReader entrada;
    private String buffer;
	private String comandos[];
	
	public AgenteMinerador(Servidor meuServidor,Janela minhaJanela,Socket socketDoCliente,int id)
	{
		this.meuServidor = meuServidor;
		this.minhaJanela = minhaJanela;
		this.socketDoCliente = socketDoCliente;
		this.id = id;
		isLivre = false;
		minhaJanela.addMsg("Agente"+id+" se conectou");
	}
	
	@Override
	public void run() {
		try
		{
				entrada = new BufferedReader(new InputStreamReader(socketDoCliente.getInputStream()));
				saida = new BufferedOutputStream(socketDoCliente.getOutputStream());
				dado = new PrintWriter(saida,false);
		} catch(IOException e)
		{
			minhaJanela.addMsg("Erro ao criar stream de dados");
			desconectar();
		}
		
		try {
			enviarMsg("OI#");
			while(!socketDoCliente.isClosed())
			{				
				buffer = entrada.readLine();
				minhaJanela.addMsg("Comando \""+buffer+"\" recebido do agente"+id);
				comandos = buffer.split("#");
				
				if(comandos[0].equals("OIOK"))
				{
					enviarMsg("SETID#"+id+"#");
				}
				
				else if(comandos[0].equals("SETIDOK"))
				{
					String posicao[] = meuServidor.getPosicaoInicialDoAgente(id).split("#");
					i = Integer.parseInt(posicao[0]);
					j = Integer.parseInt(posicao[1]);
					enviarMsg("SETPOSITION#"+i+"#"+j+"#");
				}
				
				else if(comandos[0].equals("SETPOSITIONOK"))
				{
					meuServidor.setPosicaoDoAgente(id,i,j);
				}

				else if(comandos[0].equals("AVANCAROK"))
				{					
					// Vou mudar de lugar, arruma o que tinha onde eu estava
					i = Integer.parseInt(comandos[1]); // Onde o agente estava
					j = Integer.parseInt(comandos[2]); // Onde o agente estava
					terreno = Integer.parseInt(comandos[3]); // O que tinha onde o agente estava
					meuServidor.setTerreno(id,i,j,terreno); // Atualiza i,j do mapa 
					
					// Pinta de vermelho minha nova posicao
					novoi = Integer.parseInt(comandos[4]); // Para onde o agente vai
					novoj = Integer.parseInt(comandos[5]); // Para onde o agente vai
					meuServidor.setPosicaoDoAgente(id,novoi,novoj); // Atualiza i,j do mapa
					enviarMsg("AVANCARAGENTE#"+novoi+"#"+novoj+"#"); // Atualiza i,j do cliente
				}

				else if(comandos[0].equals("DESCONECTAR"))
				{
					meuServidor.desconectar(id);
					desconectar();
				}
			}
			minhaJanela.addMsg("Agente " + id + "desconectou!");
			desconectar();
			
		} catch(IOException e){
			minhaJanela.addMsg("Erro ao receber stream do agente"+id);
			desconectar();
		}
	}
	
	private void desconectar()
	{
		try
		{
			entrada.close();
			saida.close();
			dado.close();
			socketDoCliente.close();
			isLivre = true;
			meuServidor.setIdLivre(id);
		} catch(Exception e)
		{
		}
	}
	
	public void enviarMsg(String msg)
	{
		dado.println(msg);
		dado.flush();
		minhaJanela.addMsg("Comando \""+msg+"\" enviado para agente"+id);
	}

}
