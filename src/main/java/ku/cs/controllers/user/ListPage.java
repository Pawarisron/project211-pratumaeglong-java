package ku.cs.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import ku.cs.models.accounts.Account;
import ku.cs.models.reports.*;
import ku.cs.services.ReportGroupListDataSource;
import ku.cs.services.ReportListFileDataSource;

import java.io.File;
import java.util.Comparator;

public abstract class ListPage {
    @FXML protected TableView<Report> reportTableView;
    @FXML protected TableColumn<Report,String> headerTableColumn;
    @FXML protected TableColumn<Report,String> timeTableColumn;
    @FXML protected TableColumn<Report,String> voteTableColumn;
    @FXML protected GridPane gridPane;
    @FXML protected Label timeLabel;
    @FXML protected Label groupLabel;
    @FXML protected Label headerLabel;
    @FXML protected Label statusLabel;
    @FXML protected Label detailLabel;

    @FXML protected ChoiceBox<String> filterChoiceBox;
    @FXML protected Button sortButton;
    @FXML protected Label reportDetailLabel;
    @FXML protected Label voteDiscrobtionLabel;
    @FXML protected Label headerLabel1;
    @FXML protected Label typeLabel;
    @FXML protected ChoiceBox<String> typeChoiceBox;
    @FXML protected Label inappropriateHeaderLabel;
    @FXML protected TextField inappropriateHeaderTextField;
    @FXML protected Label inappropriateDetailLabel;
    @FXML protected TextArea inappropriateDetailTextArea;
    @FXML protected Button inappropriateButton;
    @FXML protected Button inappropriateConfirmButton;
    @FXML protected Label notificationLabel;
    @FXML protected Button inappropriateCancelButton;
    @FXML protected Label voteThisReportLabel;
    @FXML protected Button voteButton;
    @FXML protected Label voteErrorLabel;
    @FXML protected ImageView groupImageView;

    protected Account loginAccount;
    protected Report report;

    protected final ReportListFileDataSource reportListFileDataSource = new ReportListFileDataSource("data","studentReportDataSources.csv");

    protected ObservableList<Report> observableList = FXCollections.observableArrayList(); //สร้าง ObservableList เอาไว้เก็บ Report

    protected ReportList reportList;

    protected final ReportGroupListDataSource groupListDataSource = new ReportGroupListDataSource();
    protected ReportGroupList reportGroupList;
    protected File reportImageFile;



