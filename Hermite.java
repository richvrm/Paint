import java.util.ArrayList;
import java.lang.Math;

public class Hermite {

    public static Hermite hermite;
    public Ponto p0,p0d,p3d,p3;
    public int umQuartox, umQuartoy;
    public int tresQuartox, tresQuartoy ;

    public Hermite(Ponto p0, Ponto p3){
        this.p0 = p0;
        this.p3 = p3;
        
        umQuartox = (p3.x-p0.x)/4;
        umQuartoy = (p3.y-p0.y)/4;
        tresQuartox = (p3.x-p0.x)-umQuartox;
        tresQuartoy = (p3.y-p0.y)-umQuartoy;
        p0d = new Ponto(umQuartox,umQuartoy);
        p3d = new Ponto(tresQuartox,tresQuartoy);

        //p0d = new Ponto(p0.x,p0.y);
        //p3d = new Ponto(p3.x,p3.y);
    }

    public void setP0d(Ponto p){
        p0d = p;
    }

    public void setP3d(Ponto p){
        p3d = p;
    }

}
