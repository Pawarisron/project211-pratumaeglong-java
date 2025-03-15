package ku.cs.controllers.admin;

import com.github.saacsos.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.accounts.DepartmentList;
import ku.cs.models.accounts.StaffAccount;
import ku.cs.services.AccountListFileDataSource;
import ku.cs.services.DataSources;
import ku.cs.services.DepartmentListFileDataSource;
import ku.cs.services.ImageFileDataSource;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class AdminDataPageController {
    @FXML private Label levelLabel;
    @FXML private Label selectedLevelLable;
    @FXML private Label accountNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label departmentInChargeLabel;
    @FXML private Label loginTimeLabel;
    @FXML private ImageView userImage;
    @FXML private TableView<Account> userTableView;
    @FXML private TableColumn<Account, String> loginTimeColumn;
    @FXML private TableColumn<Account, String> usernameColumn;
    @FXML private TableColumn<Account, String> accountNameColumn;
    @FXML private ChoiceBox<String> levelChoiceBox;
    private final DataSources<AccountList> accountListFileDataSource = new AccountListFileDataSource("data", "accountDataSources.csv");

    private final DepartmentListFileDataSource departmentListFileDataSource = new DepartmentListFileDataSource("data","departmentDataSources.csv");
    private Account account;
    private final DepartmentList departmentList = departmentListFileDataSource.readData();
    private AccountList accountList;
    private final ObservableList<Account> observableList = FXCollections.observableArrayList(); //สร้าง ObservableList เอาไว้เก็บ Account

    private final ImageFileDataSource imageFileDataSource = new ImageFileDataSource("account");
    private File userImageFile;


    @FXML
    public void initialize() {
        accountList =  accountListFileDataSource.readData();

        levelChoiceBox.getItems().addAll("ทั้งหมด","ผู้ดูแลระบบ", "เจ้าหน้าที่", "นักศึกษา");

        loginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        userTableView.setItems(observableList);

        levelChoiceBox.setValue("ทั้งหมด");
        levelLabel.setText("บัญชีทั้งหมด");
        observableList.addAll(accountList.getAllAccounts());
        sortByLoginTime();
        levelChoiceBox.setOnAction(this::selectChoiceBox);
        showSelectedUser();
    }
    public void selectChoiceBox(ActionEvent actionEvent){
        userTableView.getItems().clear();
        if (levelChoiceBox.getValue().equals("ทั้งหมด")){
            levelLabel.setText("บัญชีทั้งหมด");
            observableList.addAll(accountList.getAllAccounts());
            sortByLoginTime();
        }else if (levelChoiceBox.getValue().equals("ผู้ดูแลระบบ")){
            levelLabel.setText("ผู้ดูแลระบบ");
            for (Account account : accountList.getAllAccounts()){
                if (account.getLevel().equals("Admin")) {
                    observableList.add(account);
                }
            }
            sortByLoginTime();
        } else if (levelChoiceBox.getValue().equals("เจ้าหน้าที่")) {
            levelLabel.setText("เจ้าหน้าที่");
            for (Account account : accountList.getAllAccounts()){
                if (account.getLevel().equals("Staff")) {
                    observableList.add(account);
                }
            }
            sortByLoginTime();
        }  else if (levelChoiceBox.getValue().equals("นักศึกษา")) {
            levelLabel.setText("นักศึกษา");
            for (Account account : accountList.getAllAccounts()) {
                if (account.getLevel().equals("Student")) {
                    observableList.add(account);
                }
            }
            sortByLoginTime();
        }
    }
    public void showSelectedUser() {
        //เมื่อมีการกดเมาส์ใน TableView จะไปเรียก reportTableViewGetItem()
        userTableView.setOnMouseClicked(e -> {
            if( userTableView.getSelectionModel().getSelectedItem() != null) {
                Account selectedAccount = userTableView.getSelectionModel().getSelectedItem();
                //แสดงข้อมูล user ที่กดเลือก
                if (selectedAccount.getLevel().equals("Admin")){selectedLevelLable.setText("ผู้ดูแลระบบ");}
                else if (selectedAccount.getLevel().equals("Staff")){selectedLevelLable.setText("เจ้าหน้าที่");}
                else {selectedLevelLable.setText("นักศึกษา");}
                accountNameLabel.setText(selectedAccount.getAccountName());
                usernameLabel.setText(selectedAccount.getUsername());
                if (selectedAccount.getLevel().equals("Staff")) {
                    //เช็คว่าเจอ id ในหน่วยงานหรือไม่ ถ้าไม่เจอจะ return เป็น null
                    if(departmentList.getName(((StaffAccount)selectedAccount).getDepartmentsInChargeID()) != null){
                        String department = departmentList.getName(((StaffAccount)selectedAccount).getDepartmentsInChargeID());
                        departmentInChargeLabel.setText(department);
                    }else{
                        //ถ้าหาหน่วยงานไม่เจอ ใน ไฟล์
                        departmentInChargeLabel.setText("ไม่ทราบหน่วยงาน");
                    }
                } else {
                    departmentInChargeLabel.setText("-");
                }
                loginTimeLabel.setText(selectedAccount.getLoginTime());
                //เอาไฟล์รูปภาพของ user ที่กดเลือกมา
                userImageFile = new File("data" +File.separator + "image" + File.separator + "account" + File.separator + selectedAccount.getUsername()+ ".jpg");
                //ถ้าหาไฟล์เจอ
                if(userImageFile.canRead()){
                    System.out.println("userImage can Read userImageFile, will loading "+ userImageFile.getPath());
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
            }
        });
    }
    public void sortByLoginTime(){
        observableList.sort(new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return -(o1.compareLoginTime(o2));
            }
        });
    }
    @FXML
    public void dataPageBackButton (ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_main_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");}
    }
}
