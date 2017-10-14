package tel_ran.hsa.entities.util;

import tel_ran.hsa.entities.dto.*;

public class HeartbeatDiagramData {
	Iterable<HeartBeat> heartBeats;
	Patient patient;
	
	public HeartbeatDiagramData() {
	}

	public Iterable<HeartBeat> getHeartBeats() {
		return heartBeats;
	}

	public void setHeartBeats(Iterable<HeartBeat> heartBeats) {
		this.heartBeats = heartBeats;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	
}
