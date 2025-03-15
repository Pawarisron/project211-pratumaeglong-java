package ku.cs.models;


/*
class ที่เอาไว้ใช้เก็บ สถานะ ต่างๆ ใน software ของเรา
 */
public class Status {
    private int departmentCount;
    private int reportGroupCount;
    private int reportCount;

    public Status(){
        super();
    }

    public Status(String departmentCount, String reportGroupCount, String reportCount) {
        this.departmentCount = Integer.parseInt(departmentCount);
        this.reportGroupCount = Integer.parseInt(reportGroupCount);
        this.reportCount = Integer.parseInt(reportCount);
    }

    public int getDepartmentCount() {
        return departmentCount;
    }

    public void increaseDepartmentCount() {
        this.departmentCount++;
    }

    public void increaseReportCount(){
        this.reportCount++;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void increaseReportGroupCount(){
        this.reportGroupCount++;
    }
    public int getReportGroupCount() {
        return reportGroupCount;
    }


    public String toCSV(){
        return departmentCount + "," +reportGroupCount + "," + reportCount;
    }
}
