package ku.cs.models.requests;

import ku.cs.models.accounts.Account;

import java.util.ArrayList;

public class RequestList {
    private ArrayList<Request> requests;

    public RequestList() {
        // ใช้ new เพื่อสร้าง instance ของ ArrayList
        requests = new ArrayList<>();
    }
    public void addRequest(Request request) {
        // เรียก method add จาก ArrayList เพื่อเพิ่มข้อมูล
        requests.add(request);
    }
    public ArrayList<Request> getAllRequests() {
        return requests;
    }
}