    @FXML public void initialize(){
        loginAccount = (Account) com.github.saacsos.FXRouter.getData();
        /*
        //ทําให้ headerTableColumn  รู้ว่าตัองเก็บ ตัวแปร ชื่อ reportHeader
        //ทําให้ timeTableColumn    รู้ว่าตัองเก็บ ตัวแปร ชื่อ time
        //ทําให้ voteTableColumn    รู้ว่าตัองเก็บ ตัวแปร ชื่อ numberOfVote
        //แล้วก็อย่าลืมสร้าง getter ด้วยนะ ไม่งั้นมันไม่ให้ดึงข้อมูลเอา
         */
        headerTableColumn.setCellValueFactory(new PropertyValueFactory<Report,String>("reportHeader"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<Report,String>("time"));
        voteTableColumn.setCellValueFactory(new PropertyValueFactory<Report,String>("numberOfVote"));

        buildChoiceBox();
        clearText();
        readData();
        showSelectedData();
    }
    //สร้างหมวดหมู่ใน choiceBox
    protected void buildChoiceBox(){
        reportGroupList = groupListDataSource.readData();

        //for reset filter
        filterChoiceBox.getItems().add("ยกเลิก");

        for(ReportGroup reportGroup : reportGroupList.getAllReportGroups()){
            filterChoiceBox.getItems().add(reportGroup.getName());
        }
    }

    // ใช้ ล้าง lable ที่ยังไม่มีข้อมูลมาแสดง
    protected void clearText(){
        headerLabel.setText("");
        statusLabel.setText("");
        groupLabel.setText("");
        detailLabel.setText("");
        timeLabel.setText("");
    }
    protected void showSelectedData(){

        //แสดง ข้อมูลใน tableView
        reportTableView.setItems(observableList);

        //เมื่อมีการกดเมาส์ใน TableView จะไปเรียก reportTableViewGetItem()
        reportTableView.setOnMouseClicked(e -> {
            if( reportTableView.getSelectionModel().getSelectedItem() != null) {
                showLabel();
            }
        });
    }
    protected void showLabel(){
        //ดึงข้อมูลจาก TableView ที่เราเลือก มาเก็บใน object report
        report = reportTableView.getSelectionModel().getSelectedItem();
        //setText Label
        statusLabel.setText(report.getReportStatusToLabel());
        headerLabel.setText(report.getReportHeader());

        ReportGroup reportGroup = null;
        if(reportGroupList.findGroup( report.getGroupID() ) != null){
            reportGroup = reportGroupList.findGroup(report.getGroupID());
            groupLabel.setText(reportGroupList.findGroup(report.getGroupID()).getName());

        }else {
            groupLabel.setText(report.getGroupID());
        }
        timeLabel.setText(report.getTime());
        report.deCompressReportDetail();
        detailLabel.setText(report.getReportDetail());


        if(reportGroup!= null){
            if(reportGroup.getRepuirement().equals(RequirementTypeGroup.IMAGE)){
                groupImageView.setVisible(true);
                showReportImage();
            }else {
                groupImageView.setVisible(false);
            }
        }

    }
    protected void readData(){
        //อ่าน data จาก ไฟล์ ไปเก็บใน observableList
        reportList =  reportListFileDataSource.readData();
        observableList.addAll(reportList.getAllReport());
    }

    protected ReportListFileDataSource getReportListFileDataSource() {
        return reportListFileDataSource;
    }

    public ObservableList<Report> getObservableList() {
        return observableList;
    }

    public Account getLoginAccount() {
        return loginAccount;
    }

    @FXML
    public void handleSortButton(){
        String textNow = sortButton.getText();

        switch (textNow){
            case "จัดเรียงตาม" :
                observableList.sort(new Comparator<Report>() {
                    @Override
                    public int compare(Report o1, Report o2) {
                        return o1.compareVote(o2);
                    }
                });

                sortButton.setText("โหวตน้อย");
            break;
            case "โหวตน้อย" :
                observableList.sort(new Comparator<Report>() {
                    @Override
                    public int compare(Report o1, Report o2) {
                        return - o1.compareVote(o2);
                    }
                });
                sortButton.setText("โหวตมาก");
            break;
            case "โหวตมาก" :
                observableList.sort(new Comparator<Report>() {
                    @Override
                    public int compare(Report o1, Report o2) {;
                        return o1.compareTime(o2);
                    }
                });
                sortButton.setText("มาก่อน");
            break;
            case "มาก่อน" :
                observableList.sort(new Comparator<Report>() {
                    @Override
                    public int compare(Report o1, Report o2) {
                        return - o1.compareTime(o2);
                    }
                });
                sortButton.setText("มาหลัง");
            break;
            case "มาหลัง" :
                sortButton.setText("จัดเรียงตาม");
                break;
            default:
                break;
        }
    }

    protected ReportList filterByGroup(String reportGroupName){
        return reportList.filterBy(new Filterer<Report>() {
            @Override
            public Boolean filter(Report report) {
                return report.getGroupID().equals(reportGroupList.findGroup(reportGroupName).getId());
            }
        });
    }
    // to filter the List
    @FXML public void handleFilterChoiceBox(){
        if(filterChoiceBox.getValue().equals("ยกเลิก")){
            observableList = FXCollections.observableArrayList();
            readData();
        }else {
            observableList = FXCollections.observableArrayList();
            if(filterByGroup(filterChoiceBox.getValue()).getAllReport().size() != 0){
                observableList = FXCollections.observableArrayList(filterByGroup(filterChoiceBox.getValue()).getAllReport());
            }
        }
        showSelectedData();
    }

    public void showReportImage(){
        //เอาไฟล์รูปภาพ ของ account มา
        reportImageFile = new File("data" +File.separator + "image" + File.separator + "report" + File.separator + report.getId() + ".jpg");
        //ถ้าหาไฟล์เจอ
        if(reportImageFile.canRead()){
            System.out.println("userImage can Read userImageFile, will loading "+ reportImageFile.getPath());
            //แสดงรูป
            groupImageView.setImage(new Image(reportImageFile.toURI().toString()));
        }
        //ถ้าหาไฟล์ไม่เจอ
        else {
            groupImageView.setVisible(false);
            System.out.println("userImage can't Read userImageFile.");
        }


    }


}
