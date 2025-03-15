package ku.cs.services;

import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.models.accounts.StaffAccount;
import ku.cs.models.accounts.StudentAccount;

import java.io.*;

public class AccountListFileDataSource implements DataSources<AccountList> {

    private final String directoryName;
    private final String fileName;

    public AccountListFileDataSource(String directoryName, String fileName) {
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
    public AccountList readData() {
        AccountList accountList = new AccountList();
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

                    if (data[0].equals("Staff")) {
                        StaffAccount staffAccount = new StaffAccount(
                                data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), data[5].trim(), data[6].trim()
                                , Boolean.parseBoolean(data[7].trim()));
                        accountList.addAccount(staffAccount);

                    } else if (data[0].equals("Student")) {
                        StudentAccount studentAccount = new StudentAccount(
                                data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), data[5].trim(), Boolean.parseBoolean(data[6].trim()));
                        accountList.addAccount(studentAccount);

                    } else if (data[0].equals("Admin")) {
                        Account account = new Account(
                                data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), data[5].trim(), Boolean.parseBoolean(data[6].trim()));
                        accountList.addAccount(account);

                    }
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
        return accountList;
    }

    // Make it to Get Append to make overwriting
    @Override
    public void writeData(AccountList accountList, Boolean append) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, append);
            buffer = new BufferedWriter(writer);
            for (Account account : accountList.getAllAccounts()) {
                String line = account.toCSV();
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
