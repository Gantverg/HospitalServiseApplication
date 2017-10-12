package tel_ran.hsa.generation;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.stereotype.Component;

import tel_ran.hsa.generation.controller.HeartBeatController;

@EnableBinding(Source.class)
@Component
public class HeartBeatGenerator {
	public HeartBeatGenerator() {
	}

	/*
	 * @InboundChannelAdapter(Source.OUTPUT) String sendSensorData() throws
	 * JsonProcessingException{ Sensor sensor = getRandomSensorData(); ObjectMapper
	 * mapper = new ObjectMapper(); String jsonRes =
	 * mapper.writeValueAsString(sensor); return jsonRes; }
	 */
	SecureRandom sec = new SecureRandom();

	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "1", maxMessagesPerPoll = "4000"))
	public int sendData() {
		//System.out.println("send data");
		return getData();
	}

	public int getData() {
		while(true) {
			Integer index = ThreadLocalRandom.current().nextInt(HeartBeatController.maxPatientId);
			HeartBeatController.currentBeats[index] = HeartBeatController.currentBeats[index]
					+ sec.nextInt(HeartBeatController.entropyRND);
			if (HeartBeatController.currentBeats[index] > HeartBeatController.maxBeatRate) {
				HeartBeatController.currentBeats[index] = HeartBeatController.maxBeatRate;
			}
			else {
				return index;
			}
		}
	}
}
