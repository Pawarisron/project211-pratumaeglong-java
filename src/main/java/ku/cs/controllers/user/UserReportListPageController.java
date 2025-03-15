package ku.cs.controllers.user;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.reports.ReportGroup;
import ku.cs.models.requests.InappropriateReport;
import ku.cs.models.requests.InappropriateReportList;
import ku.cs.services.InappropriateReportListFileDataSource;


public class UserReportListPageController extends ListPage {
    private final InappropriateReportListFileDataSource inappropriateReportListFileDataSource = new InappropriateReportListFileDataSource();
    @FXML private Label errorLabel;

    @Override
    protected void clearText(){
        super.clearText();
        voteErrorLabel.setText("");
        errorLabel.setText("");
        notificationLabel.setText("");

        showInappropriateUI(false);
        typeChoiceBox.getItems().addAll("รายงานเนื้อหา", "รายงานผู้ใช้");
    }

    @FXML public void handleVoteButton(){
        if((report = reportTableView.getSelectionModel().getSelectedItem()) != null){
            Boolean voted = false;
            Boolean isSelf ;
            if(report.getAllVote() != null) {
                for (String username : report.getAllVote())
                    if (username.equals(loginAccount.getUsername()))
                        voted = true;
            }
            if(loginAccount.isSamePerson(report.getUsername())){
                //สีแดง
                voteErrorLabel.setStyle("-fx-text-fill: #FF3719;");
                voteErrorLabel.setText("ท่านไม่สามารถโหวตเรื่องของตัวเองได้");
            }
            else if(voted){
                //สีแดง
                voteErrorLabel.setStyle("-fx-text-fill: #FF3719;");
                voteErrorLabel.setText("ท่านโหวตเรื่องนี้ไปแล้ว ลองดูเรื่องอื่น");
            }
            else {
                //สีน้ำเงืิน
                voteErrorLabel.setStyle("-fx-text-fill: #0061ff;");
                voteErrorLabel.setText("โหวตสำเร็จ");
                report.vote(loginAccount.getUsername());
                reportListFileDataSource.writeData(reportList, false);
            }
        }else{
            voteErrorLabel.setStyle("-fx-text-fill: #FF3719;");
            voteErrorLabel.setText("กรุณาเลือกเรื่องร้องเรียนก่อน");
        }

    }
    @FXML
    public void handleInappropriateButton(){
        errorLabel.setText("");
        if ((report = reportTableView.getSelectionModel().getSelectedItem()) != null) {
            headerLabel1.setText(report.getReportHeader());
            reportTableView.setDisable(true);
            showInappropriateUI(true);
            showReportUI(false);
        }else {
            errorLabel.setStyle("-fx-text-fill: #FF3719;");
            errorLabel.setText("กรุณาเลือกเรื่องร้องเรียนก่อน");
        }
    }
    @FXML
    public void handleInappropriateConfirmButton(){
        if (typeChoiceBox.getValue() == null) {
            notificationLabel.setStyle("-fx-text-fill: #FF3719;");
            notificationLabel.setText("โปรดใส่ข้อมูลให้ครบ");
        }else if (inappropriateHeaderTextField.getText().equals("")){
            notificationLabel.setStyle("-fx-text-fill: #FF3719;");
            notificationLabel.setText("โปรดใส่ข้อมูลให้ครบ");
        }else if (inappropriateDetailTextArea.getText().equals("")){
            notificationLabel.setStyle("-fx-text-fill: #FF3719;");
            notificationLabel.setText("โปรดใส่ข้อมูลให้ครบ");
        }else {
            InappropriateReportList inappropriateReportList = new InappropriateReportList();
            InappropriateReport inappropriateReport = new InappropriateReport(report.getUsername(), report.getReportHeader(), typeChoiceBox.getValue()
                    , inappropriateHeaderTextField.getText(), inappropriateDetailTextArea.getText());
            inappropriateReportList.addInappropriateReport(inappropriateReport);
            inappropriateReportListFileDataSource.writeData(inappropriateReportList, true);
            inappropriateHeaderTextField.clear();
            inappropriateDetailTextArea.clear();
            errorLabel.setStyle("-fx-text-fill: #0061ff;");
            errorLabel.setText("รายงานสำเร็จ");
            handleInappropriateCancelButton();

        }
    }
    @FXML
    public void handleInappropriateCancelButton(){
        reportTableView.setDisable(false);
        showInappropriateUI(false);
        showReportUI(true);
    }
    protected void showInappropriateUI(boolean b){
        headerLabel1.setVisible(b);
        typeLabel.setVisible(b);
        typeChoiceBox.setVisible(b);
        inappropriateHeaderLabel.setVisible(b);
        inappropriateHeaderTextField.setVisible(b);
        inappropriateDetailLabel.setVisible(b);
        inappropriateDetailTextArea.setVisible(b);
        inappropriateConfirmButton.setVisible(b);
        notificationLabel.setVisible(b);
        inappropriateCancelButton.setVisible(b);

    }
    protected void showReportUI(boolean b){
        gridPane.setVisible(b);
        detailLabel.setVisible(b);
        reportDetailLabel.setVisible(b);
        inappropriateButton.setVisible(b);
        if (voteDiscrobtionLabel != null){
            voteDiscrobtionLabel.setVisible(b);
        }
        voteThisReportLabel.setVisible(b);
        voteButton.setVisible(b);
        voteErrorLabel.setVisible(b);
    }

    @FXML public void handleBackButton(){
        UserMainPageController.changePage();
    }
}


