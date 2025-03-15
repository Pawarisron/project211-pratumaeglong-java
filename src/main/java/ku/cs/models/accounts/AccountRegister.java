package ku.cs.models.accounts;

import ku.cs.models.accounts.Account;
import ku.cs.models.accounts.AccountList;
import ku.cs.services.DataSources;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ku.cs.services.StringChecker.*;

/*
 เอาไว้ใช้เช็ค input ของ Register
*/

public class AccountRegister extends Account {
    private final String confirmPassword ;

    private final DataSources<AccountList> dataSources;
    //สร้าง Pattern
    private final Pattern whitespace = Pattern.compile("\\s"); //สร้าง Pattern
    private Matcher matcherWhitespace;

    public AccountRegister(String level, String username, String password, String accountName, String banStatus, String loginTime, String confirmPassword, DataSources<AccountList> dataSources) {
        super(level, username, password, accountName, banStatus, loginTime);
        this.confirmPassword = confirmPassword;
        this.dataSources = dataSources;
    }

    public String checkFormatUsername(){
        // เก็บ String ที่เป็น Error string
        String userErr = "";
        String username = getUsername();

        //ตรวจสอบข้อมูลเข้าเป็น String ว่างมั้ย
        if(Objects.equals(username, "")) return "";

        //ตรวจสอบว่า ใน string เจอ whitespace มั้ย
        matcherWhitespace = whitespace.matcher( username.stripTrailing() ); //String ที่เข้าไป จะลบ Whitespace เฉพาะส่วนหางออก(ส่วนหลังออก)

        //username ส่วนของ Error Message
        if( username.length() >16 ){
            userErr += "ตัวอักษรทั้งหมดห้ามเกิน 16 ตัว\n";
        }
        if( !username.stripTrailing().matches("[a-zA-Z0-9]+") ){ //Regular Expressions (regex)
            userErr += "ต้องเป็นภาษาอังกฤษเท่านั้น\n";
        }
        if( isAllNumeric(username) ){
            userErr += "ต้องมีตัวอักษรอย่างน้อยหนึ่งตัว\n";
        }
        if( matcherWhitespace.find() ){
            userErr += "ต้องไม่มีเว้นวรรค\n";
        }
        if( username.length() < 6 ){
            userErr += "ตัวอักษรต้องยาวกว่าหรือเท่ากับ 6 ตัว\n";
        }
        return userErr;
    }

    public String checkFormatPassword(){
        // เก็บ String ที่เป็น Error string
        String passErr = "";
        String password = getPassword();

        //ตรวจสอบข้อมูลเข้าเป็น String ว่างมั้ย
        if(Objects.equals(password, "")) return "";

        //password ส่วนของ Error Message
        if( isAllLower(password) ){
            passErr += "ต้องมีตัวอักษรตัวใหญ่อย่างน้อยหนึ่งตัว\n";
        }
        if( isAllUpper(password) ){
            passErr += "ต้องมีตัวอักษรตัวเล็กอย่างน้อยหนึ่งตัว\n";
        }
        if( isAllNumeric(password) ){
            passErr += "ต้องมีตัวอักษรอย่างน้อยหนึ่งตัว\n";
        }
        if( isAllLetter(password) ){
            passErr += "ต้องมีตัวเลขอย่างน้อยหนึ่งตัว\n";
        }

        //ตรวจสอบว่า ใน string เจอ whitespace มั้ย & เปลี่ยน String ที่เอามาเทียบ
        matcherWhitespace = whitespace.matcher( password.stripTrailing() ); //String ที่เข้าไป จะลบ Whitespace เฉพาะส่วนหางออก หรือ ส่วนหลังออก

        if( matcherWhitespace.find() ){
            passErr += "ต้องไม่มีเว้นวรรค\n";
        }
        if( password.length() < 8 ){
            passErr += "ตัวอักษรต้องยาวกว่าหรือเท่ากับ 8 ตัว\n";
        }
        if( password.length() > 16){
            passErr += "ตัวอักษรค้องยาวน้อยกว่า 16 ตัว\n";
        }
        return passErr;
    }

    public boolean isFillUp(){
        //เอาไว้ช็ค ว่า ต้องใส่ให้ครบทุกช่อง
        return !confirmPassword.isEmpty() && !super.getPassword().isEmpty() && !super.getUsername().isEmpty() && !super.getAccountName().isEmpty();
    }

    public boolean isPasswordEqualConfirmPassword(){
        //เอาไว้เช็คว่า password กับ confirmPassword ตรงกันมั้ย
        return !super.getPassword().equals( confirmPassword ) ;
    }

    public boolean isUsernameSimilarPassword(){
        //เช็คว่า username เหมือนกับ password มั้ย
        return getUsername().equalsIgnoreCase(getPassword());
    }
    public boolean isUserDuplicate(){

        AccountList data = dataSources.readData();
        ArrayList<Account> accounts =  data.getAllAccounts();

        if(accounts.size() == 0) return false;

        for(Account account : accounts ){
            if(account.getUsername().equals(getUsername())) return true;
        }

        return false;
    }
    public boolean signUp(){
        AccountList accountList = new AccountList();

        //สร้าง account และเพิ่มใน accountList
        Account account = new Account(getLevel(), getUsername(), getPassword(), getAccountName(), getBanStatus(), getLoginTime(), isRequestStatus());
        accountList.addAccount(account);

        //write data ใน file accountDataSources.csv
        dataSources.writeData(accountList, true);

        return true;

    }
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public DataSources<AccountList> getDataSources() {
        return dataSources;
    }
}
