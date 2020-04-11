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

	//variaveis das coordenadas da reta bresenham
	private int RBx1 = -1;
	private int RBy1 = -1;
	private int RBx2 = -1;
	private int RBy2 = -1;

	//variaveis da circunferencia de bresenham
	private int CBx = -1;
	private int CBy = -1;
	private int CBraio = 50;

	private enum Ferramentas {
		NORMAL, RETANGULO, DDA, RETA_BRESENHAM, CIRC_BRESENHAM
	};

	private Ferramentas ferramenta_atual = Ferramentas.CIRC_BRESENHAM;

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

		public void dda(int x1, int y1, int x2, int y2) 
		{
			int dx, dy, passos, k;
			double x_inc, y_inc, x,y;
			dx = x2-x1;
			dy = y2-y1;
			if (abs(dx) > abs(dy))
				passos = abs(dx);
			else
				passos = abs(dy);

			x_inc = double(dx)/double(passos);
			y_inc = double(dy)/double(passos);

			x=x1;
			y=y1;
			drawLine(round(x),round(y),round(x),round(y));

			for(k=1; k< passos; k++) {
				x = x+x_inc;
				y = y+y_inc;
				drawLine(round(x),round(y),round(x),round(y));
			}
		}

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

		public void reta_bresenham() {
			int x, y, dx, dy, i, incrx, incry, const1, const2, p;
			dx = RBx2 - RBx1;
			dy = RBy2 - RBy1;
			if (dx >= 0)
				incrx = 1;
			else {
				incrx = -1;
				dx = -dx;
			}
			if (dy >= 0)
				incry = 1;
			else {
				incry = -1;
				dy= -dy;
			}
			x = RBx1;
			y = RBy1;
			ponto(x, y);
			if (dy < dx) {
				p = 2 * dy - dx;
				const1 = 2 * dy;
				const2 = 2 * (dy - dx);
				for(i = 0; i < dx; i++) {
					x += incrx;
					if (p < 0)
						p += const1;
					else {
						y += incry;
						p += const2;
					}
					ponto(x, y);
				}
			}
			else {
				p = 2 * dx - dy;
				const1 = 2 * dx;
				const2 = 2 * (dx - dy);
				for(i = 0; i < dy; i++) {
					y += incry;
					if (p < 0)
						p += const1;
					else { x += incrx;
						p += const2;
					}
					ponto(x, y);
				}
			}
			RBx1 = RBy1 = RBx2 = RBy2 = -1;
		}

		public void colorirSimetricos(int x, int y) {
			ponto(CBx + x, CBy + y);
			ponto(CBx - x, CBy + y);
			ponto(CBx + x, CBy - y);
			ponto(CBx - x, CBy - y);
			ponto(CBx + y, CBy + x);
			ponto(CBx - y, CBy + x);
			ponto(CBx + y, CBy - x);
			ponto(CBx - y, CBy - x);
		}

		public void circunferencia_bresenham() {
			int x, y, p;
			x = 0;
			y = CBraio;
			p = 3 - 2 * CBraio;
			colorirSimetricos(x, y);
			while(x < y) {
				if (p < 0)
					p += 4 * x + 6;
				else {
					p += 4 * (x - y) + 10;
					y--;
				}
				x++;
				colorirSimetricos(x, y);
			}
			CBx = CBy = -1;
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
			} else if(ferramenta_atual == Ferramentas.RETA_BRESENHAM) {
				//captura o primeiro ponto se as primeiras variaveis da
				//reta forem -1
				if (RBx1 == -1) {
					RBx1 = x1;
					RBy1 = y1;
				//Captura o segundo ponto se as primeiras variaveis da 
				//reta forem != -1 e as segundas forem -1
				} else if(RBx2 == -1) {
					RBx2 = x1;
					RBy2 = y1;
					//Desenha a reta bresenham
					reta_bresenham();
				}
			} else if(ferramenta_atual == Ferramentas.CIRC_BRESENHAM) {
				CBx = x1;
				CBy = y1;
				circunferencia_bresenham();
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
