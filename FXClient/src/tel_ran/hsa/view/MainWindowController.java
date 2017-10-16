package tel_ran.hsa.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;
import tel_ran.hsa.application.HSAFxClientAppl;
import tel_ran.hsa.entities.jfx.PersonJfx;

public class MainWindowController extends Form {

	@FXML
	private MenuItem doctorDataMenuItem;

	@FXML
	private void handleDoctorDataMenuItem() {
		// DoctorDialog dialog = new DoctorDialog();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(HSAFxClientAppl.class.getResource("../view/DoctorWindow.fxml"));
			BorderPane doctorsOverview = (BorderPane) loader.load();

			parentNode.setCenter(doctorsOverview);
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
}
