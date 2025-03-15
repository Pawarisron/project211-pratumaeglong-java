package ku.cs.models.requests;

import ku.cs.services.DataSources;
import ku.cs.services.InappropriateReportListFileDataSource;
import ku.cs.services.RequestListFileDataSource;

import java.util.ArrayList;

public class InappropriateReport {
    private String username;
    private String reportHeader;
    private String type;
    private String inappropriateReportHeader;
    private String inappropriateReportDetail;
    private InappropriateReportListFileDataSource dataSources = new InappropriateReportListFileDataSource();
    private InappropriateReportList inappropriateReportList;

    public InappropriateReport(String username, String reportHeader, String type, String inappropriateReportHeader, String inappropriateReportDetail) {
        this.username = username;
        this.reportHeader = reportHeader;
        this.type = type;
        this.inappropriateReportHeader = inappropriateReportHeader;
        this.inappropriateReportDetail = inappropriateReportDetail;
    }

    public String getUsername() {return username;}
    public String getReportHeader() {
        return reportHeader;
    }

    public String getType() {
        return type;
    }

    public String getInappropriateReportHeader() {
        return inappropriateReportHeader;
    }

    public String getInappropriateReportDetail() {
        return inappropriateReportDetail;
    }

    public boolean isDuplicate(){

        inappropriateReportList = dataSources.readData();
        ArrayList<InappropriateReport> inappropriateReports = inappropriateReportList.getAllInappropriateReports();

        for(InappropriateReport inappropriateReport : inappropriateReports){
            if(inappropriateReport.getReportHeader().equals(getReportHeader())) return true;
        }

        return false;
    }
    public String toCSV(){
        return this.getUsername() + "," + this.getReportHeader() + "," + this.getType() + "," + this.getInappropriateReportHeader() + "," + this.getInappropriateReportDetail();
    }
}
