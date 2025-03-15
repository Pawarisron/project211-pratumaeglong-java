package ku.cs.controllers.admin;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ku.cs.ProjectApplication;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.accounts.Department;
import ku.cs.models.accounts.StaffAccountRegister;
import ku.cs.services.AccountListFileDataSource;
import ku.cs.services.DataSources;
import ku.cs.services.DepartmentListFileDataSource;
import ku.cs.services.ImageFileDataSource;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AdminStaffRegisterPageController {

    private final AccountList accountList = new AccountList();
    //write file ใน folder data
    private final DataSources<AccountList> dataSources = new AccountListFileDataSource("data", "accountDataSources.csv");
    private final DepartmentListFileDataSource departmentListFileDataSource = new DepartmentListFileDataSource("data","departmentDataSources.csv");
    private final FileChooser fileChooser = new FileChooser();
    private final ImageFileDataSource imageFileDataSource = new ImageFileDataSource("account");
    @FXML private Label passwordErrMessageLabel;
    @FXML private Label usernameErrMessageLabel;
    @FXML private ChoiceBox<Department> departmentChoiceBox;
    @FXML private Label profileErrMassageLabel;
    @FXML private ImageView staffImage;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField accountnameTextField;
    @FXML private Label confirmPasswordLabel;
    @FXML private Label registrationMessageLabel;

    public void initialize(){
        System.out.println("visit: admin_staff_register_page");
        //setup
        setupLabel();
        setupImageDefault();
        setupFileChooser();
        //เพิ่มข้อมูลเข้าไปใน choiceBox
        departmentChoiceBox.getItems().addAll(departmentListFileDataSource.readData().getAllDepartments());

    }
    public void handleRegisterButton(ActionEvent actionEvent){
        //เช็ค password กับ confirmpassword ว่าตรงกันไหมตอนลงทะเบียน
        confirmPasswordLabel.setVisible(false);
        registrationMessageLabel.setText("ลงทะเบียนสำเร็จ");

        String departmentsID = null;
        if(departmentChoiceBox.getSelectionModel().getSelectedItem() != null) {
            departmentsID = departmentChoiceBox.getSelectionModel().getSelectedItem().getId();
        }
        //รับค่า username และ password จาก TextField เป็น String
        String level = "Staff";
        String usernameString = usernameTextField.getText();
        String passwordString = passwordField.getText();
        String accountNameString = accountnameTextField.getText();

        String banStatusString = "-1";
        String loginTimeString = null;
        String confirmPasswordString = confirmPasswordField.getText();

        //สร้างเอาไว้ใช้เช็ค format
        StaffAccountRegister staffAccountRegister = new StaffAccountRegister(level,usernameString,passwordString,accountNameString,departmentsID,
                banStatusString,loginTimeString,confirmPasswordString,dataSources);
        //เช็ค String format
        if( isCollectFormat(staffAccountRegister)){
            //เอาค่าของ registerFormat ไป register แล้วก็ไปเขียนไฟล์
            if(staffAccountRegister.signUp()){
                System.out.println("Register success");
                //เขียนไฟล์ รูป
                imageFileDataSource.writeData(usernameString);
                //เปลี่ยนสี textLabel เป็นสีนํ้าเงิน
                registrationMessageLabel.setVisible(true);
                registrationMessageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                registrationMessageLabel.setText("ลงทะเบียนสำเร็จ");
                clearInput();
            }
        }

    }
    public void handleUploadFileButton(ActionEvent actionEvent){
        File imageFile = fileChooser.showOpenDialog(null);
        if(imageFile != null){
            System.out.println(imageFile.getAbsolutePath());

            //อ่านไฟล์รูป
            imageFileDataSource.readData(imageFile);
            if(imageFileDataSource.isReadDone() ){
                //แสดงรูป
                staffImage.setImage(new Image(imageFile.toURI().toString()));
                profileErrMassageLabel.setVisible(false);
            }else {
                profileErrMassageLabel.setVisible(true);
                profileErrMassageLabel.setText("ความกว้างภาพต้องไม่เกิน 1000 x 1000");
            }
        } else {
            System.out.println("A file is invalid!");
        }
    }
    private boolean isCollectFormat(StaffAccountRegister staffAccountRegister){
        //String ที่เอาไว้เก็บ ErrorMessage ที่จะส่งมาผ่าน RegisterFormat
        String userErr = staffAccountRegister.checkFormatUsername();
        String passErr = staffAccountRegister.checkFormatPassword();

        //SetText ของ ErrorMessage
        usernameErrMessageLabel.setVisible(true);
        passwordErrMessageLabel.setVisible(true);

        usernameErrMessageLabel.setText(userErr);
        passwordErrMessageLabel.setText(passErr);

        //เซ็ค ว่าข้อมูลใน TextField ทุกอันต้องใส่ให้ครบทุกช่อง
        if(!isFillUp(staffAccountRegister)) return false;

        //เช็ค password กับ confirmPasswordString ว่าตรงกันไหมตอนลงทะเบียน
        if(!isPasswordEqualConfirmPassword(staffAccountRegister)) return false;

        //เช็ค ว่า String ของ ErrorMessage ต้องว่าง
        if( userErr.isEmpty() && passErr.isEmpty()){
            if( staffAccountRegister.isUsernameSimilarPassword()){
                usernameErrMessageLabel.setVisible(true);
                usernameErrMessageLabel.setText("ชื่อผู้ใช้กับรหัสผ่านเหมือนกัน");
                return false;
            }

            if( staffAccountRegister.isUserDuplicate() ){
                usernameErrMessageLabel.setVisible(true);
                usernameErrMessageLabel.setText("ชื่อผู้ใช้ซํ้า");
                return false;
            }
            return true;
        }
        return false;
    }
    private boolean isFillUp(StaffAccountRegister staffAccountRegister){
        //เช็คว่าต้องใส่ข้อมูลครบทุกช่อง
        if(! staffAccountRegister.isFillUp()){
            confirmPasswordLabel.setVisible(false);
            //เปลี่ยนสี textLabel เป็นสีแดง
            registrationMessageLabel.setVisible(true);
            registrationMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
            registrationMessageLabel.setText("โปรดใส่ข้อมูลให้ครบทุกช่อง");
            return false;
        }
        registrationMessageLabel.setVisible(false);
        return true;
    }
    private boolean isPasswordEqualConfirmPassword(StaffAccountRegister staffAccountRegister){
        //เช็ค password กับ confirmPasswordString ว่าตรงกันไหมตอนลงทะเบียน
        if(staffAccountRegister.isPasswordEqualConfirmPassword() ){
            System.out.println(staffAccountRegister.getPassword() +""+ staffAccountRegister.getConfirmPassword());
            registrationMessageLabel.setVisible(false);
            confirmPasswordLabel.setVisible(true);
            confirmPasswordLabel.setText("รหัสผ่านไม่ตรงกัน");
            return false;
        }
        confirmPasswordLabel.setVisible(false);
        return true;
    }
    private void clearInput() {
        setupImageDefault();
        usernameTextField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        accountnameTextField.clear();

    }
    private void setupLabel(){
        //ซ้อน Label Message
        profileErrMassageLabel.setVisible(false);
        usernameErrMessageLabel.setVisible(false);
        passwordErrMessageLabel.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        registrationMessageLabel.setVisible(false);
        //กําหนดสี
        profileErrMassageLabel.setTextFill(ProjectApplication.COLOR_RED);
        usernameErrMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
        passwordErrMessageLabel.setTextFill(ProjectApplication.COLOR_RED);
        confirmPasswordLabel.setTextFill(ProjectApplication.COLOR_RED);
        registrationMessageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
    }
    private void setupImageDefault(){
        //ดึงรูป default ไป โชว์
        String url = Objects.requireNonNull( getClass().getResource("/ku/cs/images/account/default.jpg") ).toExternalForm();
        Image defaultImage = new Image(url);
        try {
            staffImage.setImage(defaultImage);
        }catch (Exception e){
            System.out.println("ไม่สามารถโหลดรูปได้" + e);
        }
    }
    private void setupFileChooser(){
        //setup fileChooser
        fileChooser.setTitle("เลือกรูปโปรไฟล์ของคุณ");
        //ตั้งจุดเรื่มต้นที่จะให้ หาไฟล์
        fileChooser.setInitialFileName(System.getProperty("usr.home"));
        //Filter ที่เอาไว้ คัดเฉพาะ ไฟล์ ที่มีนามสกุล (.png,.jpg,.gif)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*.png","*.jpg","*.gif"));
    }
    @FXML
    public void staffRegisterPageBackButton (ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_main_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");}
    }
}
