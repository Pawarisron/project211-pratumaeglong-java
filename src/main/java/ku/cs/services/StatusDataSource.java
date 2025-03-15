package ku.cs.services;

import ku.cs.models.Status;
import ku.cs.models.accounts.Department;
import ku.cs.models.accounts.DepartmentList;

import java.io.*;

public class StatusDataSource implements DataSources<Status>{

    private final String directoryName;

    private final String fileName;

    public StatusDataSource(String directoryName, String fileName) {
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
        //สร้าง ข้อมูลพื้นฐาน ในไฟล์เริ่มต้นถ้า ไฟล์นั้น ไม่มีข้อมูลอะไรอยู่เลย
        if(file.length() == 0){
            FileWriter writer = null;
            BufferedWriter buffer = null;
            try {
                writer = new FileWriter(file);
                buffer = new BufferedWriter(writer);
                //ข้อมูลเริ่มต้น
                buffer.append("0,0,0");
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

    @Override
    public Status readData() {
        Status status = new Status();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            String line = "";
            if((line = buffer.readLine()) != null){
                //เช็ค line ต้องไม่ใช้ String ว่าง
                if (!line.isBlank()) {
                    String[] data = line.split(",");
                    status = new Status(data[0],data[1],data[2]);
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    @Override
    public void writeData(Status status, Boolean append) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {

            writer = new FileWriter(file, append);
            buffer = new BufferedWriter(writer);
            buffer.append(status.toCSV());

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
