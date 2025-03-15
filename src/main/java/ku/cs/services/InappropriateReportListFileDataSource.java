package ku.cs.services;

import ku.cs.models.reports.ReportGroup;
import ku.cs.models.reports.ReportGroupList;
import ku.cs.models.reports.RequirementTypeGroup;
import ku.cs.models.requests.InappropriateReport;
import ku.cs.models.requests.InappropriateReportList;

import java.util.ArrayList;

public class InappropriateReportListFileDataSource implements DataSourceList<InappropriateReportList, InappropriateReport>{
    private static final String DIRECTORY_NAME = "data";
    private static final String FILE_NAME = "inappropriateReportDataSources.csv";
    private static final String SPLIT_BY = "\\,";

    public InappropriateReportListFileDataSource() {
        super();
        checkFileIsExisted();
    }
    @Override
    public String getDirectoryName() {
        return DIRECTORY_NAME;
    }
    @Override
    public String getFileName() {
        return FILE_NAME;
    }
    @Override
    public String getSplitBy() {
        return SPLIT_BY;
    }
    @Override
    public InappropriateReportList createT() {
        return new InappropriateReportList();
    }
    @Override
    public void add(InappropriateReportList inappropriateReportList, String[] data) {
        InappropriateReport inappropriateReport = new InappropriateReport(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim());
        inappropriateReportList.addInappropriateReport(inappropriateReport);
    }
    @Override
    public ArrayList<InappropriateReport> getAllValue(InappropriateReportList inappropriateReportList) {
        return inappropriateReportList.getAllInappropriateReports();
    }
    @Override
    public String toCSV(InappropriateReport inappropriateReport) {
        return inappropriateReport.toCSV();
    }
}
