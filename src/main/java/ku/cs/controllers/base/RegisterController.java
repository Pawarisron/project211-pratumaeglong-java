package ku.cs.controllers.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import ku.cs.ProjectApplication;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.accounts.AccountRegister;
import ku.cs.services.AccountListFileDataSource;
import ku.cs.services.DataSources;
import ku.cs.services.ImageFileDataSource;

import java.io.*;

import java.util.Objects;

public class RegisterController  {
    private final DataSources<AccountList> dataSources = new AccountListFileDataSource("data", "accountDataSources.csv");
    private final FileChooser fileChooser = new FileChooser();

    private final ImageFileDataSource imageFileDataSource = new ImageFileDataSource("account");
    @FXML private ImageView profileImageView;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;
    @FXML private TextField accountnameTextField;
    @FXML private Label confirmPasswordLabel;
    @FXML private Label registrationMessageLabel;
    @FXML private Label usernameErrMessageLabel;
    @FXML private Label passwordErrMessageLabel;

    @FXML private Label profileErrMassageLabel;

    //สร้งไฟร์รอ
    @FXML
    private void initialize(){
        System.out.println("visit: RegisterPage");

        setupImageDefault();
        setupFileChooser();

    }
    @FXML
    private void handleRegisterButton(){

        //รับค่า username และ password จาก TextField เป็น String
        String level = "Student";
        String usernameString = usernameTextField.getText();
        String passwordString = passwordTextField.getText();
        String confirmPasswordString = confirmPasswordTextField.getText();
        String accountNameString = accountnameTextField.getText();
        String banStatusString = "-1";
        String loginTimeString = null;

        //สร้าง registerFormat เอาไว้เช็คว่า format ของ password และ name ใช้ได้มั้ย
        AccountRegister accountRegister = new AccountRegister(level, usernameString, passwordString, accountNameString, banStatusString, loginTimeString, confirmPasswordString, dataSources);

        //เช็ค String format
        if( isCollectFormat(accountRegister) ){
            //เอาค่าของ registerFormat ไป register แล้วก็ไปเขียนไฟล์
            if(accountRegister.signUp()){
                System.out.println("Register success");
                //เขียนไฟล์ รูป
                imageFileDataSource.writeData(usernameString);
                //เปลี่ยนสี textLabel เป็นสีนํ้าเงิน
                registrationMessageLabel.setStyle("-fx-text-fill: #0061ff;");
                registrationMessageLabel.setText("ลงทะเบียนสำเร็จ");

                clearInput();
            }
        }
    }

    private void clearInput() {
        usernameTextField.clear();
        passwordTextField.clear();
        confirmPasswordTextField.clear();
        accountnameTextField.clear();
        setupImageDefault();

    }

    @FXML void handleUploadFileButton(ActionEvent actionEvent){
        //เปิด popup ที่เอาไว้หาไฟล์
        File imageFile = fileChooser.showOpenDialog(null);

        if(imageFile != null){
            System.out.println(imageFile.getAbsolutePath());
            //อ่านไฟล์รูป
            imageFileDataSource.readData(imageFile);
            if(imageFileDataSource.isReadDone() ){
                //แสดงรูป
                profileImageView.setImage(new Image(imageFile.toURI().toString() ));
                profileErrMassageLabel.setText("");
            }else {
                profileErrMassageLabel.setText("ความกว้างภาพต้องไม่เกิน 1000 x 1000");
            }
        }else {
            System.out.println("A file is invalid!");
        }

    }
    @FXML
    private void handleBackButton(ActionEvent actionEvent) {

        HomeController.clickBackButton();
    }


    private boolean isFillUp(AccountRegister accountRegister){
        //เช็คว่าต้องใส่ข้อมูลครบทุกช่อง
        if(! accountRegister.isFillUp()){
            confirmPasswordLabel.setText("");
            //เปลี่ยนสี textLabel เป็นสีแดง
            registrationMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
            registrationMessageLabel.setText("โปรดใส่ข้อมูลให้ครบทุกช่อง");
            return false;
        }
        registrationMessageLabel.setText("");
        return true;
    }

    private boolean isPasswordEqualConfirmPassword(AccountRegister accountRegister){

        //เช็ค password กับ confirmPasswordString ว่าตรงกันไหมตอนลงทะเบียน
        if(accountRegister.isPasswordEqualConfirmPassword() ){

            registrationMessageLabel.setText("");
            confirmPasswordLabel.setText("รหัสผ่านไม่ตรงกัน");
            return false;
        }
        confirmPasswordLabel.setText("");
        return true;
    }

    private boolean isCollectFormat(AccountRegister accountRegister){

        //String ที่เอาไว้เก็บ ErrorMessage ที่จะส่งมาผ่าน RegisterFormat
        String userErr = accountRegister.checkFormatUsername();
        String passErr = accountRegister.checkFormatPassword();

        //SetText ของ ErrorMessage
        usernameErrMessageLabel.setText(userErr);
        passwordErrMessageLabel.setText(passErr);

        //เซ็ค ว่าข้อมูลใน TextField ทุกอันต้องใส่ให้ครบทุกช่อง
        if(!isFillUp(accountRegister)) return false;

        //เช็ค password กับ confirmPasswordString ว่าตรงกันไหมตอนลงทะเบียน
        if(!isPasswordEqualConfirmPassword(accountRegister)) return false;


        //เช็ค ว่า String ของ ErrorMessage ต้องว่าง
        if( userErr.isEmpty() && passErr.isEmpty()){
            if( accountRegister.isUsernameSimilarPassword()){
                usernameErrMessageLabel.setText("ชื่อผู้ใช้กับรหัสผ่านเหมือนกัน");
                return false;
            }

            if( accountRegister.isUserDuplicate() ){
                usernameErrMessageLabel.setText("ชื่อผู้ใช้ซํ้า");
                return false;
            }
            return true;
        }
        return false;
    }
    private void setupImageDefault(){
        //ดึงรูป default ไป โชว์
        String url = Objects.requireNonNull( getClass().getResource("/ku/cs/images/account/default.jpg") ).toExternalForm();
        Image defaultImage = new Image(url);
        try {
            profileImageView.setImage(defaultImage);
        }catch (Exception e){
            System.out.println("ไม่สามารถโหลดรูปได้" + e);
        }
    }
    private void setupFileChooser(){
        fileChooser.setTitle("เลือกรูปโปรไฟล์ของคุณ");
        //ตั้งจุดเรื่มต้นที่จะให้ หาไฟล์
        fileChooser.setInitialFileName(System.getProperty("usr.home"));
        //Filter ที่เอาไว้ คัดเฉพาะ ไฟล์ ที่มีนามสกุล (.png,.jpg,.gif)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*.png","*.jpg","*.gif"));
    }
}
