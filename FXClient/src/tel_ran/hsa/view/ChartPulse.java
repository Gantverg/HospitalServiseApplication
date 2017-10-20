package tel_ran.hsa.view;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tel_ran.hsa.controller.JfxProxy;
import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.entities.jfx.HeartBeatJfx;
import tel_ran.hsa.entities.util.HeartbeatDiagramData;
import tel_ran.hsa.view.Form;

public class ChartPulse extends Form {

	public ChartPulse() {
	}

	public void perform(LocalDate beginDate, LocalDate endDate, int patientId) {
		Patient patient =  jfxProxy.getPatient(patientId).get();
		if (patient == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid id");
            alert.setContentText("There are no patient with id "+patientId);

            alert.showAndWait();
			return;
		}
		Iterable<HeartBeat> res = StreamSupport.stream(jfxProxy.getPulse(patientId, beginDate, endDate).spliterator(), false)
				.map(HeartBeatJfx::get)
				.collect(Collectors.toList());

		if (res == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No data");
            alert.setHeaderText("Please correct input data");
            alert.setContentText("There are no data");

            alert.showAndWait();
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No data");
            alert.setHeaderText("Unexpected error show chart");
            alert.setContentText("");

            alert.showAndWait();
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
