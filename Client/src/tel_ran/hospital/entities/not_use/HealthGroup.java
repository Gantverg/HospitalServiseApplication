package tel_ran.hospital.entities.not_use;

public class HealthGroup {
	String name;
	int minNormalPulse;
	int maxNormalPulse;
	int serveyPeriod;
	public String getName() {
		return name;
	}
	public int getMinNormalPulse() {
		return minNormalPulse;
	}
	public int getMaxNormalPulse() {
		return maxNormalPulse;
	}
	public int getServeyPeriod() {
		return serveyPeriod;
	}
	public HealthGroup(String name, int minNormalPulse, int maxNormalPulse, int serveyPeriod) {
		super();
		this.name = name;
		this.minNormalPulse = minNormalPulse;
		this.maxNormalPulse = maxNormalPulse;
		this.serveyPeriod = serveyPeriod;
	}
	

}
