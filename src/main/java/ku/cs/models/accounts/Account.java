package ku.cs.models.accounts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Account {
    private String level;
    private String username;
    private String password;
    private String accountName;
    private String banStatus; //ค่าลบไม่โดนแบน, ค่า 0 โดนแบน, มากกว่า 0 คือจน.ครั้งที่พยายามเข้าตอนโดนแบน
    private String loginTime;
    private boolean requestStatus; // true:โดนแบนแล้วร้องขอปลดมา false:ไม่ได้ส่งคำขอปลดมา


    public Account(String level, String username, String password, String accountName, String banStatus, String loginTime, boolean requestStatus) {
        this.level = level;
        this.username = username;
        this.password = password;
        this.accountName = accountName;
        this.banStatus = banStatus;
        this.loginTime = loginTime;
        this.requestStatus = requestStatus;
    }

    public Account(String level, String username, String password, String accountName, String banStatus, String loginTime) {
        this(level, username, password, accountName, banStatus, loginTime, false);
    }

    public void tryToLoginCount(){
        int banCount = Integer.parseInt(this.banStatus);
        if(banCount >= 0){
            banCount += 1;
        }
        this.banStatus = String.valueOf(banCount);
    }

    // to check the same preson
    public Boolean isSamePerson(String otherName){
        return this.username.equals(otherName);
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getLevel() {
        return level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBanStatus() {return banStatus;}
    public String getBanStatusString() {
        String banStatusString = "ถูกระงับ";
        if(banStatus == "-1"){banStatusString = "ไม่ถูกระงับ";}
        return banStatusString;
    }

    public void setBanStatus(String banStatus) {this.banStatus = banStatus;}

    public void setLoginTime(){
        LocalDateTime loginTime = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.loginTime = loginTime.format(myFormatObj);
    }

    public String getLoginTime(){return loginTime;}
    public void makeRequest() {
        this.requestStatus = true;
    }

    public boolean isRequestStatus() {
        return requestStatus;
    }
    public void banAccount(){
        this.banStatus = String.valueOf(0);
    }
    public void unbanAccount(){
        this.banStatus = String.valueOf(-1);
        this.requestStatus = false;
    }

    public int compareLoginTime(Account other){
        return loginTime.compareTo(other.getLoginTime());
    }

    @Override
    public String toString() {
        return "Account{" +
                "level='" + level + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountName='" + accountName + '\'' +
                ", banStatus='" + banStatus + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }

    public String toCSV() {
        return getLevel() + "," + getUsername() + "," + getPassword() + "," + getAccountName() + "," + getBanStatus() + "," + getLoginTime() + "," + isRequestStatus();
    }

}
