package ku.cs.services;

import ku.cs.models.accounts.*;
import ku.cs.models.reports.ReportGroupList;

import java.io.*;
import java.util.Arrays;

public class DepartmentListFileDataSource implements DataSources<DepartmentList>{
    private final String directoryName;

    private final String fileName;

    public DepartmentListFileDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted(){
        File file = new File(directoryName);
        if ( ! file.exists()){
            file.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if ( ! file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public DepartmentList readData() {
        DepartmentList departmentList = new DepartmentList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {
                //เช็ค line ต้องไม่ใช้ String ว่าง
                if (!line.equals("")) {

                        String[] data = line.split("\\|");

                        //เพิ่ม reportGroupList
                        ReportGroupList reportGroupList = new ReportGroupList();
                        if(data.length == 3) {
                            String[] subData = data[2].trim().split(",");
                            reportGroupList.addAllReportGroup(subData);
                        }
                        //เพิ่ม department
                        Department department = new Department(data[0].trim(),data[1].trim(),reportGroupList);
                        departmentList.addDepartment(department);



                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return departmentList;
    }

    @Override
    public void writeData(DepartmentList departmentList, Boolean append) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, append);
            buffer = new BufferedWriter(writer);
            for (Department department : departmentList.getAllDepartments()) {
                buffer.append(department.toCSV());
                buffer.newLine();
            }
//            System.out.println("write department success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
