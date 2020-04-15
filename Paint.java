import java.util.ArrayList;

import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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

public class Paint extends JFrame implements ActionListener{ //MouseListener, MouseMotionListener{
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
    private JButton buttonRetaD;
    private JButton buttonTrans;
    private JButton buttonRetaB;
    private JButton buttonMirrorX;
    private JButton buttonMirrorY;
    private JButton buttonMirrorXY;
	private Point mousePos;

	private int x1, y1, x2,y2;
	private MouseHandler mouse;
	private Graphics g;

	protected Point mouseReleased;
	protected Point mousePressed;
	protected JColorChooser Cores;
	protected Color corE = Color.BLACK;

    private Icon pen      = new ImageIcon(getClass().getResource("img/pen.png"));
    private Icon ret      = new ImageIcon(getClass().getResource("img/retangulo.png"));
    private Icon circ     = new ImageIcon(getClass().getResource("img/circulo.png"));
    private Icon retaD    = new ImageIcon(getClass().getResource("img/DDA.png"));
    private Icon retaB    = new ImageIcon(getClass().getResource("img/bresenham.png"));
    private Icon trans    = new ImageIcon(getClass().getResource("img/translacao.png"));
    private Icon mirrorX  = new ImageIcon(getClass().getResource("img/mirror_x.png"));
    private Icon mirrorY  = new ImageIcon(getClass().getResource("img/mirror_y.png"));
    private Icon mirrorXY = new ImageIcon(getClass().getResource("img/mirror_xy.png"));


    //Tamanho do Canvas
    private int inicioL = 0;
    private int inicioA = 60;
    private int Largura = 800;
    private int Altura = 540;

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

	//variaveis da escala
	private int TEx = 20;
	private int TEy = 20;

	//variaveis do recorte
	private Ponto ReMin = new Ponto();
	private Ponto ReMax = new Ponto();

	//Ferramentas possiveis
	private enum Ferramentas {
		NORMAL, RETANGULO, DDA, RETA_BRESENHAM, CIRC_BRESENHAM, TRANSLACAO, RECORTE
	};
	private Ferramentas ferramenta_atual = Ferramentas.NORMAL;

