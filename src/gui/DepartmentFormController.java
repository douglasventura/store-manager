package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

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

    @FXML
    public void onBtSaveAction() {
        System.out.println("onBtSaveAction");
    }

    @FXML
    public void onBtCancelAction() {
        System.out.println("onBtCancelAction");
    }

    public void setDepartment(Department department) {
        this.department = department;
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
}
