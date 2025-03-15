package ku.cs.controllers.user;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import ku.cs.ProjectApplication;
import ku.cs.models.accounts.Account;
import ku.cs.models.reports.*;
import ku.cs.services.*;
import com.github.saacsos.FXRouter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class UserReportPageController {
    @FXML private Label fileNameTextLabel;
    @FXML private Label addImageMessageLabel;
    @FXML private Button addImageButton;
    @FXML private Label messageLabel;
    @FXML private Label reportGroupDetailLabel;
    @FXML private Label detailMassageWarningLabel;
    @FXML private Label groupMassageWarningLabel;
    @FXML private Label headMassageWarningLabel;
    @FXML private TextField reportHeaderTextField;
    @FXML private TextArea reportDetailTextArea;
    @FXML private Label timeLabel;

    private Account account;
    private final DataSources<ReportList> dataSources = new ReportListFileDataSource("data","studentReportDataSources.csv");

    private final ImageFileDataSource imageFileDataSource = new ImageFileDataSource(1500,1500,"report");

    private File imageFile;
    private final FileChooser fileChooser = new FileChooser();
    private final ReportGroupListDataSource groupListDataSource = new ReportGroupListDataSource();
    private ReportGroupList reportGroupList;
    private ReportList reportList;
    private ReportGroup group;

    private CalendarThread calendarThread;

    @FXML private ChoiceBox<ReportGroup> groupChoiceBox;

    @FXML private void initialize(){
        //เอาไว้ดูว่าเราอยู่หน้าไหนแล้ว
        System.out.println("Report Page");

        //รับในส่วนของ account ที่ login มา ถูกส่งมาจาก user_login-> user_main_page-> user_report_page
        account = (Account) FXRouter.getData();



        buildChoiceBox();

        setupAllMessageLabel();
        //โชว์เวลาแบบ real time
        timeNow();

        setupFileChooser();
    }
    private void setupFileChooser(){
        //setup fileChooser
        fileChooser.setTitle("เลือกรูปภาพ");
        //ตั้งจุดเรื่มต้นที่จะให้ หาไฟล์
        fileChooser.setInitialFileName(System.getProperty("usr.home"));
        //Filter ที่เอาไว้ คัดเฉพาะ ไฟล์ ที่มีนามสกุล (.png,.jpg,.gif)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File","*.png","*.jpg","*.gif"));
    }
    private void buildChoiceBox(){
        reportGroupList = groupListDataSource.readData();
        for(ReportGroup reportGroup : reportGroupList.getAllReportGroups()){
            groupChoiceBox.getItems().add(reportGroup);
        }
        groupChoiceBox.setOnAction(this::setGroupChoiceBox);
    }
    private void setGroupChoiceBox(ActionEvent actionEvent){
        clearInput();
        clearMessageLabel();

        ReportGroup selectedItem =  groupChoiceBox.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            selectedItem.decompressDetail();
            reportGroupDetailLabel.setText(selectedItem.getDetail());
            if(selectedItem.getRepuirement().equals(RequirementTypeGroup.IMAGE)){
                addImageButton.setVisible(true);
            }else {
                addImageButton.setVisible(false);

            }
        }
    }
    private void setupAllMessageLabel(){
        addImageButton.setVisible(false);
        fileNameTextLabel.setVisible(false);
        fileNameTextLabel.setTextFill(ProjectApplication.COLOR_BLUE);

        addImageMessageLabel.setVisible(false);
        addImageMessageLabel.setTextFill(ProjectApplication.COLOR_RED);

        headMassageWarningLabel.setVisible(false);
        headMassageWarningLabel.setTextFill(ProjectApplication.COLOR_RED);

        groupMassageWarningLabel.setVisible(false);
        groupMassageWarningLabel.setTextFill(ProjectApplication.COLOR_RED);

        detailMassageWarningLabel.setVisible(false);
        detailMassageWarningLabel.setTextFill(ProjectApplication.COLOR_RED);

        messageLabel.setVisible(false);

    }
    private void timeNow(){
        //สร้างเวลา format ของ เวลา
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //ตั้งเวลาแรก
        timeLabel.setText(timeFormat.format(Calendar.getInstance().getTime()));
        calendarThread = new CalendarThread("Calendar",timeLabel,1);
    }
    private void clearInput(){
        reportHeaderTextField.clear();
//        groupChoiceBox.getItems().clear();
        reportDetailTextArea.clear();
        fileNameTextLabel.setText("");
        imageFile = null;
    }
    private void clearMessageLabel(){
        headMassageWarningLabel.setVisible(false);
        groupMassageWarningLabel.setVisible(false);
        addImageMessageLabel.setVisible(false);
        detailMassageWarningLabel.setVisible(false);
        messageLabel.setVisible(false);
    }

    @FXML private void handleBackButton(){
        //หยุด Thread
        calendarThread.stop();
        System.out.println(calendarThread.getThread() +": isStop "+calendarThread.isStop());
        UserMainPageController.changePage();
    }
    @FXML private void handleReportButton(ActionEvent actionEvent) {
        clearMessageLabel();
        //รับค่าจาก textField, textArea และ groupChoiceBox มาเก็บเป็น string
        String usernameString = account.getUsername();
        String reportHeaderString = reportHeaderTextField.getText();
        String reportDetailString = reportDetailTextArea.getText();
        String timeString = calendarThread.getTime();
        group = groupChoiceBox.getSelectionModel().getSelectedItem();

        //เช็คว่าต้องใส่ช่อง หมวดหมู่
        if(group == null){
            groupMassageWarningLabel.setVisible(true);
        }else {
            groupMassageWarningLabel.setVisible(false);
        }

        //เช็คว่าต้องใส่ช่อง หัวเรื่อง
        if(reportHeaderString.isBlank()){
            headMassageWarningLabel.setText("โปรดใส่หัวเรื่อง");
            headMassageWarningLabel.setVisible(true);
        }else {
            headMassageWarningLabel.setVisible(false);
        }

        if(reportHeaderString.contains("|") || reportHeaderString.contains(",")) {
            headMassageWarningLabel.setText("รูปแบบข้อมูลไม่ถูกต้อง");
            headMassageWarningLabel.setVisible(true);
        }else {
            if(!reportHeaderString.isBlank()){
                headMassageWarningLabel.setVisible(false);
            }
        }



        //เช็คว่าต้องใส่ช่อง รายระเอียด
        if(reportDetailString.isBlank()){
            detailMassageWarningLabel.setVisible(true);
        }else {
            detailMassageWarningLabel.setVisible(false);
        }

        if(imageFile == null && group != null){
            if(group.getRepuirement().equals(RequirementTypeGroup.IMAGE)){
                addImageMessageLabel.setVisible(true);
                addImageMessageLabel.setText("โปรดใส่รูปภาพ");
            }
        }else {
            addImageMessageLabel.setVisible(false);
        }

            //เช็ค
            if(usernameString != null
                    && group != null
                    && (! reportHeaderString.isBlank() )
                    && (! reportDetailString.isBlank() )
                    && !(reportHeaderString.contains("|") || reportHeaderString.contains(","))
            ){
                clearMessageLabel();
                if(group.getRepuirement().equals(RequirementTypeGroup.IMAGE)) {
                    if (imageFile != null) {
                        addImageMessageLabel.setVisible(false);

                        //สร้าง report และเพิ่มใน reportList
                        Report report = new Report(usernameString, group,timeString, reportHeaderString, reportDetailString);
                        reportList = new ReportList();
                        reportList.addReport(report);
                        //เขียนไฟล์รูป
                        imageFileDataSource.writeData(String.valueOf(report.getId()));
                        //write data ใน file studentReportDataSources.csv
                        dataSources.writeData(reportList, true);
                        System.out.println(usernameString + ": writeReportData done");
                        System.out.println(usernameString +""+report);

                        messageLabel.setText("ยื่นคําร้องเรียนเสร็จสิ้น");
                        messageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                        messageLabel.setVisible(true);

                        clearInput();
                    }else {
                        System.out.println("imageFile == null");
                    }
                }else if (group.getRepuirement().equals(RequirementTypeGroup.WORD)) {
                    addImageMessageLabel.setVisible(false);
                    //สร้าง report และเพิ่มใน reportList
                    Report report = new Report(usernameString, group,timeString, reportHeaderString, reportDetailString);
                    reportList = new ReportList();
                    reportList.addReport(report);

                    //write data ใน file studentReportDataSources.csv
                    dataSources.writeData(reportList,true);
                    System.out.println(usernameString + ": writeReportData done");
                    System.out.println(usernameString+""+report );

                    messageLabel.setText("ยื่นคําร้องเรียนเสร็จสิ้น");
                    messageLabel.setTextFill(ProjectApplication.COLOR_BLUE);
                    messageLabel.setVisible(true);

                    clearInput();
                }
            }
    }

    @FXML private void handleAddImageButton(ActionEvent actionEvent) {

        clearMessageLabel();
        if(groupChoiceBox.getValue().getRepuirement().equals(RequirementTypeGroup.IMAGE)){

            imageFile = fileChooser.showOpenDialog(null);
            if(imageFile != null){
                System.out.println(imageFile.getAbsolutePath());

                //อ่านไฟล์รูป
                imageFileDataSource.readData(imageFile);
               if(imageFileDataSource.isReadDone() ){
                    //แสดงรูป
                    fileNameTextLabel.setVisible(true);
                    fileNameTextLabel.setText(imageFile.getName());
                    addImageMessageLabel.setVisible(false);
                }else {
                    addImageMessageLabel.setVisible(true);
                    addImageMessageLabel.setText("ความกว้างภาพต้องไม่เกิน 1500 x 1500");
                }
            } else {
                System.out.println("A file is invalid!");
            }

        }else {
            System.out.println("invalid RequirementTypeGroup");
        }

    }
}