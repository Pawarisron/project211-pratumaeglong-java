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
import ku.cs.models.accounts.Department;
import ku.cs.models.accounts.DepartmentList;
import ku.cs.models.reports.Report;
import ku.cs.models.reports.ReportGroup;
import ku.cs.models.reports.ReportGroupList;
import ku.cs.models.reports.RequirementTypeGroup;
import ku.cs.services.DepartmentListFileDataSource;
import ku.cs.services.ReportGroupListDataSource;


import java.io.IOException;
import java.util.ArrayList;

public class AdminDepartmentInChargeController {

    private final DepartmentListFileDataSource departmentListFileDataSource = new DepartmentListFileDataSource("data","departmentDataSources.csv");
    private final ReportGroupListDataSource reportGroupListDataSource = new ReportGroupListDataSource();


    //firstGroup
    @FXML private Group firstGroup;
    @FXML private Button addDepartmentButton;
    @FXML private Button deleteDepartmentButton;
    @FXML private Button editDepartmentButton;
    @FXML private TableView<ReportGroup> firstReportGroupTableView;
    @FXML private TableColumn<ReportGroup,RequirementTypeGroup> firstTypeTableColumn;
    @FXML private TableColumn<ReportGroup,String> firstIdTableColumn;
    @FXML private TableColumn<ReportGroup,String> firstReportGroupTableColumn;
    @FXML private Label departmentNameTextLabel;
    @FXML private Label idDepartmentTextLabel;


    //secondGroup

    @FXML private Group secondGroup;
    @FXML private Label headTextLabel;
    @FXML private TableView<ReportGroup> mainReportGroupTableView;
    @FXML private TableColumn<ReportGroup,RequirementTypeGroup> mainTypeTableColumn;
    @FXML private TableColumn<ReportGroup,String> mainIdTableColumn;
    @FXML private TableColumn<ReportGroup,String> mainNameTableColumn;
    @FXML private TextField departmentTextField;
    @FXML private Label nameMessageLabel;
    @FXML private Label reportGroupMessageLabel;
    @FXML private TableView<ReportGroup> subReportGroupTableView;
    @FXML private TableColumn<ReportGroup,RequirementTypeGroup> subTypeTableColumn;
    @FXML private TableColumn<ReportGroup,String> subIdTableColumn;
    @FXML private TableColumn<ReportGroup,String> subNameTableColumn;
    @FXML private Label addReporGroupMessageLabel;
    @FXML private Label deleteReportGroupMessageLabel;


    //thirdGroup
    @FXML private Group thirdGroup;
    @FXML private Label deleteMessageLabel;


    //nonGroup
    @FXML private Label allDepartmentTextLabel;
    @FXML private TableView<Department> departmentTableView;
    @FXML private TableColumn<Department,String> idTableColumn;
    @FXML private TableColumn<Department,String> departmentTableColumn;

    @FXML private Label messageLabel;


