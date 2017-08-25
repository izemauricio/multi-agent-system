package cliente;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JanelaMapa extends JFrame {

	Mapa meuMapa;
	
	public JanelaMapa()
	{
		criarJanela();
	}
	
	private void criarJanela()
	{		
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch(Exception e) {
		}
		
		setFont(new Font("Verdana",1,10));
		setTitle("Mundo Real");
		setBounds(0,0,325,354);
		setResizable(false);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		meuMapa = new Mapa();
		meuMapa.setDoubleBuffered(true);
		getContentPane().add(meuMapa,BorderLayout.CENTER);
		
	}
	
}
