package tel_ran.hsa.controller.items.visiting;

import java.time.LocalDateTime;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class ReplaceVisitsDoctor extends HospitalItem {
	@Override
	public String displayedName() {
		return "Replace visit by doctor";
	}

	@Override
	public void perform() {
		
		Integer oldDoctorId=inputOutput.getInteger("Enter id doctor which must be replaced ");
		Integer hoursStart=inputOutput.getInteger("Enter hour of beginning range for replace");
		Integer minutesStart=inputOutput.getInteger("Enter minutes of beginning range for replace");
		LocalDateTime beginDateTime=inputOutput.getDate
				("Enter  date of beginning range for replace "+format, format).atTime(hoursStart, minutesStart);
		Integer hoursEnd=inputOutput.getInteger("Enter hour of ending range for replace");
		Integer minutesEnd=inputOutput.getInteger("Enter minutes of ending range for replace");
		LocalDateTime endDateTime=inputOutput.getDate
				("Enter date of ending range for replace "+format, format).atTime(hoursEnd, minutesEnd);
		
		String res=hospital.replaceVisitsDoctor(oldDoctorId,beginDateTime, endDateTime);
		if (res.equals(RestResponseCode.NO_DOCTOR))
		{
			inputOutput.put(String.format("Doctor with id %d or doctor with id %d don`t exist", oldDoctorId));
			return;
		}
		if (res.equals(RestResponseCode.OK)) {
		inputOutput.put(String.format("Doctor with id %d  was replaced in time range %s - %s",
		oldDoctorId,beginDateTime,endDateTime));
		}
	}

}
