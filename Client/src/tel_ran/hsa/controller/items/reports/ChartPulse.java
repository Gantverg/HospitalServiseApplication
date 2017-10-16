package tel_ran.hsa.controller.items.reports;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

import tel_ran.hsa.controller.items.HospitalItem;
import tel_ran.hsa.controller.utils.HeartbeatLineChart;
import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.entities.util.HeartbeatDiagramData;

public class ChartPulse extends HospitalItem {

	@Override
	public String displayedName() {
		return "Show pulse histogram";
	}

	@Override
	public void perform() {
		Integer patientId = inputOutput.getInteger("Enter patient id");
		Patient patient = hospital.getPatient(patientId);
		if (patient == null) {
			inputOutput.put(String.format("Patient with id %s doesn`t exist"));
			return;
		}
		LocalDate beginDate = inputOutput.getDate("Enter begin date in the format " + format, format);
		LocalDate endDate = inputOutput.getDate("Enter end date in the format " + format, format);
		Iterable<HeartBeat> res = hospital.getPulse(patientId, beginDate, endDate);

		if (res == null) {
			inputOutput.put(String.format("Patient with id %d have no pulse information from %s until %s", patientId,
					beginDate.toString(), endDate.toString()));
			return;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		HeartbeatDiagramData diagramData = new HeartbeatDiagramData();
		diagramData.setHeartBeats(res);
		diagramData.setPatient(patient);

		String fileName = getTempFileName();
		try (PrintStream stream = new PrintStream(fileName)){
			stream.println(mapper.writeValueAsString(diagramData));
		} catch (Exception e) {
			inputOutput.put("Unexpected error show chart");
			return;
		}
		String[] args = {fileName};
		
		  try {
		        //Загружаем класс с именем className
		        Class<?> targetClass = Class.forName("tel_ran.hsa.controller.utils.HeartbeatLineChart");

		        //Находим метод с именем "main" и списком параметров String[]
		        Class[] argTypes = new Class[]{String[].class};
		        Method mainMethod = targetClass.getDeclaredMethod("main", argTypes);

		        //В качестве параметра передаем пустой массив
		        Object[] arguments = new Object[]{args};
		        mainMethod.invoke(null, arguments);
		    } catch (ClassNotFoundException ex) {
		        ex.printStackTrace();
		    } catch (NoSuchMethodException ex) {
		    	ex.printStackTrace();
		    } catch (InvocationTargetException ex) {
		    	ex.printStackTrace();
		    } catch (IllegalAccessException ex) {
		    	ex.printStackTrace();
		    }	
		
/*		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "c:/tmp/chart.jar", fileName);
		try {
			Process p = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
*/		
//		HeartbeatLineChart lineChart = new HeartbeatLineChart();
//		lineChart.main(fileName);
//		lineChart.launch(fileName);
		//inputOutput.put(res);
	}

	private String getTempFileName() {
	    String prefix = "chart";
	    String suffix = ".tmp";
	    
	    // this temporary file remains after the jvm exits
	    File tempFile;
		try {
			tempFile = File.createTempFile(prefix, suffix);
			return tempFile.getAbsolutePath();
		} catch (IOException e) {
			return null;
		}
	}

}
