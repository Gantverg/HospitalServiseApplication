package tel_ran.hsa.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;

import org.springframework.messaging.handler.annotation.SendTo;

import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.bigdata.dto.HeartBeatData;
import tel_ran.hsa.bigdata.dto.HeartInfo;
import tel_ran.hsa.bigdata.dto.InputInfo;
import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.model.SqlModel;

@EnableBinding(Processor.class)
public class MapReducerBeats {
	// You need to think about creation map with list, cause i have a problem
	// with simple method put in a map
	Map<Integer, HeartInfo> map = new HashMap<>();
	private InputInfo inp;
	SqlModel sql;

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public HeartBeatData takePulse(String heartBeatInput) {
		if(sql==null) {
			sql = new SqlModel();
		}
		System.err.println("Received information");
		ObjectMapper mapper = new ObjectMapper();

		try {
			inp = mapper.readValue(heartBeatInput, InputInfo.class);
			System.err.println(inp);
			// map reducing will be here
			return sendAlert(mapReducing(), inp.getPulse());
		} catch (Exception e) {
		}
		return null;

	}

	private HeartInfo mapReducing() {
		System.err.println("StartReducing");
		HeartInfo info;
		if (map.get(inp.getIdPatient()) == null) {
			System.err.println("Creating new info");
			info = new HeartInfo(inp.getPulse(), 1, sql.getPatientById(inp.getIdPatient()));
			map.put(inp.getIdPatient(), info);
		} else {
			System.err.println("Getting info from map");
			info = map.get(inp.getIdPatient());
			System.err.println("updating map");
			info.setAverage(info.getAverage() + inp.getPulse() / (info.getCount() + 1));
			map.replace(inp.getIdPatient(), info);
		}
		if (info.getCount() >= info.getPatient().getHealthGroup().getSurveyPeriod()) {
			Patient patient = info.getPatient();
			HeartBeat heartBeat = new HeartBeat(patient.getId(), 
												inp.getTime(), 
												info.getAverage(),
												patient.getHealthGroup().getSurveyPeriod());
			sql.putHeartBeatToBase(heartBeat);
		}
		System.err.println("Ending of Reducing");
		return info;
	}

	private HeartBeatData sendAlert(HeartInfo info, int pulse) {
		HealthGroup health = info.getPatient().getHealthGroup();
		if (pulse > health.getMaxNormalPulse() || pulse < health.getMinNormalPulse()) {
			System.err.println("Sending to Base");
			return new HeartBeatData(info.getPatient(), LocalDateTime.parse(inp.getTime()), inp.getPulse(), info.getPatient().getHealthGroup().getSurveyPeriod());
		}
		System.err.println("Pulse is normal");
		return null;
	}

}
