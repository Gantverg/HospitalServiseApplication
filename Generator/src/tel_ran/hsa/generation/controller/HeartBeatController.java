package tel_ran.hsa.generation.controller;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import tel_ran.hsa.generation.*;

//@SpringBootApplication
public class HeartBeatController {
	public static int   maxPatientId = 1000; //number of patients
	public static long  beatPeriod = 60; //period for counting beats in seconds
	public static int[] currentBeats = new int[maxPatientId]; //array for storing beats based on beatPeriod time
	public static int   maxBeatRate = 250; //maximum stored beat rate value
	public static int   sleepPeriod = 6; //time for thread sleep (for better beats assignment)
	public static int   entropyRND = 30; //entropy value (for better beats assignment)
	
	public static void main(String[] args) {
		// SpringApplication.run(HeartBeatController.class, args );
		try {
			HeartBeatCleaner hbc = new HeartBeatCleaner();
			HeartBeatGenerator hbg = new HeartBeatGenerator();
			hbc.start();
			hbg.start();
		} catch (Exception e) {}
	}
}
