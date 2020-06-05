import java.util.ArrayList;
import java.lang.Math;

public class Hermite {

    public Ponto p0,p0d,p3d,p3;

    Hermite(Ponto p0, Ponto p3){
        this.p0 = p0;
        this.p3 = p3;
        
        p0d = new Ponto(p0.x, p0.y+2);
        p3d = new Ponto(p3.x, p3.y-3);
    }


}
