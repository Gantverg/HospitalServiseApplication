package tel_ran.hospital.entities;

public class HealthGroup {
	int id;
	String groupName;
	int minNormalPulse;
	int maxNormalPulse;
	int surveyPeriod;
	
	public HealthGroup(int id, String groupName, int minNormalPulse, int maxNormalPulse, int surveyPeriod) {
		super();
		this.id = id;
		this.groupName = groupName;
		this.minNormalPulse = minNormalPulse;
		this.maxNormalPulse = maxNormalPulse;
		this.surveyPeriod = surveyPeriod;
	}
	
	public int getId() {
		return id;
	}
	public String getGroupName() {
		return groupName;
	}
	public int getMinNormalPulse() {
		return minNormalPulse;
	}
	public int getMaxNormalPulse() {
		return maxNormalPulse;
	}
	public int getSurveyPeriod() {
		return surveyPeriod;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setMinNormalPulse(int minNormalPulse) {
		this.minNormalPulse = minNormalPulse;
	}
	public void setMaxNormalPulse(int maxNormalPulse) {
		this.maxNormalPulse = maxNormalPulse;
	}
	public void setSurveyPeriod(int surveyPeriod) {
		this.surveyPeriod = surveyPeriod;
	}
	
	@Override
	public String toString() {
		return "HealthGroup [name=" + groupName + ", minNormalPulse=" + minNormalPulse + ", maxNormalPulse=" + maxNormalPulse
				+ ", surveyPeriod(minutes)=" + surveyPeriod + "]";
	}
	

}
