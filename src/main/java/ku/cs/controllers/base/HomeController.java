package ku.cs.controllers.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.github.saacsos.FXRouter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.services.AccountListFileDataSource;
import ku.cs.services.DataSources;

import java.io.IOException;
import java.util.Objects;

//ใช้เป็นหน้า home
public class HomeController {
    @FXML protected ImageView logoImageView;
    @FXML
    private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Label notificationLabel;
    @FXML private Button requestUnbanButton;
    private AccountList accountList;
    DataSources<AccountList> accountListFileDataSource = new AccountListFileDataSource("data", "accountDataSources.csv");
    @FXML private void initialize(){
        System.out.println("visit: HomePage");
        String url = Objects.requireNonNull(getClass().getResource("/ku/cs/images/ui/logo.png")).toExternalForm();
        logoImageView.setImage(new Image(url));
        notificationLabel.setText("");
        accountList = accountListFileDataSource.readData();
        requestUnbanButton.setVisible(false);
    }
    public void handleLoginButton(ActionEvent actionEvent){
        //รับค่า username และ password จาก TextField เป็น String
        String usernameString = usernameTextField.getText();
        String passwordString = passwordField.getText();
        //ตรวจสอบ username และ password จากนั้นจึง login

        // ตรวจสอบโดยให้ username กับ password ตรงกัน (ver. ยังไม่ได้ทำ equal ใน Account)
        for(Account account : accountList.getAllAccounts()){
            if(Objects.equals(account.getLevel(), "Student") || Objects.equals(account.getLevel(), "Staff") || Objects.equals(account.getLevel(), "Admin")) {
                if (account.getUsername().equals(usernameString) && account.getPassword().equals(passwordString)) {
                    try {
                        if (Integer.parseInt(account.getBanStatus()) >= 0) {
                            notificationLabel.setText("บัญชีนี้ถูกระงับการใช้งาน");
                            account.tryToLoginCount();
                            requestUnbanButton.setVisible(true);
                            break;
                        } else {
                            if(Objects.equals(account.getLevel(), "Student")){
                                account.setLoginTime();
                                com.github.saacsos.FXRouter.goTo("user_main_page", account);
                            } else if(Objects.equals(account.getLevel(), "Staff")){
                                account.setLoginTime();
                                com.github.saacsos.FXRouter.goTo("staff_main_page", account);
                            } else if (Objects.equals(account.getLevel(), "Admin")){
                                account.setLoginTime();
                                com.github.saacsos.FXRouter.goTo("admin_main_page", account);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("ไปที่หน้า main_page ไม่ได้");
                        ;
                        System.err.println("ให้ตรวจสอบการกำหนด route");
                        System.out.println(e);
                    }
                } else {
                    notificationLabel.setText("ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
                }
            }
        }
        usernameTextField.clear();
        passwordField.clear();
        //update file
        accountListFileDataSource.writeData(accountList, false);
    }
    public void handleRequestUnbanButton(ActionEvent actionEvent) {
        try {
            //เปลี่ยนการแสดงผลไปที่ route
            com.github.saacsos.FXRouter.goTo("request_unban");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า request_unban ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            System.out.println(e);
        }
    }
    @FXML
    public void handleAboutButton(ActionEvent actionEvent){
        try {
            // เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ about
            FXRouter.goTo("about");
        } catch ( IOException e) {
            System.err.println("ไปที่หน้า about ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            System.out.println(e);
        }
    }
    @FXML
    public void handleHelpButton(ActionEvent actionEvent){
        try {
            //เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ help
            FXRouter.goTo("help");
        }catch (IOException e){
            System.err.println("ไปที่หน้า help ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            System.out.println(e);
        }
    }
    public void handleRegisterButton(){
        try {
            //เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ register
            FXRouter.goTo("register");
        }catch (IOException e){
            System.err.println("ไปที่หน้า register ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            System.out.println(e);
        }
    }
    @FXML private void usernameTextFieldKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER ){
            System.out.println("ENTER Key Preesed");
            handleLoginButton(null);
        }
    }
    @FXML private void passwordTextFieldKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            System.out.println("ENTER Key Preesed");
            handleLoginButton(null);
        }
    }
    public static void clickBackButton(){
        try {
            //เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ project
            com.github.saacsos.FXRouter.goTo("home");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    public static void clickBackButton(String goToTarget){
        try {
            //เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ project
            com.github.saacsos.FXRouter.goTo(goToTarget);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า project ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}
