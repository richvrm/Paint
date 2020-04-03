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
		public void mousePressed( MouseEvent e )
		{
			x1 = e.getX();
			y1 = e.getY();

			setupDesenho();

			x2=x1;
			y2=y1;
		}

		public void mouseDragged( MouseEvent e )
		{
			x1 = e.getX();
			y1 = e.getY();

			g.drawLine(x1,y1,x2,y2);

			x2=x1;
			y2=y1;
		}
	}
}
