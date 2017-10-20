package tel_ran.hsa.view;

import java.util.List;
import java.util.stream.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import tel_ran.hsa.entities.jfx.DoctorJfx;
import tel_ran.hsa.entities.jfx.TimeSlotJfx;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class DoctorDialog extends Form {
	ObservableList<DoctorJfx> doctors;

	@FXML
	private TableView<DoctorJfx> tableDoctors;

	@FXML
	private TableView<TimeSlotJfx> tableSlots;

	@FXML
	private TableColumn<DoctorJfx, Number> doctorIdColumn;

	@FXML
	private TableColumn<DoctorJfx, String> doctorNameColumn;

	@FXML
	private TableColumn<TimeSlotJfx, Number> timeSlotDayColumn;

	@FXML
	private TableColumn<TimeSlotJfx, String> timeSlotBeginTimeColumn;

	@FXML
	private TableColumn<TimeSlotJfx, String> timeSlotEndTimeColumn;

	@FXML
	private Label idLabel;

	@FXML
	private Label nameLabel;

	@FXML
	private Label phoneNumberLabel;

	@FXML
	private Label eMailLabel;

	public DoctorDialog() {
	}

	@FXML
	public void initialize() {
		List<DoctorJfx> doctorsJfxList = StreamSupport.stream(jfxProxy.getDoctors().spliterator(), false)
				.collect(Collectors.toList());
		doctors = FXCollections.observableList(doctorsJfxList);
		tableDoctors.setItems(doctors);
		tableSlots.setItems(FXCollections.emptyObservableList());

		doctorIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		tableDoctors.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonalDetails(newValue));
	}

	@FXML
	private void showPersonalDetails(DoctorJfx doctor) {
		if (doctor == null) {
			idLabel.setText("");
			nameLabel.setText("");
			eMailLabel.setText("");
			phoneNumberLabel.setText("");
			tableSlots.setItems(FXCollections.emptyObservableList());
		} else {
			idLabel.setText(String.valueOf(doctor.getId()));
			nameLabel.setText(doctor.getName());
			eMailLabel.setText(doctor.geteMail());
			phoneNumberLabel.setText(doctor.getPhoneNumber());
			List<TimeSlotJfx> timeSlotsList = doctor.getTimeSlots().stream().map(TimeSlotJfx::new)
					.collect(Collectors.toList());
			tableSlots.setItems(FXCollections.observableList(timeSlotsList));
			timeSlotDayColumn.setCellValueFactory(cellData -> cellData.getValue().numberDayOfWeekProperty());
			timeSlotBeginTimeColumn.setCellValueFactory(
					cellData -> new SimpleStringProperty(cellData.getValue().getBeginTime().toString()));
			timeSlotEndTimeColumn.setCellValueFactory(
					cellData -> new SimpleStringProperty(cellData.getValue().getEndTime().toString()));
		}

	}

	@FXML
	private void handleDeleteDoctor() {
		int selectedIndex = tableDoctors.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			String result = jfxProxy.removeDoctor(tableDoctors.getSelectionModel().getSelectedItem().getId());
			if (result.equalsIgnoreCase(RestResponseCode.OK))
				tableDoctors.getItems().remove(selectedIndex);
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Bed result");
				alert.setHeaderText("Doctor does not deleted!");
				alert.setContentText("Reason: " + result);
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");
			alert.showAndWait();
		}

	}

	@FXML
	private void handleNewPerson() {
		DoctorJfx tempPerson = new DoctorJfx();
		tempPerson.setId(getMaxId() + 1);
		boolean okClicked = mainController.showPersonEditDialog(tempPerson);
		if (okClicked) {
			String result = jfxProxy.addDoctor(tempPerson);
			if (result.equalsIgnoreCase(RestResponseCode.OK)) {
				doctors.add(tempPerson);
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(primaryStage);
				alert.setTitle("No adding");
				alert.setHeaderText("Person didn't add");
				alert.setContentText("Reason: " + result);

				alert.showAndWait();
			}
		}
	}

	private int getMaxId() {
		return doctors.stream().map(DoctorJfx::getId).max(Integer::compareTo).orElse(0);
	}

	@FXML
	private void handleEditPerson() {
		DoctorJfx selectedPerson = tableDoctors.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = mainController.showPersonEditDialog(selectedPerson);
			if (okClicked) {
				String result = jfxProxy.updateDoctor(selectedPerson);
				if (result.equalsIgnoreCase(RestResponseCode.OK)) {
					showPersonalDetails(selectedPerson);
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(primaryStage);
					alert.setTitle("No update");
					alert.setHeaderText("Person didn't update");
					alert.setContentText("Reason: " + result);

					alert.showAndWait();
				}
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(primaryStage);
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");

			alert.showAndWait();
		}
	}
}
