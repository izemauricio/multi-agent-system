package servidor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Mapa extends JPanel 
{
	private int mapa[][] = 
		{
			{ 4,4,4,4,4,4,4,4,4,4, 4,4,4,4,4,4,4,4,4,4 },
			{ 4,4,4,4,4,4,4,4,4,4, 4,4,4,4,4,4,4,4,4,4 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 },
			{ 0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0,0 }
	};
	
	//*
	private int idMap[][] = new int[20][20];
	
	public int getTerreno(int i,int j)
	{
		return mapa[i][j];
	}
	
	public void setTerreno(int i,int j,int terreno)
	{
		mapa[i][j] = terreno;
	}
	
	public void setPosicaoDoAgente(int id,int i,int j)
	{
		//*
		idMap[i][j]=id;
		
		mapa[i][j] = 3;
		repaint2();
	}
	
	public void repaint2()
	{
		repaint();
		revalidate();
	}
	
	public void paintComponent(Graphics g)
	{
		int i,j; // linha,coluna
		
		//*
		g.setFont(new Font("Verdana",1,10));
		
		g.setColor(Color.white);
		g.fillRect(0,0,450,690);
		
		for(i=0;i<20;i++)
		{
			for(j=0;j<20;j++)
			{
				switch(mapa[i][j])
				{
				case 0:
					g.setColor(new Color(185,122,87));
					break;
				case 1:
					g.setColor(new Color(195,195,195));
					break;
				case 2:
					g.setColor(Color.YELLOW);
					break;
				case 3:
					g.setColor(Color.RED);
					break;
				case 4:
					g.setColor(new Color(153,217,234));
					break;
				}
				
				if(mapa[i][j]!=3)
					g.fillRect(55+(j*15)+(j),(i*15)+(i),15,15);
				else {
					g.fillRect(55+(j*15)+(j),(i*15)+(i),15,15);
					g.setColor(Color.black);
					g.drawString(""+idMap[i][j],55+(j*15)+(j)+4,(i*15)+(i)+10);
				}
				
				//g.drawRect((j*10)+(j),(i*10)+(i),9,9);
			}
		}
		
		/*
		g.setColor(Color.black);
		g.drawRect(2,2,10,10);
		
		g.setColor(new Color(200,200,150));
		g.fillRect(2,2,10,10);
		*/
		
	}
}
