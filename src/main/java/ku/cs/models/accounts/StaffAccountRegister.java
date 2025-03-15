package ku.cs.models.accounts;

import ku.cs.services.DataSources;

public class StaffAccountRegister extends AccountRegister{
    private final String departmentID;

    public StaffAccountRegister(String level, String username, String password, String accountName,String departmentID,
                                String banStatus, String loginTime, String confirmPassword, DataSources<AccountList> dataSources) {
        super(level, username, password, accountName, banStatus, loginTime, confirmPassword, dataSources);
        this.departmentID = departmentID;
    }


    @Override
    public boolean isFillUp(){
        //เอาไว้ช็ค ว่า ต้องใส่ให้ครบทุกช่อง
        if(isDepartmentIDFillUp()){
            return !super.getConfirmPassword().isEmpty() && !super.getPassword().isEmpty() && !super.getUsername().isEmpty() && !super.getAccountName().isEmpty()
                    && !departmentID.isEmpty();
        }
        return false;

    }
    @Override
    public boolean signUp(){
        AccountList accountList = new AccountList();

        //สร้าง account และเพิ่มใน accountList
        StaffAccount staffAccount = new StaffAccount(getLevel(), getUsername(), getPassword(), getAccountName(),departmentID, getBanStatus(), getLoginTime());
        accountList.addAccount(staffAccount);

        //write data ใน file accountDataSources.csv
        super.getDataSources().writeData(accountList, true);

        return true;
    }
    public boolean isDepartmentIDFillUp(){
        if(departmentID != null){
            return true;
        }
        return false;
    }




}
