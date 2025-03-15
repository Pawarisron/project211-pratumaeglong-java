package ku.cs.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ku.cs.ProjectApplication;
import ku.cs.models.accounts.DepartmentList;
import ku.cs.models.reports.*;
import ku.cs.services.DepartmentListFileDataSource;
import ku.cs.services.ReportGroupListDataSource;
import ku.cs.services.ReportListFileDataSource;

import java.io.IOException;
import java.util.Objects;

public class AdminReportGroupController {

    private final ReportGroupListDataSource reportGroupListDataSource = new ReportGroupListDataSource();
    private ReportGroupList reportGroupList = new ReportGroupList();
    private final DepartmentListFileDataSource departmentListFileDataSource = new DepartmentListFileDataSource("data","departmentDataSources.csv");
    private final ReportListFileDataSource reportListFileDataSource =new ReportListFileDataSource("data","studentReportDataSources.csv");

    @FXML private TableView<ReportGroup> reportGroupTableView;
    @FXML private TableColumn<ReportGroup,RequirementTypeGroup> typeTableColumn;
    @FXML private TableColumn<ReportGroup,String> idTableColumn;
    @FXML private TableColumn<ReportGroup,String> nameTableColumn;

    //firstGroup
    @FXML private Group firstGroup;
    @FXML private Label reportNameTextLabel;
    @FXML private Label idTextLabel;
    @FXML private Label typeTextLabel;
    @FXML private Label detailTextLabel;
    @FXML private Button editReportGroupButton;
    @FXML private Button deleteReportGroupButton;
    @FXML private Button addReportGroupButton;
    @FXML private Label mainMessageLabel;

    //secondGroup
    @FXML private Group secondGroup;
    @FXML private Label headTextLabel;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private Label typeMessageLabel;
    @FXML private TextField nameTextField;
    @FXML private Label nameMessageLabel;
    @FXML private TextArea detailTextArea;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    //thirdGroup
    @FXML private Group thirdGroup;
    @FXML private Button confirmDeleteButton;
    @FXML private Button cancelDeleteButton;
    @FXML private Label deleteMessageLabel;

