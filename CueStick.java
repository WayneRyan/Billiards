import java.awt.Graphics;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;


public class CueStick
{
    private Image cue;
    public CueStick()
    {
		try {
			cue = ImageIO.read(getClass().getResource("/resources/CueStick.png"));
		} catch (IOException e) {}
    }

    public void paint(Graphics g ,ImageObserver observer)
    {
        g.drawImage(cue,0,0, observer);
    }
}
