package ku.cs.models.requests;

import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.services.DataSources;
import ku.cs.services.RequestListFileDataSource;

import java.util.ArrayList;

public class Request {
    private String username;
    private String requestHeader;
    private String requestDetail;
    private DataSources<RequestList> dataSources = new RequestListFileDataSource("data", "requestDataSources.csv");
    private RequestList requestList;

    public Request(String username, String requestHeader, String requestDetail) {
        this.username = username;
        this.requestHeader = requestHeader;
        this.requestDetail = requestDetail;
    }

    public String getUsername() {
        return username;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public String getRequestDetail() {
        return requestDetail;
    }
    public boolean isRequestDuplicate(){

        requestList = dataSources.readData();
        ArrayList<Request> requests = requestList.getAllRequests();

        for(Request request : requests){
            if(request.getUsername().equals(getUsername())) return true;
        }

        return false;
    }
    public String toCSV(){
        return this.getUsername() + "," + this.getRequestHeader() + "," + this.getRequestDetail();
    }
}
