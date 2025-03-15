package ku.cs.controllers.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ku.cs.ProjectApplication;
import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.requests.Request;
import ku.cs.models.requests.RequestList;
import ku.cs.services.AccountListFileDataSource;
import ku.cs.services.DataSources;
import ku.cs.services.RequestListFileDataSource;

public class RequestUnbanController {
    @FXML private TextField usernameTextField;
    @FXML private TextField requestHeaderTextField;
    @FXML private TextArea requestDetailTextArea;
    @FXML private Label usernameWarningLabel;
    @FXML private Label headerWarningLabel;
    @FXML private Label detailWarningLabel;
    @FXML private Label successRequestLabel;
    DataSources<AccountList> accountListDataSources = new AccountListFileDataSource("data", "accountDataSources.csv");
    private AccountList accountList;
    DataSources<RequestList> requestListDataSources = new RequestListFileDataSource("data", "requestDataSources.csv");
    private RequestList requestList;
    public void initialize(){
        accountList = accountListDataSources.readData();
        requestList = requestListDataSources.readData();
        usernameWarningLabel.setText("");
        headerWarningLabel.setText("");
        detailWarningLabel.setText("");
        successRequestLabel.setText("");
    }
    public void handleRequestButton(ActionEvent actionEvent) {
        String usernameString = usernameTextField.getText();
        String requestHeaderString = requestHeaderTextField.getText();
        String requestDetailString = requestDetailTextArea.getText();
        Request request = new Request(usernameString, requestHeaderString, requestDetailString);

        if (usernameString == null || usernameString.contains("|") || usernameString.contains(",")){
            assert usernameString != null;
            if (usernameString.contains("|") || usernameString.contains(",")){
                usernameWarningLabel.setText("ชื่อผู้ใช้รูปแบบไม่ถูกต้อง");
            }else {
                usernameWarningLabel.setText("โปรดใส่ชื่อผู้ใช้");
            }


        } else if (requestHeaderString == null || requestHeaderString.contains("|") || requestHeaderString.contains(",")){
            usernameWarningLabel.setText("");

            assert requestHeaderString != null;
            if (requestHeaderString.contains("|") || requestHeaderString.contains(",")){
                headerWarningLabel.setText("หัวเรื่องรูปแบบไม่ถูกต้อง");
            }else {
                headerWarningLabel.setText("โปรดใส่หัวเรื่อง");
            }

        } else if (requestDetailString == null || requestDetailString.contains("|") || requestDetailString.contains(",") ){
            headerWarningLabel.setText("");

            assert requestDetailString != null;
            if (requestDetailString.contains("|") || requestDetailString.contains(",")){
                detailWarningLabel.setText("รายละเอียดรูปแบบไม่ถูกต้อง");
            }else {
                detailWarningLabel.setText("โปรดใส่รายละเอียด");
            }

        } else if (request.isRequestDuplicate()) {
            detailWarningLabel.setText("");
            successRequestLabel.setTextFill(ProjectApplication.COLOR_RED);
            successRequestLabel.setText("ส่งคำร้องไปแล้วไม่สามารถส่งซ้ำได้");
        } else {
            detailWarningLabel.setText("");
            RequestList requestList = new RequestList();
            requestList.addRequest(request);
            requestListDataSources.writeData(requestList, true);

            for (Account account : accountList.getAllAccounts()){
                if (account.getUsername().equals(usernameString)){
                    account.makeRequest();
                    break;
                }
            }

            successRequestLabel.setText("ส่งคำร้องสำเร็จ");
            usernameTextField.clear();
            requestHeaderTextField.clear();
            requestDetailTextArea.clear();

            accountListDataSources.writeData(accountList, false);
        }

    }

    public void handleBackButton(ActionEvent actionEvent) {

        if (com.github.saacsos.FXRouter.getData() != null) {
            HomeController.clickBackButton((String) com.github.saacsos.FXRouter.getData());
        } else {
            HomeController.clickBackButton();
        }
    }
}
