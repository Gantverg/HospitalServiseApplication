package tel_ran.hsa.application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import tel_ran.hsa.controller.JfxProxy;
import tel_ran.hsa.model.WebClient;
import tel_ran.hsa.view.Form;
import tel_ran.hsa.view.LoginDialog;
import tel_ran.hsa.view.MainWindowController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class HSAFxClientAppl extends Application {
	@Override
	public void start(Stage primaryStage) {
		Form.setHospital(new JfxProxy(new WebClient("http://localhost:8080")));
		try {
			
			LoginDialog loginDialog = new LoginDialog();
			if(loginDialog.login()) {
				initMainWindow(primaryStage);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initMainWindow(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HSAFxClientAppl.class.getResource("../view/MainWindow.fxml"));
        BorderPane rootLayout = (BorderPane) loader.load();
        Form.setParentNode(rootLayout);

        //MainWindowController controller = loader.getController();
        //controller.setMainAppl(this);
        
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}