import java.util.ArrayList;
import java.lang.Math;

public class Bezier {
    
    public static Bezier bezier;
    public Ponto p0,p0d,p3d,p3;

    public Bezier(Ponto p0, Ponto p3){
        this.p0 = p0;
        this.p3 = p3;
        
        p0d = new Ponto(p0.x, p0.y);
        p3d = new Ponto(p3.x, p3.y);
    }

    public void setP0d(Ponto p){
        p0d = p;
    }

    public void setP3d(Ponto p){
        p3d = p;
    }

}