	//main: inicializar tela e captura de eventos
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					Paint frame = new Paint();
					frame.setVisible(true);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public Paint(){

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
        buttonCor.setPreferredSize(new Dimension(30, 30));
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
        buttonRetangulo.setBackground(Color.decode("#e70065"));
        buttonRetangulo.setHorizontalTextPosition(SwingConstants.CENTER); 

		//botao circulo

		buttonCirculo = new JButton();
		buttonCirculo.addActionListener(this);
		buttonCirculo.setIcon(circ);
        buttonCirculo.setBackground(Color.decode("#e70065"));
		buttonCirculo.setHorizontalTextPosition(SwingConstants.CENTER); 

        //botao retaDDA

        buttonRetaD = new JButton();
        buttonRetaD.addActionListener(this);
        buttonRetaD.setIcon(retaD);
        buttonRetaD.setBackground(Color.decode("#e70065"));
        buttonRetaD.setHorizontalTextPosition(SwingConstants.CENTER); 

        //botao retaB
        buttonRetaB = new JButton();
        buttonRetaB.addActionListener(this);
        buttonRetaB.setIcon(retaB);
        buttonRetaB.setBackground(Color.decode("#e70065"));
        buttonRetaB.setHorizontalTextPosition(SwingConstants.CENTER); 

        //botao transform
        buttonTrans = new JButton();
        buttonTrans.addActionListener(this);
        buttonTrans.setIcon(trans);
        buttonTrans.setBackground(Color.decode("#e70065"));
        buttonTrans.setHorizontalTextPosition(SwingConstants.CENTER); 


        buttonMirrorX = new JButton();
        buttonMirrorX.addActionListener(this);
        buttonMirrorX.setIcon(mirrorX);
        buttonMirrorX.setBackground(Color.decode("#e70065"));
        buttonMirrorX.setHorizontalTextPosition(SwingConstants.CENTER); 


        buttonMirrorY = new JButton();
        buttonMirrorY.addActionListener(this);
        buttonMirrorY.setIcon(mirrorY);
        buttonMirrorY.setBackground(Color.decode("#e70065"));
        buttonMirrorY.setHorizontalTextPosition(SwingConstants.CENTER); 


        buttonMirrorXY = new JButton();
        buttonMirrorXY.addActionListener(this);
        buttonMirrorXY.setIcon(mirrorXY);
        buttonMirrorXY.setBackground(Color.decode("#e70065"));
        buttonMirrorXY.setHorizontalTextPosition(SwingConstants.CENTER); 


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
                    .addGap(10)
                    .addComponent(buttonRetaD)
                    .addGap(10)
                    .addComponent(buttonRetaB)
                    .addGap(10)
                    .addComponent(buttonTrans)
                    .addGap(10)
                    .addComponent(buttonMirrorX)
                    .addGap(10)
                    .addComponent(buttonMirrorY)
                    .addGap(10)
                    .addComponent(buttonMirrorXY)
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
                        .addComponent(buttonRetaD)
                        .addComponent(buttonRetaB)
                        .addComponent(buttonTrans)
                        .addComponent(buttonMirrorX)
                        .addComponent(buttonMirrorY)
                        .addComponent(buttonMirrorXY)
						))
				);
		panelMenu.setLayout(g1_panelMenu);

		//Painel de desenho
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(inicioL,inicioA,Largura,Altura);
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
        if(arg0.getSource() == buttonRetaD){
            do_buttonReta_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonRetaB){
            do_buttonReta_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonTrans){
            do_buttonTrans_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonMirrorX){
            do_buttonMirrorX_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonMirrorY){
            do_buttonMirrorY_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonMirrorXY){
            do_buttonMirrorXY_actionPerfomed(arg0);
        }
	}

	//mudar cor
	protected void do_buttonCor_actionPerfomed(ActionEvent arg0){
		Cores = new JColorChooser();
		corE = Cores.showDialog(null,"Escolha a cor", Color.BLACK);
		buttonCor.setBackground(corE);
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

    protected void do_buttonReta_actionPerfomed(ActionEvent arg0){
        ferramenta_atual = Ferramentas.DDA;
    }

    protected void do_buttonTrans_actionPerfomed(ActionEvent arg0){
        String xis;
        String yis;
        xis = JOptionPane.showInputDialog("Digite X:");
        yis = JOptionPane.showInputDialog("Digite Y:");
            try {
                TEx = Integer.parseInt(xis);
                TEy = Integer.parseInt(yis);
                ferramenta_atual = Ferramentas.TRANSLACAO;
                mouse.translacao();
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Digite apenas números inteiros");
            }     
    }

    protected void do_buttonMirrorX_actionPerfomed(ActionEvent arg0){
		mouse.mirror(true,false);
    }

    protected void do_buttonMirrorY_actionPerfomed(ActionEvent arg0){
		mouse.mirror(false,true);
    }

    protected void do_buttonMirrorXY_actionPerfomed(ActionEvent arg0){
		mouse.mirror(true,true);
    }


	private void setupDesenho(){
		g = panel.getGraphics();
	}


	//Classe interna para lidar com eventos de mouse
	private class MouseHandler extends MouseAdapter
	{

		public void setPixel(Ponto ponto, Color cor) {
			setupDesenho();
			g.setColor(cor);
			g.drawLine(ponto.x, ponto.y-80, ponto.x, ponto.y-80);
			g.setColor(corE);
		}

		public void apaga_dda(RetaDDA reta) {
			dda(reta.p1, reta.p2, Color.WHITE);
		}

		public void dda(Ponto p1, Ponto p2, Color cor) {
			int dx, dy, passos, k;
			double x_inc, y_inc, x, y;
			Ponto p;
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
			setPixel(p, cor);
			for(k=1; k< passos; k++) {
				x = x + x_inc;
				y = y + y_inc;
				p = new Ponto((int)Math.floor(x), (int)Math.floor(y));
				setPixel(p, cor);
			}		
			DDAx1 = DDAy1 = DDAx2 = DDAy2 = -1;
		}

		public void apaga_retangulo(Retangulo retangulo) {
			retangulo(retangulo.p1, retangulo.p2, Color.WHITE);
		}

		public void retangulo(Ponto p1, Ponto p2, Color cor) {
			Ponto pr1, pr2;
			//Reta superior
			RBx1 = p1.x;
			RBy1 = p1.y;
			RBx2 = p2.x;
			RBy2 = p1.y;
			pr1 = new Ponto(RBx1, RBy1);
			pr2 = new Ponto(RBx2, RBy2);
			reta_bresenham(pr1, pr2, cor);
			//lateral esquerda
			RBx1 = p1.x;
			RBy1 = p1.y;
			RBx2 = p1.x;
			RBy2 = p2.y;
			pr1 = new Ponto(RBx1, RBy1);
			pr2 = new Ponto(RBx2, RBy2);
			reta_bresenham(pr1, pr2, cor);
			//reta inferior
			RBx1 = p1.x;
			RBy1 = p2.y;
			RBx2 = p2.x;
			RBy2 = p2.y;
			pr1 = new Ponto(RBx1, RBy1);
			pr2 = new Ponto(RBx2, RBy2);
			reta_bresenham(pr1, pr2, cor);
			//lateral direita
			RBx1 = p2.x;
			RBy1 = p1.y;
			RBx2 = p2.x;
			RBy2 = p2.y;
			pr1 = new Ponto(RBx1, RBy1);
			pr2 = new Ponto(RBx2, RBy2);
			reta_bresenham(pr1, pr2, cor);
			Rx1 = Ry1 = Rx2 = Ry2 = -1;
		}

		public void apaga_reta_bresenham(RetaBRE reta) {
			reta_bresenham(reta.p1, reta.p2, Color.WHITE);
		}

		public void reta_bresenham(Ponto p1, Ponto p2, Color cor) {
			int x, y, dx, dy, i, incrx, incry, const1, const2, p;
			Ponto ponto;
			dx = p2.x - p1.x;
			dy = p2.y - p1.y;
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
			setPixel(ponto, cor);
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
					setPixel(ponto, cor);
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
					setPixel(ponto, cor);
				}
			}
			RBx1 = RBy1 = RBx2 = RBy2 = -1;
		}

		public void apaga_circunferencia_bresenham(Circunferencia circ) {
			circunferencia_bresenham(circ.centro, circ.raio, Color.WHITE);
		}

		public void colorirSimetricos(Ponto centro, int x, int y, Color cor) {
			Ponto[] pontos = new Ponto[8];
			pontos[0] = new Ponto(centro.x + x, centro.y + y);
			pontos[1] = new Ponto(centro.x - x, centro.y + y);
			pontos[2] = new Ponto(centro.x + x, centro.y - y);
			pontos[3] = new Ponto(centro.x - x, centro.y - y);
			pontos[4] = new Ponto(centro.x + y, centro.y + x);
			pontos[5] = new Ponto(centro.x - y, centro.y + x);
			pontos[6] = new Ponto(centro.x + y, centro.y - x);
			pontos[7] = new Ponto(centro.x - y, centro.y - x);
			for(int i = 0; i < pontos.length; i++)
				setPixel(pontos[i], cor);
		}

		public void circunferencia_bresenham(Ponto centro, int raio, Color cor) {
			int x, y, p;
			x = 0;
			y = raio;
			p = 3 - 2 * raio;
			colorirSimetricos(centro, x, y, cor);
			while(x < y) {
				if (p < 0)
					p += 4 * x + 6;
				else {
					p += 4 * (x - y) + 10;
					y--;
				}
				x++;
				colorirSimetricos(centro, x, y, cor);
			}
			CBx = CBy = -1;
		}


		public void translacao() {
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;

			// apaga imagens atuais na tela
			for(int i = 0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				apaga_dda(retaDDA);
			}
			for(int i = 0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				apaga_reta_bresenham(retaBRE);
			}
			for(int i = 0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				apaga_retangulo(r);
			}
			for(int i = 0; i < Circunferencia.lista.size(); i++) {
				circ = Circunferencia.lista.get(i);
				apaga_circunferencia_bresenham(circ);
			}

			// aplica translacao nas retas DDA
			for(int i = 0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				// atualiza p1
				retaDDA.p1.x = retaDDA.p1.x + TEx;
				retaDDA.p1.y = retaDDA.p1.y + TEy;
				// atualiza p2
				retaDDA.p2.x = retaDDA.p2.x + TEx;
				retaDDA.p2.y = retaDDA.p2.y + TEy;
				// substitui a reta pela nova na lista
				RetaDDA.lista.set(i, retaDDA);
			}
			// redesenha as retas da lista
			for(int i = 0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				dda(retaDDA.p1, retaDDA.p2, retaDDA.cor);
			}

			// aplica translacao nas retas bresenham
			for(int i = 0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				// atualiza p1
				retaBRE.p1.x = retaBRE.p1.x + TEx;
				retaBRE.p1.y = retaBRE.p1.y + TEy;
				// atualiza p2
				retaBRE.p2.x = retaBRE.p2.x + TEx;
				retaBRE.p2.y = retaBRE.p2.y + TEy;
				// substitui a reta pela nova na lista
				RetaBRE.lista.set(i, retaBRE);
			}
			// redesenha as retas da lista
			for(int i = 0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				reta_bresenham(retaBRE.p1, retaBRE.p2, retaBRE.cor);
			}

			// aplica translacao nos retangulos
			for(int i = 0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				// atualiza p1
				r.p1.x = r.p1.x + TEx;
				r.p1.y = r.p1.y + TEy;
				// atualiza p2
				r.p2.x = r.p2.x + TEx;
				r.p2.y = r.p2.y + TEy;
				// substitui o retangulo pelo novo na lista
				Retangulo.lista.set(i, r);
			}
			// redesenha os retangulos da lista
			for(int i = 0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				retangulo(r.p1, r.p2, r.cor);
			}

			// aplica translacao nas circunferencias
			for(int i = 0; i < Circunferencia.lista.size(); i++) {
				circ = Circunferencia.lista.get(i);
				// atualiza o centro
				circ.centro.x = circ.centro.x + TEx;
				circ.centro.y = circ.centro.y + TEy;
				// substitui a circunferencia pela nova na lista
				Circunferencia.lista.set(i, circ);
			}
			// redesenha as circunferencias da lista
			for(int i = 0; i < Circunferencia.lista.size(); i++) {
				circ = Circunferencia.lista.get(i);
				circunferencia_bresenham(circ.centro, circ.raio, circ.cor);
			}
		}

		// Reflexoes,
		// x -> x constante, y varia
		// y -> y constante, x varia
		// se rx for True, espelha em x
		// se ry for True, espelha em y
		// se ambos forem True, espelha em xy
		public void mirror(boolean rx, boolean ry) {
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;

			// como nao existem posicoes negativas no canvas subtraimos o x do 
			// ponto mais distante da origem para espelhar no meio do canvas
			//
			for(int i=0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				//apaga e plota a refletida
				apaga_dda(retaDDA);
				if (rx) {
					retaDDA.p1.y = 800 - retaDDA.p1.y;
					retaDDA.p2.y = 800 - retaDDA.p2.y;
				}
				if (ry) {
					retaDDA.p1.x = 600 - retaDDA.p1.x;
					retaDDA.p2.x = 600 - retaDDA.p2.x;
				}
				dda(retaDDA.p1,retaDDA.p2,Color.BLACK);
			}

			for(int i=0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				//apaga e plota a refletida
				apaga_reta_bresenham(retaBRE);
				if (rx) {
					retaBRE.p1.y = 800 - retaBRE.p1.y;
					retaBRE.p2.y = 800 - retaBRE.p2.y;
				}
				if (ry) {
					retaBRE.p1.x = 600 - retaBRE.p1.x;
					retaBRE.p2.x = 600 - retaBRE.p2.x;
				}
				reta_bresenham(retaBRE.p1,retaBRE.p2,Color.BLACK);
			}

			for(int i=0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				//apaga e plota a refletida
				apaga_retangulo(r);
				if (rx) {
					r.p1.y = 800 - r.p1.y;
					r.p2.y = 800 - r.p2.y;
				}
				if (ry) {
					r.p1.x = 600 - r.p1.x;
					r.p2.x = 600 - r.p2.x;
				}
				retangulo(r.p1,r.p2,Color.BLACK);
			}

			for(int i=0; i < Circunferencia.lista.size(); i++) {
				circ = Circunferencia.lista.get(i);
				//apaga e plota a refletida
				apaga_circunferencia_bresenham(circ);
				if (rx) {
					circ.centro.y = 800 - circ.centro.y;
				}
				if (ry) {
					circ.centro.x = 600 - circ.centro.x;
				}
				circunferencia_bresenham(circ.centro,circ.raio,Color.BLACK);
			}

		}

		public int region_code(Ponto p) {
			int codigo =0;
			if (p.x < ReMin.x) {
				++codigo;
			}
			if (p.x > ReMax.x) {
				codigo += 2;
			}
			if (p.y < ReMin.y) {
				codigo += 4;
			}
			if (p.y > ReMax.y) {
				codigo += 8;
			}
			return codigo;
		}

		public void cohenSutherland(Ponto p1,Ponto p2) {
			boolean aceite = false;
			boolean feito = false;
			int c1;
			int c2;
			int cfora;
			float xint = 0;
			float yint = 0;
			float x1 = 0,x2 = 0,y1 = 0,y2 = 0;
			while (!feito) {
				c1 = region_code(p1);
				c2 = region_code(p2);
				if (c1 == 0 && c2 == 0) {// 100% dentro
					aceite = true;
					feito = true;
				}else if (c1 != 0 && c2 != 0) {//100% fora
					feito = true;
				}else{
					if(c1 != 0) //determina um ponto fora
						cfora = c1;
					else
						cfora = c2;

					if ((cfora & 1) == 1) { // limite esquerdo
						xint = ReMin.x;
						yint = p1.y + (p2.y - p1.y) * (ReMin.x - p1.x)/(p2.x-p1.x);
					}else if ((cfora & 2) == 1){//limite direito
						xint = ReMax.x;
						yint = p1.y + (p2.y - p1.y) * (ReMax.x - p1.x)/(p2.x-p1.x);
					}else if ((cfora & 4) == 1) {//limite inferior
						yint = ReMin.y;
						xint = p1.x + (p2.x - p1.x) * (ReMin.y - p1.y)/(p2.y-p1.y);
					}else if ((cfora & 8) == 1) {//limite superior
						yint = ReMax.y;
						xint = p1.x + (p2.x - p1.x) * (ReMax.y - p1.y)/(p2.y-p1.y);
					}
					if (c1 == cfora) {
						x1 = xint;
						y1 = yint;
					}else{
						x2 = xint;
						y2 = yint;
						
					}
				}
			}
			if (aceite) {
				Ponto f1 = new Ponto(Math.round(x1),Math.round(y1));
				Ponto f2 = new Ponto(Math.round(x2),Math.round(y2));
				dda(f1,f2,Color.BLACK);
			}
		}

		public void recorte() {
			//Troca valores caso ponto minimo seja maximo em x ou em y
			if(ReMin.x > ReMax.x) {
				int aux = ReMax.x;
				ReMax.x = ReMin.x;
				ReMin.x = aux;
			}

			if(ReMin.y > ReMax.y) {
				int aux = ReMax.y;
				ReMax.y = ReMin.y;
				ReMin.y = aux;
			}

			RetaDDA retaDDA;
			RetaBRE retaBRE;
			for(int i=0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				//apaga e plota a reta recortada
				apaga_dda(retaDDA);
				cohenSutherland(retaDDA.p1,retaDDA.p2);
			}
			for(int i=0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				//apaga e plota a reta recortada
				apaga_reta_bresenham(retaBRE);
				cohenSutherland(retaBRE.p1,retaBRE.p2);
			}
		}


		public void mousePressed( MouseEvent e )
		{
			x1 = e.getX();
			y1 = e.getY();

			if (ferramenta_atual == Ferramentas.NORMAL) {
				Ponto p = new Ponto(x1, y1);
				setPixel(p, corE);
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
					Ponto Rp1 = new Ponto(Rx1, Ry1);
					Ponto Rp2 = new Ponto(Rx2, Ry2);
					Retangulo r = new Retangulo(Rp1, Rp2, corE);
					retangulo(Rp1, Rp2, corE);
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
					Ponto RBp1 = new Ponto(RBx1, RBy1);
					Ponto RBp2 = new Ponto(RBx2, RBy2);
					RetaBRE retaBRE = new RetaBRE(RBp1, RBp2, corE);
					reta_bresenham(RBp1, RBp2, corE);
				}
			} else if(ferramenta_atual == Ferramentas.CIRC_BRESENHAM) {
				CBx = x1;
				CBy = y1;
				//Desenha a circunferencia
				Ponto CBp = new Ponto(CBx, CBy);
				Circunferencia circ = new Circunferencia(CBp, CBraio, corE);
				circunferencia_bresenham(CBp, CBraio, corE);
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
					Ponto DDAp1 = new Ponto(DDAx1, DDAy1);
					Ponto DDAp2 = new Ponto(DDAx2, DDAy2);
					RetaDDA retaDDA = new RetaDDA(DDAp1, DDAp2, corE);
					dda(DDAp1, DDAp2, corE);
				}
			} else if(ferramenta_atual == Ferramentas.TRANSLACAO) {
			//	translacao();
			} else if (ferramenta_atual == Ferramentas.RECORTE) {
				if (ReMin.x == -1) {
					ReMin.x = x1;
					ReMin.y = y1;
				}else if(ReMax.x == -1) {
					ReMax.x = x1;
					ReMax.y = y1;
					recorte();
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

				Ponto p1 = new Ponto(RBx1, RBy1);
				Ponto p2 = new Ponto(RBx2, RBy2);
				reta_bresenham(p1, p2, corE);

				x2=x1;
				y2=y1;
			}
		}
	}
}
