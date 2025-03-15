package ku.cs.controllers.base;
import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.accounts.AccountRegister;
import ku.cs.services.AccountListFileDataSource;
import ku.cs.services.DataSources;

import java.io.IOException;

public class ChangePasswordController {
    String directoryName = "data";
    String fileName = "accountDataSources.csv";
    private final DataSources<AccountList> dataSources = new AccountListFileDataSource(directoryName, fileName);
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField oldPasswordTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField confirmNewPasswordTextField;
    @FXML
    private Label errorMassageLabel;
    private String level;
    private Account account;

    public void initialize() {
        account = (Account) com.github.saacsos.FXRouter.getData();
        errorMassageLabel.setText("");
        if (FXRouter.getData().equals("user_main_page")) level = "Student";
        else if (FXRouter.getData().equals("staff_main_page")) level = "Staff";
        else if (FXRouter.getData().equals("admin_main_page")) level = "Admin";
    }

    public void handleChangePasswordButton(ActionEvent actionEvent) {

        AccountList accounts = dataSources.readData();
        if (newPasswordTextField.getText().isEmpty()) {
            errorMassageLabel.setText("กรุณาใส่รหัสผ่านใหม่");
            clearData();
        } else {
            // เก็บรหัสผ่านหใหม่
            String newPassword = newPasswordTextField.getText();

            if ((account = haveAccountInData(accounts)) != null){
                account.setPassword(newPassword);

                // สร้าง account register สำหรับตัวจสอบการใส่ username และ password
                AccountRegister accountRegister = new AccountRegister(account.getLevel(), account.getUsername()
                        , newPassword, account.getAccountName()
                        , confirmNewPasswordTextField.getText(), account.getBanStatus(), account.getLoginTime(), dataSources);
                //เช็ค String format

                if (isCollectFormat(accountRegister)) {
                    //เอาค่าของ registerFormat ไป register แล้วก็ไปเขียนไฟล์
                    account.setPassword(newPassword);

                    dataSources.writeData(accounts, false);
                    //เปลี่ยนสี textLabel เป็นสีนํ้าเงิน
                    errorMassageLabel.setStyle("-fx-text-fill: #0061ff;");
                    errorMassageLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");

                }
            }
            clearData();
        }
    }

    public void clearData() {
        usernameTextField.setText("");
        newPasswordTextField.setText("");
        oldPasswordTextField.setText("");
        confirmNewPasswordTextField.setText("");
    }


    public void handleBackButton(ActionEvent actionEvent) {

        if (FXRouter.getData() != null) {
            if (account.getLevel().equals("Staff")){
                changePage("staff_main_page", account);
            } else if (account.getLevel().equals("Student")) {
                changePage("user_main_page", account);
            }else {
                changePage("admin_main_page", account);
            }
        }
    }
    //Private Method
    private boolean isFillUp(AccountRegister accountRegister){
        //เช็คว่าต้องใส่ข้อมูลครบทุกช่อง
        if(! accountRegister.isFillUp()){
            errorMassageLabel.setText("โปรดใส่ข้อมูลให้ครบทุกช่อง");
            return false;
        }
        return true;
    }

    private boolean isPasswordEqualConfirmPassword(AccountRegister accountRegister){
        //เช็ค password กับ confirmPasswordString ว่าตรงกันไหมตอนลงทะเบียน
        if(! accountRegister.isPasswordEqualConfirmPassword() ){
            errorMassageLabel.setText("รหัสผ่านไม่ตรงกัน");
            return false;
        }
        errorMassageLabel.setText("");
        return true;
    }

    private boolean isCollectFormat(AccountRegister accountRegister){

        //String ที่เอาไว้เก็บ ErrorMessage ที่จะส่งมาผ่าน RegisterFormat
        String passErr = accountRegister.checkFormatPassword();

        //SetText ของ ErrorMessage
        errorMassageLabel.setText(passErr);

        //เซ็ค ว่าข้อมูลใน TextField ทุกอันต้องใส่ให้ครบทุกช่อง
        if(!isFillUp(accountRegister)) return false;

        //เช็ค password กับ confirmPasswordString ว่าตรงกันไหมตอนลงทะเบียน
        if(!isPasswordEqualConfirmPassword(accountRegister)) return false;

        //เช็ค ว่า String ของ ErrorMessage ต้องว่าง
        if( accountRegister.isUsernameSimilarPassword()){
            errorMassageLabel.setText("ชื่อผู้ใช้กับรหัสผ่านเหมือนกัน");
            return false;
        }
        return true;
    }

    private Account haveAccountInData(AccountList accounts){
        // if not have return false
        for(Account account : accounts.getAllAccounts()){
            if (account.getPassword().equals(oldPasswordTextField.getText())
                    && account.getUsername().equals(usernameTextField.getText())){
                return account;
            }
        }
        errorMassageLabel.setText("ชื่อบัญชีหรือรหัสผ่านไม่ถูกต้อง");
        return  null;
    }

    public static void changePage(String gotoTarget, Object data){
        try {
            //เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ user_main_page
            com.github.saacsos.FXRouter.goTo(gotoTarget, data);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า"+gotoTarget +"ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
