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

	//variaveis das coordenadas do DDA
	private int DDAx1 = -1;
	private int DDAy1 = -1;
	private int DDAx2 = -1;
	private int DDAy2 = -1;

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

	private Ferramentas ferramenta_atual = Ferramentas.NORMAL;

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
		public void dda() 
		{
			int dx, dy, passos, k;
			double x_inc, y_inc, x, y;
			Ponto p;
			Ponto p1 = new Ponto(DDAx1, DDAy1);
			Ponto p2 = new Ponto(DDAx2, DDAy2);
			dx = p2.x - p1.x;
			dy = p2.y - p1.y;
			if (Math.abs(dx) > Math.abs(dy))
				passos = Math.abs(dx);
			else
				passos = Math.abs(dy);

			x_inc = (double)dx / (double)passos;
			y_inc = (double)dy / (double)passos;

			x = p1.x;
			y = p1.y;

			p = new Ponto((int)Math.floor(x), (int)Math.floor(y));
			setPixel(p);

			for(k=1; k< passos; k++) {
				x = x+x_inc;
				y = y+y_inc;
				p = new Ponto((int)Math.floor(x), (int)Math.floor(y));
				setPixel(p);
			}

			DDAx1 = DDAy1 = DDAx2 = DDAy2 = -1;
		}

		public void setPixel(Ponto ponto) {
			setupDesenho();
			g.drawLine(ponto.x, ponto.y, ponto.x, ponto.y);
		}

		public void retangulo() {
			setupDesenho();
			//Reta superior
			RBx1 = Rx1;
			RBy1 = Ry1;
			RBx2 = Rx2;
			RBy2 = Ry1;
			reta_bresenham();
			//lateral esquerda
			RBx1 = Rx1;
			RBy1 = Ry1;
			RBx2 = Rx1;
			RBy2 = Ry2;
			reta_bresenham();
			//reta inferior
			RBx1 = Rx1;
			RBy1 = Ry2;
			RBx2 = Rx2;
			RBy2 = Ry2;
			reta_bresenham();
			//lateral direita
			RBx1 = Rx2;
			RBy1 = Ry1;
			RBx2 = Rx2;
			RBy2 = Ry2;
			reta_bresenham();
			Rx1 = Ry1 = Rx2 = Ry2 = -1;
		}

		public void reta_bresenham() {
			int x, y, dx, dy, i, incrx, incry, const1, const2, p;
			Ponto ponto;
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
			ponto = new Ponto(x, y);
			setPixel(ponto);
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
					ponto = new Ponto(x, y);
					setPixel(ponto);
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
					ponto = new Ponto(x, y);
					setPixel(ponto);
				}
			}
			RBx1 = RBy1 = RBx2 = RBy2 = -1;
		}

		public void colorirSimetricos(int x, int y) {
			Ponto[] pontos = new Ponto[8];
			pontos[0] = new Ponto(CBx + x, CBy + y);
			pontos[1] = new Ponto(CBx - x, CBy + y);
			pontos[2] = new Ponto(CBx + x, CBy - y);
			pontos[3] = new Ponto(CBx - x, CBy - y);
			pontos[4] = new Ponto(CBx + y, CBy + x);
			pontos[5] = new Ponto(CBx - y, CBy + x);
			pontos[6] = new Ponto(CBx + y, CBy - x);
			pontos[7] = new Ponto(CBx - y, CBy - x);
			for(int i = 0; i < pontos.length; i++)
				setPixel(pontos[i]);
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
				Ponto p = new Ponto(x1, y1);
				setPixel(p);
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
			} else if(ferramenta_atual == Ferramentas.DDA) {
				//captura o primeiro ponto se as primeiras variaveis da
				//reta forem -1
				if (DDAx1 == -1) {
					DDAx1 = x1;
					DDAy1 = y1;
				//Captura o segundo ponto se as primeiras variaveis da 
				//reta forem != -1 e as segundas forem -1
				} else if(DDAx2 == -1) {
					DDAx2 = x1;
					DDAy2 = y1;
					dda();
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

				RBx1 = x1;
				RBy1 = y1;
				RBx2 = x2;
				RBy2 = y2;

				reta_bresenham();

				x2=x1;
				y2=y1;
			}
		}
	}
}