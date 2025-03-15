package ku.cs.models.reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ku.cs.services.ReportGroupListDataSource;

import java.util.ArrayList;
import java.util.Comparator;

public class ReportGroupList {
    private ArrayList<ReportGroup> reportGroups;

    public ReportGroupList() {
        reportGroups = new ArrayList<>();
    }

    public ReportGroupList(ArrayList<ReportGroup> reportGroups) {
        this.reportGroups = reportGroups;
    }

    public ArrayList<ReportGroup> getAllReportGroups() {
        return reportGroups;
    }
    public ObservableList<ReportGroup> getAllReportGroupsObservableList(){
        //เอาไว้ใช่กับ TableView
        ObservableList<ReportGroup> reportGroupObservableList = FXCollections.observableArrayList();
        reportGroupObservableList.addAll(reportGroups);
        return reportGroupObservableList;
    }
    public void addReportGroup(ReportGroup reportGroup){
        reportGroups.add(reportGroup);
    }
    public void addAllReportGroup(String[] strings){
            if(strings.length == 0 ){
                return;
            }
            ReportGroupListDataSource reportGroupListDataSource = new ReportGroupListDataSource();
            ReportGroupList nowReportGroupList = reportGroupListDataSource.readData();

            for (String id : strings){
                ReportGroup found = nowReportGroupList.getReportGroup(id);
                reportGroups.add(found);
            }
    }
    public void addAllReportGroupsObservableList(ObservableList<ReportGroup> observableList){
        reportGroups.addAll(observableList);
    }

    public ReportGroup getReportGroup(String id){
        if(id != null){
            for (ReportGroup reportGroup : reportGroups){
                if(reportGroup.getId().equals(id)){
                    return reportGroup;
                }
            }
        }
        return null;
    }


    //ใช้ตรวจสอบว่ามี name ซํ้ามั้ย
    public boolean isDuplicateName(String name){
        for (ReportGroup reportGroup : reportGroups){
            if(reportGroup.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public boolean isDuplicateName(String name,String exceptName){
        for (ReportGroup reportGroup : reportGroups){
            if(reportGroup.getName().equals(name) && !reportGroup.getName().equals(exceptName)){
                return true;
            }
        }
        return false;
    }
    //ใช้แก้ไขข้อมูลใน ReportGroupList
    public boolean editBy(ReportGroup editThis, ReportGroup byThis){
        if(editThis != null && byThis != null){
            if(reportGroups.contains(editThis)){
                int index = reportGroups.indexOf(editThis);
                reportGroups.set(index,byThis);
                return true;
            }
        }
        return false;
    }
    //ใช้ลบข้อมูลใน reportGroup
    public boolean delete(ReportGroup deleteReportGroup){
        if(deleteReportGroup != null){
            if(reportGroups.contains(deleteReportGroup)){
                reportGroups.remove(deleteReportGroup);
                return true;
            }
        }
        return false;
    }
    public boolean delete(String reportGroupID){
        if(reportGroupID != null){
            for(ReportGroup reportGroup : reportGroups){
                if(reportGroup != null){

                    if(reportGroup.getId().equals(reportGroupID)){
                        reportGroups.remove(reportGroup);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean deleteAll(ReportGroupList deleteReportGroupList){
        if(deleteReportGroupList != null){
            for (ReportGroup found : deleteReportGroupList.getAllReportGroups()){
                this.delete(found.getId());
            }
            return true;
        }
        return false;
    }
    public boolean contains(ReportGroup reportGroup){
        if(reportGroup != null){
            if(reportGroups.contains(reportGroup)){
                return true;
            }
        }
        return false;
    }

    public void compressDetailAll(){
        for(ReportGroup reportGroup : reportGroups){
            reportGroup.compressDetail();
        }
    }

    // หา reportGroup ใน lsit
    public ReportGroup getReportGroupByName(String name){
        ReportGroup thisGroup = null;
        for(ReportGroup group : reportGroups){
            if(group.find(name)){
                thisGroup = group;
            }
        }

        return thisGroup;
    }

    @Override
    public String toString() {
        return "ReportGroupList{" +
                "reportGroups=" + reportGroups +
                '}';
    }

    public String toCSV() {
        String line = "";
        //sort ก่อน แล่วค่อยเขียนไฟล์
        if(reportGroups != null || !reportGroups.isEmpty()){
            reportGroups.sort(new Comparator<ReportGroup>() {
                @Override
                public int compare(ReportGroup o1, ReportGroup o2) {
                    if(o1 == null){
                        return -1;
                    }
                    if(o2 == null){
                        return 1;
                    }
                    return Integer.compare( Integer.parseInt(o1.getId()), Integer.parseInt(o2.getId()) );
                }
            });
            for (ReportGroup reportGroup : reportGroups) {
                if(reportGroup != null){
                    line = line + reportGroup.getId().trim() + ",";
                }
            }
        }
        return line;

    }
    public ReportGroup findGroup(String gruopID){
        for(ReportGroup reportGroup : reportGroups){
            if(reportGroup.equals(gruopID)){
                return reportGroup;
            }
        }
        return null;
    }
}
