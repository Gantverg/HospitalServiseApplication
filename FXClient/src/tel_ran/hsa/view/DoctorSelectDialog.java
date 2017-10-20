package tel_ran.hsa.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.entities.jfx.*;

public class DoctorSelectDialog extends Form {
	ObservableList<DoctorJfx> doctors;

	@FXML
	private TableView<DoctorJfx> tableDoctors;
	
	@FXML
	private TableColumn<DoctorJfx, Number> doctorIdColumn;
	
	@FXML
	private TableColumn<DoctorJfx, String> doctorNameColumn;
	
	private Doctor selectedDoctor = null;

	private Stage dialogStage;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		doctorIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		int selectedIndex = tableDoctors.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			selectedDoctor = tableDoctors.getSelectionModel().getSelectedItem().get();

			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		selectedDoctor = null;
		dialogStage.close();
	}

	public Doctor getSelectedDoctor() {
		return selectedDoctor;
	}

}
