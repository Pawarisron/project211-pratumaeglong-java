package ku.cs.controllers.staff;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.ProjectApplication;
import ku.cs.controllers.user.ListPage;
import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.Department;
import ku.cs.models.accounts.DepartmentList;
import ku.cs.models.accounts.StaffAccount;
import ku.cs.models.reports.*;
import ku.cs.services.DepartmentListFileDataSource;

public class StaffReportHandlePageController extends ListPage {

    @FXML public Label solveErrorLabel;

    @FXML private Label filterLabel;
    @FXML private Label pageHeaderLabel;
    @FXML private Button solveButton;
    private final DepartmentListFileDataSource departmentListFileDataSource = new DepartmentListFileDataSource("data", "departmentDataSources.csv");

    // field that set visible to false
    @FXML private Button confirmButton;
    @FXML private MenuButton changeStatusButton;
    @FXML private Label changeStatusErrLabel;
    @FXML private Label notVisibleLabel2;
    @FXML private Label notVisibleLabel1;
    @FXML private TextArea solveDetailTextArea;
    private StaffAccount staffAccount;
    private Department accountDepartment;
    private ReportList filtedReportList;

    @Override
    @FXML public void initialize(){
        System.out.println("initializing");
        staffAccount = (StaffAccount) com.github.saacsos.FXRouter.getData();
        System.out.println("account department should be" + staffAccount.getDepartmentsInChargeID());
        accountDepartment = findDepartment(staffAccount);
        System.out.println("department now " + accountDepartment.getId());

        /*
        //ทําให้ headerTableColumn  รู้ว่าตัองเก็บ ตัวแปร ชื่อ reportHeader
        //ทําให้ timeTableColumn    รู้ว่าตัองเก็บ ตัวแปร ชื่อ time
        //ทําให้ voteTableColumn    รู้ว่าตัองเก็บ ตัวแปร ชื่อ numberOfVote
        //แล้วก็อย่าลืมสร้าง getter ด้วยนะ ไม่งั้นมันไม่ให้ดึงข้อมูลเอา
         */
        headerTableColumn.setCellValueFactory(new PropertyValueFactory<Report,String>("reportHeader"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<Report,String>("time"));
        voteTableColumn.setCellValueFactory(new PropertyValueFactory<Report,String>("numberOfVote"));

        buildChoiceBox();
        clearText();
        readData();
        showSelectedData();
    }

    @Override
    protected void clearText(){
        super.clearText();
        solveErrorLabel.setText("");
        changeStatusErrLabel.setText("");
    }
    @Override
    protected void readData(){
        //อ่าน data จาก ไฟล์ ไปเก็บใน observableList
        reportList =  reportListFileDataSource.readData();
        filtedReportList = filterReportList(accountDepartment, reportList);
        observableList.addAll(filtedReportList.getAllReport());
    }
    @FXML public void handleBackButton(){

        StaffMainPageController.changePage();
    }

    @Override
    protected void buildChoiceBox(){
        reportGroupList = accountDepartment.getReportGroupList();

        //for reset filter
        filterChoiceBox.getItems().add("ยกเลิก");

        for(ReportGroup reportGroup : reportGroupList.getAllReportGroups()){
            filterChoiceBox.getItems().add(reportGroup.getName());
        }
    }

    @FXML public void hanldeSolveButton(){
        if(reportTableView.getSelectionModel().getSelectedItem() == null){
            //สีแดง
            changeStatusErrLabel.setTextFill(ProjectApplication.COLOR_RED);
            solveErrorLabel.setText("โปรดเลือกเรื่่องก่อน");
        }else{
            setListVisible(false);
            setSolveVisivle(true);
        }
    }
    @FXML public void handleConfirmButton(){
        setSolveVisivle(false);
        setListVisible(true);
    }
    @FXML public void handleProcessingMenuButton(){
        if(report.getReportStatus() == ReportStatus.NOTSOVED){
            report.setReportStatus(ReportStatus.PROCESSONG);
            //สีน้ำเงืิน
            changeStatusErrLabel.setTextFill(ProjectApplication.COLOR_BLUE);
            changeStatusErrLabel.setText("เปลี่ยนสถานะสำเร็จ");
            reportListFileDataSource.writeData(reportList,false);
        }else {
            //สีแดง
            changeStatusErrLabel.setTextFill(ProjectApplication.COLOR_RED);
            changeStatusErrLabel.setText("ตรวจสอบสถานะ");
        }
        changeStatus();
    }
    @FXML public void handleSolvedMunuButton(){
        if(solveDetailTextArea.getText().isEmpty() || solveDetailTextArea.getText().contains("|") || solveDetailTextArea.getText().contains(",")){
            //สีแดง
            changeStatusErrLabel.setTextFill(ProjectApplication.COLOR_RED);
            changeStatusErrLabel.setText("โปรดใส่รายละเอียด");
        }else if(report.getReportStatus() == ReportStatus.SOLVED){
            //สีแดง
            changeStatusErrLabel.setTextFill(ProjectApplication.COLOR_RED);
            changeStatusErrLabel.setText("ท่านได้บันทึกการแก้ไขไปแล้ว");
        } else {
            // ใช้วิธีเพิ่มลงไปที่ detail
            report.addSovedDetail(solveDetailTextArea.getText(), accountDepartment);
            report.setReportStatus(ReportStatus.SOLVED);
            //สีน้ำเงืิน
            changeStatusErrLabel.setTextFill(ProjectApplication.COLOR_BLUE);
            changeStatusErrLabel.setText("เปลี่ยนสถานะสำเร็จ");

            reportListFileDataSource.writeData(reportList,false);
        }
        changeStatus();
    }

    @Override
    protected void showSelectedData(){

        //แสดง ข้อมูลใน tableView
        reportTableView.setItems(observableList);

        //เมื่อมีการกดเมาส์ใน TableView จะไปเรียก reportTableViewGetItem()
        reportTableView.setOnMouseClicked(e -> {
            if( reportTableView.getSelectionModel().getSelectedItem() != null) {
                showLabel();
            }
        });
    }

    private void changeStatus(){

        statusLabel.setText(report.getReportStatusToLabel());
        report.deCompressReportDetail();
        detailLabel.setText(report.getReportDetail());
    }

    private void setListVisible(Boolean b){
        reportTableView.setVisible(b);
        sortButton.setVisible(b);
        filterLabel.setVisible(b);
        filterChoiceBox.setVisible(b);
        pageHeaderLabel.setVisible(b);
        solveErrorLabel.setVisible(b);
        solveButton.setVisible(b);
        solveErrorLabel.setText("");

    }

    private void setSolveVisivle(Boolean b){
        confirmButton.setVisible(b);
        changeStatusButton.setVisible(b);
        changeStatusErrLabel.setVisible(b);
        notVisibleLabel1.setVisible(b);
        notVisibleLabel2.setVisible(b);
        solveDetailTextArea.setVisible(b);
    }

    private Department findDepartment(StaffAccount account){
        DepartmentList departmentList = departmentListFileDataSource.readData();
        Department found = null;
        for(Department department : departmentList.getAllDepartments()){
            if(department.sameID(account.getDepartmentsInChargeID())){
                found = department;
                break;
            }
        }
        return found;
    }

    private ReportList filterReportList(Department department, ReportList report){
        ReportList staffList = new ReportList();
        for(ReportGroup reportGroup : department.getReportGroupList().getAllReportGroups()){
            System.out.println("now filetering" + reportGroup.getName());
            staffList.addAllList(report.filterBy(new Filterer<Report>() {
                @Override
                public Boolean filter(Report report) {
                    return report.getGroupID().equals(reportGroupList.findGroup(reportGroup.getName()).getId());
                }
            }));
        }
        return staffList;
    }
}
