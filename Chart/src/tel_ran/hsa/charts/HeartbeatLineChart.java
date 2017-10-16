package tel_ran.hsa.charts;

import java.io.File;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Application;
import javafx.stage.Stage;
import tel_ran.hsa.entities.dto.HeartBeat;
import tel_ran.hsa.entities.util.HeartbeatDiagramData;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class HeartbeatLineChart extends Application {
	private static HeartbeatDiagramData heartBeatsDiagram;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			final LineChart<String, Number> lineChart = getLineChart();
			Scene scene = new Scene(lineChart, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setHeartBeatsDiagram(HeartbeatDiagramData heartBeatsDiagram) {
		HeartbeatLineChart.heartBeatsDiagram = heartBeatsDiagram;
	}

	private LineChart<String, Number> getLineChart() {
		// defining the axes
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("periods of time");
		yAxis.setLabel("heart beating");
		// creating the chart
		final LineChart<String, Number> lineChart = new LineChart(xAxis, yAxis);

		lineChart.setTitle("Heartbeat data of "+heartBeatsDiagram.getPatient().getName());
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("Heartbeating");
		
		for (HeartBeat heartBeat : heartBeatsDiagram.getHeartBeats()) {
			series.getData().add(new XYChart.Data<String, Integer>(heartBeat.getDateTime().toString(), heartBeat.getValue()));
		}
		
		lineChart.getData().add(series);
		return lineChart;
	}

	public static void main(String[] args) {
		if(args.length==0)
			return;
		try {
			File file = new File(args[0]);
			ObjectMapper mapper = new ObjectMapper();
			heartBeatsDiagram = mapper.readValue(file, new TypeReference<HeartbeatDiagramData>() {});
			if(heartBeatsDiagram==null)
				return;
			launch(args);
		} catch (Exception e) {
		}
	}
}
