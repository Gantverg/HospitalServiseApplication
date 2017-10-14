package tel_ran.hsa.entities.jfx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import tel_ran.hsa.entities.dto.HealthGroup;

public class HealthGroupJfx {
	IntegerProperty groupId;
	StringProperty groupName;
	IntegerProperty minNormalPulse;
	IntegerProperty maxNormalPulse;
	IntegerProperty surveyPeriod;

	public HealthGroupJfx() {
	}

	public HealthGroupJfx(int groupId, String groupName, int minNormalPulse, int maxNormalPulse, int surveyPeriod) {
		super();
		this.groupId.set(groupId);
		this.groupName.set(groupName);
		this.minNormalPulse.set(minNormalPulse);
		this.maxNormalPulse.set(maxNormalPulse);
		this.surveyPeriod.set(surveyPeriod);
	}
	
	public HealthGroupJfx(HealthGroup healthGroup) {
		this(healthGroup.getGroupId(), healthGroup.getGroupName(), 
				healthGroup.getMinNormalPulse(), healthGroup.getMaxNormalPulse(), 
				healthGroup.getSurveyPeriod());
	}
	
	public HealthGroup get() {
		return new HealthGroup(groupId.get(), groupName.get(), minNormalPulse.get(), maxNormalPulse.get(), surveyPeriod.get());
	}

	public int getGroupId() {
		return groupId.get();
	}

	public String getGroupName() {
		return groupName.get();
	}

	public int getMinNormalPulse() {
		return minNormalPulse.get();
	}

	public int getMaxNormalPulse() {
		return maxNormalPulse.get();
	}

	public int getSurveyPeriod() {
		return surveyPeriod.get();
	}

	public void setGroupName(String groupName) {
		this.groupName.set(groupName);
	}

	public void setMinNormalPulse(int minNormalPulse) {
		this.minNormalPulse.set(minNormalPulse);
	}

	public void setMaxNormalPulse(int maxNormalPulse) {
		this.maxNormalPulse.set(maxNormalPulse);
	}

	public void setSurveyPeriod(int surveyPeriod) {
		this.surveyPeriod.set(surveyPeriod);
	}
	
	public IntegerProperty minNormalPulseProperty() {
		return minNormalPulse;
	}

	public IntegerProperty maxNormalPulseProperty() {
		return maxNormalPulse;
	}
	
	public IntegerProperty surveyPeriodProperty() {
		return surveyPeriod;
	}
	
	public StringProperty groupNameProperty() {
		return groupName;
	}

	@Override
	public String toString() {
		return "HealthGroup [name=" + groupName.get() + ", minNormalPulse=" + minNormalPulse.get() + ", maxNormalPulse="
				+ maxNormalPulse.get() + ", surveyPeriod(minutes)=" + surveyPeriod.get() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groupId.get();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HealthGroupJfx other = (HealthGroupJfx) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (groupId.get() != other.groupId.get())
			return false;
		return true;
	}

}
