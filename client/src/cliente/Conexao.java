package cliente;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Conexao implements Runnable {
	
	/*
	 * 0 = terra
	 * 1 = pedra
	 * 2 = minerio
	 * 3 = agente
	 * 4 = ceu
	 */
	int mundoReal[][] = 
		{
				{ 4,4,4,4,4,4,4,4,4,4, 4,4,4,4,4,4,4,4,4,4 },
				{ 4,4,4,4,4,4,4,4,4,4, 4,4,4,4,4,4,4,4,4,4 },
				{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				
				{ 0,0,0,0,0,1,1,1,1,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,0,0,1,1,1,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 1,0,0,0,0,0,0,0,0,0, 0,1,1,1,0,0,0,0,0,0 },
				{ 1,1,0,0,0,0,0,0,0,0, 0,0,1,1,1,0,0,0,0,0 },
				{ 1,1,0,0,0,0,0,0,0,0, 0,0,1,1,0,0,0,0,0,0 },
				
				{ 1,1,1,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 1,1,1,0,0,2,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 1,0,0,0,2,2,2,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,2,2,2,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				
				{ 0,0,2,2,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 0,0,2,2,2,2,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
				{ 1,0,0,0,2,2,0,0,0,0, 0,0,0,0,0,2,2,2,0,0 },
				{ 1,1,0,0,0,0,2,2,0,0, 0,0,0,0,2,2,2,2,0,0 },
				{ 1,1,1,1,0,2,2,2,2,0, 0,0,0,0,0,0,0,0,0,0 }
		};
		
	private boolean control = false;
	
	// Dados do agente
	private int id;
	private String ip;
	private int porta;
	private int i=0;
	private int j=0;
	private int terreno=4;
	private String direcao;
	
	// Dados da conexão
	private Socket meuSocket;
	private BufferedOutputStream saida;
	private BufferedReader entrada;
	private PrintWriter dado;
	private String buffer;
	private String comandos[];
	
	// Referência de outros objetos
	private Janela minhaJanela;
	
	public Conexao(Janela minhaJanela,String ip,int porta)
	{
		this.minhaJanela = minhaJanela;
		this.ip = ip;
		this.porta = porta;
	}

	@Override
	public void run()
	{
		if(conectar(ip,porta))
		{
			try
			{
				while(meuSocket.isConnected())
				{
					buffer = entrada.readLine();
					minhaJanela.addTexto(">" + buffer);
					comandos = buffer.split("#");
					
					if(comandos[0].equals("OI"))
					{
						enviarMsg("OIOK#");
					}
					
					else if(comandos[0].equals("SETID"))
					{
						id = Integer.parseInt(comandos[1]);
						minhaJanela.setTitle("AgenteMinerador"+id);
						enviarMsg("SETIDOK#");
					}
					
					else if(comandos[0].equals("SETPOSITION"))
					{
						i = Integer.parseInt(comandos[1]);
						j = Integer.parseInt(comandos[2]);
						terreno = mundoReal[i][j];
						enviarMsg("SETPOSITIONOK#");
					}
				
					else if(comandos[0].equals("AVANCAR"))
					{
						int novoi=i;
						int novoj=j;
						int novoterreno=-1;
						
						Random numero = new Random();
						int gerado;
						
						while(novoterreno==-1)
						{
							gerado = numero.nextInt(4);
							
							//0=cima 1=baixo 2=esquerda 3=direita
							switch(gerado)
							{
							case 0:
								if(i>3 && (control==true))
								{
									if(i>18)
										control=true;
									
									novoi = i-1;
									novoj=j;
									novoterreno = mundoReal[novoi][novoj];
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+" CIMA");
									direcao = "CIMA";
							
								} else {
									novoterreno=-1;
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+": INVÁLIDO");
								}
								break;
							case 1:
								if(i<19)
								{
									novoi = i+1;
									novoj=j;
									novoterreno = mundoReal[novoi][novoj];
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+" BAIXO");
									direcao = "BAIXO";
								} else {
									novoterreno=-1;
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+": INVÁLIDO");
								}
								break;
							case 2:
								if(j>0)
								{
									novoi = i;
									novoj=j-1;
									novoterreno = mundoReal[novoi][novoj];
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+": ESQUERDA");
									direcao = "ESQUERDA";
								} else {
									novoterreno=-1;
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+": INVÁLIDO");
								}
								break;
							case 3:
								if(j<19)
								{
									novoi = i;
									novoj=j+1;
									novoterreno = mundoReal[novoi][novoj];
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+": DIREITA");
									direcao = "DIREITA";
								} else {
									novoterreno=-1;
									minhaJanela.addTexto("Numero aleatório gerado: "+gerado+": INVÁLIDO");
								}
								break;
							}
							
						}
						
						enviarMsg("AVANCAROK#"+i+"#"+j+"#"+terreno+"#"+novoi+"#"+novoj+"#");
					}
					
					else if(comandos[0].equals("AVANCARAGENTE"))
					{
						i = Integer.parseInt(comandos[1]);
						j = Integer.parseInt(comandos[2]);
						terreno = mundoReal[i][j];
						minhaJanela.setInfo("AgenteID: "+id+" X="+i+" Y="+j+" Dir: "+direcao);
						enviarMsg("AVANCARAGENTEOK#");
					}
					
					else if(comandos[0].equals("DESCONECTE"))
					{
						minhaJanela.setVisualStatus(Janela.DESCONECTADO);
					}
					
				}
				minhaJanela.addTexto("Socket while terminou");
				minhaJanela.setVisualStatus(Janela.DESCONECTADO);
			} catch(IOException e) {
				minhaJanela.addTexto("Erro ao receber dados");
				minhaJanela.setVisualStatus(Janela.DESCONECTADO);
			}
		}
	}
	
	public void enviarMsg(String msg)
	{
		dado.println(msg);
		dado.flush();
		minhaJanela.addTexto("<"+msg);
	}
	
	public boolean conectar(String ip,int porta)
	{
		try {
			minhaJanela.addTexto("Tentando conectar...");
			meuSocket = new Socket(ip,porta);
			saida = new BufferedOutputStream(meuSocket.getOutputStream());
			dado = new PrintWriter(saida,false);
			entrada = new BufferedReader(new InputStreamReader(meuSocket.getInputStream()));
			minhaJanela.setVisualStatus(Janela.CONECTADO);
			return true;
		} catch(Exception e) {
			minhaJanela.addTexto("Erro ao conectar! " + e.getMessage());
			minhaJanela.setVisualStatus(Janela.DESCONECTADO);
			return false;
		}
	}
	
	public void desconectar()
	{
		enviarMsg("DESCONECTAR#");
	}
	
}
