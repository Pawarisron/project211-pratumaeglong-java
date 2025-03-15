package ku.cs.services;

import ku.cs.models.reports.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.TreeSet;

public class ReportListFileDataSource implements DataSources<ReportList>{
    private final String directoryName;

    private final String fileName;

    public ReportListFileDataSource(String directoryName, String fileName) {
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
    public ReportList readData() {
        ReportList reportLists = new ReportList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {

            reader = new FileReader(file);
            buffer = new BufferedReader(reader);
            String line = "";
            while((line = buffer.readLine()) != null ) {
                if(!line.equals("")){

//                    return username + "|" + group.getName() + "|" + group.getRepuirement()
//                            + "|" + time + "|" + reportHeader + "|" + reportDetail + "|" + reportStatus.toString() + "|" + voteToCSV();
                    String[] data = line.split("\\|");

                    TreeSet<String> vote = null;

                    // data 6 is a status of the report
                    ReportStatus status = ReportStatus.valueOf(data[6]);

                    // data 7 is a set of voter
                    if(! data[7].equals("novoter")) {
                        String[] voteData = data[7].split(",");
                        vote = new TreeSet<>(Arrays.asList(voteData));
                    }
                    Report report = new Report(data[0].trim(),data[1],data[2].trim(),data[3].trim(),data[4].trim(),data[5].trim(), vote, status);
                    reportLists.addReport(report);
                    // ทําให้ในส่วน ของ reportDetail ที่อัดเป็นบรรทัดเดี่ยว กลายเป็น ปกติ
                    report.deCompressReportDetail();
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

        return reportLists;

    }

    @Override
    public void writeData(ReportList reportList, Boolean append) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, append);
            buffer = new BufferedWriter(writer);
            for (Report report : reportList.getAllReport()){

                //toCSV ด้านในจะมีการ อัดให้ String อยู่ในบรรทัดเดี่ยวกัน
                String line = report.toCSV();
                buffer.append(line);
                buffer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
