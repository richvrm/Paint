import java.util.ArrayList;
import java.lang.Math;

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

	private JPanel contentPane, panelMenu, panelStatus, panel;
	private JLabel labelPosX, labelPosY;
	private JButton buttonCor, buttonPonto, buttonRetangulo, buttonCirculo, buttonRetaD, buttonTrans, buttonRetaB,
                    buttonMirrorX, buttonMirrorY, buttonMirrorXY, buttonRota, buttonClear, buttonCS, buttonLB;
	private int x1,y1,x2,y2;
	private MouseHandler mouse;
	private Graphics g;
	private Point mouseReleased, mousePressed,mousePos;
	private JColorChooser Cores;
	private Color corE = Color.BLACK;

    private Icon pen      = new ImageIcon(getClass().getResource("img/pen.png"));
    private Icon ret      = new ImageIcon(getClass().getResource("img/retangulo.png"));
    private Icon circ     = new ImageIcon(getClass().getResource("img/circulo.png"));
    private Icon retaD    = new ImageIcon(getClass().getResource("img/DDA.png"));
    private Icon retaB    = new ImageIcon(getClass().getResource("img/bresenham.png"));
    private Icon trans    = new ImageIcon(getClass().getResource("img/translacao.png"));
    private Icon mirrorX  = new ImageIcon(getClass().getResource("img/mirror_x.png"));
    private Icon mirrorY  = new ImageIcon(getClass().getResource("img/mirror_y.png"));
    private Icon mirrorXY = new ImageIcon(getClass().getResource("img/mirror_xy.png"));
    private Icon rota     = new ImageIcon(getClass().getResource("img/rotacao.png"));
    private Icon clear    = new ImageIcon(getClass().getResource("img/apaga_tudo.png"));
    private Icon cs    = new ImageIcon(getClass().getResource("img/cohen_sutherland.png"));
    private Icon lb    = new ImageIcon(getClass().getResource("img/liang_barsky.png"));

    //Tamanho do Canvas
    private int inicioL = 0;
    private int inicioA = 120;
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
	private int DAx = -1;
	private int DAy = -1;
	private int DBx = -1;
	private int DBy = -1;

	//variaveis da escala
	private int TEx;
	private int TEy;

	//variaveis do recorte
	private Ponto ReMin = new Ponto();
	private Ponto ReMax = new Ponto();

	//variaveis da rotacao
	private int Grau = 20;

	//Ferramentas possiveis
	private enum Ferramentas {
		NORMAL, RETANGULO, DDA, RETA_BRESENHAM, CIRC_BRESENHAM, TRANSLACAO, RECORTE, ROTACAO, RECORTELB
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
		setTitle("Paint Brush");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,800,600);
		contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		//Painel com botoes
		panelMenu = new JPanel();
		panelMenu.setBounds(0,0,800,120);
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

        buttonRota = new JButton();
        buttonRota.addActionListener(this);
        buttonRota.setIcon(rota);
        buttonRota.setBackground(Color.decode("#e70065"));
        buttonRota.setHorizontalTextPosition(SwingConstants.CENTER);

        buttonClear = new JButton();
        buttonClear.addActionListener(this);
        buttonClear.setIcon(clear);
        buttonClear.setBackground(Color.decode("#e70065"));
        buttonClear.setHorizontalTextPosition(SwingConstants.CENTER);  

        buttonCS = new JButton();
        buttonCS.addActionListener(this);
        buttonCS.setIcon(cs);
        buttonCS.setBackground(Color.decode("#e70065"));
        buttonCS.setHorizontalTextPosition(SwingConstants.CENTER);

        buttonLB = new JButton();
        buttonLB.addActionListener(this);
        buttonLB.setIcon(lb);
        buttonLB.setBackground(Color.decode("#e70065"));
        buttonLB.setHorizontalTextPosition(SwingConstants.CENTER);


		//configurar grupo de botoes
		GroupLayout g1_panelMenu = new GroupLayout(panelMenu);
		g1_panelMenu.setHorizontalGroup(
				g1_panelMenu.createParallelGroup(Alignment.CENTER)
				.addGroup( g1_panelMenu.createSequentialGroup()
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
					.addComponent(buttonCor)

					)
                .addGroup( g1_panelMenu.createSequentialGroup()
                    .addGap(30)
                    .addComponent(buttonMirrorX)
                    .addGap(10)
                    .addComponent(buttonMirrorY)
                    .addGap(10)
                    .addComponent(buttonMirrorXY)
                    .addGap(10)
                    .addComponent(buttonRota)
                    .addGap(10)
                    .addComponent(buttonCS)
                    .addGap(10)
                    .addComponent(buttonLB)
                    .addGap(10)
                    .addComponent(buttonClear)
                )
			);

		g1_panelMenu.setVerticalGroup(
				g1_panelMenu.createParallelGroup(Alignment.CENTER)
				.addGroup(g1_panelMenu.createSequentialGroup()
					.addGap(10)
					.addGroup(g1_panelMenu.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonPonto)
						.addComponent(buttonRetangulo)
						.addComponent(buttonCirculo)
                        .addComponent(buttonRetaD)
                        .addComponent(buttonRetaB)
                        .addComponent(buttonTrans)
						.addComponent(buttonCor)

						)
                    .addGap(10)
					.addGroup(g1_panelMenu.createParallelGroup(Alignment.BASELINE)
                        .addComponent(buttonMirrorX)
                        .addComponent(buttonMirrorY)
                        .addComponent(buttonMirrorXY)
                        .addComponent(buttonRota)
                        .addComponent(buttonCS)
                        .addComponent(buttonLB)
                        .addComponent(buttonClear)
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
        if(arg0.getSource() == buttonRota){
            do_buttonRota_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonClear){
            do_buttonClear_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonCS){
            do_buttonCS_actionPerfomed(arg0);
        }
        if(arg0.getSource() == buttonLB){
            do_buttonLB_actionPerfomed(arg0);
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
            if ((xis != null) && (xis.length() > 0)) {    
                yis = JOptionPane.showInputDialog("Digite Y:");
                if((yis != null) ){
                    try {
                        TEx = Integer.parseInt(xis);
                        TEy = Integer.parseInt(yis);
                        ferramenta_atual = Ferramentas.TRANSLACAO;
                        mouse.translacao();
                    }catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null, "Digite apenas números inteiros");
                    }   
                }            
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

    protected void do_buttonRota_actionPerfomed(ActionEvent arg0){
		String grau;
        grau = JOptionPane.showInputDialog("Digite o grau:");
        if ((grau != null) && (grau.length() > 0)) {    
            try {
                Grau = Integer.parseInt(grau);
                ferramenta_atual = Ferramentas.ROTACAO;
                mouse.rotation();
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Digite apenas números inteiros");
            }  
        }
    }

    protected void do_buttonClear_actionPerfomed(ActionEvent arg0){
		panel.repaint();
        mouse.apagartudo();
    }

    protected void do_buttonCS_actionPerfomed(ActionEvent arg0){
        ferramenta_atual = Ferramentas.RECORTE;
    }

    protected void do_buttonLB_actionPerfomed(ActionEvent arg0){
        ferramenta_atual = Ferramentas.RECORTELB;
    }

	private void setupDesenho(){
		g = panel.getGraphics();
	}


	//Classe interna para lidar com eventos de mouse
	private class MouseHandler extends MouseAdapter
	{
        public void apagartudo(){
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;
            while( RetaDDA.lista.size() > 0){
         	    RetaDDA.lista.remove(0);           
            }
            while( RetaBRE.lista.size() > 0){
         	    RetaBRE.lista.remove(0);           
            }
            while( Retangulo.lista.size() > 0){
         	    Retangulo.lista.remove(0);           
            }
            while( Circunferencia.lista.size() > 0){
         	    Circunferencia.lista.remove(0);           
            }
        }

		public void setPixel(Ponto ponto, Color cor) {
			setupDesenho();
			g.setColor(cor);
			g.drawLine(ponto.x, ponto.y-140, ponto.x, ponto.y-140);
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
			circunferencia_bresenham(circ, Color.WHITE);
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

		public void circunferencia_bresenham(Circunferencia circ, Color cor) {
			int x, y, p;
			x = 0;
			y = circ.raio;
			p = 3 - 2 * circ.raio;
			colorirSimetricos(circ.centro, x, y, cor);
			while(x < y) {
				if (p < 0)
					p += 4 * x + 6;
				else {
					p += 4 * (x - y) + 10;
					y--;
				}
				x++;
				colorirSimetricos(circ.centro, x, y, cor);
			}
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
				circunferencia_bresenham(circ, circ.cor);
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

			//Levando em consideracao a barra superior e inferior mais os botoes
			int altura = Altura + (inicioA+80);

			// como nao existem posicoes negativas no canvas subtraimos o x do 
			// ponto mais distante da origem para espelhar no meio do canvas
			//
			for(int i=0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				//apaga e plota a refletida
				apaga_dda(retaDDA);
				if (rx) {
					retaDDA.p1.y = altura - retaDDA.p1.y;
					retaDDA.p2.y = altura - retaDDA.p2.y;
				}
				if (ry) {
					retaDDA.p1.x = Largura - retaDDA.p1.x;
					retaDDA.p2.x = Largura - retaDDA.p2.x;
				}
				dda(retaDDA.p1,retaDDA.p2,Color.BLACK);
			}

			for(int i=0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				//apaga e plota a refletida
				apaga_reta_bresenham(retaBRE);
				if (rx) {
					retaBRE.p1.y = altura - retaBRE.p1.y;
					retaBRE.p2.y = altura - retaBRE.p2.y;
				}
				if (ry) {
					retaBRE.p1.x = Largura - retaBRE.p1.x;
					retaBRE.p2.x = Largura - retaBRE.p2.x;
				}
				reta_bresenham(retaBRE.p1,retaBRE.p2,Color.BLACK);
			}

			for(int i=0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				//apaga e plota a refletida
				apaga_retangulo(r);
				if (rx) {
					r.p1.y = altura - r.p1.y;
					r.p2.y = altura - r.p2.y;
				}
				if (ry) {
					r.p1.x = Largura - r.p1.x;
					r.p2.x = Largura - r.p2.x;
				}
				retangulo(r.p1,r.p2,Color.BLACK);
			}

			for(int i=0; i < Circunferencia.lista.size(); i++) {
				circ = Circunferencia.lista.get(i);
				//apaga e plota a refletida
				apaga_circunferencia_bresenham(circ);
				if (rx) {
					circ.centro.y = altura - circ.centro.y;
				}
				if (ry) {
					circ.centro.x = Largura - circ.centro.x;
				}
				circunferencia_bresenham(circ,Color.BLACK);
			}

		}

		//método para rotacionar objetos
		public void rotation() {
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;

			//apaga e plota a reta rotacionada
			for(int i=0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				apaga_dda(retaDDA);

				int x0 = retaDDA.p2.x;
				int y0 = retaDDA.p2.y;
				int xr = retaDDA.p1.x;
				int yr = retaDDA.p1.y;

				double x = (x0 - xr) * Math.cos(Grau+180) - (y0 - yr) * Math.sin(Grau+180) + xr;
				double y = (y0 - yr) * Math.cos(Grau+180) + (x0 - xr) * Math.sin(Grau+180) + yr;

				retaDDA.p2.x = (int)x;
				retaDDA.p2.y = (int)y;

				dda(retaDDA.p1,retaDDA.p2,Color.BLACK);
			}

			//apaga e plota a reta rotacionada
			for(int i=0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				apaga_reta_bresenham(retaBRE);
				
				int x0 = retaBRE.p2.x;
				int y0 = retaBRE.p2.y;
				int xr = retaBRE.p1.x;
				int yr = retaBRE.p1.y;

				double x = (x0 - xr) * Math.cos(Grau+180) - (y0 - yr) * Math.sin(Grau+180) + xr;
				double y = (y0 - yr) * Math.cos(Grau+180) + (x0 - xr) * Math.sin(Grau+180) + yr;

				retaBRE.p2.x = (int)x;
				retaBRE.p2.y = (int)y;

				reta_bresenham(retaBRE.p1,retaBRE.p2,Color.BLACK);
			}

			//apaga e plota o retangulo rotacionado
			for(int i=0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				apaga_retangulo(r);

				int x0 = r.p2.x;
				int y0 = r.p2.y;
				int xr = r.p1.x;
				int yr = r.p1.y;

				double x = (x0 - xr) * Math.cos(Grau+180) - (y0 - yr) * Math.sin(Grau+180) + xr;
				double y = (y0 - yr) * Math.cos(Grau+180) + (x0 - xr) * Math.sin(Grau+180) + yr;

				r.p2.x = (int)x;
				r.p2.y = (int)y;

				retangulo(r.p1,r.p2,Color.BLACK);
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
			float x1 = p1.x,
				  x2 = p2.x,
				  y1 = p1.y,
				  y2 = p2.y;
			Ponto f1 = new Ponto(Math.round(x1),Math.round(y1));
			Ponto f2 = new Ponto(Math.round(x2),Math.round(y2));
			while (!feito) {
				c1 = region_code(f1);
				c2 = region_code(f2);
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
						yint = y1 + (y2 - y1) * (ReMin.x - x1)/(x2-x1);
					}else if ((cfora>>>1 & 1) == 1){//limite direito
						xint = ReMax.x;
						yint = y1 + (y2 - y1) * (ReMax.x - x1)/(x2-x1);
					}else if ((cfora>>>2 & 1) == 1) {//limite inferior
						yint = ReMin.y;
						xint = x1 + (x2 - x1) * (ReMin.y - y1)/(y2-y1);
					}else if ((cfora>>>3 & 1) == 1) {//limite superior
						yint = ReMax.y;
						xint = x1 + (x2 - x1) * (ReMax.y - y1)/(y2-y1);
					}
					if (c1 == cfora) {
						x1 = xint;
						y1 = yint;
					}else{
						x2 = xint;
						y2 = yint;
					}
				}
				f1 = new Ponto(Math.round(x1),Math.round(y1));
				f2 = new Ponto(Math.round(x2),Math.round(y2));
			}
			if (aceite) {
				dda(f1,f2,corE);
			}
		}

		public boolean cliptest(float p, float q, float u1, float u2){
			boolean result = true;
			float r;
			if (p < 0){ //fora pra dentro
				r = q/p;
				if (r > u2){
					result = false;
				}
				else if (r > u1){
					u1 = r;
				}
			}
			else if (p > 0){ //dentro pra fora
				r = q/p;
				if (r < u1){
					result= false;
				}
				else if (r < u2){
					u2 = r;
				}
			}
			else if (q < 0){
				result = false;
			}
			return result;
	}

	//ReMin.x, ReMax.x, ReMin.y, ReMax.y -> limites da janela
	public void liangBarsky(Ponto p1, Ponto p2) {
		float x1 = p1.x;
		float x2 = p2.x;
		float y1 = p1.y;
		float y2 = p2.y;
		float u1 = 0;
		float u2 = 1;
		float dx = x2-x1;
		float dy = y2-y1;
		Ponto f1 = new Ponto(Math.round(x1),Math.round(y1));
		Ponto f2 = new Ponto(Math.round(x2),Math.round(y2));
		if (cliptest(-dx, x1-ReMin.x, u1, u2)){
			if (cliptest(dx, ReMax.x-x1, u1, u2)){
				if (cliptest(-dy, y1-ReMin.y, u1, u2)){
					if (cliptest(dy, ReMax.y-y1, u1, u2)){
						if (u2 < 1){
							x2 = x1 + u2*dx;
							y2 = y1 + u2*dy;
						}
						if (u1 > 0){
							x1 = x1 + u1*dx;
							y1 = y1 + u1*dy;
						}
						f1 = new Ponto(Math.round(x1),Math.round(y1));
						f2 = new Ponto(Math.round(x2),Math.round(y2));
						dda(f1,f2,corE);
					}
				}
			}
		}
	}

		public void recorte(int recorte) {
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
			Retangulo ret;
			for(int i=0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				//apaga e plota a reta recortada
				apaga_dda(retaDDA);
				if (recorte == 0)
					cohenSutherland(retaDDA.p1,retaDDA.p2);
				else
					liangBarsky(retaDDA.p1,retaDDA.p2);
			}
			for(int i=0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				//apaga e plota a reta recortada
				apaga_reta_bresenham(retaBRE);
				if (recorte == 0)
					cohenSutherland(retaBRE.p1,retaBRE.p2);
				else
					liangBarsky(retaBRE.p1,retaBRE.p2);
			}
			for(int i=0; i < Retangulo.lista.size(); i++) {
				ret = Retangulo.lista.get(i);
				//apaga e plota o retangulo
				apaga_retangulo(ret);
				Ponto p1 = new Ponto(ret.p1.x,ret.p1.y);
				Ponto p2 = new Ponto(ret.p1.x,ret.p2.y);
				Ponto p3 = new Ponto(ret.p2.x,ret.p1.y);
				Ponto p4 = new Ponto(ret.p2.x,ret.p2.y);
				if (recorte == 0) {
					cohenSutherland(p1,p2);
					cohenSutherland(p2,p4);
					cohenSutherland(p3,p4);
					cohenSutherland(p1,p3);
				}
				else {
					liangBarsky(p1,p2);
					liangBarsky(p2,p4);
					liangBarsky(p3,p4);
					liangBarsky(p1,p3);
				}
			}
			ReMax.x=-1;ReMax.y=-1;
			ReMin.x=-1;ReMin.y=-1;
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
				if(DAx == -1) {
					DAx = x1;
					DAy = y1;
				}else if(DBx == -1) {
					DBx = x1;
					DBy = y1;
					//Centro da circunferencia
					int x_centro = Math.round((DAx+DBx)/2);
					int y_centro = Math.round((DAy+DBy)/2);
					Ponto Cc = new Ponto(x_centro,y_centro);
					//raio da circunferencia = (dx^2 + dy^2) /2
					double diametro = Math.sqrt(((DBx-DAx)*(DBx-DAx) + (DBy-DAy)*(DBy-DAy)));
					int raio = (int) Math.round(diametro / 2);
					//Desenha a circunferencia
					Circunferencia circ = new Circunferencia(Cc, raio, corE);
					circunferencia_bresenham(circ,corE);
					//reseta variaveis
					DAx = DAy = DBx = DBy = -1;
				}
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
			} else if(ferramenta_atual == Ferramentas.ROTACAO) {
			} else if (ferramenta_atual == Ferramentas.RECORTE) {
				if (ReMin.x == -1) {
					ReMin.x = x1;
					ReMin.y = y1;
				}else if(ReMax.x == -1) {
					ReMax.x = x1;
					ReMax.y = y1;
					recorte(0);
				}
			} else if (ferramenta_atual == Ferramentas.RECORTELB) {
				if (ReMin.x == -1) {
					ReMin.x = x1;
					ReMin.y = y1;
				}else if(ReMax.x == -1) {
					ReMax.x = x1;
					ReMax.y = y1;
					recorte(1);
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
