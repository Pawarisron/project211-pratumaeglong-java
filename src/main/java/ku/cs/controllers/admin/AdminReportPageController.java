package ku.cs.controllers.admin;

import com.github.saacsos.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.ProjectApplication;
import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.reports.Report;
import ku.cs.models.reports.ReportList;
import ku.cs.models.requests.InappropriateReport;
import ku.cs.models.requests.InappropriateReportList;
import ku.cs.models.requests.Request;
import ku.cs.models.requests.RequestList;
import ku.cs.services.AccountListFileDataSource;
import ku.cs.services.DataSources;
import ku.cs.services.InappropriateReportListFileDataSource;
import ku.cs.services.ReportListFileDataSource;

import java.io.IOException;
import java.util.ArrayList;

public class AdminReportPageController {
    @FXML private Label usernameLabel;
    @FXML private Label typeLabel;
    @FXML private Label headerLabel;
    @FXML private Label detailLabel;
    @FXML private Label notificationLabel;
    @FXML private TableView<InappropriateReport> reportedTableView;
    @FXML private TableColumn<InappropriateReport, String> usernameColumn;
    @FXML private TableColumn<InappropriateReport, String> headerColumn;
    @FXML private TableColumn<InappropriateReport, String> typeColumn;

    private final DataSources<AccountList> accountListFileDataSource = new AccountListFileDataSource("data", "accountDataSources.csv");
    private AccountList accountList;
    private final DataSources<ReportList> reportListDataSources = new ReportListFileDataSource("data", "studentReportDataSources.csv");
    private ReportList reportList;
    private final InappropriateReportListFileDataSource inappropriateReportListFileDataSource = new InappropriateReportListFileDataSource();
    private InappropriateReport inappropriateReport;
    private InappropriateReportList inappropriateReportList;
    private final ObservableList<InappropriateReport> observableList = FXCollections.observableArrayList(); //สร้าง ObservableList

