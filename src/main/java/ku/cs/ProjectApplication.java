package ku.cs;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import com.github.saacsos.FXRouter;

public class ProjectApplication extends Application {

    //สี
    public static final Color COLOR_RED = Color.rgb(255,129,129);
    public static final Color COLOR_BLUE = Color.rgb(128, 176, 255);

    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "KU Report", 1020, 720);
        configRoute();
        FXRouter.goTo("home");
    }

    public static void configRoute(){
        String packageStr = "ku/cs/";
        FXRouter.when("home", packageStr + "bases/home.fxml");
        FXRouter.when("about", packageStr + "bases/about.fxml");
        FXRouter.when("help", packageStr + "bases/help.fxml");
        FXRouter.when("staff_login", packageStr + "bases/staff_login.fxml");
        FXRouter.when("user_login", packageStr + "bases/user_login.fxml");
        FXRouter.when("register", packageStr + "bases/register.fxml");
        FXRouter.when("change_password", packageStr + "bases/change_password.fxml");
        FXRouter.when("request_unban", packageStr + "bases/request_unban.fxml");

        //admin
        FXRouter.when("admin_main_page", packageStr + "admin/admin_main_page.fxml");
        FXRouter.when("admin_data_page", packageStr + "admin/admin_data_page.fxml");
        FXRouter.when("admin_report_page", packageStr + "admin/admin_report_page.fxml");
        FXRouter.when("admin_blacklist_page", packageStr + "admin/admin_blacklist_page.fxml");
        FXRouter.when("admin_staff_register_page", packageStr + "admin/admin_staff_register_page.fxml");
        FXRouter.when("admin_change_password_page", packageStr + "admin/admin_change_password_page.fxml");
        FXRouter.when("admin_department_in_charge", packageStr + "admin/admin_department_in_charge.fxml");
        FXRouter.when("admin_report_group",packageStr + "admin/admin_report_group_page.fxml");
        //user or Students
        FXRouter.when("user_main_page", packageStr + "user/user_main_page.fxml");
        FXRouter.when("user_report_list_page", packageStr + "user/user_report_list_page.fxml");
        FXRouter.when("user_report_page", packageStr + "user/user_report_page.fxml");
        FXRouter.when("user_self_report_page", packageStr + "user/user_self_report_page.fxml");
        //staff
        FXRouter.when("staff_main_page", packageStr +"staff/staff_main_page.fxml");
        FXRouter.when("staff_report_handle_page", packageStr + "staff/staff_report_handle_page.fxml");
        FXRouter.when("staff_report_sove_page", packageStr + "staff/staff_report_sove_page.fxml");
    }


    public static void main(String[] args) {
        launch();
    }
}