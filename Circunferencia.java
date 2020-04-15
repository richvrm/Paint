import java.awt.Color;
import java.util.ArrayList;

public class Circunferencia {
	public Ponto centro;
   public int raio;
   public Color cor;
   public static ArrayList<Circunferencia> lista = new ArrayList<Circunferencia>();
	public Circunferencia() {
		this.centro = new Ponto();
      this.raio = 0;
      this.cor = Color.BLACK;
	}
	public Circunferencia(Ponto centro, int raio, Color cor) {
		this.centro = centro;
      this.raio = raio;
      this.cor = cor;
      this.lista.add(this);
	}
}