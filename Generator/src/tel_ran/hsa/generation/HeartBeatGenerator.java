package tel_ran.hsa.generation;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.messaging.Source;
//import org.springframework.integration.annotation.InboundChannelAdapter;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.generation.controller.HeartBeatController;

//@EnableBinding(Source.class)
public class HeartBeatGenerator extends Thread {
	public HeartBeatGenerator() {
	}
	/* @InboundChannelAdapter(Source.OUTPUT) String sendSensorData() throws
	 * JsonProcessingException{ Sensor sensor = getRandomSensorData(); ObjectMapper
	 * mapper = new ObjectMapper(); String jsonRes =
	 * mapper.writeValueAsString(sensor); return jsonRes; }
	 */
	SecureRandom sec = new SecureRandom();
	public void run() {
		while (true) {
		  //Integer index = sec.nextInt(HeartBeatController.maxPatientId);
			Integer index = ThreadLocalRandom.current().nextInt(HeartBeatController.maxPatientId);
			HeartBeatController.currentBeats[index] = HeartBeatController.currentBeats[index]+sec.nextInt(HeartBeatController.entropyRND);
		if (HeartBeatController.currentBeats[index] > HeartBeatController.maxBeatRate) HeartBeatController.currentBeats[index] = HeartBeatController.maxBeatRate;
	    try { Thread.sleep(HeartBeatController.sleepPeriod); } catch (InterruptedException e) { e.printStackTrace(); }
	    	// send beat out from here
		}
	}
}
