package ku.cs.models.accounts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ku.cs.models.reports.ReportGroup;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class DepartmentList{
    private ArrayList<Department> departments;

    public DepartmentList() {
        departments = new ArrayList<>();
    }
    public void addDepartment(Department department){
        this.departments.add(department);
    }
    public ArrayList<Department> getAllDepartments(){
        return departments;
    }

    public ObservableList<Department> getAllDepartmentsObservableList(){
        //เอาไว้ใช่กับ TableView
        ObservableList<Department> departmentObservableList = FXCollections.observableArrayList();
        departmentObservableList.addAll(departments);
        return departmentObservableList;
    }


    public boolean contains(String name){
        for(Department department :departments){
            if(department.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public boolean contains(String name,String exceptName){
        for(Department department :departments){
            if(department.getName().equals(name) && !department.getName().equals(exceptName)){
                return true;
            }
        }
        return false;
    }

    public boolean contains(Department department){

        if(department != null){
            if(departments.contains(department)){
                return true;
            }
        }
        return false;
    }
    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public void addAllDepartment(ArrayList<Department> arrayList){
        this.departments.addAll(arrayList);
    }

    public int indexOf(Department department){
        return departments.indexOf(department);
    }

    public void set(int index, Department department){
        departments.set(index,department);
    }

    public Department get(int index){
        return departments.get(index);
    }
    public Department getByID(int id){
        for (Department department : departments){
            if(department.getId().equals(String.valueOf(id))){
                return department;
            }
        }
        return null;
    }
    public void deleteReportGroup(String reportGroupID){
        for(Department department : departments){
            if(department.getReportGroupList() != null){
                department.getReportGroupList().delete(reportGroupID);
            }
        }
    }
    public boolean deleteDepartment(Department department){
        if(departments.remove(department)){
            return true;
        }
        return false;
    }


    public String getName(int departmentID){
        //เอาไว้ใช่หา name โดย ผ่านค่า ID ไป
        if(departmentID >0 ){
            for(Department department :departments){
                String id = String.valueOf(departmentID);
                if(department.getId().equals(id)){
                    return department.getName();
                }
            }
        }
        return null;
    }
    public String getName(String departmentID){
        /*เอาไว้ใช่หา name โดย ผ่านค่า ID ไป
        ถ้าเจอ id จะ return string
        ถ้าไม่เจอหรือ เกิด error จะ return null
         */
        if(departmentID != null){
            try{
                int data = Integer.parseInt( departmentID);
                if(  data >0 ){
                    for(Department department :departments){
                        String id = String.valueOf(departmentID);
                        if(department.getId().equals(id)){
                            return department.getName();
                        }
                    }
                }
            }catch (NumberFormatException e){
                return null;
            }
        }
        return null;
    }

    public Department fondDepartment(String id){
        Department found = null;
        for(Department department : departments){
            if(department.sameID(id)){
                found = department;
            }
        }
        return found;
    }


    @Override
    public String toString() {
        return "DepartmentList{" +
                "departments=" + departments +
                '}';
    }
}
