import java.util.ArrayList;

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

public class Tela extends JFrame implements MouseMotionListener, ActionListener, MouseListener{
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
    private int forma;
    private int x1, y1, x2,y2;

    private Graphics g;
    private Point mousePos;

    protected Point mouseReleased;
    protected Point mousePressed;
    protected JColorChooser Cores;
    protected Color corE = Color.BLACK;

    private Icon pen  = new ImageIcon(getClass().getResource("img/pen.png"));
    private Icon ret  = new ImageIcon(getClass().getResource("img/ret.png"));
    private Icon circ = new ImageIcon(getClass().getResource("img/circ.png"));

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
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
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
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.setBackground(Color.WHITE);
        panel.setBounds(0,60,800,442);
        contentPane.add(panel);
        panel.setLayout(null);



        //Painel que exibe a posição no Painel de desenho
        panelStatus = new JPanel();
        panelStatus.setBounds(0,502,800,60);
        contentPane.add(panelStatus);
	    
	// label que exibe a posicao X

        labelPosX = new JLabel("");
        labelPosX.setHorizontalTextPosition(SwingConstants.CENTER);
        labelPosX.setVerticalTextPosition(SwingConstants.CENTER);
        labelPosX.setBorder(new TitledBorder(null, "X", TitledBorder.LEADING,TitledBorder.TOP, null,null));
	    
	// label que exibe a posicao Y

        labelPosY = new JLabel("");
        labelPosY.setHorizontalTextPosition(SwingConstants.CENTER);
        labelPosY.setVerticalTextPosition(SwingConstants.CENTER);
        labelPosY.setBorder(new TitledBorder(null, "Y", TitledBorder.LEADING,TitledBorder.TOP, null,null));

        //configurar grupo do painel de posição

        GroupLayout g1_panelStatus = new GroupLayout(panelStatus);     
        g1_panelStatus.setHorizontalGroup(
            g1_panelStatus.createParallelGroup(Alignment.LEADING)
            .addGroup(g1_panelStatus.createSequentialGroup()
            .addGap(10)
            .addComponent(labelPosX,GroupLayout.PREFERRED_SIZE,40,GroupLayout.PREFERRED_SIZE)
            .addComponent(labelPosY,GroupLayout.PREFERRED_SIZE,40,GroupLayout.PREFERRED_SIZE)
            )
        );

        g1_panelStatus.setVerticalGroup(
            g1_panelStatus.createParallelGroup(Alignment.TRAILING)
            .addGroup(g1_panelStatus.createSequentialGroup()
            .addContainerGap()
            .addGroup(g1_panelStatus.createParallelGroup(Alignment.TRAILING)
            .addComponent(labelPosX, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,40,Short.MAX_VALUE)
            .addComponent(labelPosY, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,40,Short.MAX_VALUE))
            .addContainerGap()
            )
        );
        panelStatus.setLayout(g1_panelStatus);
	    

    }


    public void mouseDragged(MouseEvent arg0){
        mousePos = panel.getMousePosition();
        labelPosX.setText(String.valueOf(mousePos.x));
        labelPosY.setText(String.valueOf(mousePos.y));
		

        //se forma == 1 desenha o ponto

        if(forma == 1) {
			x1 = mousePos.x;
			y1 = mousePos.y;
			g.drawLine(x1,y1+60,x2,y2+60);
			x2=x1;
			y2=y1;
		}
    }

    public void mouseMoved(MouseEvent arg0){
        if(arg0.getSource() == panel){
            do_panel_mouseMoved(arg0);
        }
    }

    protected void do_panel_mouseMoved(MouseEvent arg0){
        //print da posicao X e Y na tela

        mousePos = panel.getMousePosition();
        labelPosX.setText(String.valueOf(mousePos.x));
        labelPosY.setText(String.valueOf(mousePos.y));
    }

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

    protected void do_buttonCor_actionPerfomed(ActionEvent arg0){
        Cores = new JColorChooser();
        corE = Cores.showDialog(null,"Escolha a cor", Color.BLACK);
        buttonCor.setBackground(corE);
    }

    protected void do_buttonPonto_actionPerfomed(ActionEvent arg0){
        forma = 1;
    }
    
    protected void do_buttonRetangulo_actionPerfomed(ActionEvent arg0){
        forma = 2;
    }

    protected void do_buttonCirculo_actionPerfomed(ActionEvent arg0){
        forma = 3;
    }

    public void mouseClicked(MouseEvent arg0){

    }
    public void mouseEntered(MouseEvent arg0){

    }
    public void mouseExited(MouseEvent arg0){

    }

    public void mousePressed(MouseEvent arg0){
        mousePos = panel.getMousePosition();
		int x1 = mousePos.x;
		int	y1 = mousePos.y;

        //se forma == 1 desenha o ponto

        if(forma == 1){
		    ponto(x1,y1);
        }
	    x2=x1;
        y2=y1;
    }
    public void ponto(int x,int y){
		setupDesenho();
		g.drawLine(x,y+60,x,y+60);
    }
	private void setupDesenho(){
		g = getGraphics();
    }
    


    public void mouseReleased(MouseEvent arg0){

    }

    public void do_panel_mousePressed(MouseEvent arg0){
        mousePressed = panel.getMousePosition();
    }


}
