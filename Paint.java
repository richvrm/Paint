import java.util.ArrayList;
import java.lang.Math;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.Reader;

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

	// Definição de variáveis relacionadas à tela
	private JPanel contentPane, panelMenu, panelStatus, panel;
	private JLabel labelPosX, labelPosY;
	private JButton buttonCor, buttonPonto, buttonRetangulo, buttonCirculo, buttonRetaD, buttonTrans, buttonEscala, buttonRetaB,
			buttonMirrorX, buttonMirrorY, buttonMirrorXY, buttonRota, buttonClear, buttonCS, buttonLB, buttonSalvar, buttonRestaurar, buttonBoundary, buttonFlood, buttonHermite;
	private int x1,y1,x2,y2;
	private static MouseHandler mouse;
	private Graphics g;
	private Point mouseReleased, mousePressed,mousePos;
	private JColorChooser Cores;
	private static Color corE = Color.BLACK;
	//private static Color corB = Color.WHITE;

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
	private Icon salvar    = new ImageIcon(getClass().getResource("img/salvar.png"));
	private Icon restaurar    = new ImageIcon(getClass().getResource("img/restaurar.png"));
	private Icon escala    = new ImageIcon(getClass().getResource("img/escala.png"));
	private Icon hermite    = new ImageIcon(getClass().getResource("img/hermite.png"));
	private Icon bezier    = new ImageIcon(getClass().getResource("img/bezier.png"));
	//private Icon flood    = new ImageIcon(getClass().getResource("img/flood_fill.png"));
	//private Icon boundary    = new ImageIcon(getClass().getResource("img/boundary_fill.png"));

	//Tamanho do Canvas
	private int inicioL = 0;
	private int inicioA = 120;
	private int Largura = 800;
	private int Altura = 540;

	// Variáveis relacionadas às funções

	//variáveis das coordenadas do retangulo
	private int Rx1 = -1;
	private int Ry1 = -1;
	private int Rx2 = -1;
	private int Ry2 = -1;

	//variáveis das coordenadas do DDA
	private int DDAx1 = -1;
	private int DDAy1 = -1;
	private int DDAx2 = -1;
	private int DDAy2 = -1;

	//variáveis das coordenadas da reta bresenham
	private int RBx1 = -1;
	private int RBy1 = -1;
	private int RBx2 = -1;
	private int RBy2 = -1;

	//variáveis da circunferência de bresenham
	private int DAx = -1;
	private int DAy = -1;
	private int DBx = -1;
	private int DBy = -1;

    //variaveis da curva Hermite
    private int Hx1 = -1;
    private int Hy1 = -1;
    private int Hx2 = -1;
    private int Hy2 = -1;

	//variáveis da translação
	private int TEx;
	private int TEy;

	//variáveis da escala
	private int TAx = 1;
	private int TAy = 2;

	//variáveis do recorte
	private Ponto ReMin = new Ponto();
	private Ponto ReMax = new Ponto();
	float u1 = 0;
	float u2 = 1;

	//variáveis da rotação
	private double Grau = 20;

	//Ferramentas possíveis
	private enum Ferramentas {
		NORMAL,
		RETANGULO,
		DDA,
		RETA_BRESENHAM,
		CIRC_BRESENHAM,
		TRANSLACAO,
		ESCALA,
		RECORTE,
		ROTACAO,
		RECORTELB,
		FLOOD,
		BOUNDARY,
        HERMITE,
	};

	private Ferramentas ferramenta_atual = Ferramentas.NORMAL;

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
		buttonPonto.setBackground(Color.decode("#e70065"));
		buttonPonto.setHorizontalTextPosition(SwingConstants.CENTER); 

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

		//botao escala
		buttonEscala = new JButton();
		buttonEscala.addActionListener(this);
		buttonEscala.setIcon(escala);
		buttonEscala.setBackground(Color.decode("#e70065"));
		buttonEscala.setHorizontalTextPosition(SwingConstants.CENTER); 

		//botão reflexão
		buttonMirrorX = new JButton();
		buttonMirrorX.addActionListener(this);
		buttonMirrorX.setIcon(mirrorX);
		buttonMirrorX.setBackground(Color.decode("#e70065"));
		buttonMirrorX.setHorizontalTextPosition(SwingConstants.CENTER); 

		//botão reflexão
		buttonMirrorY = new JButton();
		buttonMirrorY.addActionListener(this);
		buttonMirrorY.setIcon(mirrorY);
		buttonMirrorY.setBackground(Color.decode("#e70065"));
		buttonMirrorY.setHorizontalTextPosition(SwingConstants.CENTER); 

		//botão reflexão
		buttonMirrorXY = new JButton();
		buttonMirrorXY.addActionListener(this);
		buttonMirrorXY.setIcon(mirrorXY);
		buttonMirrorXY.setBackground(Color.decode("#e70065"));
		buttonMirrorXY.setHorizontalTextPosition(SwingConstants.CENTER);

		//botão rotação
		buttonRota = new JButton();
		buttonRota.addActionListener(this);
		buttonRota.setIcon(rota);
		buttonRota.setBackground(Color.decode("#e70065"));
		buttonRota.setHorizontalTextPosition(SwingConstants.CENTER);

		//botão limpar
		buttonClear = new JButton();
		buttonClear.addActionListener(this);
		buttonClear.setIcon(clear);
		buttonClear.setBackground(Color.decode("#e70065"));
		buttonClear.setHorizontalTextPosition(SwingConstants.CENTER);  

		//botão Cohen Sutherland
		buttonCS = new JButton();
		buttonCS.addActionListener(this);
		buttonCS.setIcon(cs);
		buttonCS.setBackground(Color.decode("#e70065"));
		buttonCS.setHorizontalTextPosition(SwingConstants.CENTER);

		//botão LiangBarsky
		buttonLB = new JButton();
		buttonLB.addActionListener(this);
		buttonLB.setIcon(lb);
		buttonLB.setBackground(Color.decode("#e70065"));
		buttonLB.setHorizontalTextPosition(SwingConstants.CENTER);

		//botão salvar aquivo
		buttonSalvar = new JButton();
		buttonSalvar.addActionListener(this);
		buttonSalvar.setIcon(salvar);
		buttonSalvar.setBackground(Color.decode("#e70065"));
		buttonSalvar.setHorizontalTextPosition(SwingConstants.CENTER);

		//botão restaurar arquivo
		buttonRestaurar = new JButton();
		buttonRestaurar.addActionListener(this);
		buttonRestaurar.setIcon(restaurar);
		buttonRestaurar.setBackground(Color.decode("#e70065"));
		buttonRestaurar.setHorizontalTextPosition(SwingConstants.CENTER);

		//botão restaurar arquivo
		buttonHermite = new JButton();
		buttonHermite.addActionListener(this);
		buttonHermite.setIcon(hermite);
		buttonHermite.setBackground(Color.decode("#e70065"));
		buttonHermite.setHorizontalTextPosition(SwingConstants.CENTER);


		/*
		   buttonFlood = new JButton();
		   buttonFlood.addActionListener(this);
		   buttonFlood.setIcon(flood);
		   buttonFlood.setBackground(Color.decode("#e70065"));
		   buttonFlood.setHorizontalTextPosition(SwingConstants.CENTER);*/


		//botão boundary fill
		/*buttonBoundary = new JButton();
		buttonBoundary.addActionListener(this);
		buttonBoundary.setIcon(boundary);
		buttonBoundary.setBackground(Color.decode("#e70065"));
		buttonBoundary.setHorizontalTextPosition(SwingConstants.CENTER);*/

		//configurar grupo de botoes
		GroupLayout g1_panelMenu = new GroupLayout(panelMenu);
		g1_panelMenu.setHorizontalGroup(
				g1_panelMenu.createParallelGroup(Alignment.CENTER)
				.addGroup( g1_panelMenu.createSequentialGroup()
					.addGap(15)
					.addComponent(buttonCor)
					.addGap(10)
					.addComponent(buttonSalvar)
					.addGap(10)
					.addComponent(buttonRestaurar)
					.addGap(10)
					.addComponent(buttonClear)
					.addGap(10)
					.addComponent(buttonPonto)
					.addGap(10)
					.addComponent(buttonRetaD)
					.addGap(10)
					.addComponent(buttonRetaB)
					.addGap(10)
					.addComponent(buttonRetangulo)
					.addGap(10)
					.addComponent(buttonCirculo)
					.addGap(10)
					.addComponent(buttonHermite)
					)
				.addGroup( g1_panelMenu.createSequentialGroup()
					.addGap(60)
					.addComponent(buttonEscala)
					.addGap(10)
					.addComponent(buttonTrans)
					.addGap(10)
					.addComponent(buttonRota)
					.addGap(10)
					.addComponent(buttonMirrorX)
					.addGap(10)
					.addComponent(buttonMirrorY)
					.addGap(10)
					.addComponent(buttonMirrorXY)
					.addGap(10)
					.addComponent(buttonCS)
					.addGap(10)
					.addComponent(buttonLB)
					//.addGap(10)
					//.addComponent(buttonBoundary)
					//.addGap(10)
					//.addComponent(buttonFlood)
					)
					);

		g1_panelMenu.setVerticalGroup(
				g1_panelMenu.createParallelGroup(Alignment.CENTER)
				.addGroup(g1_panelMenu.createSequentialGroup()
					.addGap(10)
					.addGroup(g1_panelMenu.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonCor)
						.addComponent(buttonSalvar)
						.addComponent(buttonRestaurar)
						.addComponent(buttonClear)
						.addComponent(buttonPonto)
						.addComponent(buttonRetaD)
						.addComponent(buttonRetaB)
						.addComponent(buttonRetangulo)
						.addComponent(buttonCirculo)
						.addComponent(buttonHermite)
						)
					.addGap(10)
					.addGroup(g1_panelMenu.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonEscala)
						.addComponent(buttonTrans)
						.addComponent(buttonRota)
						.addComponent(buttonMirrorX)
						.addComponent(buttonMirrorY)
						.addComponent(buttonMirrorXY)
						.addComponent(buttonCS)
						.addComponent(buttonLB)
						//.addComponent(buttonBoundary)
						//.addComponent(buttonFlood)
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
		} else if(arg0.getSource() == buttonPonto){
			do_buttonPonto_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonRetangulo){
				do_buttonRetangulo_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonCirculo){
			do_buttonCirculo_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonRetaD){
			do_buttonReta_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonRetaB){
			do_buttonRetaB_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonTrans){
			do_buttonTrans_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonEscala){
			do_buttonEscala_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonMirrorX){
			do_buttonMirrorX_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonMirrorY){
			do_buttonMirrorY_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonMirrorXY){
			do_buttonMirrorXY_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonRota){
			do_buttonRota_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonClear){
			do_buttonClear_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonCS){
			do_buttonCS_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonLB){
			do_buttonLB_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonSalvar){
			do_buttonSalvar_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonRestaurar){
			do_buttonRestaurar_actionPerfomed(arg0);
		} else if(arg0.getSource() == buttonBoundary){
			do_buttonBoundary_actionPerfomed(arg0);
		}  else if(arg0.getSource() == buttonHermite){
			do_buttonHermite_actionPerfomed(arg0);
		}//else if(arg0.getSource() == buttonFlood){
			//do_buttonFlood_actionPerfomed(arg0);
		//}
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


	protected void do_buttonRetaB_actionPerfomed(ActionEvent arg0){
		ferramenta_atual = Ferramentas.RETA_BRESENHAM;
	}

	protected void do_buttonSalvar_actionPerfomed(ActionEvent arg0){
		salvar();
	}

	protected void do_buttonRestaurar_actionPerfomed(ActionEvent arg0){
		String arq;
		arq = JOptionPane.showInputDialog("Digite o nome do arquivo:");
		if ((arq != null) && (arq.length() > 0)) {    
			try {
				mouse.restaurar(arq);
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, "Digite o nome do arquivo");
			}  
		}
	}
	/*
	   protected void do_buttonFlood_actionPerfomed(ActionEvent arg0){
	   ferramenta_atual = Ferramentas.FLOOD;
	   }*/

	protected void do_buttonBoundary_actionPerfomed(ActionEvent arg0){
		ferramenta_atual = Ferramentas.BOUNDARY;
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
					mouse.translacao(true);
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, "Digite apenas números inteiros");
				}   
			}            
		}
	}

	protected void do_buttonEscala_actionPerfomed(ActionEvent arg0){
		String xis;
		String yis;
		xis = JOptionPane.showInputDialog("Digite X:");
		if ((xis != null) && (xis.length() > 0)) {    
			yis = JOptionPane.showInputDialog("Digite Y:");
			if((yis != null) ){
				try {
					TAx = Integer.parseInt(xis);
					TAy = Integer.parseInt(yis);
					ferramenta_atual = Ferramentas.ESCALA;
					mouse.escala();
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

	protected void do_buttonHermite_actionPerfomed(ActionEvent arg0){
		ferramenta_atual = Ferramentas.HERMITE;
	}

	//Escreve em um arquivo todos os objetos criados no canvas em seu estado atual
	private void salvar() {
		RetaDDA retaDDA;
		RetaBRE retaBRE;
		Retangulo r;
		Circunferencia circ;
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("canvas.txt"), "utf-8"));

			for(int i=0; i < RetaDDA.lista.size(); ++i){
				retaDDA = RetaDDA.lista.get(i);
				writer.write("RetaDDA "+retaDDA.p1.x+" "+retaDDA.p1.y+" "+retaDDA.p2.x+" "+retaDDA.p2.y+"\n");
			}
			for(int i=0; i < RetaBRE.lista.size(); ++i){
				retaBRE = RetaBRE.lista.get(i);
				writer.write("RetaBRE "+retaBRE.p1.x+" "+retaBRE.p1.y+" "+retaBRE.p2.x+" "+retaBRE.p2.y+"\n");
			}
			for(int i=0; i < Retangulo.lista.size(); ++i){
				r= Retangulo.lista.get(i);
				writer.write("Retangulo "+r.p1.x+" "+r.p1.y+" "+r.p2.x+" "+r.p2.y+"\n");
			}
			for(int i=0; i < Circunferencia.lista.size(); ++i){
				circ = Circunferencia.lista.get(i);
				writer.write("Circunferencia "+circ.centro.x+" "+circ.centro.y+" "+circ.raio+"\n");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {writer.close();} catch (Exception ex) {/*ignore*/}
		}	
	}

	// Desenho
	private void setupDesenho(){
		g = panel.getGraphics();
	}

	//Classe para lidar com eventos de mouse
	public class MouseHandler extends MouseAdapter
	{
		//Métodos para Apagar=======================
		//plotam os objetos com a cor do fundo
		//Note que os métodos de apagar não excluem os objetos
		//pois são usados para redesenhar esses objetos
		//O único método que realmente exclui é o apagartudo()
		public void apaga_circunferencia_bresenham(Circunferencia circ) {
			circunferencia_bresenham(circ, Color.WHITE);
		}

		public void apaga_reta_bresenham(RetaBRE reta) {
			reta_bresenham(reta.p1, reta.p2, Color.WHITE);
		}

		public void apaga_retangulo(Retangulo retangulo) {
			retangulo(retangulo, Color.WHITE);
		}

		public void apaga_dda(RetaDDA reta) {
			dda(reta.p1, reta.p2, Color.WHITE);
		}

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

		//Método para Ler de arquivos ================
		//Le um estado do canvas salvo anteriormente
		//e recria e plota os objetos
		private void restaurar(String nome) {
			Reader reader = null;
			BufferedReader br = null;

			try {

				br = new BufferedReader(new FileReader(nome));
				String line = br.readLine();

				while (line != null) {
					int a1,b1,a2,b2;
					RetaDDA retaDDA;
					RetaBRE retaBRE;
					Retangulo r;
					Circunferencia circ;
					//para cada linha lida, separa por espaços e 
					//vê que tipo de objeto é e cria e plota o objeto
					String[] objeto = line.split(" ");
					if (objeto[0].equals("RetaDDA")) {
						a1 = Integer.parseInt(objeto[1]);
						b1 = Integer.parseInt(objeto[2]);
						a2 = Integer.parseInt(objeto[3]);
						b2 = Integer.parseInt(objeto[4]);
						Ponto p1 = new Ponto(a1,b1);
						Ponto p2 = new Ponto(a2,b2);
						retaDDA = new RetaDDA(p1,p2,corE);
						dda(retaDDA.p1,retaDDA.p2,corE);
					}else if (objeto[0].equals("RetaBRE")) {
						a1 = Integer.parseInt(objeto[1]);
						b1 = Integer.parseInt(objeto[2]);
						a2 = Integer.parseInt(objeto[3]);
						b2 = Integer.parseInt(objeto[4]);
						Ponto p1 = new Ponto(a1,b1);
						Ponto p2 = new Ponto(a2,b2);
						retaBRE = new RetaBRE(p1,p2,corE);
						reta_bresenham(retaBRE.p1,retaBRE.p2,corE);
					}else if (objeto[0].equals("Retangulo")) {
						a1 = Integer.parseInt(objeto[1]);
						b1 = Integer.parseInt(objeto[2]);
						a2 = Integer.parseInt(objeto[3]);
						b2 = Integer.parseInt(objeto[4]);
						Ponto p1 = new Ponto(a1,b1);
						Ponto p2 = new Ponto(a2,b2);
						r = new Retangulo(p1,p2,corE);
						retangulo(r,corE);
					}else if (objeto[0].equals("Circunferencia")) {
						a1 = Integer.parseInt(objeto[1]);
						b1 = Integer.parseInt(objeto[2]);
						int raio = Integer.parseInt(objeto[3]);
						Ponto p1 = new Ponto(a1,b1);
						circ = new Circunferencia(p1,raio,corE);
						circunferencia_bresenham(circ,corE);
					}

					line = br.readLine();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {br.close();} catch (Exception ex) {/*ignore*/}
			}

		}

		// Métodos para plotagem =============================

		//calcula o tamanho da reta fg
		//Tamanho da reta = sqrt(dx^2 + dy^2)
		public int tamanho_reta(Ponto f, Ponto g /*hihihi*/) {
			double tam = Math.sqrt( ( (g.x-f.x)*(g.x-f.x) + (g.y-f.y)*(g.y-f.y) ) );
			return (int) Math.round(tam);
		}

		//Seta um ponto individual na tela
		//Por utilizarmos uma interface java,
		//não conseguimos interagir diretamente
		//com o canvas. Foi necessário
		//utilizar a função drawLine já implementada no java
		//só utilizamos ela aqui para fazer uma linha de um ponto até
		//ele mesmo, ou seja, setar um pixel
		public void setPixel(Ponto ponto, Color cor) {
			setupDesenho();
			g.setColor(cor);
			g.drawLine(ponto.x, ponto.y-140, ponto.x, ponto.y-140);
			g.setColor(corE);
		}
/*
        public void curvaH(Hermite cHermite){
            steps = tamanho_reta(cHermite.p0, cHermite.p3);
            for (int t=0; t < steps; t++){
              float s = (float)t / (float)steps;    // scale s to go from 0 to 1
              float h1 =  2 * Math.pow(s,3) - 3 * Math.pow(s,2) + 1;          // calculate basis function 1
              float h2 = -2 * Math.pow(s,3) + 3 * Math.pow(s,2);              // calculate basis function 2
              float h3 =  1 * Math.pow(s,3) - 2 * Math.pow(s,2) + s;          // calculate basis function 3
              float h4 =  1 * Math.pow(s,3) - 1 * Math.pow(s,2);              // calculate basis function 4
              int p =    h1*cHermite.p0 +                    // multiply and sum all funtions
                         h2*cHermite.p3 +                    // together to build the interpolated
                         h3*cHermite.p0d +                    // point along the curve.
                         h4*cHermite.p3d;
             // setPixel(p)                            // draw to calculated point on the curve
            }
        }
*/
		//Plota uma reta utilizando o algoritmo DDA
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

		//Plota uma reta utilizando o algoritmo de Bresenham
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
			x = p1.x;
			y = p1.y;
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

		//Plota um retangulo utilizando seus pontos diagonais
		public void retangulo(Retangulo r, Color cor) {
			//Reta superior
			reta_bresenham(r.p1, r.p3, cor);
			//lateral esquerda
			reta_bresenham(r.p1, r.p4, cor);
			//reta inferior
			reta_bresenham(r.p2, r.p3, cor);
			//lateral direita
			reta_bresenham(r.p2, r.p4, cor);
			Rx1 = Ry1 = Rx2 = Ry2 = -1;
		}

		//Plota o segundo, terceiro e quarto quadrantes da circunferência
		//refletindo o primeiro quadrante
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

		// Plota o primeiro quadrante da circunferência utilizando
		// o algoritmo de bresenham e chama colorirSimetricos para
		// plotar os outros
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

		//Métodos de transformações=========================================

		// boolean plotar: controla se vai plotar os objetos ou só transformá-los
		// dessa forma podemos utilizar o método juntamente com a escala
		// utiliza os parâmetros globais TEx e TEy (vetor de translacao)
		public void translacao(boolean plotar) {
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;

			// aplica translacao nas retas DDA
			for(int i = 0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				apaga_dda(retaDDA);
				// atualiza p1
				retaDDA.p1.x = retaDDA.p1.x + TEx;
				retaDDA.p1.y = retaDDA.p1.y + TEy;
				// atualiza p2
				retaDDA.p2.x = retaDDA.p2.x + TEx;
				retaDDA.p2.y = retaDDA.p2.y + TEy;
				// substitui a reta pela nova na lista
				RetaDDA.lista.set(i, retaDDA);
				if(plotar)
					dda(retaDDA.p1, retaDDA.p2, retaDDA.cor);
			}

			// aplica translacao nas retas bresenham
			for(int i = 0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				apaga_reta_bresenham(retaBRE);
				// atualiza p1
				retaBRE.p1.x = retaBRE.p1.x + TEx;
				retaBRE.p1.y = retaBRE.p1.y + TEy;
				// atualiza p2
				retaBRE.p2.x = retaBRE.p2.x + TEx;
				retaBRE.p2.y = retaBRE.p2.y + TEy;
				// substitui a reta pela nova na lista
				RetaBRE.lista.set(i, retaBRE);
				if(plotar)
					reta_bresenham(retaBRE.p1, retaBRE.p2, retaBRE.cor);
			}

			// aplica translacao nos retangulos
			for(int i = 0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				apaga_retangulo(r);
				// atualiza p1
				r.p1.x = r.p1.x + TEx;
				r.p1.y = r.p1.y + TEy;
				// atualiza p2
				r.p2.x = r.p2.x + TEx;
				r.p2.y = r.p2.y + TEy;
				// substitui o retangulo pelo novo na lista
				Retangulo.lista.set(i, r);
				if(plotar)
					retangulo(r, r.cor);
			}

			// aplica translacao nas circunferencias
			for(int i = 0; i < Circunferencia.lista.size(); i++) {
				circ = Circunferencia.lista.get(i);
				apaga_circunferencia_bresenham(circ);
				// atualiza o centro
				circ.centro.x = circ.centro.x + TEx;
				circ.centro.y = circ.centro.y + TEy;
				// substitui a circunferencia pela nova na lista
				Circunferencia.lista.set(i, circ);
				if(plotar)
					circunferencia_bresenham(circ, circ.cor);
			}
		}

		//Aplica a escala nos objetos utilizando os parâmetros globais
		//TAx e TAy (vetor de escala)
		public void escala() {
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;

			// aplica a escala nas retas DDA
			for(int i = 0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				apaga_dda(retaDDA);
				//translada para a origem para nao mudar a imagem de posicao
				TEx = - retaDDA.p1.x;
				TEy = - retaDDA.p1.y;
				translacao(false);
				// atualiza p1
				retaDDA.p1.x = retaDDA.p1.x * TAx;
				retaDDA.p1.y = retaDDA.p1.y * TAy;
				// atualiza p2
				retaDDA.p2.x = retaDDA.p2.x * TAx;
				retaDDA.p2.y = retaDDA.p2.y * TAy;
				//translada de volta para onde estava
				TEx = - TEx;
				TEy = - TEy;
				translacao(false);
				// substitui a reta pela nova na lista
				RetaDDA.lista.set(i, retaDDA);
				dda(retaDDA.p1, retaDDA.p2, retaDDA.cor);
			}

			// aplica escala nas retas bresenham
			for(int i = 0; i < RetaBRE.lista.size(); i++) {
				retaBRE = RetaBRE.lista.get(i);
				apaga_reta_bresenham(retaBRE);
				//translada para a origem para nao mudar a imagem de posicao
				TEx = - retaBRE.p1.x;
				TEy = - retaBRE.p1.y;
				translacao(false);
				// atualiza p1
				retaBRE.p1.x = retaBRE.p1.x * TAx;
				retaBRE.p1.y = retaBRE.p1.y * TAy;
				// atualiza p2
				retaBRE.p2.x = retaBRE.p2.x * TAx;
				retaBRE.p2.y = retaBRE.p2.y * TAy;
				//translada de volta para onde estava
				TEx = - TEx;
				TEy = - TEy;
				translacao(false);
				// substitui a reta pela nova na lista
				RetaBRE.lista.set(i, retaBRE);
				reta_bresenham(retaBRE.p1, retaBRE.p2, retaBRE.cor);
			}
			// aplica escala nos retangulos
			for(int i = 0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);
				apaga_retangulo(r);
				//translada para a origem para nao mudar a imagem de posicao
				TEx = - r.p1.x;
				TEy = - r.p1.y;
				translacao(false);
				// atualiza p1
				r.p1.x = r.p1.x * TAx;
				r.p1.y = r.p1.y * TAy;
				// atualiza p2
				r.p2.x = r.p2.x * TAx;
				r.p2.y = r.p2.y * TAy;
				//translada de volta para onde estava
				TEx = - TEx;
				TEy = - TEy;
				translacao(false);
				// substitui o retangulo pelo novo na lista
				Retangulo.lista.set(i, r);
				retangulo(r, r.cor);
			}
			// aplica escala nas Circunferencias
			for(int i = 0; i < Circunferencia.lista.size(); i++) {
				circ = Circunferencia.lista.get(i);
				apaga_circunferencia_bresenham(circ);
				//translada para a origem para nao mudar a imagem de posicao
				TEx = - circ.centro.x;
				TEy = - circ.centro.y;
				translacao(false);

				//Pega um ponto qualquer que esteja na circunferencia
				//dessa forma a escala é aplicada no raio da circunferência,
				//aumentando o raio aumenta a circunferencia toda
				int xg = circ.centro.x - circ.raio;
				int yg = circ.centro.y;
				circ.centro.x = circ.centro.x * TAx;
				circ.centro.y = circ.centro.y * TAy;
				xg = xg * TAx;
				yg = yg * TAy;
				//recalcula o raio com base no ponto generico obtido e o novo centro
				circ.raio = tamanho_reta(new Ponto(xg,yg),circ.centro);
				//translada de volta para onde estava
				TEx = - TEx;
				TEy = - TEy;
				translacao(false);
				// substitui a Circunferencia pela nova na lista
				Circunferencia.lista.set(i,circ);
				circunferencia_bresenham(circ,corE);
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
				retangulo(r,Color.BLACK);
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
		//utiliza a variável global Grau
		//Não se rotaciona um círculo por motivos óbvios
		public void rotation() {
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;

			//Ajusta o grau ja que o canvas não representa todos os quadrantes
			//inverte o sinal pois a posição 0,0 é em cima na esquerda
			Grau = -(Grau * 3.14/180);

			//apaga e plota a reta rotacionada
			for(int i=0; i < RetaDDA.lista.size(); i++) {
				retaDDA = RetaDDA.lista.get(i);
				apaga_dda(retaDDA);

				int x0 = retaDDA.p2.x;
				int y0 = retaDDA.p2.y;
				int xr = retaDDA.p1.x; //ponto de ref
				int yr = retaDDA.p1.y; //ponto de ref

				double x = (x0 - xr) * Math.cos(Grau) - (y0 - yr) * Math.sin(Grau) + xr;
				double y = (y0 - yr) * Math.cos(Grau) + (x0 - xr) * Math.sin(Grau) + yr;
				
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
				int xr = retaBRE.p1.x; //ponto de ref
				int yr = retaBRE.p1.y; //ponto de ref

				double x = (x0 - xr) * Math.cos(Grau) - (y0 - yr) * Math.sin(Grau) + xr;
				double y = (y0 - yr) * Math.cos(Grau) + (x0 - xr) * Math.sin(Grau) + yr;

				retaBRE.p2.x = (int)x;
				retaBRE.p2.y = (int)y;

				reta_bresenham(retaBRE.p1,retaBRE.p2,Color.BLACK);
			}

			//apaga e plota o retangulo rotacionado
			for(int i=0; i < Retangulo.lista.size(); i++) {
				r = Retangulo.lista.get(i);

				//pega todos os 4 pontos do retangulo
				int x0 = r.p2.x;//canto direito sup
				int y0 = r.p2.y;

				int x1 = r.p3.x;//canto esquerdo sup
				int y1 = r.p3.y;

				int x2 = r.p4.x;//canto direito inf
				int y2 = r.p4.y;

				int xr = r.p1.x;//canto esquerdo inf (ponto de ref)
				int yr = r.p1.y;

				apaga_retangulo(r);

				//aplica a equação para cada ponto com base no ponto 1 do retangulo.
				//dessa forma a rotação do retangulo se dá em torno de um ponto e não de seu centro

				//rotação do ponto direito sup
				double nx0 = (x0 - xr) * Math.cos(Grau) - (y0 - yr) * Math.sin(Grau) + xr;
				double ny0 = (y0 - yr) * Math.cos(Grau) + (x0 - xr) * Math.sin(Grau) + yr;

				//rotação do ponto esquerdo sup
				double nx1 = (x1 - xr) * Math.cos(Grau) - (y1 - yr) * Math.sin(Grau) + xr;
				double ny1 = (y1 - yr) * Math.cos(Grau) + (x1 - xr) * Math.sin(Grau) + yr;

				//rotação do ponto direito inf
				double nx2 = (x2 - xr) * Math.cos(Grau) - (y2 - yr) * Math.sin(Grau) + xr;
				double ny2 = (y2 - yr) * Math.cos(Grau) + (x2 - xr) * Math.sin(Grau) + yr;

				Ponto p2 = new Ponto((int)Math.floor(nx0),(int)Math.floor(ny0));
				Ponto p3 = new Ponto((int)Math.floor(nx1),(int)Math.floor(ny1));
				Ponto p4 = new Ponto((int)Math.floor(nx2),(int)Math.floor(ny2));

				r.p2 = p2;
				r.p3 = p3;
				r.p4 = p4;

				retangulo(r, corE);
			}
		}


		//Métodos de recorte de regiões =================================================
		//ReMin.x, ReMax.x, ReMin.y, ReMax.y -> limites da janela

		// Método geral do recorte.
		// pega os dados da janela, os trata se for necessário
		// e chama o método de recorte ativo no momento
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
				else{
					liangBarsky(retaDDA.p1,retaDDA.p2);
				}
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

		// Calcula o código de região utilizado no 
		// algoritmo cohenSutherland
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

		//Plota segmentos de retas que estejam dentro da região
		//selecionada utilizando o region_code
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
				}else if ((c1 & c2) == 1 || (c1 & c2) == 2 || (c1 & c2) == 4 || (c1 & c2) == 8) {//100% fora
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

		//Determina se a reta está dentro, fora ou parciamente dentro
		//da região selecionada
		//Utilizado no liangBarsky
		public boolean cliptest(float p, float q){
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

		//Plota somente segmentos de reta que estejam dentro da área
		//selecionada utilizando cliptest
		public void liangBarsky(Ponto p1, Ponto p2) {
			u1 = 0;
			u2 = 1;
			float x1 = p1.x;
			float x2 = p2.x;
			float y1 = p1.y;
			float y2 = p2.y;
			float dx = x2-x1;
			float dy = y2-y1;
			if (cliptest(-dx, x1-ReMin.x)){
				if (cliptest(dx, ReMax.x-x1)){
					if (cliptest(-dy, y1-ReMin.y)){
						if (cliptest(dy, ReMax.y-y1)){
							if (u2 < 1.0){
								x2 = x1 + u2*dx;
								y2 = y1 + u2*dy;
							}
							if (u1 > 0.0){
								x1 = x1 + u1*dx;
								y1 = y1 + u1*dy;
							}
							Ponto f1 = new Ponto(Math.round(x1),Math.round(y1));
							Ponto f2 = new Ponto(Math.round(x2),Math.round(y2));
							dda(f1,f2,corE);
						}
					}
				}
			}
		}


		//Métodos de preenchimento de áreas =========================================

		//Retorna True se o ponto p pertence à algum
		//objeto plotado
		//testa se p em relacao a um ponto do objeto tem
		//o mesmo coeficiente angular que os pontos
		//conhecidos do objeto e esta entre os dois pontos
		public boolean toca_borda(Ponto p) {
			boolean retorno = false;
			RetaDDA retaDDA;
			RetaBRE retaBRE;
			Retangulo r;
			Circunferencia circ;
			Ponto pt;
			double m = 0; // coeficiente angula da reta

			//checa se chegou nos limites do canvas
			if (p.x > Largura || p.y > Altura) retorno = true;

			//checa se o ponto ja foi colorido
			for(int i=0; i < Ponto.lista.size() && !retorno; i++) {
				pt = Ponto.lista.get(i);
				if (p.x == pt.x && p.y == pt.y) retorno = true;
			}

			for(int i=0; i < RetaDDA.lista.size() && !retorno; i++) {
				retaDDA = RetaDDA.lista.get(i);
				//delta x,y da propria reta
				int dx = Math.abs(retaDDA.p2.x - retaDDA.p1.x);
				int dy = Math.abs(retaDDA.p2.y - retaDDA.p1.y);
				// dx é zero se a reta é paralela ao eixo x
				// if para evitar divisao por 0
				if(dx != 0) m = dx/dy;
				//delta x,y do ponto com a reta
				dx = Math.abs(retaDDA.p2.x - p.x);
				dy = Math.abs(retaDDA.p2.y - p.y);

				if(dx != 0 && m == dy/dx)
					retorno = true;

				//exclui casos nos quais o ponto esta fora do segmento de reta
				//pois o ponto pode estar na reta mas nao no segmento
				if(retaDDA.p2.x < retaDDA.p1.x){
					if(p.x < retaDDA.p2.x || p.x > retaDDA.p1.x)
						retorno = false;
				}else{
					if(p.x > retaDDA.p2.x || p.x < retaDDA.p1.x)
						retorno = false;
				}
				if(retaDDA.p2.y < retaDDA.p1.y){
					if(p.y < retaDDA.p2.y || p.y > retaDDA.p1.y)
						retorno = false;
				}else{
					if(p.y > retaDDA.p2.y || p.y < retaDDA.p1.y)
						retorno = false;
				}

			}
			for(int i=0; i < RetaBRE.lista.size() && ! retorno; i++) {
				retaBRE = RetaBRE.lista.get(i);
				//delta x,y da propria reta
				int dx = Math.abs(retaBRE.p2.x - retaBRE.p1.x);
				int dy = Math.abs(retaBRE.p2.y - retaBRE.p1.y);
				// dx é zero se a reta é paralela ao eixo x
				// if para evitar divisao por 0
				if(dx != 0) m = dx/dy;
				//delta x,y do ponto com a reta
				dx = Math.abs(retaBRE.p2.x - p.x);
				dy = Math.abs(retaBRE.p2.y - p.y);

				if(dx != 0 && m == dy/dx)
					retorno = true;

				//exclui casos nos quais o ponto esta fora do segmento de reta
				//pois o ponto pode estar na reta mas nao no segmento
				if(retaBRE.p2.x < retaBRE.p1.x){
					if(p.x < retaBRE.p2.x || p.x > retaBRE.p1.x)
						retorno = false;
				}else{
					if(p.x > retaBRE.p2.x || p.x < retaBRE.p1.x)
						retorno = false;
				}
				if(retaBRE.p2.y < retaBRE.p1.y){
					if(p.y < retaBRE.p2.y || p.y > retaBRE.p1.y)
						retorno = false;
				}else{
					if(p.y > retaBRE.p2.y || p.y < retaBRE.p1.y)
						retorno = false;
				}
			}
			for(int i=0; i < Retangulo.lista.size() && ! retorno; i++) {
				r= Retangulo.lista.get(i);
				//testa com as 4 retas do retangulo
				//delta x,y da propria reta
				int dx,dy;

				//pega todos os 4 pontos do retangulo
				int x0 = r.p1.x;
				int y0 = r.p1.y;

				int x1 = r.p1.x;
				int y1 = r.p2.y;

				int x2 = r.p2.x;
				int y2 = r.p1.y;

				int xr = r.p2.x;
				int yr = r.p2.y;

				Ponto[] pts = {new Ponto(x0,y0), new Ponto(x1,y1), new Ponto(x2,y2), new Ponto(xr,yr)};

				//reta esquerda
				dx = Math.abs(pts[0].x - pts[1].x);
				dy = Math.abs(pts[0].y - pts[1].y);
				// dx é zero se a reta é paralela ao eixo x
				// if para evitar divisao por 0
				if(dx != 0) m = dy/dx;
				//delta x,y do ponto com a reta
				dx = Math.abs(pts[0].x - p.x);
				dy = Math.abs(pts[0].y - p.y);
				if(dx != 0 && m == dy/dx)
					retorno = true;

				//so testa o proximo se nao pertence ao anterior
				if (! retorno) {
					//reta superior
					dx = Math.abs(pts[0].x - pts[2].x);
					dy = Math.abs(pts[0].y - pts[2].y);
					// dx é zero se a reta é paralela ao eixo x
					// if para evitar divisao por 0
					if(dx != 0) m = dy/dx;
					//delta x,y do ponto com a reta
					dx = Math.abs(pts[0].x - p.x);
					dy = Math.abs(pts[0].y - p.y);
					if(dx != 0 && m == dy/dx)
						retorno = true;

					//so testa o proximo se nao pertence ao anterior
					if (! retorno) {
						//reta direita
						dx = Math.abs(pts[2].x - pts[3].x);
						dy = Math.abs(pts[2].y - pts[3].y);
						// dx é zero se a reta é paralela ao eixo x
						// if para evitar divisao por 0
						if(dx != 0) m = dy/dx;
						//delta x,y do ponto com a reta
						dx = Math.abs(pts[2].x - p.x);
						dy = Math.abs(pts[2].y - p.y);
						if(dx != 0 && m == dy/dx)
							retorno = true;

						//so testa o proximo se nao pertence ao anterior
						if (! retorno) {
							//reta inferior
							dx = Math.abs(pts[1].x - pts[3].x);
							dy = Math.abs(pts[1].y - pts[3].y);
							// dx é zero se a reta é paralela ao eixo x
							// if para evitar divisao por 0
							if(dx != 0) m = dy/dx;
							//delta x,y do ponto com a reta
							dx = Math.abs(pts[1].x - p.x);
							dy = Math.abs(pts[1].y - p.y);
							if(dx != 0 && m == dy/dx)
								retorno = true;
						}
					}
				}

				//exclui casos nos quais o ponto esta fora do segmento de reta
				//pois o ponto pode estar na reta mas nao no segmento
				if(r.p2.x < r.p1.x){
					if(p.x < r.p2.x || p.x > r.p1.x)
						retorno = false;
				}else{
					if(p.x > r.p2.x || p.x < r.p1.x)
						retorno = false;
				}
				if(r.p2.y < r.p1.y){
					if(p.y < r.p2.y || p.y > r.p1.y)
						retorno = false;
				}else{
					if(p.y > r.p2.y || p.y < r.p1.y)
						retorno = false;
				}
			}
			//No circulo, verifica se a distancia até o centro é igual ao raio
			for(int i=0; i < Circunferencia.lista.size() && ! retorno; i++) {
				circ = Circunferencia.lista.get(i);

				//distancia do ponto ao centro da circunferencia
				int dist_pt_centro = (int)Math.round(tamanho_reta(circ.centro,p));

				//teste feito com +1 e -1 para levar em conta possivel erro de arredondamento
				if ( dist_pt_centro == circ.raio || dist_pt_centro +1 == circ.raio || dist_pt_centro-1 == circ.raio)
					retorno = true;

			}
			return retorno;
		}

		/*
		//x, y = ponto inicial
		//cor_preenche = cor de preenchiemnto
		//cor_antiga = cor do interior
		public void flood(int x, int y, int cor_preenche, int cor_antiga){
		if (inquirir_cor(x,y) = cor_antiga){
		Ponto p = new Ponto(x, y);
		setPixel(p,cor_preenche);
		flood(x+1,y,cor_preenche,cor_antiga);
		flood(x-1,y,cor_preenche,cor_antiga);
		flood(x,y+1,cor_preenche,cor_antiga);
		flood(x,y-1,cor_preenche,cor_antiga);
		}
		}*/

		//x, y = ponto inicial
		//algoritmo adaptado pois nao usamos matriz de pixels
		//ele verifica se chegou na borda olhando se o ponto
		//intercepta algum objeto criado
		//preenche em uma direção até chegar na borda. Depois preenche em outra
		/*
		public void boundary(int x, int y,int lim){
			Ponto p = new Ponto(x, y);
			//se o ponto nao eh de nenhuma borda, plota
			if ( !toca_borda(p) && lim > 0){
				setPixel(p,corE);
				boundary(x+1,y,lim-1);
				boundary(x-1,y,lim-1);
				boundary(x,y+1,lim-1);
				boundary(x,y-1,lim-1);
			}
		}*/


		// Métodos para capturar eventos ==================================

		// Captura um clique e define seu significado
		// conforme a ferramenta em uso
		public void mousePressed( MouseEvent e ){
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
					retangulo(r, corE);
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
					//Como capturamos os pontos do diametro da circunferencia, 
					//é necessário calcular o centro e o raio
					DBx = x1;
					DBy = y1;
					//Centro da circunferencia
					int x_centro = Math.round((DAx+DBx)/2);
					int y_centro = Math.round((DAy+DBy)/2);
					Ponto Cc = new Ponto(x_centro,y_centro);
					//distancia do centro ate um dos pontos marcados
					int raio = (int) Math.round( tamanho_reta(Cc,new Ponto(DAx,DAy)));
					//Desenha a circunferencia
					Circunferencia circ = new Circunferencia(Cc, raio, corE);
					circunferencia_bresenham(circ,corE);
					//reseta variaveis
					DAx = DAy = DBx = DBy = -1;
				}
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
			}else if(ferramenta_atual == Ferramentas.HERMITE){
                //captura o primeiro ponto se as primeiras variaveis da
				//reta forem -1
				if (Hx1 == -1) {
					Hx1 = x1;
					Hy1 = y1;
					//Captura o segundo ponto se as primeiras variaveis da 
					//reta forem != -1 e as segundas forem -1
				} else if(Hx2 == -1) {
					Hx2 = x1;
					Hy2 = y1;
					Ponto Hp1 = new Ponto(Hx1, Hy1);
					Ponto Hp2 = new Ponto(Hx2, Hy2);

					Hermite curvaHermite = new Hermite(Hp1, Hp2);
					//curvaH(curvaHermite);
                 }

            }
			x2=x1;
			y2=y1;
		}

		public void mouseDragged( MouseEvent e ){
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
