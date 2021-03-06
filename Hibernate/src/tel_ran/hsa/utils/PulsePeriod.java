package tel_ran.hsa.utils;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.*;

public class PulsePeriod {
	private int patientId;
	@JsonSerialize (using = LocalDateSerializer.class)
	@JsonDeserialize(using =  LocalDateDeserializer.class)
	private LocalDate startDate;
	@JsonSerialize (using = LocalDateSerializer.class)
	@JsonDeserialize(using =  LocalDateDeserializer.class)
	private LocalDate finishDate;
	private int surveyPeriod;
	
	@JsonCreator
	public PulsePeriod(@JsonProperty("patientId") int patientId,
			@JsonProperty("startDate") LocalDate startDate,
			@JsonProperty("finishDate") LocalDate finishDate,
			@JsonProperty("surveyPeriod") int surveyPeriod) {
		this.patientId=patientId;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.surveyPeriod=surveyPeriod;
	}

	public int getPatientId() {
		return patientId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getFinishDate() {
		return finishDate;
	}

	public int getSurveyPeriod() {
		return surveyPeriod;
	}
	
	
}
