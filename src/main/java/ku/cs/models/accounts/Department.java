package ku.cs.models.accounts;

import ku.cs.models.Status;
import ku.cs.models.reports.ReportGroup;
import ku.cs.models.reports.ReportGroupList;
import ku.cs.services.StatusDataSource;

import java.util.*;

public class Department {
    private String id;
    private String name;

    private ReportGroupList reportGroupList;

    private StatusDataSource statusDataSource = new StatusDataSource("data","status.csv");
    public Department(String name,ReportGroupList reportGroupList,Boolean wirteStatus){
        if(wirteStatus){
            Status status = statusDataSource.readData();
            if(status != null){
                this.id = String.valueOf(status.getDepartmentCount() +1 );
                status.increaseDepartmentCount();
                statusDataSource.writeData(status,false);
            }else {
                this.id = "1";
            }
        }else {
            this.id = "1";
        }
        this.name = name;
        this.reportGroupList = reportGroupList;
    }

    public Department(String id, String name,ReportGroupList reportGroupList) {
        this.id = id;
        this.name = name;
        this.reportGroupList = reportGroupList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toCSV() {
        return id + "|" + name + "|" + reportGroupList.toCSV();
    }

    @Override
    public boolean equals(Object that) {
        if(that != null){
            Department department = (Department) that;
            if(Objects.equals(this.id, department.id) && Objects.equals(this.name, department.name)){
                return true;
            }
        }
        return false;
    }
    public Boolean sameID(String otherID){
        return id.equals(otherID);
    }

    public ReportGroupList getReportGroupList() {
        return reportGroupList;
    }

    @Override
    public String toString() {
        return name + "";
    }


}
