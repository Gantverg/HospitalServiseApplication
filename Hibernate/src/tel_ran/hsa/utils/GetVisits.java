package tel_ran.hsa.utils;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tel_ran.jackson.*;

public class GetVisits {
	private int id;
	@JsonSerialize (using = LocalDateSerializer.class)
	@JsonDeserialize(using =  LocalDateDeserializer.class)
	private LocalDate startDate;
	@JsonSerialize (using = LocalDateSerializer.class)
	@JsonDeserialize(using =  LocalDateDeserializer.class)
	private LocalDate finishDate; 
	
	@JsonCreator
	public GetVisits(@JsonProperty("id") int id,
			@JsonProperty("startDate") LocalDate startDate,
			@JsonProperty("finishDate") LocalDate finishDate) {
		this.id=id;
		this.startDate = startDate;
		this.finishDate = finishDate;
	}

	public int getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getFinishDate() {
		return finishDate;
	}
	
	
}
