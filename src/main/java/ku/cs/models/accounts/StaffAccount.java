package ku.cs.models.accounts;

/*
    Sub Class ของ Account จะเก็นข้อมูลเพิ่มเติมของ Account ของเจ้าหน้าที่ ซึ่งมี
        username
        accountname
        password
        departments in charge (this subclass)

*/

public class StaffAccount extends Account {
    private String departmentsInChargeID;

    public StaffAccount(String level, String username, String password, String accountName, String departmentsInChargeID, String banStatus, String loginTime) {
        super(level, username, password, accountName, banStatus, loginTime);
        this.departmentsInChargeID = departmentsInChargeID;
    }

    public StaffAccount(String level, String username, String password, String accountName, String departmentsInChargeID, String banStatus, String loginTime, boolean requestStatus) {
        super(level, username, password, accountName, banStatus, loginTime, requestStatus);
        this.departmentsInChargeID = departmentsInChargeID;
    }

    public String getDepartmentsInChargeID() {
        return departmentsInChargeID;
    }

    public String toCSV() {
        return getLevel() + "," + getUsername() + "," + getPassword() + "," + getAccountName() + "," + getDepartmentsInChargeID() + "," + getBanStatus() + "," + getLoginTime() + "," + isRequestStatus();
    }

}
