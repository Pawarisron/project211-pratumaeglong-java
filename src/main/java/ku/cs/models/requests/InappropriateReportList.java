package ku.cs.models.requests;

import java.util.ArrayList;

public class InappropriateReportList {
    private ArrayList<InappropriateReport> inappropriateReports;

    public InappropriateReportList() {
        // ใช้ new เพื่อสร้าง instance ของ ArrayList
        inappropriateReports = new ArrayList<>();
    }
    public void addInappropriateReport(InappropriateReport inappropriateReport) {
        // เรียก method add จาก ArrayList เพื่อเพิ่มข้อมูล
        inappropriateReports.add(inappropriateReport);
    }
    public ArrayList<InappropriateReport> getAllInappropriateReports() {
        return inappropriateReports;
    }
}
