package tel_ran.hsa.view;

import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import tel_ran.hsa.protocols.api.RestResponseCode;

public class LoginDialog extends Form{
	boolean isLogin;
	boolean needInput; 
	
	public LoginDialog() {
	}
	
	public boolean login() {
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText("Look, a Custom Login Dialog");

		// Set the icon (must be included in the project).
		//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});

		needInput = true;
		isLogin = false;
		while(needInput) {
			Optional<Pair<String, String>> result = dialog.showAndWait();
			needInput = false;
			result.ifPresent(usernamePassword -> {
				String key = usernamePassword.getKey();
				String value = usernamePassword.getValue();
				isLogin = jfxProxy.login(key, value).equals(RestResponseCode.OK);
				if(!isLogin) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Wrong authorization");
					alert.setHeaderText("Wrong login or password");
					alert.setContentText("You are not log in!");
					alert.showAndWait();				

				}
				needInput = !isLogin;
			});
		}
		return isLogin;
	}
}
