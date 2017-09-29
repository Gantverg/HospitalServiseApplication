package tel_ran.hsa.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.*;

public class StartFinishDate {
	@JsonSerialize (using = LocalDateSerializer.class)
	@JsonDeserialize(using =  LocalDateDeserializer.class)
	private LocalDate startDate;
	@JsonSerialize (using = LocalDateSerializer.class)
	@JsonDeserialize(using =  LocalDateDeserializer.class)
	private LocalDate finishDate; 
	
	@JsonCreator
	public StartFinishDate(@JsonProperty("startDate") LocalDate startDate,
			@JsonProperty("finishDate") LocalDate finishDate) {
		this.startDate = startDate;
		this.finishDate = finishDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getFinishDate() {
		return finishDate;
	}
	
	
}