    @FXML
    public void initialize() {
        notificationLabel.setText("");
        accountList = accountListFileDataSource.readData();
        inappropriateReportList = inappropriateReportListFileDataSource.readData();

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        headerColumn.setCellValueFactory(new PropertyValueFactory<>("reportHeader"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportedTableView.setItems(observableList);

        observableList.addAll(inappropriateReportList.getAllInappropriateReports());

        showSelectedUser();
    }
    public void showSelectedUser() {
        //เมื่อมีการกดเมาส์ใน TableView จะไปเรียก reportTableViewGetItem()
        reportedTableView.setOnMouseClicked(e -> {
            if( reportedTableView.getSelectionModel().getSelectedItem() != null) {
                InappropriateReport selectedItem = reportedTableView.getSelectionModel().getSelectedItem();
                //แสดงข้อมูล user ที่กดเลือก
                usernameLabel.setText(selectedItem.getUsername());
                typeLabel.setText(selectedItem.getType());
                headerLabel.setText(selectedItem.getInappropriateReportHeader());
                detailLabel.setText(selectedItem.getInappropriateReportDetail());
            }else {
                notificationLabel.setTextFill(ProjectApplication.COLOR_RED);
                notificationLabel.setText("โปรดเลือกผู้ใช้งาน");
            }
        });
    }

    public void handleRemoveButton(ActionEvent actionEvent){
        if ((inappropriateReport = reportedTableView.getSelectionModel().getSelectedItem()) != null) {
            //read data
            InappropriateReportList inappropriateReportList = inappropriateReportListFileDataSource.readData();
            //ข้อมูลของ รายงาน ที่ select
            InappropriateReport selectedItem = reportedTableView.getSelectionModel().getSelectedItem();
            //สร้าง arraylist มาเก็บ รายงาน
            ArrayList<InappropriateReport> inappropriateReportArrayList = inappropriateReportList.getAllInappropriateReports();
            //หา รายงาน ที่ username ตรงกับที่ select และลบออกจาก arraylist
            for (InappropriateReport inappropriateReport : inappropriateReportList.getAllInappropriateReports()){
                if (inappropriateReport.getInappropriateReportHeader().equals(selectedItem.getInappropriateReportHeader())){
                    inappropriateReportArrayList.remove(inappropriateReport);
                    reportedTableView.getItems().remove(selectedItem);
                    break;
                }
            }
            inappropriateReportListFileDataSource.writeData(inappropriateReportList, false);
        }
    }
    public void handleRemoveReportButton(ActionEvent actionEvent){
        if ((inappropriateReport = reportedTableView.getSelectionModel().getSelectedItem()) != null) {
            //read data
            ReportList reportList = reportListDataSources.readData();
            InappropriateReportList inappropriateReportList = inappropriateReportListFileDataSource.readData();
            //ข้อมูลของ รายงาน ที่ select
            InappropriateReport selectedItem = reportedTableView.getSelectionModel().getSelectedItem();
            //สร้าง arraylist มาเก็บ รายงาน
            ArrayList<Report> reportListArrayList = reportList.getAllReport();
            ArrayList<InappropriateReport> inappropriateReportArrayList = inappropriateReportList.getAllInappropriateReports();
            //หา รายงาน ที่ username ตรงกับที่ select และลบออกจาก arraylist
            for (Report report : reportList.getAllReport()){
                if (report.getReportHeader().equals(selectedItem.getReportHeader())){
                    reportListArrayList.remove(report);
                    for (InappropriateReport inappropriateReport : inappropriateReportList.getAllInappropriateReports()){
                        if (inappropriateReport.getInappropriateReportHeader().equals(selectedItem.getInappropriateReportHeader())){
                            inappropriateReportArrayList.remove(inappropriateReport);
                            break;
                        }
                    }
                    reportedTableView.getItems().remove(selectedItem);
                    notificationLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                    notificationLabel.setText("ลบเนื้อหาสำเร็จ");
                    break;
                }else {
                    notificationLabel.setTextFill(ProjectApplication.COLOR_RED);
                    notificationLabel.setText("เนื้อหาถูกลบไปแล้วโปรดกดนำออกเพื่อนำออกจากรายงาน");
                }
            }
            inappropriateReportListFileDataSource.writeData(inappropriateReportList, false);
            reportListDataSources.writeData(reportList, false);
        }
    }
    @FXML void handleBanButton (ActionEvent actionEvent){
        if ((inappropriateReport = reportedTableView.getSelectionModel().getSelectedItem()) != null) {
            //read data
            InappropriateReportList inappropriateReportList = inappropriateReportListFileDataSource.readData();
            //ข้อมูลของ รายงาน ที่ select
            InappropriateReport selectedItem = reportedTableView.getSelectionModel().getSelectedItem();
            //สร้าง arraylist มาเก็บ รายงาน
            ArrayList<InappropriateReport> inappropriateReportArrayList = inappropriateReportList.getAllInappropriateReports();
            //หา รายงาน ที่ username ตรงกับที่ select และลบออกจาก arraylist
            for (InappropriateReport inappropriateReport : inappropriateReportList.getAllInappropriateReports()){
                if (inappropriateReport.getUsername().equals(selectedItem.getUsername())){
                    inappropriateReportArrayList.remove(inappropriateReport);
                    reportedTableView.getItems().remove(selectedItem);
                    break;
                }
            }
            //แบน
            for (Account account : accountList.getAllAccounts()){
                if (account.getUsername().equals(selectedItem.getUsername())){
                    account.banAccount();
                    break;
                }
            }
            notificationLabel.setTextFill(ProjectApplication.COLOR_BLUE);
            notificationLabel.setText("ระงับสิทธ์สำเร็จ");

            //เขียนข้อมูลใหม่ แบบไม่ append
            accountListFileDataSource.writeData(accountList, false);
            inappropriateReportListFileDataSource.writeData(inappropriateReportList, false);
        }
    }

    @FXML
    public void handleReportPageBackButton (ActionEvent actionEvent){
        try {
            FXRouter.goTo("admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_blacklist_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");}
    }
}
