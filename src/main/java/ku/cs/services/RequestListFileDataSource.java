package ku.cs.services;

import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.accounts.StaffAccount;
import ku.cs.models.accounts.StudentAccount;
import ku.cs.models.requests.Request;
import ku.cs.models.requests.RequestList;

import java.io.*;

public class RequestListFileDataSource implements DataSources<RequestList>{
    private final String directoryName;
    private final String fileName;

    public RequestListFileDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public RequestList readData() {
        RequestList requestList = new RequestList();
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
                    String[] data = line.split(",");

                    Request request = new Request(
                            data[0].trim(), data[1].trim(), data[2].trim());
                    requestList.addRequest(request);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return requestList;
    }

    // Make it to Get Append to make overwriting
    @Override
    public void writeData(RequestList requestList, Boolean append) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, append);
            buffer = new BufferedWriter(writer);
            for (Request request : requestList.getAllRequests()) {
                String line = request.toCSV();
                buffer.append(line);
                buffer.newLine();
            }
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
