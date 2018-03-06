import java.util.TimerTask;
import java.awt.*;

public class Task extends TimerTask
{
	Table parent;
	Graphics g;

	public Task(Table myParent){
		parent = myParent;
	}

	public void run(){
		parent.update();
	}
}
