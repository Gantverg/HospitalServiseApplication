package tel_ran.hsa.bigdata.dto;

import tel_ran.hsa.entities.dto.*;

public class HeartInfo {
	int average;
	int count;
	Patient patient;

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Patient getPatient() {
		return patient;
	}

	public HeartInfo(int average, int count, Patient patient) {
		super();
		this.average = average;
		this.count = count;
		this.patient = patient;
	}

	public HeartInfo() {
		super();
	}

}
