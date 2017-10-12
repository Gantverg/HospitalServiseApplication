package tel_ran.hsa.bigdata.dto;

public class InputInfo {
	int idPatient;
	int pulse;
	public InputInfo(int idPatient, int pulse) {
		super();
		this.idPatient = idPatient;
		this.pulse = pulse;
	}
	public InputInfo() {
		super();
	}
	public int getIdPatient() {
		return idPatient;
	}
	public int getPulse() {
		return pulse;
	}

}
