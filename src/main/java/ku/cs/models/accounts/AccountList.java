package ku.cs.models.accounts;

import ku.cs.models.accounts.Account;

import java.util.ArrayList;

public class AccountList {
    private ArrayList<Account> accounts;

    public AccountList() {
        // ใช้ new เพื่อสร้าง instance ของ ArrayList
        accounts = new ArrayList<>();
    }
    public void addAccount(Account account) {
        // เรียก method add จาก ArrayList เพื่อเพิ่มข้อมูล
        accounts.add(account);
    }
    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }
}
