package ku.cs.services;

import java.io.*;
import java.util.ArrayList;

public interface DataSourceList<T, S>{

    /*
    T คือ Class List
    S คือ Class ที่ list นั้นเก็บ
     */

    String getDirectoryName();
    String getFileName();
    String getSplitBy();

    //ส่วนของอ่านไฟล์
    T createT();
    void add(T t,String[] data);
    //ส่วนของเขียนไฟล์
    ArrayList<S> getAllValue(T t);
    String toCSV(S s);
    default void checkFileIsExisted(){
        File file = new File( getFileName() );
        if ( ! file.exists()){
            file.mkdirs();
        }
        String filePath = getDirectoryName() + File.separator + getFileName();
        file = new File(filePath);
        if ( ! file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    default T readData() {
        T t = createT();
        String filePath = getDirectoryName() + File.separator + getFileName();
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
                    String[] data = line.split( getSplitBy() );
                    add(t,data);
                }
            }
        } catch (FileNotFoundException err) {
            throw new RuntimeException(err);
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
        return t;
    }
    default void writeOneLineData(S s, boolean append) {
        String filePath = getDirectoryName() + File.separator + getFileName();
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, append);
            buffer = new BufferedWriter(writer);
            String line = toCSV(s);
            buffer.append(line);
            buffer.newLine();
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

    default void writeData(T t, Boolean append) {
        String filePath = getDirectoryName() + File.separator + getFileName();
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, append);
            buffer = new BufferedWriter(writer);
            for (S s : getAllValue(t)) {
                String line = toCSV(s);
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
