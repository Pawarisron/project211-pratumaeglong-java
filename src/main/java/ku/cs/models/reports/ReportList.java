package ku.cs.models.reports;

import java.util.ArrayList;

public class ReportList {
    private ArrayList<Report> reports;

    public ReportList(){
        // ใช้ new เพื่อสร้าง instance ของ ArrayList
        reports = new ArrayList<>();
    }

    public void addReport(Report report){
        // เรียก method add จาก ArrayList เพื่อเพิ่มข้อมูล
        reports.add(report);
    }
    public boolean setAllReportsIdByName(ReportGroup deletedReportGroup){
        if(deletedReportGroup == null){
            return false;
        }
        for(Report report : reports){
            if(report.getGroupID().equals(deletedReportGroup.getId())){
                report.setGroupID(deletedReportGroup.getName());
            }
        }
        return true;
    }

    public boolean contains(Report report){
        if(report != null){
            return reports.contains(report);
        }
        return false;
    }
    public boolean containsID(String id){
        if(id != null){
            for (Report report : reports){
                if(report.getId().equals(id)){
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Report> getAllReport(){
        return reports;
    }


    public ReportList filterBy(Filterer<Report> filterer){
        ReportList filtedReport = new ReportList();
        for(Report report : reports){
            if(filterer.filter(report)){
                filtedReport.addReport(report);
            }
        }
        return filtedReport;
    }

    public void addAllList(ReportList other){
        reports.addAll(other.getAllReport());
    }

}
