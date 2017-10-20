package tel_ran.hsa.view;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tel_ran.hsa.entities.dto.HealthGroup;
import tel_ran.hsa.entities.jfx.DoctorJfx;
import tel_ran.hsa.entities.jfx.HealthGroupJfx;

public class HealthgroupSelectDialog extends Form {
	ObservableList<HealthGroupJfx> healthgroups;

	@FXML
	private TableView<HealthGroupJfx> tableHealthgroups;
	
	@FXML
	private TableColumn<HealthGroupJfx, Number> groupIdColumn;
	
	@FXML
	private TableColumn<HealthGroupJfx, String> groupNameColumn;

	@FXML
	private TableColumn<HealthGroupJfx, Number> minPulseColumn;

	@FXML
	private TableColumn<HealthGroupJfx, Number> maxPulseColumn;

	@FXML
	private TableColumn<HealthGroupJfx, Number> surveyPeriodColumn;

	private HealthGroup selectedHealthgroup = null;

	private Stage dialogStage;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		List<HealthGroupJfx> healthgroupJfxList = StreamSupport.stream(jfxProxy.getHealthGroups().spliterator(), false)
				.collect(Collectors.toList());
		healthgroups = FXCollections.observableList(healthgroupJfxList);
		tableHealthgroups.setItems(healthgroups);

		groupIdColumn.setCellValueFactory(cellData -> cellData.getValue().groupIdProperty());
		groupNameColumn.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
		minPulseColumn.setCellValueFactory(cellData -> cellData.getValue().minNormalPulseProperty());
		maxPulseColumn.setCellValueFactory(cellData -> cellData.getValue().maxNormalPulseProperty());
		surveyPeriodColumn.setCellValueFactory(cellData -> cellData.getValue().surveyPeriodProperty());
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		int selectedIndex = tableHealthgroups.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			selectedHealthgroup = tableHealthgroups.getSelectionModel().getSelectedItem().get();

			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		selectedHealthgroup = null;
		dialogStage.close();
	}

	public HealthGroup getSelectedHealthgroup() {
		return selectedHealthgroup;
	}

}
