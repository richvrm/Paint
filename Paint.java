import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Paint extends JPanel
{
	private final int LARGURA  = 800;
	private final int ALTURA = 800;
	private final Color FUNDO   = Color.WHITE;

	private int x1, y1, x2, y2;

	private MouseHandler mouse;
	private Graphics g;

	//variaveis das coordenadas do retangulo
	private int Rx1 = -1;
	private int Ry1 = -1;
	private int Rx2 = -1;
	private int Ry2 = -1;

	private enum Ferramentas {
		NORMAL, RETANGULO, DDA, BRESENHAM
	};

	private Ferramentas ferramenta_atual = Ferramentas.RETANGULO;

	public Paint()
	{
		setBackground( FUNDO );
		setPreferredSize( new Dimension( LARGURA, ALTURA ) );

		mouse  = new MouseHandler();

		this.addMouseListener( mouse );
		this.addMouseMotionListener( mouse );
	}

	private void setupDesenho()
	{
		g = getGraphics();
	}

	//Classe interna para lidar com eventos de mouse
	private class MouseHandler extends MouseAdapter
	{

		public void ponto(int x,int y)
		{
			setupDesenho();
			g.drawLine(x,y,x,y);
		}

		public void retangulo() {
			setupDesenho();
			//Reta superior
			g.drawLine(Rx1,Ry1,Rx2,Ry1);
			//lateral esquerda
			g.drawLine(Rx1,Ry1,Rx1,Ry2);
			//reta inferior
			g.drawLine(Rx1,Ry2,Rx2,Ry2);
			//lateral direita
			g.drawLine(Rx2,Ry1,Rx2,Ry2);
			Rx1 = Ry1 = Rx2 = Ry2 = -1;
		}

		public void mousePressed( MouseEvent e )
		{
			x1 = e.getX();
			y1 = e.getY();
			if (ferramenta_atual == Ferramentas.NORMAL) {
				ponto(x1,y1);
			} else if(ferramenta_atual == Ferramentas.RETANGULO) {
				//captura o primeiro ponto se as primeiras variaveis do
				//retangulo forem -1
				if (Rx1 == -1) {
					Rx1 = x1;
					Ry1 = y1;
				//Captura o segundo ponto se as primeiras variaveis do 
				//retangulo forem != -1 e as segundas forem -1
				}else if(Rx2 == -1) {
					Rx2 = x1;
					Ry2 = y1;
					//Desenha o retangulo 
					retangulo();
				}
			}
			x2=x1;
			y2=y1;
		}

		public void mouseDragged( MouseEvent e )
		{
			if(ferramenta_atual == Ferramentas.NORMAL) {
				x1 = e.getX();
				y1 = e.getY();

				g.drawLine(x1,y1,x2,y2);

				x2=x1;
				y2=y1;
			}
		}
	}
}
