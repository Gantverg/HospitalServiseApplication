package tel_ran.hsa.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.messaging.handler.annotation.SendTo;

import com.google.gson.Gson;

import tel_ran.hsa.dto.*;

@EnableBinding(Processor.class)
public class SensorDataGenerator {
	LocalDateTime timeStart;
	Map<Integer, Info> map = new HashMap<>();
	
	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	String sendSensorData(int idPatient)
	{
		//ObjectMapper mapper = new ObjectMapper();
		//Integer idPatient = mapper.readValue(id, Integer.class);
		//int idPatient = Integer.parseInt(id);
		timeStart = LocalDateTime.now();
		updateMap(idPatient);
		
		return checkPulse(map.get(idPatient), idPatient);
	}

	private void updateMap(int idPatient) {
		Info info=map.get(idPatient);
		if(info!=null) {
			info.setCount(info.getCount()+1);
			map.replace(idPatient, info);
		}else {
			map.put(idPatient, new Info(1, timeStart.toString()));
		}
	}

	private String checkPulse(Info info, int idPatient) {
		if(ChronoUnit.SECONDS.between(LocalDateTime.parse(info.getTime()), timeStart)>=60) {
			String res = getJson(info, idPatient);
			map.remove(idPatient);
		return res;}
		else return null;
	}

	private String getJson(Info info, int idPatient) {
		Gson gson = new Gson();
		InputInfo inf = new InputInfo(idPatient, info.getCount(), info.getTime());
		map.remove(idPatient);
		return gson.toJson(inf);
	}

}
