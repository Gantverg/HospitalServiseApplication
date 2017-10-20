package tel_ran.hsa.view;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import tel_ran.hsa.entities.dto.Doctor;
import tel_ran.hsa.entities.dto.HealthGroup;
import tel_ran.hsa.entities.jfx.DoctorJfx;
import tel_ran.hsa.entities.jfx.PatientJfx;
import tel_ran.hsa.entities.jfx.TimeSlotJfx;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class PatientDialog extends Form {
	ObservableList<PatientJfx> patients;

	@FXML
	private TableView<PatientJfx> tablePatients;

	@FXML
	private TableColumn<PatientJfx, Number> patientIdColumn;

	@FXML
	private TableColumn<PatientJfx, String> patientNameColumn;

	@FXML
	private Label idLabel;

	@FXML
	private Label nameLabel;

	@FXML
	private Label phoneNumberLabel;

	@FXML
	private Label eMailLabel;

	@FXML
	private Label therapistLabel;

	@FXML
	private Label healthgroupLabel;
	
	@FXML
	private Button therapistButton;
	
	@FXML
	private Button healthgroupButton;

	public PatientDialog() {
	}

	@FXML
	public void initialize() {
		List<PatientJfx> patientsJfxList = StreamSupport.stream(jfxProxy.getPatients().spliterator(), false)
				.collect(Collectors.toList());
		patients = FXCollections.observableList(patientsJfxList);
		tablePatients.setItems(patients);

		patientIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		patientNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		tablePatients.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonalDetails(newValue));
	}

	@FXML
	private void showPersonalDetails(PatientJfx patient) {
		if (patient == null) {
			idLabel.setText("");
			nameLabel.setText("");
			eMailLabel.setText("");
			phoneNumberLabel.setText("");
			therapistLabel.setText("");
			healthgroupLabel.setText("");
			therapistButton.disarm();
			healthgroupButton.disarm();
		} else {
			idLabel.setText(String.valueOf(patient.getId()));
			nameLabel.setText(patient.getName());
			eMailLabel.setText(patient.geteMail());
			phoneNumberLabel.setText(patient.getPhoneNumber());
			therapistLabel.setText(patient.getTherapist().getName());
			healthgroupLabel.setText(patient.getHealthGroup().getGroupName());
			therapistButton.arm();
			healthgroupButton.arm();
		}

	}

	@FXML
	private void handleDeletePatient() {
		int selectedIndex = tablePatients.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			String result = jfxProxy.removePatient(tablePatients.getSelectionModel().getSelectedItem().getId());
			if (result.equalsIgnoreCase(RestResponseCode.OK))
				tablePatients.getItems().remove(selectedIndex);
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Bed result");
				alert.setHeaderText("Patient does not deleted!");
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
		PatientJfx tempPerson = new PatientJfx();
		tempPerson.setId(getMaxId() + 1);
		boolean okClicked = mainController.showPersonEditDialog(tempPerson);
		if (okClicked) {
			String result = jfxProxy.addPatient(tempPerson);
			if (result.equalsIgnoreCase(RestResponseCode.OK)) {
				patients.add(tempPerson);
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
		return patients.stream().map(PatientJfx::getId).max(Integer::compareTo).orElse(0);
	}

	@FXML
	private void handleEditPerson() {
		PatientJfx selectedPerson = tablePatients.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = mainController.showPersonEditDialog(selectedPerson);
			if (okClicked) {
				String result = jfxProxy.updatePatient(selectedPerson);
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
	
	@FXML
	private void handleSelectHealthgroup() {
		PatientJfx selectedPerson = tablePatients.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			HealthGroup healthgroup = mainController.showSelectHealthgroupDialog(selectedPerson);
			if (healthgroup != null) {
				String result = jfxProxy.setHealthGroup(selectedPerson.getId(), healthgroup.getGroupId());
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

	@FXML
	private void handleSelectTherapist() {
		PatientJfx selectedPerson = tablePatients.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			Doctor therapist = mainController.showSelectTherapistDialog(selectedPerson);
			if (therapist != null) {
				String result = jfxProxy.setTherapist(selectedPerson.getId(), therapist.getId());
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
