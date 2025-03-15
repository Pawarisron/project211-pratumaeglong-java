module cs.ku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;   // เอาไปใช้กับ TableView
    requires java.desktop;  //เอาไว้ใช้กับ ImageIO

    opens ku.cs to javafx.fxml;
    exports ku.cs;

    // ทําให้ javafx.base เรียกใช้ Class ใน models ได้
    opens ku.cs.models.accounts to javafx.base;
    opens ku.cs.models.reports to javafx.base;
    opens ku.cs.models.requests to javafx.base;
    opens ku.cs.models to javafx.base;


    // ทำให้ controller ของ package controllers ใช้งานได้
    exports ku.cs.controllers.base;
    opens ku.cs.controllers.base to javafx.fxml;

    exports ku.cs.controllers.admin;
    opens ku.cs.controllers.admin to javafx.fxml;

    exports ku.cs.controllers.user;
    opens ku.cs.controllers.user to javafx.fxml;

    exports ku.cs.controllers.staff;
    opens ku.cs.controllers.staff to javafx.fxml;
}