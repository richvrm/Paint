import java.awt.Color;
import java.util.ArrayList;

public class Retangulo {
	public Ponto p1;
   public Ponto p2;
   public Color cor;
   public static ArrayList<Retangulo> lista = new ArrayList<Retangulo>();
	public Retangulo() {
		this.p1 = new Ponto();
      this.p2 = new Ponto();
      this.cor = Color.BLACK;
	}
	public Retangulo(Ponto p1, Ponto p2, Color cor) {
		this.p1 = p1;
      this.p2 = p2;
      this.cor = cor;
      this.lista.add(this);
	}
}