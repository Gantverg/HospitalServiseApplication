package tel_ran.hsa.view;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import tel_ran.hsa.application.HSAFxClientAppl;
import tel_ran.hsa.entities.jfx.DoctorJfx;

public class DoctorDialog extends Form {
	ObservableList<DoctorJfx> doctors;

	@FXML
	TableView<DoctorJfx> tableDoctors;

	@FXML
	private TableColumn<DoctorJfx, Number> doctorIdColumn;
	@FXML
	private TableColumn<DoctorJfx, String> doctorNameColumn;

	public DoctorDialog() {
	}

	@FXML
	public void initialize() {
		Iterable<DoctorJfx> doctorsJfx = jfxProxy.getDoctors();
		doctors = FXCollections.observableList(
				StreamSupport.stream(doctorsJfx.spliterator(), false).collect(Collectors.toList()));
		tableDoctors.setItems(doctors);

		doctorIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	}

}
