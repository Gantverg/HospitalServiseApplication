package tel_ran.hsa.view;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tel_ran.hsa.controller.JfxProxy;

public abstract class Form {
	static JfxProxy jfxProxy;
	static BorderPane parentNode;
	static Stage primaryStage;
	static MainWindowController mainController;

	public static void setHospital(JfxProxy jfxProxy) {
		Form.jfxProxy = jfxProxy;
	}
	
	public static void setParentNode(BorderPane node) {
		Form.parentNode = node;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		Form.primaryStage = primaryStage;
	}

	public static void setMainController(MainWindowController mainController) {
		Form.mainController = mainController;
	}
	
}
