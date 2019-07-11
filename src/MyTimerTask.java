
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
	private int tickNumber;
	
    private NetClient client = null;
    
    public MyTimerTask() {
    	tickNumber=0;
    	new NetServer().launch();
    	client = new NetClient();
    }
   
	@Override
    public void run() {
       
        tickNumber = (tickNumber)%3;
        String timermsg = "Timer Value: " +(tickNumber+1);
        System.out.println(timermsg);
        if (client!=null) {
        	String resp;
			try {
				resp = client.send(timermsg);
				System.out.println(resp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        
        if (tickNumber==0) {
        	Stage1();
        	System.out.println("   Stage 1 ");
        	
        }
        
        if (tickNumber==1) {
        	Stage2();
        	System.out.println("   Stage 2 ");
        }
        
        if (tickNumber==2) {
        	Stage3();
        	System.out.println("   Stage 3 ");
        	
        }
        
        tickNumber++;
    }

    
    private void Stage3() {
		// TODO Auto-generated method stub
		
	}
	private void Stage2() {
		// TODO Auto-generated method stub
		
	}
	private void Stage1() {
		// TODO Auto-generated method stub
		
	}
}