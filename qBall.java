import java.awt.Graphics;
import java.awt.Color;

public class qBall extends Billiard
{
    public qBall(double x, double y)
    {
        super(x,y);
    }

    public void paint(Graphics g)
    {
       g.setColor(Color.white);
       g.fillOval((int)super.getX(),(int)super.getY(),20,20);
    }
}
