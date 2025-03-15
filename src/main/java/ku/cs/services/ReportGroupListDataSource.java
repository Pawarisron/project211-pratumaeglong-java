package ku.cs.services;

import ku.cs.models.reports.*;

import java.awt.*;
import java.util.ArrayList;

public class ReportGroupListDataSource implements DataSourceList<ReportGroupList,ReportGroup>{
    private static final String DIRECTORY_NAME = "data";
    private static final String FILE_NAME = "reportGroupDataSources.csv";
    private static final String SPLIT_BY = "\\|";

    public ReportGroupListDataSource() {
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
    public ReportGroupList createT() {
        return new ReportGroupList();
    }
    @Override
    public void add(ReportGroupList reportGroupList, String[] data) {
        ReportGroup reportGroup = new ReportGroup(RequirementTypeGroup.valueOf( data[0].trim() ), data[1].trim(), data[2].trim(), data[3].trim() );
        reportGroupList.addReportGroup(reportGroup);
    }
    @Override
    public ArrayList<ReportGroup> getAllValue(ReportGroupList reportGroupList) {
        return reportGroupList.getAllReportGroups();
    }
    @Override
    public String toCSV(ReportGroup reportGroup) {
        return reportGroup.toCSV();
    }




}
