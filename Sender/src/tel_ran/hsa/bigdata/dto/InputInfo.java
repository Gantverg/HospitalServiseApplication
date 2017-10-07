package tel_ran.hsa.bigdata.dto;

public class InputInfo {
	int idPatient;
	int pulse;
	String time;

	public int getIdPatient() {
		return idPatient;
	}

	public int getPulse() {
		return pulse;
	}

	public InputInfo() {
		super();
	}

	public InputInfo(int idPatient, int pulse, String time) {
		super();
		this.idPatient = idPatient;
		this.pulse = pulse;
		this.time = time;
	}

	public String getTime() {
		return time;
	}
}
