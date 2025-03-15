package ku.cs.controllers.admin;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ku.cs.models.accounts.Account;
import ku.cs.services.ImageFileDataSource;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AdminMainPageController {

    @FXML private Label profileMessageLabel;
    @FXML private ImageView adminImage;

    File userImageFile;
    private final FileChooser fileChooser = new FileChooser();

    private final ImageFileDataSource imageFileDataSource = new ImageFileDataSource("account");

    private Account account;

    @FXML private void initialize(){
        //รับ Account จาก UserLoginController
        account = (Account) com.github.saacsos.FXRouter.getData();

        fileChooser.setTitle("เลือกรูปโปรไฟล์ของคุณ");
        //ตั้งจุดเรื่มต้นที่จะให้ หาไฟล์
        fileChooser.setInitialFileName(System.getProperty("usr.home"));
        //Filter ที่เอาไว้ คัดเฉพาะ ไฟล์ ที่มีนามสกุล (.png,.jpg,.gif)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*.png","*.jpg","*.gif"));

        profileMessageLabel.setVisible(false);
        userImageFile = new File("data" +File.separator + "image" + File.separator + "account" + File.separator + account.getUsername()+ ".jpg");
        if(userImageFile.canRead()){
            adminImage.setImage(new Image(userImageFile.toURI().toString()));
        }else {
            adminImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/ku/cs/images/account/default.jpg")).toExternalForm()));
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
                profileMessageLabel.setVisible(false);
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
                adminImage.setImage(new Image(userImageFile.toURI().toString()));
            }else {
                System.out.println("can't read file to "+imageFileDataSource.getClass());
                profileMessageLabel.setText("ความกว้างภาพต้องไม่เกิน 1000 x 1000");
                profileMessageLabel.setVisible(true);
            }

        }else {
            System.out.println("con't select file");
        }
    }
    @FXML
    public void logoutButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("home");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");
        }
    }

    @FXML
    public void dataPageButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_data_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_data_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");
        }
    }
    @FXML
    public void blacklistPageButton (ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_blacklist_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_blacklist_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");}
    }

    @FXML
    public void reportPageButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_report_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_report_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");
        }
    }

    @FXML
    public void staffRegisterPageButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_staff_register_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_staff_register_page ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");
        }
    }

    @FXML
    public void changePasswordPageButton(ActionEvent actionEvent) {
        changePage("change_password", account);
    }

    @FXML
    private void addReportGroup(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_report_group", "admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_report_group ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");
        }
    }

    @FXML
    private void addDepartmentsInCharge(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin_department_in_charge", "admin_main_page");
        } catch (IOException e) {
            System.err.println("ไปทีหน้า admin_department_in_charge ไม่ได้");
            System.err.println("ให้ตรวจสอบการกําหนด route");
            System.out.println(e);
        }
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
