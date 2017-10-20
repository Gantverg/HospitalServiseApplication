package tel_ran.hsa.view;

import java.io.IOException;

import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;
import tel_ran.hsa.application.HSAFxClientAppl;
import tel_ran.hsa.entities.dto.*;
import tel_ran.hsa.entities.jfx.*;

public class MainWindowController extends Form {

	@FXML
	private void handleDoctorDataMenuItem() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/DoctorWindow.fxml"));
			BorderPane doctorsOverview = (BorderPane) loader.load();
			parentNode.setCenter(doctorsOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handlePatientDataMenuItem() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/PatientWindow.fxml"));
			BorderPane patientsOverview = (BorderPane) loader.load();

			parentNode.setCenter(patientsOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleHealthgroupDataMenuItem() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/SelectHealthgroupWindow.fxml"));
			AnchorPane healthgroupOverview = (AnchorPane) loader.load();

			parentNode.setCenter(healthgroupOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handlePulseChartMenuItem() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/PulseChart.fxml"));
			AnchorPane pulseChartOverview = (AnchorPane) loader.load();

			parentNode.setCenter(pulseChartOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MainWindowController() {
	}

	@FXML
	public void initialize() {

	}

	public boolean showPersonEditDialog(PersonJfx person) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/PersonEdit.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			PersonEditDialog controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public HealthGroup showSelectHealthgroupDialog(PatientJfx selectedPerson) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/HealthgroupSelect.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Select healthgroup");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			HealthgroupSelectDialog controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.showAndWait();
			if(controller.isOkClicked())
				return controller.getSelectedHealthgroup();
			else
				return null;
		} catch (IOException e) {
			return null;
		}
	}

	public Doctor showSelectTherapistDialog(PatientJfx selectedPerson) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/DoctorSelect.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Select doctor");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			DoctorSelectDialog controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.showAndWait();

			if(controller.isOkClicked())
				return controller.getSelectedDoctor();
			else
				return null;
		} catch (IOException e) {
			return null;
		}
	}
}
