package tel_ran.hsa.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import tel_ran.hsa.entities.jfx.*;

public class PersonEditDialog extends Form {
	@FXML
	private TextField idField;
	@FXML
	private TextField nameField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField eMailField;

	private Stage dialogStage;
	private PersonJfx person;
	private boolean okClicked = false;

    @FXML
    private void initialize() {
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setPerson(PersonJfx person) {
        this.person = person;
        idField.setText(Integer.toString(person.getId()));
        nameField.setText(person.getName());
        phoneNumberField.setText(person.getPhoneNumber());
        eMailField.setText(person.geteMail());
    }

    public boolean isOkClicked() {
        return okClicked;
    }
    
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            person.setId(Integer.parseInt(idField.getText()));
            person.setName(nameField.getText());
            person.setPhoneNumber(phoneNumberField.getText());
            person.seteMail(eMailField.getText());

            
            okClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n"; 
        }
        if (eMailField.getText() == null || eMailField.getText().length() == 0) {
            errorMessage += "No valid eMail!\n"; 
        }
        if (phoneNumberField.getText() == null || phoneNumberField.getText().length() == 0) {
            errorMessage += "No valid street!\n"; 
        }

        if (idField.getText() == null || idField.getText().length() == 0) {
            errorMessage += "No valid id!\n"; 
        } else {
            try {
                Integer.parseInt(idField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No id (must be an integer)!\n"; 
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
}