    private final ObservableList<ReportGroup> reportGroupObservableList = FXCollections.observableArrayList();
    @FXML private void handleBackButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_main_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");}
    }

    @FXML private void initialize(){
        //เพิ่มข้อมูลที่อ่านได้จากไฟล์ ใสใน table
        reportGroupList = reportGroupListDataSource.readData();
        reportGroupObservableList.addAll(reportGroupList.getAllReportGroups());
        reportGroupTableView.setItems(reportGroupObservableList);

        firstGroup.setVisible(true);
        secondGroup.setVisible(false);
        thirdGroup.setVisible(false);

        //เพิ่ม ตัวเลือกให้ choiceBox
        typeChoiceBox.getItems().addAll("ข้อความ","ข้อความและรูปภาพ");

        setupTableView();
        setupMessageLabel();

        reportGroupTableView.getSelectionModel().getSelectedItem();
    }
    public void handleTableViewOnMouseClicked(MouseEvent mouseEvent) {
        //method นี้ถ้ามีการ click ใน tableView จะแสดงข้อมูลใน label
        ReportGroup selectedReportGroup = reportGroupTableView.getSelectionModel().getSelectedItem();
        //แสดงข้อมูลใน label
        if(selectedReportGroup != null){
            selectedReportGroup.decompressDetail();
            reportNameTextLabel.setText(selectedReportGroup.getName());
            idTextLabel.setText(selectedReportGroup.getId());
            typeTextLabel.setText(selectedReportGroup.getRepuirement().toString());
            detailTextLabel.setText(selectedReportGroup.getDetail());
        }
    }

    //firstGroup
    @FXML private void handleAddReportGroupButton(ActionEvent actionEvent) {
        //clear
        setupMessageLabel();

        //ให้โชเฉพาะ secondGroup
        firstGroup.setVisible(false);
        secondGroup.setVisible(true);
        //ทําให้ reportGroup กดไม่ได้
        reportGroupTableView.setDisable(true);
        headTextLabel.setText("เพิ่มหมวดหมู่เรื่องร้องเรียน");
        typeChoiceBox.setValue("เลือกหมวดหมู่");

    }
    @FXML private void handleEditReportGroupButton(ActionEvent actionEvent) {
        //clear
        setupMessageLabel();

        //ส่วนของการเอาข้อมูลเก่าไปแสดงให้ admin แก้ไข
        ReportGroup selectedReportGroup = reportGroupTableView.getSelectionModel().getSelectedItem();
        if(selectedReportGroup != null) {

            //ให้โชเฉพาะ secondGroup
            firstGroup.setVisible(false);
            secondGroup.setVisible(true);
            //ทําให้ reportGroup กดไม่ได้
            reportGroupTableView.setDisable(true);
            headTextLabel.setText("แก้ไขหมวดหมู่เรื่องร้องเรียน");

            //เอา RequirementType ไป ใส่ใน ChoiceBox
            if (selectedReportGroup.getRepuirement().equals(RequirementTypeGroup.WORD)) {
                typeChoiceBox.setValue("ข้อความ");
            }
            if (selectedReportGroup.getRepuirement().equals(RequirementTypeGroup.IMAGE)) {
                typeChoiceBox.setValue("ข้อความและรูปภาพ");
            }
            //แสดง input เก่า
            nameTextField.setText(selectedReportGroup.getName());
            detailTextArea.setText(selectedReportGroup.getDetail());
        }else {
            System.out.println("Please select ReportGroup before pressing the button.");
            mainMessageLabel.setVisible(true);
            mainMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
            mainMessageLabel.setText("โปรดเลือกหมวดหมู่ที่ต้องการจะแก้ไข");
        }
    }
    @FXML private void handleDeleteReportGroupButton(ActionEvent actionEvent) {
        //clear
        setupMessageLabel();


        ReportGroup selectedReportGroup = reportGroupTableView.getSelectionModel().getSelectedItem();
        if(selectedReportGroup != null) {

            addReportGroupButton.setVisible(false);
            editReportGroupButton.setVisible(false);
            deleteReportGroupButton.setVisible(false);
            reportGroupTableView.setDisable(true);
            thirdGroup.setVisible(true);

        }else {
            System.out.println("Please select ReportGroup before pressing the button.");
            mainMessageLabel.setVisible(true);
            mainMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
            mainMessageLabel.setText("โปรดเลือกหมวดหมู่ที่ต้องการจะลบ");
        }
    }

    //secondGroup
    @FXML private void handleConfirmButoon(ActionEvent actionEvent){

            //เช็คว่าต้องใส่ข้อมูลในส่วนของ typeChoiceBox , name ก่อน
            if (isInputCollectFormat()) {

                String typeString = typeChoiceBox.getValue();
                String name = nameTextField.getText();
                String detail = detailTextArea.getText();
                RequirementTypeGroup requirementTypeGroup = null;

                //ส่วนของแปลง requirementTypeGroup
                if (Objects.equals(typeString, "ข้อความ")) {
                    requirementTypeGroup = RequirementTypeGroup.WORD;
                }
                if (Objects.equals(typeString, "ข้อความและรูปภาพ")) {
                    requirementTypeGroup = RequirementTypeGroup.IMAGE;
                }

                // 01 ส่วนของเพิ่มหมวดหมู่เรื่องร้องเรียน
                if (headTextLabel.getText().equals("เพิ่มหมวดหมู่เรื่องร้องเรียน")) {

                    //เช็คว่าชื่อหมวดหมู่ซํ้ามั้ย
                    if(reportGroupList.isDuplicateName(name) ){
                        //แจ้ง err
                        nameMessageLabel.setText("ชื่อหมวดหมู่ซํ้า");
                        nameMessageLabel.setVisible(true);
                        System.out.println("nameTextField is duplicateName");
                    }else {
                        nameMessageLabel.setVisible(false);
                        ReportGroup reportGroup = new ReportGroup(requirementTypeGroup, name, detail, true);
                        //จัดรูปแบบ reportGroup
                        reportGroup.compressDetail();
                        //เขียนไฟล์
                        reportGroupListDataSource.writeOneLineData(reportGroup, true);
                        System.out.println("wirte ReportGroup success : " + reportGroup);

                        //เพิ่ม reportGroup ไปใน TableView
                        reportGroupList.addReportGroup(reportGroup);
                        reportGroupObservableList.add(reportGroup);
                        reportGroupTableView.setItems(reportGroupObservableList);
                        reportGroupTableView.refresh();

                        //แจ้งเตือน
                        mainMessageLabel.setVisible(true);
                        mainMessageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                        mainMessageLabel.setText("เพิ่มหมวดหมู่เรื่องร้องเรียนเสร็จสิ้น");

                        //clear
                        clearInput();
                        handleCancelButton(null);
                    }
                }

                // 02 ส่วนของแก้ไขหมวดหมู่เรื่องร้องเรียน
                if (headTextLabel.getText().equals("แก้ไขหมวดหมู่เรื่องร้องเรียน")) {
                    //เอาข้อมูลจาก TableView
                    ReportGroup selectedReportGroup = reportGroupTableView.getSelectionModel().getSelectedItem();
                    //ชื่อหมวดหมู่ห้ามซํ้ากับหมวดหมู่อื่นๆแต่ซํ้าตัวมันเองได้
                    if(reportGroupList.isDuplicateName(name,selectedReportGroup.getName())) {
                        //แจ้ง err
                        nameMessageLabel.setText("ชื่อหมวดหมู่ซํ้า");
                        nameMessageLabel.setVisible(true);
                        System.out.println("nameTextField is duplicateName");

                    }else {
                        ReportGroup editedReportGroup = new ReportGroup(requirementTypeGroup,selectedReportGroup.getId(), name, detail);

                        //ส่วนแก้ไขข้อมูล
                        reportGroupList.editBy(selectedReportGroup, editedReportGroup);
                        System.out.println("Edit old: "+selectedReportGroup);
                        System.out.println("New: " + editedReportGroup);
                        //เขียนไฟล์
                        reportGroupList.compressDetailAll();
                        reportGroupListDataSource.writeData(reportGroupList,false);
                        System.out.println("edit ReportGroup success : " + editedReportGroup);

                        //เพิ่มข้อมูลที่แก้ไข ใสใน table
                        reportGroupObservableList.clear();
                        reportGroupObservableList.addAll(reportGroupList.getAllReportGroups());
                        reportGroupTableView.setItems(reportGroupObservableList);
                        reportGroupTableView.refresh();

                        //แจ้งเตื่อน
                        mainMessageLabel.setVisible(true);
                        mainMessageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                        mainMessageLabel.setText("แก้ไขหมวดหมู่เรื่องร้องเรียนเสร็จสิ้น");

                        //clear
                        clearInput();
                        handleCancelButton(null);
                    }
                }
            }

    }

    @FXML private void handleCancelButton(ActionEvent actionEvent) {
        //ให้โชเฉพาะ firstGroup
        firstGroup.setVisible(true);
        secondGroup.setVisible(false);
        thirdGroup.setVisible(false);
        reportGroupTableView.setDisable(false);
        clearInput();

    }
    @FXML private void handleDeleteConfirmButoon(ActionEvent actionEvent) {
        ReportGroup selectedReportGroup =  reportGroupTableView.getSelectionModel().getSelectedItem();
        if(selectedReportGroup != null){
            //ลบ reportGroup ใน list
            reportGroupList.delete(selectedReportGroup);
            //เขียนข้อมูล
            reportGroupListDataSource.writeData(reportGroupList,false);
            System.out.println("Delete :"+ selectedReportGroup );
            //ลบข้อมูลใน TableView
            reportGroupObservableList.remove(selectedReportGroup);
            reportGroupTableView.setItems(reportGroupObservableList);

            //ลบข้อมูลใน Department
            DepartmentList departmentList = departmentListFileDataSource.readData();
            departmentList.deleteReportGroup(selectedReportGroup.getId());
            departmentListFileDataSource.writeData(departmentList,false);
            System.out.println("delete ReportGroup in department success");

            //แก้ไขข้อมูล ใน report ให้ id = ชื่อของ reportGroup ที่ลบเลย
            ReportList reportList = reportListFileDataSource.readData();
            reportList.setAllReportsIdByName(selectedReportGroup);
            reportListFileDataSource.writeData(reportList,false);
            System.out.println("edit id in reportlist success");

            //clear
            handleDeleteCancelButton(null);
        } else {
            System.out.println("selectedReportGroup is null");
        }

    }
    @FXML private void handleDeleteCancelButton(ActionEvent actionEvent) {
        deleteReportGroupButton.setVisible(true);
        editReportGroupButton.setVisible(true);
        addReportGroupButton.setVisible(true);

        handleCancelButton(null);

    }
    private void setupMessageLabel(){
        //setup พวก message ต่างๆ
        mainMessageLabel.setVisible(false);

        nameMessageLabel.setVisible(false);
        nameMessageLabel.setTextFill(ProjectApplication.COLOR_RED);

        typeMessageLabel.setVisible(false);
        typeMessageLabel.setTextFill(ProjectApplication.COLOR_RED);

        deleteMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
    }
    private void setupTableView(){
        /*
        //ทําให้ typeTableColumn  รู้ว่าตัองเก็บ ตัวแปร ชื่อ repuirement
        //ทําให้ idTableColumn    รู้ว่าตัองเก็บ ตัวแปร ชื่อ id
        //ทําให้ nameTableColumn    รู้ว่าตัองเก็บ ตัวแปร ชื่อ name
        //แล้วก็อย่าลืมสร้าง getter ด้วยนะ ไม่งั้นมันไม่ให้ดึงข้อมูลเอา
         */
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,RequirementTypeGroup>("repuirement"));
        idTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("name"));


        reportGroupTableView.setItems(reportGroupObservableList);
    }
    private void clearInput(){
        typeChoiceBox.setValue("");
        nameTextField.clear();
        detailTextArea.clear();
    }
    private boolean isInputCollectFormat(){
        boolean collectFormat = true;
        String nameString = nameTextField.getText();

        if(typeChoiceBox.getValue().isEmpty() || typeChoiceBox.getValue().equals("เลือกหน่วยงาน")){
            //แจ้ง err
            typeMessageLabel.setVisible(true);
            System.out.println("typeChoiceBox is empty");
            collectFormat = false;
        }else {
            typeMessageLabel.setVisible(false);
        }
        if (nameString.isBlank()){
            //แจ้ง err
            nameMessageLabel.setText("โปรดใส่ข้อมูลในส่วนนี้");
            nameMessageLabel.setVisible(true);
            System.out.println("nameTextField is blank");
            collectFormat = false;
        }else {
            nameMessageLabel.setVisible(false);
        }
        if((nameString.contains("|") || nameString.contains(",")) && ! nameString.isBlank() ){
            //แจ้ง err
            nameMessageLabel.setText("รูปแบบข้อมูลผิด");
            nameMessageLabel.setVisible(true);
            System.out.println("nameTextField invalid format");
            collectFormat = false;
        }else {
            if(! nameString.isBlank()){
                nameMessageLabel.setVisible(false);
            }
        }


        return collectFormat;
    }
}
