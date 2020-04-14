import java.util.ArrayList;
//
import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel.*;
import javax.swing.JButton;
import javax.swing.border.*;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class Tela extends JFrame implements ActionListener{ //MouseListener, MouseMotionListener{
    private JPanel contentPane;
    private JPanel panelMenu;
    private JPanel panelStatus;
    private JPanel panel;
    private JLabel labelPosX;
    private JLabel labelPosY;
    private JButton buttonCor;
    private JButton buttonPonto;
    private JButton buttonRetangulo;
    private JButton buttonCirculo;
    private Point mousePos;


    private int x1, y1, x2,y2;
	private MouseHandler mouse;
    private Graphics g;

    protected Point mouseReleased;
    protected Point mousePressed;
    protected JColorChooser Cores;
    protected Color corE = Color.BLACK;

    private Icon pen  = new ImageIcon(getClass().getResource("img/pen.png"));
    private Icon ret  = new ImageIcon(getClass().getResource("img/ret.png"));
    private Icon circ = new ImageIcon(getClass().getResource("img/circ.png"));

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

    //Ferramentas possiveis
	private enum Ferramentas {
		NORMAL, RETANGULO, DDA, RETA_BRESENHAM, CIRC_BRESENHAM
	};
	private Ferramentas ferramenta_atual = Ferramentas.NORMAL;

    //main: inicializar tela e captura de eventos
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                try{
                    Tela frame = new Tela();
                    frame.setVisible(true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public Tela(){

        //Inicializando Ambiente

        setTitle("Paint Calafrio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,800,600);
        contentPane = new JPanel();
       // contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);



        //Painel com botoes
        panelMenu = new JPanel();
        panelMenu.setBounds(0,0,800,60);
        contentPane.add(panelMenu);

        //botao selecionar cor
        buttonCor = new JButton();
        buttonCor.addActionListener(this);
        buttonCor.setBackground(Color.BLACK);
        buttonCor.setHorizontalTextPosition(SwingConstants.CENTER); 
        
        //botao caneta

        buttonPonto = new JButton();
        buttonPonto.addActionListener(this);
        buttonPonto.setIcon(pen);
        buttonPonto.setHorizontalTextPosition(SwingConstants.CENTER); 
        buttonPonto.setBackground(Color.WHITE);

        //botao retangulo

        buttonRetangulo = new JButton();
        buttonRetangulo.addActionListener(this);
        buttonRetangulo.setIcon(ret);
        buttonRetangulo.setBackground(Color.WHITE);
        buttonRetangulo.setHorizontalTextPosition(SwingConstants.CENTER); 

        //botao circulo

        buttonCirculo = new JButton();
        buttonCirculo.addActionListener(this);
        buttonCirculo.setIcon(circ);
        buttonCirculo.setBackground(Color.WHITE);
        buttonCirculo.setHorizontalTextPosition(SwingConstants.CENTER); 


        //configurar grupo de botoes
        GroupLayout g1_panelMenu = new GroupLayout(panelMenu);
        g1_panelMenu.setHorizontalGroup(
            g1_panelMenu.createParallelGroup(Alignment.CENTER)

            .addGroup( g1_panelMenu.createSequentialGroup()
                .addComponent(buttonCor)
                .addGap(10)
                .addComponent(buttonPonto)
                .addGap(10)
                .addComponent(buttonRetangulo)
                .addGap(10)
                .addComponent(buttonCirculo)
            )
        );

        g1_panelMenu.setVerticalGroup(
            g1_panelMenu.createParallelGroup(Alignment.CENTER)
            .addGroup(g1_panelMenu.createSequentialGroup()
            .addGap(10)
            .addGroup(g1_panelMenu.createParallelGroup(Alignment.BASELINE)
            .addComponent(buttonCor)
            .addComponent(buttonPonto)
            .addComponent(buttonRetangulo)
            .addComponent(buttonCirculo)
            ))
         );
        panelMenu.setLayout(g1_panelMenu);

        //Painel de desenho
        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(0,60,800,540);
        contentPane.add(panel);
        panel.setLayout(null);

        mouse  = new MouseHandler();

		this.addMouseListener( mouse );
		this.addMouseMotionListener( mouse );

    }

    //capturar click em botoes
    public void actionPerformed(ActionEvent arg0){
        if(arg0.getSource() == buttonCor){
            do_buttonCor_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonPonto){
            do_buttonPonto_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonRetangulo){
            do_buttonRetangulo_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonCirculo){
            do_buttonCirculo_actionPerfomed(arg0);
        }
    }

    //mudar cor
    protected void do_buttonCor_actionPerfomed(ActionEvent arg0){
        Cores = new JColorChooser();
        corE = Cores.showDialog(null,"Escolha a cor", Color.BLACK);
        buttonCor.setBackground(corE);
 	    g.setColor(corE);
    }

	private void setupDesenho(){
		g = getGraphics();
	    g.setColor(corE);
    }

//set ferramenta atual de acordo com o botao clicado
    protected void do_buttonPonto_actionPerfomed(ActionEvent arg0){
        ferramenta_atual = Ferramentas.NORMAL;
    }
    
    protected void do_buttonRetangulo_actionPerfomed(ActionEvent arg0){
        ferramenta_atual = Ferramentas.RETANGULO;
    }

    protected void do_buttonCirculo_actionPerfomed(ActionEvent arg0){
        ferramenta_atual = Ferramentas.CIRC_BRESENHAM;
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
