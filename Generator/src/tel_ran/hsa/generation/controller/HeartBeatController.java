package tel_ran.hsa.generation.controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tel_ran.hsa.generation.*;

@SpringBootApplication(scanBasePackages="tel_ran.hsa.generation")
public class HeartBeatController {
	public static int   maxPatientId = 1000; //number of patients
	public static long  beatPeriod = 60; //period for counting beats in seconds
	public static int[] currentBeats = new int[maxPatientId]; //array for storing beats based on beatPeriod time
	public static int   maxBeatRate = 250; //maximum stored beat rate value
	public static int   sleepPeriod = 6; //time for thread sleep (for better beats assignment)
	public static int   entropyRND = 10; //entropy value (for better beats assignment)
	
	public static void main(String[] args) {
			HeartBeatCleaner hbc = new HeartBeatCleaner();
			hbc.start();
			SpringApplication.run(HeartBeatController.class, args);
		
		 
		
	}
}
