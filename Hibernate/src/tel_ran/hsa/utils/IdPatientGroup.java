package tel_ran.hsa.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IdPatientGroup {
	private int patientId;
	private int groupId;
	
	public IdPatientGroup() {}
	
	@JsonCreator
	public IdPatientGroup(@JsonProperty("patientId") int patientId,
							@JsonProperty("groupId") int groupId) {
		this.patientId = patientId;
		this.groupId = groupId;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getGroupId() {
		return groupId;
	}
	
}
