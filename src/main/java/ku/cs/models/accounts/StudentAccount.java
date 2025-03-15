package ku.cs.models.accounts;

/*
    Sub Class ของ Account จะเก็นข้อมูลเพิ่มเติมของ Account ของนิสิต ซึ่งมี
        username
        accountname
        password
        report (this subclass)

*/

import ku.cs.models.accounts.Account;

public class StudentAccount extends Account {
    private String myReport;

    public StudentAccount(String level, String username, String password, String accountName, String banStatus, String loginTime) {
        super(level, username, password, accountName, banStatus, loginTime);
        this.myReport = null;
    }
    public StudentAccount(String level, String username, String password, String accountName, String banStatus, String loginTime, boolean requestStatus) {
        super(level, username, password, accountName, banStatus, loginTime, requestStatus);
        this.myReport = null;
    }

    //    public void addReport(String group,String reportHeader, String reportDetail) {
//
////        this.myReport = new Report(super.getUsername() ,group,reportHeader, reportDetail);
//    }

    public String getMyReport() {
        return myReport;
    }

    public String toCSV() {
        return getLevel() + "," + getUsername() + "," + getPassword() + "," + getAccountName() + "," + getBanStatus() + "," + getLoginTime() + "," + isRequestStatus();
    }
}
