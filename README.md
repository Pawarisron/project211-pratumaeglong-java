# project211-pratumaeglong-java
CS211 - Project<br>
วิธีทดสอบการ RUN<br>
1. Main <br>
run Main Class
2. javafx plugin<br>
MVN Clean<br>
javafx -> javafx:run<br><br>

วิธีสร้าง Jar<br>
MVN Clean<br>
MVN install<br><br>
file จะอยู่ใน target เป็น shade.jar 

# วิธีการติดตั้งโปรแกรม

---
1. clone github
2. เข้าโฟลเดอร์ project211-pratumaeglong/File
3. แตกไฟล์ target.rar
4. เข้าโฟลเดอร์ target
5. กด run CS211-Project-1.0-SNAPSHOT-shaded.jar

# โครงสร้างไฟล์

---
* folder
  * data
    * data/image --> เก็บรูปภาพ
      * data/image/account
        * >เก็บรูปภาพของ account.jpg/png/gif
      * data/image/report/report
        * >เก็บรูปภาพของเรื่องร้องเรียน.jpg/png/gif
    * accountDataSources.csv
    * departmentDataSources.csv
    * inappropriateReportDataSources.csv
    * reportGroupDataSources.csv
    * requestDataSources.csv
    * status.csv
    * studentReportDataSources.csv
  * src
    * src/main
      * src/main/java
        * ku/cs
          * fxrouter
          * >controllers --> เก็บ controller class
          * >models --> เก็บ models class
          * >services --> เก็บ service class
          * Main.java --> main class
          * ProjectApplication.java --> project class
        * module-info.java
      * src/main/resources/ku/cs
        * >admin --> เก็บ fxml ของส่วน admin
        * >bases --> เก็บ fxml ของส่วน bases
        * >designs --> เก็บ css การตกแต่ง
        * >images/account --> เก็บภาพ default
        * >staff --> เก็บ fxml ของส่วน staff
        * >user --> เก็บ fxml ของส่วน user
# สรุปความคืบหน้า

---
### ครั้งที่ 1
* ออกแบบ flowchart การเดินทางไปหน้าต่าง ๆ
* สร้าง ui หน้า home
* สร้าง ui หน้า login
  * รับค่าจาก textField ที่ใส่มาได้
* สร้าง ui หน้า register
  * รับค่าจาก textField ที่ใส่มาได้
* สร้าง controllers class ต่าง ๆ
* ปุ่มต่าง ๆ สามารถกดเพื่อเปลี่ยนไปยังหน้าถัดไปได้

### ครั้งที่ 2
* สร้าง ui ส่วน user เพิ่ม
* สร้าง ui ส่วน staff เพิ่ม
* สร้าง ui ส่วน admin เพิ่ม
* สร้าง model class
  * Account class
  * AccountList class
* สร้าง service class
  * datasource class
* สร้าง controllers class เพิ่ม
* หน้า register มีการตรวจเช็คความถูกต้องของข้อมูลที่ใส่ใน textField ทั้งหมด
* สามารถนำค่าที่รับจาก textField มาเขียนไฟล์ลงใน csv ได้แล้ว
* สามารถอ่านไฟล์จาก csv เพื่อใช้ login ได้แล้ว

### ครั้งที่ 3
* สร้าง ui ส่วน user เพิ่ม
* สร้าง ui ส่วน staff เพิ่ม
* สร้าง ui ส่วน admin เพิ่ม
* ข้อมูลของ account มีการสร้างการแบ่งประเภท
  * Admin
  * Staff
  * Student
* นำประเภทของ account มาตรวจสอบการ login เพื่อไปหน้าการใช้งานที่ต่างกัน
* มีฟังก์ชัน toCSV เพื่อเขียนไฟล์
* สร้าง models class เพิ่ม
* สร้าง services class เพิ่ม
* สร้าง controllers class เพิ่ม
* จัดการฟังก์ชันบางอย่างที่ไม่ควรมีใน controllers class
* โปรแกรมมีการตอบสนองต่อการกดปุ่ม
  * Enter
  * ESC
* controllers มีการใช้งานฟังก์ชันต่าง ๆ ใน model class
* พัฒนาการใช้งานของโปรแกรม
  * สร้างเรื่องร้องเรียน
  * จัดการเรื่องร้องเรียน
  * อัพโหลดภาพ
  * เปลี่ยนรูปภาพ
  * เปลี่ยนรหัส
* มีการใช้งานวันที่และเวลาแบบ real time

### ครั้งที่ 4
* พัฒนาการใช้งานที่ต้องมีในโปรแกรมให้ใช้งานได้
* พัฒนา ui ของทุกหน้า
* พัฒนาระบบเรื่องร้องเรียน
* พัฒนาระบบการจัดการเรื่องร้องเรียน
* พัฒนาระบบหน่วยงาน
* พัฒนาระบบหมวดหมู่
* พัฒนาระบบระงับสิทธ์
* พัฒนาระบบคืนสิทธ์
* ปรับปรุงความสวยงามของโปรแกรม
