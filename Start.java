import java.awt.*;
import javax.swing.*;

public class Start
{
  public static void main(String[] args)
  {
    JFrame frame = new JFrame("Paint Calafrio");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Paint  paint = new Paint();
    frame.add( paint );

    frame.pack();
    frame.setVisible( true );
  }
}
