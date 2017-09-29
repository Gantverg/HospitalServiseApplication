package tel_ran.hsa.generation;

import java.util.*;
import tel_ran.hsa.generation.controller.HeartBeatController;

public class HeartBeatCleaner extends Thread {
	Timer minTimer = new Timer();

	public HeartBeatCleaner() {}

	public void run() {
		minTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println(Arrays.toString(HeartBeatController.currentBeats));
				Arrays.fill(HeartBeatController.currentBeats, 0);
			}
		}, HeartBeatController.beatPeriod * 1000, HeartBeatController.beatPeriod * 1000);
	}
}
