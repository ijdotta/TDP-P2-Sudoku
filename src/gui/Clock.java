package gui;

import java.util.TimerTask;
import java.util.Timer;

public class Clock {
	
	private Timer timer;
	private TimerTask task;
	private int ms_period;
	private int hh, mm, ss;
	
	public Clock() {
		
		hh = mm = ss = 0;
		ms_period = 1000;
		
		task = new TimerTask() {
			
			@Override
			public void run() {
				
				ss++;
				if (ss == 60) {
					ss = 0;
					mm++;
					
					if (mm == 60) {
						mm = 0;
						hh++;
					}
				}
			}
		};
		
		timer = new Timer();
		timer.schedule(task, 10, ms_period);
		
	}
	
	/**
	 * Retorna la tiempo transcurrido desde la inicialización.
	 * @return tiempo en formato "hh:mm:ss"
	 */
	public String getTime() {
		return String.format("%02d:%02d:%02d", hh, mm, ss);
	}
	

}
