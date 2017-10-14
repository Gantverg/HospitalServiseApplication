package tel_ran.hsa.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuItem;
import tel_ran.hsa.application.HSAFxClientAppl;

public class MainWindowController {

	@FXML
	private MenuItem doctorDataMenuItem;
	
	@FXML
	private Node handleDoctorDataMenuItem() {
		DoctorDialog dialog = new DoctorDialog();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/DoctorWindow.fxml"));
			VBox doctorsOverview = (VBox) loader.load();

			return doctorsOverview;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MainWindowController() {
	}
		
	@FXML
	public void initialize() {
		
	}

}
