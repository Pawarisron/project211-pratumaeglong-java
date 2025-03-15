package ku.cs.controllers.staff;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ku.cs.ProjectApplication;
import ku.cs.models.accounts.Account;
import ku.cs.services.ImageFileDataSource;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StaffMainPageController {
    @FXML
    protected Label profileErrMassageLabel;
    @FXML protected ImageView accountImageView;
    @FXML
    protected Label accountNameLabel;
    protected Account account;

    protected final ImageFileDataSource imageFileDataSource = new ImageFileDataSource("account");

    protected File userImageFile;
    protected FileChooser fileChooser = new FileChooser();
    @FXML private Label changePageErrorLabel;

    @FXML
    public void initialize(){
        //รับ Account จาก UserLoginController
        account = (Account) com.github.saacsos.FXRouter.getData();
        showAccountData();

        changePageErrorLabel.setText("");

        fileChooser.setTitle("เลือกรูปโปรไฟล์ของคุณ");
        //ตั้งจุดเรื่มต้นที่จะให้ หาไฟล์
        fileChooser.setInitialFileName(System.getProperty("usr.home"));
        //Filter ที่เอาไว้ คัดเฉพาะ ไฟล์ ที่มีนามสกุล (.png,.jpg,.gif)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*.png","*.jpg","*.gif"));

    }

    private void showAccountData(){
        accountNameLabel.setText(account.getAccountName());

        //เอาไฟล์รูปภาพ ของ account มา
        userImageFile = new File("data" +File.separator + "image" + File.separator + "account" + File.separator + account.getUsername()+ ".jpg");
        //ถ้าหาไฟล์เจอ
        if(userImageFile.canRead()){
            System.out.println("userImage can Read userImageFile, will loading "+ userImageFile.getPath());
            //แสดงรูป
            accountImageView.setImage(new Image(userImageFile.toURI().toString()));
        }
        //ถ้าหาไฟล์ไม่เจอ
        else {
            System.out.println("userImage can't Read userImageFile, will loading deafult.jpg");
            //ให้ url เท่ากับไฟล์ default.jpg
            String url = Objects.requireNonNull(getClass().getResource("/ku/cs/images/account/default.jpg")).toExternalForm();
            //แสดงรูป
            accountImageView.setImage(new Image(url));
        }
    }

    @FXML public void handleChangeProfile(ActionEvent actionEvent) {
        File newImage = fileChooser.showOpenDialog(null);
        //เช็คไฟล์ที่อ่านมา null มั้ย ถ้า null แปลว่า ผู้ใช้กดเปลี่ยนรูปแต่ไม่ได้เลือกรูปภาพ
        if(newImage != null){
            System.out.println("can select file :"+newImage.getName());
            //อ่านรูปใหม่
            imageFileDataSource.readData(newImage);
            if (imageFileDataSource.isReadDone()){
                profileErrMassageLabel.setVisible(false);
                System.out.println("can read file to "+imageFileDataSource.getClass());
                //ลบรูปเก่า
                if(userImageFile.delete()){
                    System.out.println("can delete file");
                    //ให้ ภาพใหม่มาแทนภาพเก่า
                    userImageFile = newImage;
                }else //ถ้าลบไฟล์ไม่ได้ แปลว่า เป็น account ที่ใช้ default.jpg อยู่
                {
                    System.out.println("can't delete file");
                }
                //เขียนไฟล์
                imageFileDataSource.writeData(account.getUsername());
                //แสดงรูป
                accountImageView.setImage(new Image(userImageFile.toURI().toString()));
            }else {
                System.out.println("can't read file to "+imageFileDataSource.getClass());
                profileErrMassageLabel.setText("ความกว้างภาพต้องไม่เกิน 1000 x 1000");
                profileErrMassageLabel.setVisible(true);
            }

        }else {
            System.out.println("con't select file");
        }
    }
    public void handleChangePasswordButton(ActionEvent actionEvent){
        changePage("change_password", account);
    }

    @FXML
    void handleLogoutButton(ActionEvent actionEvent){
        changePage("home");
    }
    @FXML void handleReportHandleButton(ActionEvent actionEvent){
        changePage("staff_report_handle_page",account);

        //ถ้าเข้าไม่ได้จะแสดงข้อความ
        changePageErrorLabel.setTextFill(ProjectApplication.COLOR_RED);
        changePageErrorLabel.setText("ขออภัย ท่านไม่มีหน่วยงานแล้ว");
    }

    // คำสั่งเปลี่ยนหน้า
    public static void changePage(){
        try {
            //เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ user_main_page
            com.github.saacsos.FXRouter.goTo("staff_main_page");
        } catch (IOException e) {
            System.err.println("ไปที่หน้า staff_main_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public static void changePage(String gotoTarget){
        try {
            //เปลี่ยนการแสดงผลไปที่ route ที่ชื่อ user_main_page
            com.github.saacsos.FXRouter.goTo(gotoTarget);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า"+gotoTarget +"ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public static void changePage(String gotoTarget, Object data){
        try {

            com.github.saacsos.FXRouter.goTo(gotoTarget, data);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า"+gotoTarget +"ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}
