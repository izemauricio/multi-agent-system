package servidor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Janela extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JPanel meuPainelSuperior;
	private JButton meuBotaoAvancar;
	private JButton meuBotaoRepaint;
	private Mapa meuMapa;
	private JTextArea meuTextLog;
	private JScrollPane meuTextLogScroll;
	
	private Servidor meuServidor;
	
	public Janela()
	{
		criarJanela();
		meuServidor = new Servidor(this,meuMapa);
		new Thread(meuServidor).start();
	}
	
	private void criarJanela()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch(Exception e) 
		{
		}
		
		setTitle("AgenteServidor");
		setBounds(0,0,450,690); //x,y,largura,altura
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
				
		meuPainelSuperior = new JPanel();
		meuPainelSuperior.setBackground(Color.white);
		meuPainelSuperior.setLayout(new FlowLayout());
		getContentPane().add(meuPainelSuperior,BorderLayout.NORTH);
		
		meuBotaoAvancar = new JButton();
		meuBotaoAvancar.setText("Avançar");
		meuBotaoAvancar.addActionListener(
				new ActionListener() { 
					public void actionPerformed(ActionEvent e) { 
						
						meuServidor.avancarAgentes();
					} 
				}
			);
		meuPainelSuperior.add(meuBotaoAvancar);
		
		meuBotaoRepaint = new JButton();
		meuBotaoRepaint.setText("Repaint");
		meuBotaoRepaint.addActionListener(
				new ActionListener() { 
					public void actionPerformed(ActionEvent e) { 
						
						meuMapa.repaint2();
					} 
				}
			);
		meuPainelSuperior.add(meuBotaoRepaint);
		
		meuMapa = new Mapa();
		meuMapa.setDoubleBuffered(true);
		getContentPane().add(meuMapa,BorderLayout.CENTER);
		
		meuTextLog = new JTextArea();
		meuTextLog.setRows(16);
		//meuTexto.setPreferredSize(new Dimension(0,250));
		meuTextLogScroll = new JScrollPane(meuTextLog);
		getContentPane().add(meuTextLogScroll,BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public void addMsg(String msg)
	{
		meuTextLog.append(msg+"\n");
		meuTextLog.setCaretPosition(meuTextLog.getText().length());
	}
}
