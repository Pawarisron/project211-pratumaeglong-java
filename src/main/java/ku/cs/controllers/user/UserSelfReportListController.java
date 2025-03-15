package ku.cs.controllers.user;
/*
ตอนนี้ ยัง เอา method handleVoteButton มาใช้อยู่ ซื่ฝไม่ต้องการให้มี
 */

import javafx.fxml.FXML;
import ku.cs.models.reports.Report;

public class UserSelfReportListController extends ListPage {


    @FXML
    public void handleBackButton(){
        UserMainPageController.changePage();
    }

    @Override
    protected void readData() {
        //ทําให้ Data ที่จะโชว์ โชว์เฉพาะ report ของ username ที่ login เข้ามา
        reportList = getReportListFileDataSource().readData();
        for(Report report : reportList.getAllReport()){
            if(getLoginAccount().getUsername().equals(report.getUsername())){
                getObservableList().add(report);
            }
        }
    }

}
