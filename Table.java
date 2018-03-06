import java.util.ArrayList;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.io.IOException;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.*;



public class Table extends JFrame implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;


	public static void main(String [] args){
		Table t = new Table();
		t.setSize(950,600);
		t.init();
        Timer myTimer = new Timer();
        Task myTask = new Task(t);
        myTimer.schedule(myTask,30,5);
		t.setVisible(true);
	}

    private ArrayList<Billiard> myBilliards;
    private ArrayList<pocket> myPockets;
    public qBall myCueBall;
    private CueStick myCueStick;

    private Image img;
    public MouseEvent press, release;
    public Graphics bufferGraphics;
    public BufferedImage offscreen;
    public Dimension dim;

    private AffineTransform myTransform;
    private AffineTransform myTranslate;
    private AffineTransform myStickOffset;
    private double powerOffset, power, angle;
    private boolean stopped;


    public void init()
    {
    	
        angle = Math.PI / 2.0;
        power = 0.0;
        powerOffset = 0.0;
        stopped = false;
        myBilliards = new ArrayList<Billiard>();
        myPockets = new ArrayList<pocket>();
        myPockets.add( new pocket(101,113));
        myPockets.add( new pocket(470,107));
        myPockets.add( new pocket(841,112));
        myPockets.add( new pocket(101,467));
        myPockets.add( new pocket(471,474));
        myPockets.add( new pocket(841,467));

        myCueBall = new qBall(225,290);
        myCueStick = new CueStick();

        dim = new Dimension(1000,700);

        offscreen = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB );
        bufferGraphics = offscreen.getGraphics();
		Graphics2D g2 = (Graphics2D)bufferGraphics;
	    RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHints(rh);

        try {
            img = ImageIO.read(getClass().getResource("/resources/table.jpg"));
        } catch (IOException e) {
        }

        addMouseListener(this);
        addMouseMotionListener(this);
        myTransform = new AffineTransform();
        myTransform.rotate(angle);
        myTranslate = new AffineTransform();
        myStickOffset = new AffineTransform();
        myStickOffset.setToTranslation(-6,0);

        myBilliards.add(new Billiard(585,290));
        
        myBilliards.add(new Billiard(604,279));
        myBilliards.add(new Billiard(604,301));
        
        myBilliards.add(new Billiard(623,269));
        myBilliards.add(new Billiard(623,290));
        myBilliards.add(new Billiard(623,311));
        
        myBilliards.add(new Billiard(642,258));
        myBilliards.add(new Billiard(642,279));
        myBilliards.add(new Billiard(642,301));
        myBilliards.add(new Billiard(642,322));
        
        myBilliards.add(new Billiard(661,248));
        myBilliards.add(new Billiard(661,269));
        myBilliards.add(new Billiard(661,290));
        myBilliards.add(new Billiard(661,311));
        myBilliards.add(new Billiard(661,332));
        Billiard.parent = this;

    }

    public void update()
    {
    	if(myBilliards == null) return;
        stopped = true;
        for(int i=0 ; i<myBilliards.size() ; i++){
            Billiard b = myBilliards.get(i);
            if(b.getSpeed() > 0.001) stopped = false;
            for(Billiard b2 : myBilliards){
                if(b2 != b){
                    b.collides(b2);
                }
            }
            b.collides(myCueBall);
            for(pocket p : myPockets){
                if(p.collidesWith(b)){
                    myBilliards.remove(b);
                    i--;
                }
            }
        }
        for(Billiard b : myBilliards){
            b.update();
        }
        for(pocket p : myPockets){
            if(p.collidesWith(myCueBall)){
                myCueBall = new qBall(250,275);
            }
        }
        if(myCueBall.getSpeed() > 0.001) stopped = false;

        if(stopped){
           myTranslate.setToTranslation(myCueBall.getX()+10,myCueBall.getY()+10); //code to place the queStick
        }
        myCueBall.update();
        repaint();
    }

     public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)bufferGraphics;
        AffineTransform temp = g2d.getTransform();
        AffineTransform temp2 = new AffineTransform();

        int dx1 = 0;
        int dy1 = 0;
        int dx2 = 960;
        int dy2 = 600;
        int sx1 = 0;
        int sy1 = 0;
        int sx2 = 480;
        int sy2 = 300;

        g2d.clearRect(0,0,dim.width,dim.height);
        g2d.setColor(Color.black);

        g2d.drawImage(img,dx1,dy1,dx2,dy2,sx1,sy1,sx2,sy2, this);
        for(Billiard b : myBilliards) b.paint(g2d);
        myCueBall.paint(g2d);
        
        //for(pocket p : myPockets)p.paint(g2d);

        temp2.concatenate(myTranslate);
        temp2.concatenate(myTransform);
        temp2.concatenate(myStickOffset);
        //g2d.setTransform(myTransform);
        g2d.setTransform(temp2);
        if(stopped)myCueStick.paint(bufferGraphics,this);
        g2d.setTransform(temp);
        
        if(stopped){
        	bufferGraphics.setColor(Color.black);
        	int dx = (int)(1000*Math.cos(angle-Math.PI/2));
        	int dy = (int)(1000*Math.sin(angle-Math.PI/2));
        	int size=10;
        	bufferGraphics.drawLine((int)myCueBall.getX()+size, (int)myCueBall.getY()+size, (int)myCueBall.getX()+dx+size, (int)myCueBall.getY()+dy+size);
        }

        g.drawImage(offscreen,0,0,this);
    }

    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    public void mousePressed(MouseEvent e){
        double dx = e.getX() - myCueBall.getX();
        double dy = e.getY() - myCueBall.getY();
        powerOffset = Math.sqrt(dx*dx+dy*dy);

    }

    public double distance(MouseEvent e, Billiard b){
        int x1 = e.getX();
        int y1 = e.getY();
        double x2 = b.getX();
        double y2 = b.getY();
        double dx = x1-x2;
        double dy = y1-y2;
        return Math.sqrt(dx*dx-dy*dy);
    }

    public void mouseReleased(MouseEvent e){
        if(stopped){
            myCueBall.Vx = power* Math.cos(angle - Math.PI/2)*0.1;
            myCueBall.Vy = power* Math.sin(angle - Math.PI/2)*0.1;
        }
        angle = 0;
        myTransform.setToTranslation(0,0);
    }

    public void mouseDragged(MouseEvent e){
        double dx = e.getX() - myCueBall.getX();
        double dy = e.getY() - myCueBall.getY();
        power = Math.sqrt(dx*dx+dy*dy) - powerOffset;
        angle = Math.atan2(dy,dx);
        myTransform.setToTranslation(power*Math.cos(angle),power*Math.sin(angle));
        angle -= Math.PI/2;
        myTransform.rotate(angle);
    }
    public void mouseMoved(MouseEvent e){}

    public void keyPressed(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}




}
