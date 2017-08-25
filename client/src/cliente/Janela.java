package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Janela extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public static int DESCONECTADO = 0;
	public static int CONECTADO = 1;
	
	private JanelaMapa minhaJanelaMapa;
	private JMenuBar meuMenu;
	private JMenu meuMenuArquivo;
	private JMenuItem meuItemConectarDesconectar;
	private JMenuItem meuItemMapa;
	private JTextArea meuTexto;
	private JScrollPane meuTextoScroll;
	private JTextField meuCampoInfo;
	
	private Conexao minhaConexao;
		
	public Janela()
	{
		CriarJanela();
		setVisualStatus(Janela.DESCONECTADO);
	}
	
	private void CriarJanela()
	{		
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch(Exception e) {
		}
		
		setFont(new Font("Verdana",1,10));
		setTitle("AgenteMinerador");
		setBounds(0,0,400,400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		meuMenu = new JMenuBar();
		setJMenuBar(meuMenu);
		
		meuMenuArquivo = new JMenu();
		meuMenuArquivo.setText("Opções");
		meuMenu.add(meuMenuArquivo);
			
		meuItemConectarDesconectar = new JMenuItem();
		meuItemConectarDesconectar.setAccelerator(KeyStroke.getKeyStroke("F2"));
		meuItemConectarDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	ConectarDesconectar();
            }
        });
		meuMenuArquivo.add(meuItemConectarDesconectar);
		
		meuItemMapa = new JMenuItem();
		meuItemMapa.setText("Mundo Real");
		meuItemMapa.setAccelerator(KeyStroke.getKeyStroke("F3"));
		meuItemMapa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	abrirMapa();
            }
        });
		meuMenuArquivo.add(meuItemMapa);
		
		meuTexto = new JTextArea();
		meuTextoScroll = new JScrollPane(meuTexto);
		meuTexto.setRows(10);
		meuTexto.setEditable(false);
		meuTexto.setFont(new Font("Verdana",0,12));
		getContentPane().add(meuTextoScroll,BorderLayout.CENTER);
		
		meuCampoInfo = new JTextField();
		meuCampoInfo.setText("");
		getContentPane().add(meuCampoInfo,BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	private void ConectarDesconectar()
	{
		if(meuItemConectarDesconectar.getText().equals("Conectar"))
		{
			String ip = JOptionPane.showInputDialog("Digite o ip");
			if(ip == null)
				return;
			minhaConexao = new Conexao(this,ip,21);
			new Thread(minhaConexao).start();	
		} else {
			minhaConexao.desconectar();
		}
	}

	public void addTexto(String texto)
	{
		meuTexto.append(texto + "\n");
		meuTexto.setCaretPosition(meuTexto.getText().length());
	}
	
	public void setVisualStatus(int status)
	{
		switch(status)
		{
		case 0: // desconectado
			addTexto("Desconectado");
			meuItemConectarDesconectar.setText("Conectar");
			break;
			
		case 1: // conectado
			addTexto("Conectado");
			meuItemConectarDesconectar.setText("Desconectar");
			break;
		}
	}
	
	public void abrirMapa()
	{
		if( minhaJanelaMapa==null )
		{
			minhaJanelaMapa = new JanelaMapa();
			minhaJanelaMapa.setVisible(true);
		} else {
			minhaJanelaMapa.setVisible(true);
		}
	}
	
	public void setInfo(String msg)
	{
		meuCampoInfo.setText(msg);
	}
}
