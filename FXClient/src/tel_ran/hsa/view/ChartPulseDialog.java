package tel_ran.hsa.view;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tel_ran.hsa.entities.jfx.PersonJfx;

public class ChartPulseDialog extends Form {

	@FXML
	private TextField idPatientField;

	@FXML
	private TextField beginDateField;

	@FXML
	private TextField endDateField;

	private Stage dialogStage;
	private boolean okClicked = false;

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	public void initialize() {

	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			okClicked = true;

			ChartPulse chart = new ChartPulse();
			chart.perform(LocalDate.parse(endDateField.getText()), LocalDate.parse(beginDateField.getText()),
					Integer.parseInt(idPatientField.getText()));

			dialogStage.close();
		}
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (idPatientField.getText() == null || idPatientField.getText().length() == 0) {
			errorMessage += "No valid id!\n";
		} else {
			try {
				Integer.parseInt(idPatientField.getText());
			} catch (Exception e) {
				errorMessage += "No id (must be an integer)!\n";
			}
		}
		if (beginDateField.getText() == null || beginDateField.getText().length() == 0) {
			errorMessage += "No valid begin date!\n";
		} else {
			try {
				LocalDate.parse(beginDateField.getText());
			} catch (Exception e) {
				errorMessage += "Wrong begin date (must be yyyy-mm-dd)!\n";
			}
		}
		if (endDateField.getText() == null || endDateField.getText().length() == 0) {
			errorMessage += "No valid end date!\n";
		} else {
			try {
				LocalDate.parse(endDateField.getText());
			} catch (Exception e) {
				errorMessage += "Wrong end date (must be yyyy-mm-dd)!\n";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Показываем сообщение об ошибке.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
