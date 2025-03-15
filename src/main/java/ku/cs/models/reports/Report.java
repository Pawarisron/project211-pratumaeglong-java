package ku.cs.models.reports;

import ku.cs.models.Status;
import ku.cs.models.accounts.Department;
import ku.cs.services.StatusDataSource;

import java.util.TreeSet;

/*
    - Header หัวข้อการรายงาน
    - Detail เนื้อหา
    - isSolved ส่วนที่ใช้ตรวจสอบว่าปัญหา ได้ แก้แล้วหรือยัง
    - ไว้ใช้งานใน User package
 */
public class Report {
    private String username;

    private StatusDataSource statusDataSource = new StatusDataSource("data","status.csv");
    private String id;
    private String groupID;
    private String time;
    private String reportHeader;
    private String reportDetail;
    private ReportStatus reportStatus;
    private TreeSet<String> vote;

    // for use in set cell value only
    private String numberOfVote;


    public Report(String username, ReportGroup reportGroup, String time, String reportHeader, String reportDetail) {
//        this(username, group, time, reportHeader, reportDetail, new TreeSet<>(), ReportStatus.NOTSOVED);
        this.username = username;
        this.groupID = reportGroup.getId();
        this.time = time;
        this.reportHeader = reportHeader;
        this.reportDetail = reportDetail;
        this.vote = new TreeSet<>();
        this.reportStatus = ReportStatus.NOTSOVED;
        //เพิ่มข้อมูลใน status
        Status status = statusDataSource.readData();
            if(status != null){
                this.id = String.valueOf(status.getReportCount() +1);
                status.increaseReportCount();
                statusDataSource.writeData(status,false);
            }

        }
//    public Report(String username, ReportGroup group, String time, String reportHeader, String reportDetail, TreeSet<String> voter) {
//        this(username, group, time, reportHeader, reportDetail, voter, ReportStatus.NOTSOVED);
//    }

    public Report(String username, String groupID, String id, String time, String reportHeader, String reportDetail, TreeSet<String> voter, ReportStatus status) {
        this.id = id;
        this.time = time;
        this.username = username;
        this.reportHeader = reportHeader;
        this.groupID = groupID;
        this.reportDetail = reportDetail;
        if (voter != null) {
            this.vote = voter;
            int numberOfVote = this.getvote();
            this.numberOfVote = Integer.toString(numberOfVote);
        } else {
            this.numberOfVote = Integer.toString(0);
            this.vote = new TreeSet<>();
        }
        this.reportStatus = status;

    }


    // หากแก้ปัญหาได้แล้วใช้ฟังก์ชันนี้
    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String toCSV() {
        compressReportDetail();
        return username + "|" + groupID + "|" + id
                + "|" + time + "|" + reportHeader + "|" + reportDetail + "|" + reportStatus.toString() + "|" + voteToCSV();
    }

    //อัดให้ reportDetail กลายเป็น บรรทัดเดี่ยว แล้วก็สามารถแบ่งด้วย Pipe,'|' ได้
    private void compressReportDetail() {
        reportDetail = reportDetail.replaceAll("\\|", "?PIPE_IGNORE?");
        reportDetail = reportDetail.replaceAll("\\n", "?NEW_LINE?");

    }

    //คืนค่าการอัด reportDetail ให้สามารถเอาไปใช้ต่อได้
    public void deCompressReportDetail() {
        reportDetail = reportDetail.replaceAll("\\?NEW_LINE\\?", "\n");
        reportDetail = reportDetail.replaceAll("\\?PIPE_IGNORE\\?", "|");

    }

    public void vote(String username) {
        vote.add(username);
    }

    private String voteToCSV() {
        String line = "";
        if(getvote() == 0){
            line = line + "novoter";
        } for (String username : vote) {
            line = line + username.trim() + ",";
        }
        return line;
    }

    public void addSovedDetail(String detail, Department department){
        if(! detail.isEmpty()){
            reportDetail = reportDetail + "\n\n\nรายละเอียดการแก้ไข โดย " + department.getName() + "\n" + detail;
        }
    }

    // geter

    public String getUsername() {
        return username;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getTime() {
        return time;
    }

    public TreeSet<String> getAllVote() {
        return vote;
    }

    public int getvote() {
        return vote.size();
    }

    public String getReportHeader() {
        return reportHeader;
    }

    public String getReportDetail() {
        return reportDetail;
    }

    public String getId() {
        return id;
    }

    // use in collumn
    public String getNumberOfVote() {
        return numberOfVote;
    }
    public String getReportStatusToLabel(){
        if(getReportStatus().equals(ReportStatus.NOTSOVED)){
            return "ยังไม่ถูกจัดการ";
        }else if(getReportStatus().equals(ReportStatus.PROCESSONG)){
            return "อยู่ระหว่างการดำเนินการ";
        }else if(getReportStatus().equals(ReportStatus.SOLVED)){
            return "จัดการแล้ว";
        }else {
            return "ไม่สามารถระบุได้";
        }
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @Override
    public String toString() {
        return "Report{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", groupID='" + groupID + '\'' +
                '}';
    }

    // method to compare vote of to other report
    public int compareVote(Report other){
        return this.getvote() - other.getvote();
    }

    public int compareTime(Report other){
        return time.compareTo(other.getTime());
    }
}
