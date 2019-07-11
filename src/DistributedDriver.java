
import java.util.Timer;

//without orphan nodes
public class DistributedDriver
{

	public static int portNo = 6656;
	public static void main(String args[]) {
		
		MyTimerTask stagesExecutor = new MyTimerTask();
        new Timer(true).scheduleAtFixedRate(stagesExecutor, 0, 1000);
        System.out.println("TimerTask started");
        
        for (;;) {} //infinite loop to keep the timer and socket observer active and listening
	}
}