    @FXML private void initialize(){
        setupMessageLabel();

        firstGroup.setVisible(true);
        secondGroup.setVisible(false);
        thirdGroup.setVisible(false);
        departmentTableView.setVisible(true);
        allDepartmentTextLabel.setVisible(true);

        setupTableColumn();

        //เพิ่ม department ไปใน tableView ของ หน้าแรก
        DepartmentList departmentList =  departmentListFileDataSource.readData();
        departmentTableView.setItems(departmentList.getAllDepartmentsObservableList());

    }
    @FXML private void handleTableViewOnMouseClicked(MouseEvent mouseEvent) {
        //ถ้ามีการกดเมาส์ใน departmentTableView จะ เรียกใช้ method นี้ทันที
        Department selectedItem = departmentTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            departmentNameTextLabel.setText(selectedItem.getName());
            idDepartmentTextLabel.setText(selectedItem.getId());
            firstReportGroupTableView.setItems( selectedItem.getReportGroupList().getAllReportGroupsObservableList() );
        }else {
            System.out.println("invalid selectedItem");
        }
    }

    @FXML private void handleBackButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_main_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");}
    }


    //firstGroup

    @FXML private void handleAddDepartmentButton(ActionEvent actionEvent) {
        //clear
        clearMessageLabel();
        //เปลี่ยนหน้า
        departmentTableView.setVisible(false);
        allDepartmentTextLabel.setVisible(false);
        firstGroup.setVisible(false);
        secondGroup.setVisible(true);
        thirdGroup.setVisible(false);

        //เพิ่ม reportGroup ไปใน tableView ของ group 2
        ReportGroupList reportGroupList = reportGroupListDataSource.readData();
        mainReportGroupTableView.setItems(reportGroupList.getAllReportGroupsObservableList());

        headTextLabel.setText("เพิ่มหน่วยงาน");
    }

    @FXML private void handleEditDepartmentButton(ActionEvent actionEvent) {

        //clear
        clearMessageLabel();

        //เช็คว่าต้องเลือกข้อมูลที่ต้องการแก้ไขก่อน
        if(departmentTableView.getSelectionModel().getSelectedItem()!= null){
            //ดึงข้อมูลจาก departmentTableView
            Department selectedItem = departmentTableView.getSelectionModel().getSelectedItem();

            //clear
            messageLabel.setVisible(false);
            //เปลี่ยนหน้า
            departmentTableView.setVisible(false);
            allDepartmentTextLabel.setVisible(false);
            firstGroup.setVisible(false);
            secondGroup.setVisible(true);
            thirdGroup.setVisible(false);

            headTextLabel.setText("แก้ไขหน่วยงาน");

            //::ดึงข้อมูลที่ต้องการแก้ไข ไปแสดงใน หน้าที่ต้องการแก้ไข
            departmentTextField.setText(selectedItem.getName());
            //main tableView
            ReportGroupList reportGroupList = reportGroupListDataSource.readData();

            //ลบข้อมูลที่เหมือนกันออก
            reportGroupList.deleteAll(selectedItem.getReportGroupList());

            //แสดงข้อมูลใน tableView
            mainReportGroupTableView.setItems(reportGroupList.getAllReportGroupsObservableList());
            subReportGroupTableView.getItems().addAll(selectedItem.getReportGroupList().getAllReportGroups());


        }else{
            messageLabel.setVisible(true);
            messageLabel.setTextFill(ProjectApplication.COLOR_RED);
            messageLabel.setText("โปรดเลือกหน่วยงานที่จะแก้ไข");
        }

    }


    @FXML private void handleDeleteDepartmentButton(ActionEvent actionEvent) {

        //clear
        clearMessageLabel();

        if(departmentTableView.getSelectionModel().getSelectedItem() != null){
            //เปลี่ยนหน้า
            departmentTableView.setVisible(true);
            //ปิดไม่ให้ tableView เปลี่ยนข้อมูลได้
            departmentTableView.setDisable(true);
            allDepartmentTextLabel.setVisible(true);

            firstGroup.setVisible(true);
            //เอาส่วนบางส่วนของ firstGroup ออก
            addDepartmentButton.setVisible(false);
            editDepartmentButton.setVisible(false);
            deleteDepartmentButton.setVisible(false);

            secondGroup.setVisible(false);
            thirdGroup.setVisible(true);
            deleteMessageLabel.setVisible(true);
        }else {
            messageLabel.setVisible(true);
            messageLabel.setTextFill(ProjectApplication.COLOR_RED);
            messageLabel.setText("โปรดเลือกหน่วยงานที่จะลบ");
        }

    }



    //secondGroup
    @FXML private void handleConfirmButton(ActionEvent actionEvent) {
        //ดึงข้อมูลจาก textField
        String dataSting = departmentTextField.getText();
        //ดึงข้อมูลจากไฟล์
        DepartmentList departmentList = departmentListFileDataSource.readData();

        /*  format input::
            เช็คว่า data ต้องไม่ Empty
            และ เช็คว่า dataString ต้องไม่มีข้อมูลซํ้าใน ไฟล์(ถ้าแก้ไขไฟล์ส่วนนี้จะยกเว้ย)
            และ ชื่อต้องห้ามยาวเกิน 20 ตัว
            และ ต้องใส่ข้อมูลใน Table ก่อนถึงจะให้สร้าง */

        if( !dataSting.isBlank() && dataSting.length() <= 20 && ! subReportGroupTableView.getItems().isEmpty()){
            //clear massage
            clearMessageLabel();

            //ดึงข้อมูลทั้งหมดจาก subReportGroupTableView มา
            ObservableList<ReportGroup> reportGroupObservableList  = subReportGroupTableView.getItems();
            //เอาข้อมูลที่ได้มาไปสร้าง reportGroupList
            ReportGroupList reportGroupList = new ReportGroupList();
            reportGroupList.addAllReportGroupsObservableList(reportGroupObservableList);


            // ถ้า ชื่อ ของ headTextLabel ตรงกับอันไหน การทํางานของ ปุ่มก็จะแตกต่างกันไป
            // 01 ส่วนของเพิ่มหน่วยงาน
            if(headTextLabel.getText().equals("เพิ่มหน่วยงาน")){
                if(!departmentList.contains(dataSting)){
                    //สร้าง department ใหม่
                    Department newDepartment = new Department(dataSting,reportGroupList,true);
                    DepartmentList newDepartmentList = new DepartmentList();
                    newDepartmentList.addDepartment(newDepartment);
                    //เขียน ข้อมูล
                    departmentListFileDataSource.writeData(newDepartmentList,true);
                    System.out.println("writeData complete: "+ dataSting);

                    //เพิ่ม data เข้าไปใน TableView ของ department ในหน้าแรก
                    departmentTableView.getItems().add(newDepartment);
                    departmentTableView.refresh();

                    //เรียกใช้ปุ่มยกเลิก
                    handleCancelButton(null);

                    //แสดง แจ้งเตือนเป็น fony สีนํ้าเงิน
                    messageLabel.setText("เพิ่มหน่วยงานเสร็จสิ้น");
                    messageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                    messageLabel.setVisible(true);
                    System.out.println("add complete: "+ dataSting);
                }
                else if (departmentList.contains(dataSting)) {
                    nameMessageLabel.setText("ชื่อหน่วยงานซํ้า");
                    nameMessageLabel.setVisible(true);
                    System.out.println("dataSting is dublicate name");
                }
            }
            // 02 ส่วนของแก้ไขหน่วยงาน
            else if (headTextLabel.getText().equals("แก้ไขหน่วยงาน")) {
                //ข้อมูลที่เอามาจาก TableView department หน้าแรก
                Department selectedData = departmentTableView.getSelectionModel().getSelectedItem();

                //ชื่อซํ้าตัวเองได้แต่ห้ามซํ้ากับอันอื่น
                if( !departmentList.contains(dataSting,selectedData.getName()) ){
                    //clear
                    nameMessageLabel.setVisible(false);
                    //หา index ที่ต้องการจะแก้ไข
                    int foundEditIndex = departmentList.indexOf(selectedData);
                    //สร้าง department ที่แก้ไจแล้ว
                    Department newDepartment = new Department(selectedData.getId(),dataSting,reportGroupList);
                    departmentList.set(foundEditIndex,newDepartment);

                    //เขียนค่าลงในไฟล์
                    departmentListFileDataSource.writeData(departmentList,false);
                    System.out.println("editFileDataSource success: " + newDepartment.getId() +","+ newDepartment.getName());

                    //แสดงค่าใน tableView
                    departmentTableView.getItems().clear();
                    departmentTableView.getItems().addAll(departmentList.getAllDepartments());
                    departmentTableView.refresh();

                    //แจ้ง
                    messageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                    messageLabel.setText("แก้ไขเสร็จสิ้น");
                    messageLabel.setVisible(true);

                    System.out.println("edit success : "+dataSting);

                    //clear
                    handleCancelButton(null);


                }else if (departmentList.contains(dataSting)) {
                    nameMessageLabel.setText("ชื่อหน่วยงานซํ้า");
                    nameMessageLabel.setVisible(true);
                    System.out.println("dataSting is dublicate name");
                }


                }
        }else {
            //ข้อมูล ใน tableView ต้องมี
            if (subReportGroupTableView.getItems().isEmpty()){
                reportGroupMessageLabel.setVisible(true);
            }
            //ข้อมูลใน textField ต้องไม่ว่าง
            if(dataSting.isBlank()){
                nameMessageLabel.setText("โปรดใส่ข้อมูลในส่วนนี้");
                System.out.println("dataSting.isBlank()");
            }
            //ชื่อต้องยาวห้ามเกิน 20 ตัว
            else if(dataSting.length() >= 20) {
                nameMessageLabel.setText("ความยาวตัวอักษรห้ามเกิน 20 ตัว");
                System.out.println("dataSting.length() >= 20");
            }
            //แสดง message
            nameMessageLabel.setVisible(true);

        }


    }
    @FXML private void handleCancelButton(ActionEvent actionEvent) {
        //clearInput
        departmentTextField.clear();
        subReportGroupTableView.getItems().clear();
        //clearMessage
        clearMessageLabel();

        departmentTableView.setVisible(true);
        allDepartmentTextLabel.setVisible(true);
        firstGroup.setVisible(true);
        secondGroup.setVisible(false);
        thirdGroup.setVisible(false);
    }

    @FXML private void handleAddReportGroup(ActionEvent actionEvent) {
        //ส่วนของเพิ่ม หมวดหมู่รายงาน
        ReportGroup selectedItem = mainReportGroupTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            //clear message
            addReporGroupMessageLabel.setVisible(false);

            //เอาข้อมูล ใน mainReportGroupTableView ไปใส่ใน subReportGroupTableView
            ObservableList<ReportGroup> reportGroupObservableList = subReportGroupTableView.getItems();
            reportGroupObservableList.add(selectedItem);
            subReportGroupTableView.setItems(reportGroupObservableList);

            //ส่วนของเอา ข้อมูล ใน mainReportGroupTableView ออก
            reportGroupObservableList = mainReportGroupTableView.getItems();
            reportGroupObservableList.remove(selectedItem);
            mainReportGroupTableView.setItems(reportGroupObservableList);

        }else {
            addReporGroupMessageLabel.setVisible(true);
        }
    }

    @FXML private void handleDeleteReportGroup(ActionEvent actionEvent) {
        //ส่วนของลบ หมวดหมู่รายงาน
        ReportGroup selectedItem = subReportGroupTableView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            //clear message
            deleteReportGroupMessageLabel.setVisible(false);

            //เอาข้อมูล ใน subReportGroupTableView ไปใส่ใน mainReportGroupTableView
            ObservableList<ReportGroup> reportGroupObservableList = mainReportGroupTableView.getItems();
            reportGroupObservableList.add(selectedItem);
            mainReportGroupTableView.setItems(reportGroupObservableList);

            //ส่วนของเอา ข้อมูล ใน subReportGroupTableView ออก
            reportGroupObservableList = subReportGroupTableView.getItems();
            reportGroupObservableList.remove(selectedItem);
            subReportGroupTableView.setItems(reportGroupObservableList);

        }else {
            deleteReportGroupMessageLabel.setVisible(true);
        }
    }

    //third
    @FXML private void handleConfirmDeleteButton(ActionEvent actionEvent) {
        //ข้อมูลที่เอามาจาก TableView department หน้าแรก
        Department selectedData = departmentTableView.getSelectionModel().getSelectedItem();
        //เอาข้อมูลจาก dataFile
        DepartmentList departmentList = departmentListFileDataSource.readData();

        //ลบหน่วยงานนั้น
        if(departmentList.deleteDepartment(selectedData)){
            //เขียนค่าลงในไฟล์
            departmentListFileDataSource.writeData(departmentList,false);
            System.out.println("deleteFileDataSource success: " + selectedData.getId() +","+ selectedData.getName());

            //แสดงค่าใน tableView
            departmentTableView.getItems().clear();
            departmentTableView.getItems().addAll(departmentList.getAllDepartments());
            departmentTableView.refresh();

            //แจ้ง
            messageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
            messageLabel.setText("ลบข้อมูลเสร็จสิ้น");
            messageLabel.setVisible(true);

            System.out.println("delete success : "+selectedData.getId() +","+ selectedData.getName());

            //clear
            handleCancelDeleteButton(null);
        }else {
            System.out.println("can't delete item");
        }
    }

    @FXML private void handleCancelDeleteButton(ActionEvent actionEvent) {
        handleCancelButton(null);

        //ทําให้ tableView ใช้งานได้
        departmentTableView.setDisable(false);
        addDepartmentButton.setVisible(true);
        editDepartmentButton.setVisible(true);
        deleteDepartmentButton.setVisible(true);
    }


    private void setupMessageLabel(){
        messageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
        messageLabel.setVisible(false);

        deleteMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
        deleteMessageLabel.setVisible(false);

        nameMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
        nameMessageLabel.setVisible(false);

        addReporGroupMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
        addReporGroupMessageLabel.setVisible(false);

        deleteReportGroupMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
        deleteReportGroupMessageLabel.setVisible(false);

        reportGroupMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
        reportGroupMessageLabel.setVisible(false);
    }
    private void clearMessageLabel(){

        messageLabel.setVisible(false);
        deleteMessageLabel.setVisible(false);
        nameMessageLabel.setVisible(false);
        addReporGroupMessageLabel.setVisible(false);
        deleteReportGroupMessageLabel.setVisible(false);
        reportGroupMessageLabel.setVisible(false);
    }
    private void setupTableColumn(){
        //ของ tableView หน้าแรก
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Department,String>("id"));
        departmentTableColumn.setCellValueFactory(new PropertyValueFactory<Department,String>("name"));

        firstTypeTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,RequirementTypeGroup>("repuirement"));
        firstIdTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("id"));
        firstReportGroupTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("name"));

        //ของ tableView กลุ่ม 2
        mainTypeTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,RequirementTypeGroup>("repuirement"));
        mainIdTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("id"));
        mainNameTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("name"));

        subTypeTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,RequirementTypeGroup>("repuirement"));
        subIdTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("id"));
        subNameTableColumn.setCellValueFactory(new PropertyValueFactory<ReportGroup,String>("name"));
    }
}
