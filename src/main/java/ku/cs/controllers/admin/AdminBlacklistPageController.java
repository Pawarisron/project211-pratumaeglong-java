package ku.cs.controllers.admin;

import com.github.saacsos.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.accounts.DepartmentList;
import ku.cs.models.accounts.StaffAccount;
import ku.cs.models.requests.Request;
import ku.cs.models.requests.RequestList;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AdminBlacklistPageController {
    @FXML private TableView<Account> blacklistTableView;
    @FXML private TableColumn<Account, String> usernameColumn;
    @FXML private TableColumn<Account, String> tryToLoginCountColumn;
    @FXML private TableColumn<Account, String> requestStatus;
    @FXML private ImageView userImage;
    @FXML private Label selectedLevelLable;
    @FXML private Label accountNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label departmentInChargeLabel;
    @FXML private Label loginTimeLabel;
    @FXML private Label headerLabel;
    @FXML private Label detailLabel;
    @FXML private Label notificationLabel;
    private final DataSources<RequestList> requestListDataSources = new RequestListFileDataSource("data", "requestDataSources.csv");
    private RequestList requestList;
    private final DataSources<AccountList> accountListFileDataSource = new AccountListFileDataSource("data", "accountDataSources.csv");
    private Account account;
    private AccountList accountList;
    private final DepartmentListFileDataSource departmentListFileDataSource = new DepartmentListFileDataSource("data","departmentDataSources.csv");
    private DepartmentList departmentList;
    private final ObservableList<Account> observableList = FXCollections.observableArrayList(); //สร้าง ObservableList เอาไว้เก็บ Account

    private final ImageFileDataSource imageFileDataSource = new ImageFileDataSource("account");
    private File userImageFile;
    public void initialize(){
        notificationLabel.setVisible(false);
        accountList =  accountListFileDataSource.readData();
        departmentList = departmentListFileDataSource.readData();
        requestList = requestListDataSources.readData();

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        tryToLoginCountColumn.setCellValueFactory(new PropertyValueFactory<>("banStatus"));
        requestStatus.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        blacklistTableView.setItems(observableList);

        showBlacklistAccount();
        showSelectedUser();
    }
    public void showBlacklistAccount(){
        for (Account account : accountList.getAllAccounts()){
            if (Integer.parseInt(account.getBanStatus()) >= 0) {
                observableList.add(account);
            }
        }
    }
    public void showSelectedUser(){
        //เมื่อมีการกดเมาส์ใน TableView จะไปเรียก TableViewGetItem()
        blacklistTableView.setOnMouseClicked(e -> {
            if (blacklistTableView.getSelectionModel().getSelectedItem() != null) {
                Account selectedAccount = blacklistTableView.getSelectionModel().getSelectedItem();
                //แสดงข้อมูล user ที่กดเลือก
                if (selectedAccount.getLevel().equals("Admin")) {
                    selectedLevelLable.setText("ผู้ดูแลระบบ");
                } else if (selectedAccount.getLevel().equals("Staff")) {
                    selectedLevelLable.setText("เจ้าหน้าที่");
                } else {
                    selectedLevelLable.setText("นักศึกษา");
                }
                accountNameLabel.setText(selectedAccount.getAccountName());
                usernameLabel.setText(selectedAccount.getUsername());
                if (selectedAccount.getLevel().equals("Staff")) {
                    //เช็คว่าเจอ id ในหน่วยงานหรือไม่ ถ้าไม่เจอจะ return เป็น null
                    if (departmentList.getName(((StaffAccount) selectedAccount).getDepartmentsInChargeID()) != null) {
                        String department = departmentList.getName(((StaffAccount) selectedAccount).getDepartmentsInChargeID());
                        departmentInChargeLabel.setText(department);
                    } else {
                        //ถ้าหาหน่วยงานไม่เจอ ใน ไฟล์
                        departmentInChargeLabel.setText("ไม่ทราบหน่วยงาน");
                    }
                } else {
                    departmentInChargeLabel.setText("-");
                }
                loginTimeLabel.setText(selectedAccount.getLoginTime());
                //เอาไฟล์รูปภาพของ user ที่กดเลือกมา
                userImageFile = new File("data" + File.separator + "image" + File.separator + "account" + File.separator + selectedAccount.getUsername() + ".jpg");
                //ถ้าหาไฟล์เจอ
                if (userImageFile.canRead()) {
                    System.out.println("userImage can Read userImageFile, will loading " + userImageFile.getPath());
                    //แสดงรูป
                    userImage.setImage(new Image(userImageFile.toURI().toString()));
                }
                //ถ้าหาไฟล์ไม่เจอ
                else {
                    System.out.println("userImage can't Read userImageFile, will loading deafult.jpg");
                    //ให้ url เท่ากับไฟล์ default.jpg
                    String url = Objects.requireNonNull(getClass().getResource("/ku/cs/images/account/default.jpg")).toExternalForm();
                    //แสดงรูป
                    userImage.setImage(new Image(url));
                }
                if (selectedAccount.isRequestStatus()) {
                    for (Request request : requestList.getAllRequests()) {
                        if (request.getUsername().equals(selectedAccount.getUsername())) {
                            headerLabel.setText(request.getRequestHeader());
                            detailLabel.setText(request.getRequestDetail());
                        }
                    }
                }else {
                    headerLabel.setText("ยังไม่มีการร้องขอการยกเลิกระงับสิทธ์");
                    detailLabel.setText("");
                }
            }
        });
    }
    @FXML public void handleUnbanButton(ActionEvent actionEvent) {
        if ((account = blacklistTableView.getSelectionModel().getSelectedItem()) != null){
            //read data
            RequestList requestList = requestListDataSources.readData();
            //ข้อมูลของ account ที่ select
            Account selectedAccount = blacklistTableView.getSelectionModel().getSelectedItem();
            //สร้าง arraylist มาเก็บ request
            ArrayList<Request> requestArrayList = requestList.getAllRequests();
            //หา request ที่ username ตรงกับที่ select และลบออกจาก arraylist
            for (Request request : requestList.getAllRequests()) {
                if (request.getUsername().equals(selectedAccount.getUsername())) {
                    requestArrayList.remove(request);
                    break;
                }
            }
            //ฟังก์ชันปลดแบน
            selectedAccount.unbanAccount();
            blacklistTableView.getItems().remove(selectedAccount);
            notificationLabel.setVisible(true);
            notificationLabel.setStyle("-fx-text-fill: #0061ff;");
            notificationLabel.setText("คืนสิทธ์สำเร็จ");

            //เขียนข้อมูลใหม่ แบบไม่ append
            accountListFileDataSource.writeData(accountList, false);
            requestListDataSources.writeData(requestList, false);
        }else {
            notificationLabel.setVisible(true);
            notificationLabel.setStyle("-fx-text-fill: #FF3719;");
            notificationLabel.setText("โปรดเลือกผู้ใช้งาน");
        }

    }
    @FXML
    public void blacklistPageBackButton (ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_main_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");}
    }
}
