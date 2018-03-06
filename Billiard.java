import java.awt.Graphics;
import java.awt.Color;

public class Billiard
{
    public static Table parent;
    private double previousX, previousY;
    private double x,   y;
    public double Vx, Vy;
    private double radius;

    public double getSpeed(){
        return Vx*Vx + Vy*Vy;
    }

    public double getRadius(){ return radius;}
    public double getX(){ return x;}
    public double getY(){ return y;}

    public Billiard(double x, double y)
    {
      radius = 10;
      this.x = x;
      this.y = y;
      Vx = 0;
      Vy = 0;
    }

    public void paint(Graphics g)
    {
       g.setColor(Color.black);
       int diameter = (int)(radius*2);
       g.fillOval((int)x,(int)y,diameter,diameter);
    }


    public void update()
    {
         previousX = x; previousY = y;
         Vy *= 0.998;
         Vx *= 0.998;
         if(getSpeed() < 0.05){
             Vx = 0;
             Vy = 0;
         }
         if(y>=460 && Vy>=0) Vy*=-0.75;
         if(y<=120 && Vy<=0) Vy*=-0.75;
         if(x>=832 && Vx>=0) Vx*=-0.75;
         if(x<=110 && Vx<=0) Vx*=-0.75;
         x += Vx;
         y += Vy;

    }

    public boolean hitTest(Billiard other){
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx*dx+dy*dy) < 2*radius;
    }

    public void collides(Billiard other){
        if(hitTest(other)){
            // update the velocity of this and other.
            x = previousX;
            y = previousY;
            other.x = other.previousX;
            other.y = other.previousY;
            double dx = other.x - x;
            double dy = other.y - y;
            double dist = Math.sqrt(dx*dx+dy*dy);
            if(dist == 0){
                dx = 0;
                dy = 0;
            }else{
                dx /= dist;
                dy /= dist;
            }
            double scale = (dx*Vx+dy*Vy)-(dx*other.Vx+dy*other.Vy);
            Vx -= dx*scale;
            Vy -= dy*scale;
            other.Vx += dx*scale;
            other.Vy += dy*scale;
        }
    }
}
