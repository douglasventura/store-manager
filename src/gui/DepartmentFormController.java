package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtName;

    @FXML
    private Label labelErroName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private Department department;

    private DepartmentService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (department == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (departmentService == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            department = getFormData();
            departmentService.saveOrUpdate(department);    
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (Exception e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener dataChangeListener : dataChangeListeners) {
            dataChangeListener.onDataChanged();
        }
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initializeNodes();
    }    

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData() {
        if (department == null) {
            throw new IllegalStateException();
        }
        txtId.setText(String.valueOf(department.getId()));
        txtName.setText(department.getName());
    }

    private Department getFormData() {
        Department department = new Department();

        ValidationException exception = new ValidationException("Validation error!");

        department.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtName.getText() == null || txtName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty");
        }

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        department.setName(txtName.getText());
        return department;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelErroName.setText(errors.get("name"));
        }
    }
}
