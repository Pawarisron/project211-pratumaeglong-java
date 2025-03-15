package ku.cs.models.reports;

/*
class to collect Type/ Group of report by collect name and requiment type
 */

import ku.cs.models.Status;
import ku.cs.services.ReportGroupListDataSource;
import ku.cs.services.StatusDataSource;

public class ReportGroup {
    private RequirementTypeGroup repuirement;
    private String id;
    private String name;
    private String detail;
    private StatusDataSource statusDataSource = new StatusDataSource("data","status.csv");
    private ReportGroupListDataSource reportGroupListDataSource = new ReportGroupListDataSource();

    public ReportGroup(RequirementTypeGroup repuirement,String id,String name, String detail) {
        if(detail == null || detail.isBlank()){
            this.detail = "-";
        }else {
            this.detail = detail;
        }
        this.id = id;
        this.name = name;
        this.repuirement = repuirement;
    }
    public ReportGroup(RequirementTypeGroup repuirement,String name,String detail, Boolean wirteFileStatus){
        if(detail == null || detail.isBlank()){
            this.detail = "-";
        }else {
            this.detail = detail;
        }
        this.name = name;
        this.repuirement = repuirement;

        if(wirteFileStatus){
            Status status = statusDataSource.readData();
            if(status != null){
                this.id = String.valueOf( status.getReportGroupCount() +1);
                status.increaseReportGroupCount();
                statusDataSource.writeData(status,false);
            }
        }
    }

    public String getName() {
        return name;
    }
    public RequirementTypeGroup getRepuirement() {
        return repuirement;
    }

    public Boolean equals(ReportGroup other){
        return id.equals(other.id);
    }
    public Boolean find(String name){return this.name.equals(name);}

    public String getId() {
        return id;
    }

    public String getDetail() {
        return detail;
    }

    public void compressDetail(){
        detail = detail.replaceAll("\\|", "?PIPE_IGNORE?");
        detail = detail.replaceAll("\\n", "?NEW_LINE?");
    }
    public void decompressDetail(){
        detail = detail.replaceAll("\\?NEW_LINE\\?", "\n");
        detail = detail.replaceAll("\\?PIPE_IGNORE\\?", "|");
    }

    @Override
    public String toString() {
        return name+"";
    }

    public String toCSV(){
        return repuirement + "|" + id + "|" + name + "|" +detail;
    }

    public boolean equals(String something) {
        return (something.equals(id) || something.equals(name));
    }
}
