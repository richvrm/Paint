import java.awt.Color;
import java.util.ArrayList;

public class Retangulo {
	public Ponto p1;
	public Ponto p2;
	public Ponto p3;
	public Ponto p4;
	public Color cor;
	public static ArrayList<Retangulo> lista = new ArrayList<Retangulo>();
	public Retangulo() {
		this.p1 = new Ponto();
		this.p2 = new Ponto();
		this.p3 = new Ponto();
		this.p4 = new Ponto();
		this.cor = Color.BLACK;
	}
	public Retangulo(Ponto p1, Ponto p2, Color cor) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = new Ponto(p1.x, p2.y);
		this.p4 = new Ponto(p2.x, p1.y);
		this.cor = cor;
		this.lista.add(this);
	}
}
